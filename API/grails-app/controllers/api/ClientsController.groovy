package api

import grails.converters.JSON

class ClientsController {

    def index() { 
    	def result = [
    		'results': [1,2,3,4],
    		'a':1,
    		'b':2
    	]
    	render result as JSON
    }
}
