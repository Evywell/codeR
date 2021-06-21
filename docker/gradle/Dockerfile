FROM gradle:6.9.0-jdk11-openj9 as base

ARG USER_ID
ARG GROUP_ID

USER $USER_ID:$GROUP_ID
WORKDIR /mnt/app

FROM base as dev
CMD tail -f /dev/null

FROM base as debug
