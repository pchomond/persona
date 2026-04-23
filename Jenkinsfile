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
        echo 'Compiling and Packaging...'
        sh 'mvn -B clean package -DskipTests'
      }
    }

    stage('Test') {
      steps {
        echo 'Running Unit Tests...'
        sh 'mvn -B test'
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