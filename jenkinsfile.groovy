pipeline {
    stages {
        stage('Build') {
            steps {
                sh 'echo "Nothing to build"'
            }
        }
        stage('Deploy') {
            steps {
                sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"'
                sh 'chmod +x kubectl'
                sh './kubectl apply -f hello-world-deployment.yml'
                sh './kubectl apply -f hello-world-service.yml'
            }
        }
        stage('Configure Permissions') {
            steps {
                sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"'
                sh 'chmod +x kubectl'
                sh './kubectl create clusterrolebinding jenkins-admin-binding --clusterrole cluster-admin --serviceaccount=default:jenkins-admin'
            }
        }
    }
}
