pipeline {
    agent any

    stages{
        stage("Checkout"){
            steps{
                checkout scm
            }
        }
        stage("set-up"){
            steps{
                sh """
                    java -version
                    mvn -version
                    git --version
                """
            }
        }
        stage("compile"){
            steps{
                sh "mvn clean compile"
            }
        }

        stage("Unit test"){
            steps{
                sh "mvn test"
            }
            post {
                always {
                    junit "target/surefire-reports/*.xml"
                }
            }
        }
        // stage("Coverage report"){
        //     steps{
        //         sh "mvn jacoco:report"
        //     }
        //     post{
        //         always{
        //             archiveArtifacts artifacts: "target/site/jacoco/jacoco.xml", allowEmptyArchive: true
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

        // stage("static analysis"){
        //     steps{
        //         sh "mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs"
        //     }
        //     post {
        //         always{
        //             archiveArtifacts artifacts: "target/site/**,target/spotbugs.xml", allowEmptyArchive: true
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
        stage("sonarqube analysis"){
            steps{
                withSonarQubeEnv('sonarqube'){
                    sh "mvn sonar:sonar"
                }
            }
        }
    }
}