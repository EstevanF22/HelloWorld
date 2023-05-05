pipeline {
    agent any

    stage('Deploy to Kubernetes') {
    steps {
        sh "kubectl apply -f deployment.yaml"
        sh "kubectl apply -f service.yaml"
    }
}
}