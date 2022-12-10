pipeline {
  agent any

  stages {

    stage('Jobs Create') {
      steps{
         sh 'ansible-playbook jenkins-jobs.yml'
      }
    }
  }

}