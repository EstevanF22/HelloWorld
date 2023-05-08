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
              serviceAccountName: jenkins-admin
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
                sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"'
                sh 'chmod +x kubectl'
                sh 'kubectl apply -f jenkins-admin-rolebinding.yml'
                sh './kubectl apply -f hello-world-deployment.yml'
                sh './kubectl apply -f hello-world-service.yml'
            }
        }
    }
    post {
        always {
            node('minikube') {
                def crb = """
                apiVersion: rbac.authorization.k8s.io/v1
                kind: ClusterRoleBinding
                metadata:
                  name: jenkins-admin-binding
                roleRef:
                  apiGroup: rbac.authorization.k8s.io
                  kind: ClusterRole
                  name: cluster-admin
                subjects:
                - kind: ServiceAccount
                  name: jenkins-admin
                  namespace: default
                """
                writeFile file: 'crb.yaml', text: crb
                sh './kubectl apply -f crb.yaml'
            }
        }
    }
}
