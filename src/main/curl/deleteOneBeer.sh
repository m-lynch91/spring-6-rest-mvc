#!/usr/bin/env bash

curl -X DELETE "http://localhost:8080/api/v1/beers/26233a12-69c7-467b-9254-75442745bd5e" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -s \
      -i > deleteOneBeerResponse.txt

echo "Response saved to deleteOneBeerResponse.txt"