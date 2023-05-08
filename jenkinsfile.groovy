pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            inheritFrom 'pod-template'
            yaml """
            apiVersion: v1
            kind: Pod
            metadata:
              labels:
                app: hello-world
            spec:
              containers:
              - name: hello-world
                image: busybox
                command: ['echo', 'Hello World']
            """
        }
    }
    stages {
        stage('Deploy') {
            steps {
                sh 'kubectl apply -f hello-world-deployment.yml'
            }
        }
    }
}
