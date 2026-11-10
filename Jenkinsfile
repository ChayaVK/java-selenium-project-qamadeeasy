// Jenkins pipeline: Jenkins > New Item > Pipeline > "Pipeline script from SCM" > your Git repository.
// Needs these Jenkins tools (Manage Jenkins > Tools): a JDK named "JDK17" and Maven named "Maven3".
// Plugins: Pipeline, Git, HTML Publisher, JUnit (installed with the suggested plugins, except HTML Publisher).
pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    parameters {
        choice(name: 'SUITE', choices: ['testng', 'smoke', 'parallel', 'cucumber', 'all'], description: 'Suite file in the suites folder')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run without a browser window')
        string(name: 'ENV', defaultValue: '', description: 'Environment file in env/ (empty = config.properties only)')
        string(name: 'TAGS', defaultValue: '', description: 'Cucumber tags, for example @smoke (Cucumber suites only)')
    }

    triggers {
        // Nightly run around 2 AM; remove this block if you only want manual runs
        cron('H 2 * * *')
    }

    options {
        timestamps()
        timeout(time: 60, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run tests') {
            steps {
                script {
                    def cmd = "mvn clean test -DsuiteXmlFile=suites/${params.SUITE}.xml " +
                              "-Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                    if (params.ENV?.trim()) {
                        cmd += " -Denv=${params.ENV.trim()}"
                    }
                    if (params.TAGS?.trim()) {
                        cmd += " -Dcucumber.filter.tags=\"${params.TAGS.trim()}\""
                    }
                    // isUnix() picks sh on Linux/macOS agents and bat on Windows agents
                    if (isUnix()) {
                        sh cmd
                    } else {
                        bat cmd
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            publishHTML(target: [
                reportDir: 'reports', reportFiles: 'ExtentReport.html', reportName: 'Extent Report',
                keepAll: true, alwaysLinkToLastBuild: true, allowMissing: true
            ])
            archiveArtifacts artifacts: 'reports/**, logs/**', allowEmptyArchive: true
        }
    }
}
