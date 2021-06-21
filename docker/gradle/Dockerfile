FROM gradle:6.8.2-jdk11 as base

ARG USER_ID
ARG GROUP_ID

USER root
WORKDIR /mnt/app

FROM base as dev
CMD tail -f /dev/null

FROM base as debug
