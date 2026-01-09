#!/usr/bin/env bash

curl -X PUT "http://localhost:8080/api/v1/beers/f4424260-c0ed-4036-a64c-c6ffd7acaf34" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -d '{
            "beerName": "10 Ton - UPDATED",
            "beerStyle": "STOUT",
            "upc": 2333,
            "quantityOnHand": 2333,
            "price": 10.00
          }' \
      -i > "updateOneBeerResponse.txt"

echo "Response saved to updateOneBeerResponse.txt"