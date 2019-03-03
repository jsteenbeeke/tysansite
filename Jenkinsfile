pipeline {
    agent none

	options {
		buildDiscarder(logRotator(numToKeepStr: '5'))
		disableConcurrentBuilds()
	}

	triggers {
		pollSCM('H/5 * * * *')
		upstream (upstreamProjects: 'docker-hyperion-jetty,hyperion/experimental', threshold: hudson.model.Result.SUCCESS)
	}

	stages {
		stage('Build') {
		   agent {
               docker {
                   image 'maven:3.5-jdk-8'
                   label 'docker'
               }
           }


			steps {
			    sh 'mvn clean package -U'
			    stash name: 'projectewok-war', includes: '**/*.war'
			}
		}
		stage('Dockerize & Publish') {
			agent {
				label 'docker'
			}

			steps {
				sh 'docker pull registry.jeroensteenbeeke.nl/hyperion-jetty:latest'
				unstash 'projectewok-war'
				sh 'docker build -t registry.jeroensteenbeeke.nl/projectewok:latest .'
				sh 'docker push registry.jeroensteenbeeke.nl/projectewok:latest'

			} 
		}
	}
}
