

def services = [
    ["UNW-PROJ1", "microservice1"],
    ["UNW-PROJ2", "microservice1"],
    ["UNW-PROJ3", "microservice1"]
]

services.each{
  service ->    
    createView(service[0])
 }
 
def createView(service){  
			listView('Folder1/Nightly/' + service) {
			    def regexStr = service + ".*"	
				description('All ' + service + ' jobs')
				filterBuildQueue()
				filterExecutors()
				jobs {                
					regex(/${regexStr}/)
				}    
				columns {
					status()
					weather()
					name()
					lastSuccess()
					lastFailure()
					lastDuration()
					buildButton()
				}
		    }
}

