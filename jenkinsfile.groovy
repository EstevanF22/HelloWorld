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
                sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"'
                sh 'chmod +x kubectl'

                // Create a service account for Jenkins
                sh './kubectl create sa jenkins-sa'

                // Create a role binding that grants permissions to the service account
                sh './kubectl create clusterrolebinding jenkins-sa-admin --clusterrole=cluster-admin --serviceaccount=default:jenkins-sa'

                // Apply the deployment and service configurations
                sh './kubectl apply -f hello-world-deployment.yml'
                sh './kubectl apply -f hello-world-service.yml'
            }
        }
    }
}
