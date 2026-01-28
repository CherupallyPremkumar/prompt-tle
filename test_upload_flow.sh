#!/bin/bash

# Configuration
API_BASE="http://localhost:8080/api/uploads"
AUTH_TOKEN="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVc2VyLTIwZGJiN2NmLTIwNGMtNGM1NC05NjIyLThlZTY0Mzg3MTM3Zi0wMDAxIiwiYWNscyI6WyJST0xFX0dPT0dMRSIsIlJFQURfUFJPTVBUIiwiV1JJVEVfUFJPTVBUIl0sImlhdCI6MTc2OTYzMjQyMCwiZXhwIjoxNzY5NjM2MDIwfQ.lPNJwlF7XumyLF6ENxsOdCJwGp7Xj2s-b5qPV5Diip6WrTfb0AamMWVrkKOmVKmcvu1-HfVYfCTzwN_pZXAPog"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting API Sequence Test...${NC}"

# 1. Generate Dummy File
echo "Creating dummy file..."
echo "Fake PNG Content" > test_upload.png

# 2. Get Presigned URL
echo -e "\n${GREEN}[1/3] Requesting Presigned URL...${NC}"
RESPONSE=$(curl -s -X POST "$API_BASE/presigned-url" \
  -H "Authorization: Bearer $AUTH_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "filename": "test_upload.png",
    "contentType": "image/png",
    "fileSize": 16,
    "folder": "test-scripts"
  }')

echo "Response: $RESPONSE"

# Extract uploadId and uploadUrl (using simple grep/sed for portability, though jq is better)
UPLOAD_ID=$(echo "$RESPONSE" | grep -o '"uploadId":"[^"]*"' | cut -d'"' -f4)
UPLOAD_URL=$(echo "$RESPONSE" | grep -o '"uploadUrl":"[^"]*"' | cut -d'"' -f4)
FILE_KEY=$(echo "$RESPONSE" | grep -o '"fileKey":"[^"]*"' | cut -d'"' -f4)

if [ -z "$UPLOAD_ID" ]; then
  echo -e "${RED}Failed to extract Upload ID${NC}"
  exit 1
fi

echo -e "Upload ID: $UPLOAD_ID"
# echo "Upload URL: $UPLOAD_URL"

# 3. Upload File to S3 (actually PUT to the signed URL)
echo -e "\n${GREEN}[2/3] Uploading file to S3 (Presigned PUT)...${NC}"
# Note: We use the extracted URL.
curl -s -X PUT "$UPLOAD_URL" \
  -H "Content-Type: image/png" \
  --data-binary @test_upload.png

echo -e "\nUpload step complete."

# 4. Confirm Upload
echo -e "\n${GREEN}[3/3] Confirming Upload...${NC}"
CONFIRM_RESPONSE=$(curl -s -X POST "$API_BASE/confirm" \
  -H "Authorization: Bearer $AUTH_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"uploadId\": \"$UPLOAD_ID\",
    \"fileKey\": \"$FILE_KEY\"
  }")

echo "Response: $CONFIRM_RESPONSE"

if [[ "$CONFIRM_RESPONSE" == *"success\":true"* ]]; then
    echo -e "\n${GREEN}✅ Test Sequence Completed Successfully!${NC}"
else
    echo -e "\n${RED}❌ Test Sequence Failed${NC}"
fi

# Cleanup
rm test_upload.png
