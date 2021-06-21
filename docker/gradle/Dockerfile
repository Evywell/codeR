FROM gradle:6.8.2-jdk11 as base

ARG USER_ID
ARG GROUP_ID

RUN addgroup --system --gid $GROUP_ID user \
    && adduser --uid $USER_ID --gid $GROUP_ID --disabled-password --gecos "" user

USER user
WORKDIR /mnt/app

FROM base as dev
CMD tail -f /dev/null

FROM base as debug
