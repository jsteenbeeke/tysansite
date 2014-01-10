#!/bin/bash
PORT=14128
REMOTE_PATH=/var/lib/jetty8/webapps
URL=www.tysanclan.com
ACCOUNT=root
TARGET_NAME=dist.zip

TARGET=`ls target/ProjectEwok*.war`
scp -P $PORT $TARGET $ACCOUNT@$URL:$REMOTE_PATH/$TARGET_NAME
