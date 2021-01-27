#!/bin/bash
cd "$(dirname "$0")"

proto_name=$1
protos_path=../src/main/protos
template_path=${protos_path}/proto.template
proto_path=${protos_path}/$1.proto

cat ${template_path} > ${proto_path}
sed -i "s/##PROTONAME##/${proto_name}/g" ${proto_path}
