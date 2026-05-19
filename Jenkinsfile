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

        // stage('Coverage Report') {
        //     steps {
        //         sh 'mvn jacoco:report'
        //     }
        //     post {
        //         always {
        //             archiveArtifacts artifacts: 'target/site/jacoco/jacoco.xml', allowEmptyArchive: true
        //         }
        //     }
        // }

        stage('Coverage Report') {
    steps {
        sh 'mvn jacoco:report'
    }
    post {
        always {
            publishHTML(target: [
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'JaCoCo Coverage',
                keepAll: false,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])

            archiveArtifacts artifacts: 'target/site/jacoco/jacoco.xml', allowEmptyArchive: true
        }
    }
}

        // stage('Static Analysis Reports') {
        //     steps {
        //         sh '''
        //             mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs
        //         '''
        //     }
        //     post {
        //         always {
        //             archiveArtifacts artifacts: 'target/site/**,target/spotbugsXml.xml', allowEmptyArchive: true
        //         }
        //     }
        // }

        stage('Static Analysis Reports') {
    steps {
        sh '''
            mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs
        '''
    }
    post {
        always {
            recordIssues(
                enabledForFailure: true,
                tools: [
                    checkStyle(pattern: 'target/checkstyle-result.xml'),
                    pmdParser(pattern: 'target/pmd.xml'),
                    spotBugs(pattern: 'target/spotbugsXml.xml')
                ],
                qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]]
            )

            publishHTML(target: [
                reportDir: 'target/site',
                reportFiles: 'checkstyle.html',
                reportName: 'Checkstyle HTML Report',
                keepAll: false,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])

            publishHTML(target: [
                reportDir: 'target/site',
                reportFiles: 'pmd.html',
                reportName: 'PMD HTML Report',
                keepAll: false,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])

            archiveArtifacts artifacts: 'target/checkstyle-result.xml,target/pmd.xml,target/spotbugsXml.xml', allowEmptyArchive: true
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
