#!/usr/bin/env bash

status=$(curl -s -o "getAllBeersResponse.json" -w "%{http_code}" -X GET "http://localhost:8080/api/v1/beers" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -u user:51d2e4ff-21df-40d1-974b-2160a6a3cb39)

echo "Response saved to getAllBeersResponse.json"
echo "HTTP Status: $status"