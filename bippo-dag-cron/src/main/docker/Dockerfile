FROM frolvlad/alpine-java:jdk8-slim
VOLUME /tmp
ADD /bippo-dag-cron-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "-Xms1024m", "-Xmx2g", "-verbose:gc", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-XX:+HeapDumpOnOutOfMemoryError"]