pipeline {
    agent {
        kubernetes {
            label 'minikube'
            defaultContainer 'jnlp'
            yaml """
            apiVersion: v1
            kind: Pod
            metadata:
              labels:
                app: jenkins
            spec:
              containers:
              - name: kubectl
                image: lachlanevenson/k8s-kubectl
                command:
                - cat
                tty: true
            """
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'echo "Nothing to build"'
            }
        }
        stage('Deploy') {
            steps {
                script {
                    def kubectl_version = 'v1.22.5' // replace with desired version
                    sh "curl -LO https://storage.googleapis.com/kubernetes-release/release/${kubectl_version}/bin/linux/amd64/kubectl"
                    sh 'chmod +x kubectl'
                    sh 'kubectl apply -f jenkins-admin-rolebinding.yml'
                    sh './kubectl apply -f hello-world-deployment.yml'
                    sh './kubectl apply -f hello-world-service.yml'
                }
            }
        }
    }
}
