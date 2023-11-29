def deployStep(appName) {
    env.PATH = "${env.PATH}:/opt/kubernetes/platforms/linux/amd64/"
    env.appName = appName
    sh '''
        #!/bin/bash -xe

        GIT_COMMIT=$(git rev-parse HEAD | cut -c 1-7)
        /var/lib/jenkins/kubectl --kubeconfig=/var/lib/jenkins/kube-config-hit.qa set image deployment/$appName-deployment $appName=127918707993.dkr.ecr.us-east-1.amazonaws.com/bippo/$appName:$BRANCH_NAME-$GIT_COMMIT
    '''
}

node {
    stage 'Build and Test'
    env.PATH = "${tool 'Maven 3'}/bin:${env.PATH}"
    checkout scm
    sh 'mvn clean compile'

    stage 'Docker Push'
//      sh '$(aws ecr get-login --region us-east-1)'
        sh 'mvn package -Dmaven.test.skip=true'

    stage 'Deploy BIPPO To QA'/**
        deployStep("bippo-api")*/

    stage 'Deploy BIPPO-DAG To QA'/**
        timeout(20) {
            input message: "Approve deployment?"
        }
        deployStep("bippo-dag")
        deployStep("bippo-dag-rets")
        deployStep("bippo-dag-cron")*/

}
