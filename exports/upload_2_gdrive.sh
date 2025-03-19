#!/bin/bash
FILE_PATH="$1"
ACCESS_TOKEN="$2"
FILE_NAME=$(basename "$FILE_PATH")

curl -X POST -L \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -F "metadata={name:'$FILE_NAME'};type=application/json;charset=UTF-8" \
  -F "file=@$FILE_PATH" \
  "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart"

