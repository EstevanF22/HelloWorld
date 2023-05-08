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
                def kubectl_version = 'v1.22.5' // replace with desired version
                sh "curl -LO https://storage.googleapis.com/kubernetes-release/release/${kubectl_version}/bin/linux/amd64/kubectl"
                sh 'chmod +x kubectl'
                sh 'kubectl apply -f jenkins-admin-rolebinding.yml'
                sh 'kubectl apply -f hello-world-deployment.yml'
                sh 'kubectl apply -f hello-world-service.yml'
            }
        }
    }
}
