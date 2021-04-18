def remote = [:]
    remote.name = "tomcat"
    remote.host = "tomcat.home"
    remote.allowAnyHosts = true

withCredentials([usernamePassword(credentialsId: 'ssh_default_user', usernameVariable: 'username', passwordVariable: 'password')]) {
    remote.user = username
    remote.password = password
}

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
        stage("Deploy") {
            when {
                anyOf {
                    branch 'develop'
                    branch 'rc*'
                    branch 'feature/*'
                }
            }
            steps {
                sshCommand remote: remote, command: 'mkdir -p /tmp/jenkins_tmp'

                sshPut remote: remote, from: './target/', into: '/tmp/jenkins_tmp'
                sshPut remote: remote, from: './deployment/', into: '/tmp/jenkins_tmp'
                sshCommand remote: remote, command: 'chmod +x /tmp/jenkins_tmp/deployment/install.sh'
                sshCommand remote: remote, command: '/tmp/jenkins_tmp/deployment/install.sh', sudo: true

                sshCommand remote: remote, command: 'rm -rf /tmp/jenkins_tmp', sudo: true
            }
        }
    }
    post {
        cleanup {
            cleanWs(skipWhenFailed: true)
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