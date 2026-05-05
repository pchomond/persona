pipeline {
  agent {
    label 'maven-cloud-agent'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        withMaven(
          mavenLocalRepo: '/maven-cache'
        ) {
          echo 'Compiling and Packaging...'
          sh 'mvn -B clean package -DskipTests'
        }
      }
    }

    stage('Test') {
      steps {
        withMaven(
          mavenLocalRepo: '/maven-cache'
        ) {
          echo 'Running Unit Tests...'
          sh 'mvn -B test'
        }
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Archive Artifacts') {
      steps {
        echo 'Saving the build artifact...'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }

  post {
    success {
      echo 'Pipeline completed successfully!'
    }
    failure {
      echo 'Pipeline failed. Check the logs and test reports.'
    }
  }
}