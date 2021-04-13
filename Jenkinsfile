pipeline{

    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }

    stages {
        
        stage("build") {
            steps {
                echo "======== executing build ========"
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage("test") {
            steps {
                echo "======== executing test ========"
                sh 'mvn -B test'
            }
        }

        stage("deploy") {
            steps {
                echo "======== executing deploy ========"
            }
        }

    }
}