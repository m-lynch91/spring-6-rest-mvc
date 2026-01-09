#!/usr/bin/env bash

curl -X POST "http://localhost:8080/api/v1/customers" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -d '{
            "name": "Samwise Gamgee",
            "version": 1,
            "createdDate": "2025-05-17T01:02:39.4860899",
            "modifiedDate": "2025-05-17T01:02:39.4860899"
          }' \
      -i > "addOneCustomerResponse.txt"

echo "Response saved to addOneCustomerResponse.txt"