folder('Folder1/Nightly') {
    displayName('Nightly')
    description('Nightly builds, Smoke and IT Tests')
}


def services = [
    ["UNW-PROJ1", "microservice1"],
    ["UNW-PROJ2", "microservice1"],
    ["UNW-PROJ3", "microservice1"]
]


 services.each{
  service ->    
    createTestJob(service[0], service[1])
 }



def createTestJob(service, repoName){

    job("Folder1/Nightly/" + service + "_ST_INT" ) {
        description("Starts smoke and integration tests for " + service)
        keepDependencies(false)
        scm {
            git {
                remote {
                    url("ssh://git@repo-id"+ repoName +".git")
                    credentials("Bitbucket_Access")
                }
                branch("master")
            }
        }
        triggers { 
			cron("@midnight") 
		}
        disabled(false)
        concurrentBuild(false)
        publishers {
            archiveJunit("**/target/surefire-reports/*.*.xml") {
                healthScaleFactor(1.0)
                allowEmptyResults(false)
            }
        }
        configure {
            it / 'properties' / 'jenkins.model.BuildDiscarderProperty' {
                strategy {
                    'daysToKeep'('-1')
                    'numToKeep'('30')
                    'artifactDaysToKeep'('-1')
                    'artifactNumToKeep'('-1')
                }
            }
            it / 'properties' / 'com.sonyericsson.rebuild.RebuildSettings' {
                'autoRebuild'('false')
                'rebuildDisabled'('false')
            }
        }
		
        steps {
            maven {
				goals('clean verify -P' + hub + '-INT,st, it')
                mavenOpts('-Xmx4096m -XX:MaxPermSize=4096m')
                mavenInstallation('Maven 3')
            }

        }
    }
}