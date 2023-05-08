pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'echo "Nothing to build"'
            }
        }
        stage('Deploy') {
            steps {
                sh 'kubectl apply -f hello-world-deployment.yaml'
                sh 'kubectl apply -f hello-world-service.yaml'
            }
        }
    }
}