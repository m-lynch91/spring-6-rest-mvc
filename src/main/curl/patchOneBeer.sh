#!/usr/bin/env bash

curl -X PATCH "http://localhost:8080/api/v1/beers/26233a12-69c7-467b-9254-75442745bd5e" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -d '{
            "upc": 1907,
            "quantityOnHand": 1807,
            "price": 11.00
          }' \
      -i > "patchOneBeerResponse.txt"

echo "Response saved to patchOneBeerResponse.txt"