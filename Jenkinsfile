pipeline {
    agent any
    tools {
      maven 'MAVEN'
      jdk 'JDK8'
    }
    stages {
        stage("Build") {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage("Package & Test") {
            steps {
                sh 'mvn verify'
            }
        }
        stage('Deploy') { 
            when {
              anyOf {
                branch 'develop'
                branch 'rc*'
              }
            }
            steps {
                sh "mvn deploy -DskipTests"
            }
        }
    }
    post {
        cleanup {
            cleanWs(skipWhenFailed : true)
        }
        failure {
            emailext body: "<html>FAIL : ${env.JOB_NAME} build ${env.BUILD_NUMBER}<br/>${env.BUILD_URL}</html>",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                subject: "FAIL : ${env.JOB_NAME}"
        }
        unstable {
            emailext body: "<html>INSTABLE : ${env.JOB_NAME} build ${env.BUILD_NUMBER}<br/>${env.BUILD_URL}</html>",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                subject: "SUCCESS: ${env.JOB_NAME}"
        }
    }
}