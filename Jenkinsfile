pipeline {
  agent any

  environment {
    // Optional: Maven settings for Nexus credentials ID 'nexus-creds'
    MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
  }

  triggers {
    // If using GitHub webhooks, you can omit pollSCM
    // pollSCM('H/2 * * * *')
  }

  options {
    timestamps()
    ansiColor('xterm')
  }

  stages {
    stage('Checkout') {
      steps {
        // Jenkins will use the repo the job is configured with
        checkout scm
      }
    }

    stage('Build') {
      steps {
        sh 'mvn -B -ntp clean package'
      }
    }

    stage('Unit Test') {
      steps {
        sh 'mvn -B -ntp test'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Publish (Nexus)') {
      when { expression { return fileExists('pom.xml') } }
      steps {
        // Requires server credentials set in ~/.m2/settings.xml on the agent
        sh 'mvn -B -ntp deploy -DskipTests'
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          def imageTag = "addressbook:${env.BUILD_NUMBER}"
          sh """
            docker build -t ${env.DOCKER_IMAGE_REPO}:${env.BUILD_NUMBER} .
          """
        }
      }
    }

    stage('Push Docker Image') {
      when { expression { return env.DOCKER_IMAGE_REPO } }
      steps {
        script {
          withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
            sh "docker push ${env.DOCKER_IMAGE_REPO}:${env.BUILD_NUMBER}"
          }
        }
      }
    }
  }

  post {
    success {
      echo "Build #${env.BUILD_NUMBER} succeeded 🎉"
    }
    failure {
      echo "Build #${env.BUILD_NUMBER} failed ❌"
    }
  }
}
``
