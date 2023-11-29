package com.manning.bippo.aggregator.rets.service;

import com.manning.bippo.aggregator.rets.pojo.RetsFeedTask;
import com.manning.bippo.aggregator.rets.pojo.TaskAllPhotos;
import com.manning.bippo.aggregator.rets.pojo.TaskDownloadComps;
import com.manning.bippo.aggregator.rets.pojo.TaskDownloadForFilter;
import com.manning.bippo.aggregator.rets.pojo.TaskDownloadForInterval;
import com.manning.bippo.aggregator.rets.pojo.TaskFrontPhotos;
import com.manning.bippo.aggregator.rets.pojo.TaskUpdateMatrixId;
import com.manning.bippo.aggregator.rets.pojo.TaskUpdateMlsNumber;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The RETS Feed can only be accessed by one login at a time, processing one
 * query at a time. This class acts as a filter between the RabbitMQ receivers
 * that want their various tasks processed, and actual usage of the RETS Feed.
 * Submitted tasks are processed in an order of priority for when load is high.
 */
@Service
public class RetsFeedSynchronizationService {

    @Autowired
    RetsBippoMappingService retsBippoMappingService;

    private final BlockingQueue<RetsFeedTask> downloadUpdates;
    private final BlockingQueue<RetsFeedTask> resyncData;
    private final BlockingQueue<RetsFeedTask> downloadComps;
    private final BlockingQueue<RetsFeedTask> frontPhotos;
    private final BlockingQueue<RetsFeedTask> allPhotos;
    private final AtomicReference<WorkerThread> worker;

    public RetsFeedSynchronizationService() {
        this.downloadUpdates = new LinkedBlockingQueue<>();
        this.resyncData = new ArrayBlockingQueue<>(10);
        this.frontPhotos = new ArrayBlockingQueue<>(10);
        this.allPhotos = new ArrayBlockingQueue<>(2);
        this.downloadComps = new LinkedBlockingQueue<>();
        this.worker = new AtomicReference<>();
    }

    private void startWorker() {
        final WorkerThread newWorker = new WorkerThread();

        if (this.worker.compareAndSet(null, newWorker)) {
            LogUtil.info("RetsFeedSynchronizationService: Creating and starting WorkerThread..");
            new Thread(newWorker).start();
        }
    }

    private void notifyWorker() {
        final WorkerThread worker = this.worker.get();

        if (worker == null) {
            this.startWorker();
            return;
        } else synchronized (worker) {
            worker.newTasks = true;
            worker.notifyAll();
        }
    }

    private <T> void put(T task, BlockingQueue<? super T> queue) {
        while (true) {
            try {
                queue.put(task);
            } catch (InterruptedException e) {
                continue;
            }

            break;
        }

        this.notifyWorker();
    }

    public void queueDownloadInterval(TaskDownloadForInterval task) {
        this.put(task, this.downloadUpdates);
    }

    public void queueDownloadFilter(TaskDownloadForFilter task) {
        this.put(task, this.downloadUpdates);
    }

    public void queueResyncData(TaskUpdateMlsNumber task) {
        this.put(task, this.resyncData);
    }

    public void queueResyncData(TaskUpdateMatrixId task) {
        this.put(task, this.resyncData);
    }

    public void queueDownloadComps(TaskDownloadComps task) {
        this.put(task, this.downloadComps);
    }

    public void queueFrontPhotos(TaskFrontPhotos task) {
        this.put(task, this.frontPhotos);
    }

    public void queueAllPhotos(TaskAllPhotos task) {
        this.put(task, this.allPhotos);
    }

    private class WorkerThread implements Runnable {

        volatile boolean running, newTasks;

        WorkerThread() {
            this.running = true;
            this.newTasks = false;
        }

        @Override
        public void run() {
            while (this.running) {
                // We don't need to know if there were any new tasks up to this point, as we will find them in the queues
                // Any new tasks while we're processing will set newTasks back to true, which is important information to us
                // Proceed to check queues and process work
                this.newTasks = false;

                { // Requests to download updates to MLS data for a specified interval of time or a specified filter
                    final RetsFeedTask task = RetsFeedSynchronizationService.this.downloadUpdates.poll();

                    if (task != null) {
                        this.doTask(task);
                        continue;
                    }
                }

                { // Properties queued to have their MLS data re-synced by matrix id
                    final RetsFeedTask task = RetsFeedSynchronizationService.this.resyncData.poll();

                    if (task != null) {
                        this.doTask(task);
                        continue;
                    }
                }

                { // Properties queued to ensure that nearby comps matching a filter are available
                    final RetsFeedTask task = RetsFeedSynchronizationService.this.downloadComps.poll();

                    if (task != null) {
                        this.doTask(task);
                        continue;
                    }
                }

                { // Properties queued to have front photos downloaded
                    RetsFeedTask task = RetsFeedSynchronizationService.this.frontPhotos.poll();

                    if (task != null) {
                        // Process in chunks of up to 10 at a time
                        this.doTask(task);

                        for (int i = 0; i < 9; i++) {
                            task = RetsFeedSynchronizationService.this.frontPhotos.poll();

                            if (task == null) {
                                break;
                            }

                            this.doTask(task);
                        }

                        continue;
                    }
                }

                { // Properties queued to have all associated photos downloaded
                    final RetsFeedTask task = RetsFeedSynchronizationService.this.allPhotos.poll();

                    if (task != null) {
                        this.doTask(task);
                        continue;
                    }
                }

                synchronized (this) {
                    if (this.newTasks) {
                        // We had new tasks enter queues in the time that we were processing
                        // We may have already consumed them, but we use this as a heuristic to loop again, as opposed to waiting immediately
                        continue;
                    }

                    try {
                        // There were no new tasks during this entire iteration of the while(running) loop and we turned up empty
                        // Await the submission of new tasks, at which point we will be notified
                        LogUtil.info("No tasks to process, awaiting further tasks..");
                        this.wait(60_000L);
                    } catch (InterruptedException e) {
                        // Continue, check our running state again
                        continue;
                    }
                }
            }
        }

        void doTask(RetsFeedTask task) {
            try {
                final StopWatch timer = new StopWatch();
                timer.start();
                task.accept(RetsFeedSynchronizationService.this.retsBippoMappingService);
                timer.stop();
                task.log(timer);
            } catch (Exception e) {
                LogUtil.error(task.getClass().getSimpleName() + " failed with exception:", e);
            }
        }
    }
}
