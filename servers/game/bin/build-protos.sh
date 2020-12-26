#!/bin/bash
cd $(dirname $0) || exit 1
protoc=../libs/protoc-3.14.0-osx-x86_64/bin/protoc

# main protos
DST_DIR=../src/main/java
SRC_DIR=../src/main/protos

if [ -n "$(ls -A $SRC_DIR)" ]; then
  $protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/*.proto
fi

# tests protos
TEST_DST_DIR=../src/test/java
TEST_SRC_DIR=../src/test/protos

if [ -n "$(ls -A $TEST_SRC_DIR)" ]; then
  $protoc -I=$TEST_SRC_DIR --java_out=$TEST_DST_DIR $TEST_SRC_DIR/*.proto
fi