pipeline {
    agent any
    
    stages {
        stage('Deploy to Kubernetes') {
            steps {
                sh "kubectl apply deployment.yaml"
                sh "kubectl apply service.yaml"
            }
        }
    }
}
