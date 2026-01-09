#!/usr/bin/env bash

status=$(curl -s -o "getAllBeersResponse.json" -w "%{http_code}" -X GET "http://localhost:8080/api/v1/beers" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -u user1:password)

echo "Response saved to getAllBeersResponse.json"
echo "HTTP Status: $status"