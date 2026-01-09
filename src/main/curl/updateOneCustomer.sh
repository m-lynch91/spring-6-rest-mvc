#!/usr/bin/env bash

curl -X PUT "http://localhost:8080/api/v1/customers/4a52c99d-7ff1-4f63-bf22-37230ad193e9" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -d '{
            "name": "Mike Johnson - Updated",
            "version": 1,
            "createdDate": "2025-12-23T15:52:20.4860899",
            "modifiedDate": "2025-12-23T15:52:20.4860899"
          }' \
      -i > "updateOneCustomerResponse.txt"

echo "Response saved to updateOneCustomerResponse.txt"