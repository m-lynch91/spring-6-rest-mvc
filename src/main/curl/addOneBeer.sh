#!/usr/bin/env bash

curl -X POST "http://localhost:8080/api/v1/beers" \
      -H "accept: application/json" \
      -H "Content-Type: application/json" \
      -d '{
            "version" : 0,
            "beerName": "Collective Arts - Ransack the Universe",
            "beerStyle": "IPA",
            "upc": 1234567892,
            "quantityOnHand": 200,
            "price": 9.99
          }' \
      -i > "addOneBeerResponse.txt"

echo "Response saved to addOneBeerResponse.txt"