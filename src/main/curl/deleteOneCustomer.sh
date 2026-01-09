#!/usr/bin/env bash

curl -X DELETE "http://localhost:8080/api/v1/customers/d282800f-62ed-4130-886d-e4b583023fd6" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -s \
      -i > deleteOneCustomerResponse.txt

echo "Response saved to deleteOneCustomerResponse.txt"