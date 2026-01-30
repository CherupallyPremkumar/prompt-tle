#!/bin/bash

# ===============================
# Prompt Service Scenario Script
# ===============================
# Requirements: jq installed (`brew install jq`)
# This script handles:
# - Create prompt
# - Submit & validate
# - Add/accept answer
# - Upvote, comment, revision
# - Duplicate handling and closure
# ===============================

# --------- Configuration ---------
AUTH_TOKEN="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVc2VyLTIwZGJiN2NmLTIwNGMtNGM1NC05NjIyLThlZTY0Mzg3MTM3Zi0wMDAxIiwiYWNscyI6W10sImlhdCI6MTc2OTY3OTU2NiwiZXhwIjoxNzcwMjg0MzY2fQ.SrT-wt8sWfWU1Ss8ik7wF6LzP7iCWU4mqFkq69v_lf0SVCWq656vCViCoAmPWZJk9sBtXRA-sxqpqk5ilX3Nrw"
API_BASE="http://98.93.49.18:8080/prompt"
TENANT_ID="tenant1"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting Prompt Scenario Test Sequence...${NC}"

# --------- Helper Functions ---------
check_status() {
    if [ "$1" -ge 200 ] && [ "$1" -lt 300 ]; then
        return 0
    else
        echo -e "${RED}❌ HTTP Status $1${NC}"
        return 1
    fi
}

# Make a curl call and separate body & HTTP code (macOS-compatible)
curl_request() {
    local METHOD=$1
    local URL=$2
    local DATA=$3

    RESPONSE=$(curl -s -w "\n%{http_code}" -X "$METHOD" "$URL" \
        -H "Authorization: Bearer $AUTH_TOKEN" \
        -H "x-chenile-tenant-id: $TENANT_ID" \
        -H "Content-Type: application/json" \
        -d "$DATA")

    HTTP_CODE=$(echo "$RESPONSE" | tail -1)
    BODY=$(echo "$RESPONSE" | sed '$d')

    echo "$BODY"
    return "$HTTP_CODE"
}

extract_id() {
    echo "$1" | jq -r "$2"
}

# -----------------------------------
# 1/10: Create Prompt
# -----------------------------------
echo -e "\n${GREEN}[1/10] Creating Prompt...${NC}"
BODY=$(curl_request POST "$API_BASE" '{
    "title": "How to center a div",
    "description": "Seeking best practices for centering elements",
    "tags": ["css","html","layout"]
}')
PROMPT_ID=$(echo "$BODY" | jq -r '.payload.mutatedEntity.id // empty')

if [ -z "$PROMPT_ID" ]; then
    echo -e "${RED}Failed to create prompt. Exiting.${NC}"
    exit 1
fi
echo -e "✅ Prompt Created with ID: $PROMPT_ID"

# -----------------------------------
# 2/10: Submit Prompt
# -----------------------------------
echo -e "\n${GREEN}[2/10] Submitting Prompt...${NC}"
curl_request PATCH "$API_BASE/$PROMPT_ID/submit" '{ "comment": "Submitting for review" }'

# -----------------------------------
# 3/10: Validate Prompt
# -----------------------------------
echo -e "\n${GREEN}[3/10] Validating Prompt...${NC}"
curl_request PATCH "$API_BASE/$PROMPT_ID/validate" '{ "comment": "Prompt validated by moderator", "score": 5 }'

# -----------------------------------
# 4/10: Add Answer
# -----------------------------------
echo -e "\n${GREEN}[4/10] Adding Answer...${NC}"
BODY=$(curl_request PATCH "$API_BASE/$PROMPT_ID/addAnswer" '{
    "body": "Use flexbox with justify-content and align-items set to center",
    "authorUsername": "css_expert"
}')
ANSWER_ID=$(echo "$BODY" | jq -r '.payload.mutatedEntity.answers[0].id // empty')

if [ -z "$ANSWER_ID" ]; then
    echo -e "${RED}⚠ Could not extract Answer ID${NC}"
else
    echo -e "✅ Answer Added with ID: $ANSWER_ID"
fi

# -----------------------------------
# 5/10: Accept Answer
# -----------------------------------
if [ ! -z "$ANSWER_ID" ]; then
    echo -e "\n${GREEN}[5/10] Accepting Answer...${NC}"
    curl_request PATCH "$API_BASE/$PROMPT_ID/acceptAnswer" "{
        \"answerId\": \"$ANSWER_ID\",
        \"comment\": \"This solution works perfectly\"
    }"
fi

# -----------------------------------
# 6/10: Upvote Prompt
# -----------------------------------
echo -e "\n${GREEN}[6/10] Upvoting Prompt...${NC}"
curl_request PATCH "$API_BASE/$PROMPT_ID/upvote" '{ "userId": "user123" }'

# -----------------------------------
# 7/10: Add Comment
# -----------------------------------
echo -e "\n${GREEN}[7/10] Adding Comment...${NC}"
curl_request PATCH "$API_BASE/$PROMPT_ID/addComment" '{
    "content": "Please add more context about browser compatibility",
    "authorUsername": "reviewer1"
}'

# -----------------------------------
# 8/10: Create Revision
# -----------------------------------
echo -e "\n${GREEN}[8/10] Creating Revision...${NC}"
curl_request PATCH "$API_BASE/$PROMPT_ID/addRevision" '{
    "changeComment": "Added browser compatibility requirements",
    "newContent": "Seeking best practices for centering elements across all major browsers"
}'

# -----------------------------------
# 9/10: Create Duplicate Prompt
# -----------------------------------
echo -e "\n${GREEN}[9/10] Creating Duplicate Prompt...${NC}"
BODY=$(curl_request POST "$API_BASE" '{
    "title": "Another centering question",
    "description": "How do I center things?"
}')
DUP_ID=$(echo "$BODY" | jq -r '.payload.mutatedEntity.id // empty')

if [ ! -z "$DUP_ID" ]; then
    curl_request PATCH "$API_BASE/$DUP_ID/submit" '{ "comment": "Submitting" }'
    curl_request PATCH "$API_BASE/$DUP_ID/markDuplicate" "{
        \"duplicateOfPromptId\": \"$PROMPT_ID\",
        \"comment\": \"This is a duplicate of existing question\"
    }"

    # -----------------------------------
    # 10/10: Close Duplicate Prompt
    # -----------------------------------
    echo -e "\n${GREEN}[10/10] Closing Duplicate Prompt...${NC}"
    curl_request PATCH "$API_BASE/$DUP_ID/close" '{
        "reason": "Duplicate question",
        "comment": "Closed as duplicate"
    }'
fi

echo -e "\n${GREEN}✅ Prompt Scenario Sequence Completed!${NC}"