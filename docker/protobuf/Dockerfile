# Inspired by https://github.com/yegor-usoltsev/protobuf-alpine
FROM alpine:3.7

ARG PROTOBUF_VERSION
ARG GRPC_JAVA_VERSION

ENV GLIBC_VERSION=2.32-r0
RUN apk --no-cache add wget curl \
    && wget -q https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub -O /etc/apk/keys/sgerrand.rsa.pub \
    && wget -q https://github.com/sgerrand/alpine-pkg-glibc/releases/download/${GLIBC_VERSION}/glibc-${GLIBC_VERSION}.apk -O glibc.apk \
    && apk add glibc.apk \
    && rm /etc/apk/keys/sgerrand.rsa.pub glibc.apk

RUN apk update && apk add --no-cache --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community grpc-java

RUN curl https://github.com/protocolbuffers/protobuf/releases/download/v${PROTOBUF_VERSION}/protoc-${PROTOBUF_VERSION}-linux-x86_64.zip -Lo protoc.zip \
    && unzip protoc.zip -d /usr/local \
    && rm protoc.zip

RUN apk del curl wget

WORKDIR /mnt/app

ENTRYPOINT ["protoc", "-I=/usr/local/include"]
