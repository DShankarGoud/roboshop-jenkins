def call() {
    pipeline {
        agent any

        options {
            ansiColor('xterm')
        }

        parameters {
            string(name: 'ENV', defaultValue: '', description: 'Which Environment?')
        }

        environment {
            SSH = credentials('SSH')
        }

        stages {

            stage('Terraform INIT') {
                steps {
                    sh '''
                      terraform init -data.tf-config=env-${ENV}/data.tf.tfvars
                    '''
                }
            }

            stage('Terraform Apply') {
                steps {
                    sh '''
                      terraform apply -auto-approve -var-file=env-${ENV}/${ENV}.tfvars
                    '''
                }
            }

        }

        post {
            always {
                cleanWs()
            }
        }
    }
}
