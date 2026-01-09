#!/usr/bin/env bash

curl -X GET "http://localhost:8080/api/v1/customers" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" > "getAllCustomersResponse.json" \
      -s

echo "Response saved to getAllCustomersResponse.json"