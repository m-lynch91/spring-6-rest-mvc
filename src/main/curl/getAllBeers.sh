#!/usr/bin/env bash

status=$(curl -s -o "getAllBeersResponse.json" -w "%{http_code}" -X GET "http://localhost:8080/api/v1/beers" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -u user:3b3f82eb-fa9b-4edf-aa48-951b41365a5d)

echo "Response saved to getAllBeersResponse.json"
echo "HTTP Status: $status"