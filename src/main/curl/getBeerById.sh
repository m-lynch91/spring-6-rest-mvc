#!/usr/bin/env bash

curl -X GET "http://localhost:8080/api/v1/beers/381cc21d-e486-42d1-b72e-f073868094a5" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" > "getBeerByIdResponse.json" \
      -s

echo "Response saved to getBeerByIdResponse.json"