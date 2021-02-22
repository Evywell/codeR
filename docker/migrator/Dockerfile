FROM phpdockerio/php74-fpm:latest
WORKDIR /mnt/app

# Fix debconf warnings upon build
ARG DEBIAN_FRONTEND=noninteractive
ARG USER_ID
ARG GROUP_ID

RUN addgroup --system --gid $GROUP_ID user \
    && adduser --uid $USER_ID --gid $GROUP_ID --disabled-password --gecos "" user

# Install selected extensions and other stuff
RUN apt-get update \
    && apt-get -y --no-install-recommends install  php7.4-mysql git \
    && apt-get clean; rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /usr/share/doc/*

USER user
