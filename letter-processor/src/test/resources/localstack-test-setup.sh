#!/bin/bash

echo "Initialization started"

region="us-east-1"
topic_name="test-topic"
topic_arn="arn:aws:sns:us-east-1:000000000000:test-topic"
queue_name="test-queue"
notification_endpoint="arn:aws:sqs:us-east-1:000000000000:test-queue"

# Create SNS topic
awslocal sns create-topic --name ${topic_name}
echo "SNS topic '$topic_name' created successfully"

# Create SQS queue and subscribe to SNS topic
awslocal sqs create-queue --queue-name ${queue_name}
echo "SQS queue '$queue_name' created successfully"
awslocal sns subscribe --topic-arn ${topic_arn}  --protocol sqs --notification-endpoint ${notification_endpoint}
echo "SQS queue '$queue_name' subcribed to SNS topic '$topic_name' successfully"

#Create DynamoDB table
awslocal dynamodb create-table --cli-input-json \
'{
    "TableName":"christmas_letter",
    "KeySchema":[
       {
          "AttributeName":"email",
          "KeyType":"HASH"
       }
    ],
    "AttributeDefinitions":[
       {
          "AttributeName":"email",
          "AttributeType":"S"
       }
    ],
    "BillingMode":"PAY_PER_REQUEST"
 }'

echo "Initialization completed"
