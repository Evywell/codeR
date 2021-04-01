#!/bin/bash
cd "$(dirname "$0")"

# Setup private/public key
RESOURCE_DIR="../../src/test/resources"
PRIVATE_KEY="${RESOURCE_DIR}/private.pem"
PUBLIC_KEY="${RESOURCE_DIR}/public.pem"

openssl genpkey -out $PRIVATE_KEY -algorithm RSA -pkeyopt rsa_keygen_bits:2048
openssl rsa -in $PRIVATE_KEY -pubout -out $PUBLIC_KEY
