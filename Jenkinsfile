pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Show Tool Versions') {
            steps {
                sh '''
                    whoami
                    java -version
                    mvn -version
                    git --version
                '''
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Coverage Report') {
            steps {
                sh 'mvn jacoco:report'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/site/jacoco/jacoco.xml', allowEmptyArchive: true
                }
            }
        }

        stage('Static Analysis Reports') {
            steps {
                sh '''
                    mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/site/**,target/spotbugsXml.xml', allowEmptyArchive: true
                }
            }
        }
       
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }
}
