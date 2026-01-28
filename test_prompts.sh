#!/bin/bash

# Test Script for Prompt Query Service
BASE_URL="http://localhost:8080"

echo "------------------------------------------------"
echo "Prompt Query Service - BDD Feature Test Script"
echo "------------------------------------------------"

# Optional: Add Authorization token if required
# read -p "Enter Auth Token (or press enter to skip): " AUTH_TOKEN
# if [ ! -z "$AUTH_TOKEN" ]; then
#   AUTH_HEADER="-H 'Authorization: Bearer $AUTH_TOKEN'"
# fi

echo -e "\n1. Testing: prompt.search (Pagination & Sort)"
curl -X POST "$BASE_URL/q/prompt.search" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{
	"sortCriteria" :[
		{"name":"id","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 10
}'

echo -e "\n\n2. Testing: prompt.search (Filter VALIDATED)"
curl -X POST "$BASE_URL/q/prompt.search" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{
	"filters" :{
		"stateId": "VALIDATED"
	}
}'

echo -e "\n\n3. Testing: prompt.getAnswers"
curl -X POST "$BASE_URL/q/prompt.getAnswers" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n4. Testing: prompt.getComments"
curl -X POST "$BASE_URL/q/prompt.getComments" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n5. Testing: prompt.getRevisions"
curl -X POST "$BASE_URL/q/prompt.getRevisions" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n6. Testing: prompt.getVariables"
curl -X POST "$BASE_URL/q/prompt.getVariables" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n7. Testing: prompt.getActivities"
curl -X POST "$BASE_URL/q/prompt.getActivities" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n8. Testing: prompt.getModelCompatibility"
curl -X POST "$BASE_URL/q/prompt.getModelCompatibility" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n9. Testing: prompt.getTestCases"
curl -X POST "$BASE_URL/q/prompt.getTestCases" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "promptId": "p1" } }'

echo -e "\n\n10. Testing: prompt.getNotifications"
curl -X POST "$BASE_URL/q/prompt.getNotifications" \
-H "Content-Type: application/json" $AUTH_HEADER \
-d '{ "filters": { "userId": "user1" } }'

echo -e "\n\n------------------------------------------------"
echo "Testing Complete"
echo "------------------------------------------------"
