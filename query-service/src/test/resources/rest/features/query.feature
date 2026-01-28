Feature: Tests the Prompt Query Service generic query capability
  Background:
     Given I am logged in as "user1" with role "ROLE_USER"

Scenario: Tests out pagination capability
When I POST a REST request to URL "/q/prompt.search" with payload
"""json
{
	"sortCriteria" :[
		{"name":"id","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}
"""
Then the http status code is 200
And success is true 
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.id" is "p1"
And the REST response key "list[1].row.id" is "p2"

Scenario: Test status filter
When I POST a REST request to URL "/q/prompt.search" with payload
"""json
{
	"filters" :{
		"stateId": "VALIDATED"
	}
}
"""
Then the http status code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "p1"

Scenario: Test taskType filter
When I POST a REST request to URL "/q/prompt.search" with payload
"""json
{
	"filters" :{
		"taskType": "CODE_GENERATION"
	}
}
"""
Then the http status code is 200
And success is true
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.id" is "p2"
