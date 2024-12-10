#!/bin/bash

echo "Initialization started"

# Create SNS topic
awslocal sns create-topic --name christmas-letter
echo "SNS topic created christmas-letter successfully"

echo "Initialization completed"