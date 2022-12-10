def call() {
    pipeline {
        agent any

        options {
            ansiColor('xterm')
        }

        parameters {
            string(name: 'ENV', defaultValue: '', description: 'Which Environment?')
            string(name: 'COMPONENT', defaultValue: '', description: 'Which Component?')
        }

        environment {
            SSH = credentials('SSH')
        }

        stages {

            stage('CheckOut Code - DEV') {
                when {
                    expression {
                        ENV == "dev"
                    }
                }
                steps {
                    dir('ANSIBLE') {
                        git branch: 'dev', url: 'https://github.com/raghudevopsb64/roboshop-ansible.git'
                    }
                }
            }

            stage('CheckOut Code - PROD') {
                when {
                    expression {
                        ENV == "prod"
                    }
                }
                steps {
                    dir('ANSIBLE') {
                        git branch: 'main', url: 'https://github.com/raghudevopsb64/roboshop-ansible.git'
                    }
                }
            }

            stage('Create Instance') {
                steps {
                    dir('ANSIBLE') {
                        sh 'bash create-ec2-with-env.sh ${COMPONENT} ${ENV}'
                    }
                }
            }

            stage('Run Ansible Playbook') {
                steps {
                    dir('ANSIBLE') {
                        sh 'ansible-playbook -i inv roboshop.yml -e ENV=${ENV} -e ansible_user=${SSH_USR} -e ansible_password=${SSH_PSW} -e HOST=${COMPONENT} -e ROLE_NAME=${COMPONENT}'
                    }
                }
            }

        }

    }
}