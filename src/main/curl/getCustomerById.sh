#!/usr/bin/env bash

curl -X GET "http://localhost:8080/api/v1/customers/20168aa5-2808-4143-805f-e5ec005ba763" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" > "getCustomerByIdResponse.json" \
      -s

echo "Response saved to getCustomerByIdResponse.json"