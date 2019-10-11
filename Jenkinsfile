pipeline {
    agent none

	options {
		buildDiscarder(logRotator(numToKeepStr: '5'))
		disableConcurrentBuilds()
	}

	triggers {
		pollSCM('H/5 * * * *')
		upstream (upstreamProjects: 'docker-hyperion-jetty,hyperion/master', threshold: hudson.model.Result.SUCCESS)
	}

	stages {
		stage('Build') {
		   agent {
               docker {
                   image 'maven:3.6-jdk-11'
                   label 'docker'
               }
           }


			steps {
				sh 'echo `git log -n 1 --pretty=format:"%H"` > '+ env.WORKSPACE +'/src/main/java/com/tysanclan/site/projectewok/revision.txt'
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
