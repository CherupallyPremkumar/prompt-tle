#!/bin/bash

# Configuration
API_BASE="http://98.93.49.18:8080/api/uploads"
AUTH_TOKEN="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVc2VyLTIwZGJiN2NmLTIwNGMtNGM1NC05NjIyLThlZTY0Mzg3MTM3Zi0wMDAxIiwiYWNscyI6WyJST0xFX0dPT0dMRSIsIlJFQURfUFJPTVBUIiwiV1JJVEVfUFJPTVBUIl0sImlhdCI6MTc2OTY3OTU2NiwiZXhwIjoxNzY5NjgzMTY2fQ.3FhrvjiDwfaf4-LT37KmBvYnmcxo90hcQKr9pcydZhvCkC3Eor8rreub-VTa-da-39F225uNyjRPALmLriKmQA"
USER_ID="premkumarcherupally060@gmail.com" # Must match the user in the token

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting API Sequence Test...${NC}"

# 1. Generate Dummy File
echo "Creating dummy file..."
echo -n "Fake PNG Content" > test_upload.png
FILE_SIZE=$(wc -c < test_upload.png | xargs)

# 2. Get Presigned URL
echo -e "\n${GREEN}[1/3] Requesting Presigned URL...${NC}"
RESPONSE=$(curl -s -X POST "$API_BASE/presigned-url" \
  -H "Authorization: Bearer $AUTH_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"filename\": \"test_upload.png\",
    \"contentType\": \"image/png\",
    \"fileSize\": $FILE_SIZE,
    \"folder\": \"test-scripts\"
  }")

echo "Response: $RESPONSE"

# Extract values
UPLOAD_ID=$(echo "$RESPONSE" | grep -o '"uploadId":"[^"]*"' | cut -d'"' -f4)
UPLOAD_URL=$(echo "$RESPONSE" | grep -o '"uploadUrl":"[^"]*"' | cut -d'"' -f4)
FILE_KEY=$(echo "$RESPONSE" | grep -o '"fileKey":"[^"]*"' | cut -d'"' -f4)

if [ -z "$UPLOAD_ID" ]; then
  echo -e "${RED}Failed to extract Upload ID${NC}"
  exit 1
fi

echo -e "Upload ID: $UPLOAD_ID"

# 3. Upload File to S3 (PUT to the signed URL)
echo -e "\n${GREEN}[2/3] Uploading file to S3 (Presigned PUT)...${NC}"
echo "Headers being sent:"
echo "  x-amz-meta-original-filename: test_upload.png"
echo "  x-amz-meta-upload-id: $UPLOAD_ID"
echo "  x-amz-meta-user-id: $USER_ID"

# Note: Using eval to handle the URL properly if it contains special characters, 
# but mostly just quoting correctly is enough.
# Crucial: AWS requires the exact headers that were signed.
curl -v -X PUT "$UPLOAD_URL" \
  -H "Content-Type: image/png" \
  -H "x-amz-meta-original-filename: test_upload.png" \
  -H "x-amz-meta-upload-id: $UPLOAD_ID" \
  -H "x-amz-meta-user-id: $USER_ID" \
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

if [[ "$CONFIRM_RESPONSE" == *"success\":true"* || "$CONFIRM_RESPONSE" == *"COMPLETED"* ]]; then
    echo -e "\n${GREEN}✅ Test Sequence Completed Successfully!${NC}"
else
    echo -e "\n${RED}❌ Test Sequence Failed${NC}"
fi

# Cleanup
rm test_upload.png
