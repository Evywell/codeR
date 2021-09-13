FROM mysql:8.0.23 AS base

ARG USER_ID
ARG GROUP_ID

# Map mysql user which will be used by the mysqld ("gosu mysql" in image entrypoint)
RUN usermod -u ${USER_ID} mysql \
    && groupmod -g ${GROUP_ID} mysql

# Set timezone
ENV TZ Europe/Paris
RUN rm /etc/localtime && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Random root password
ENV MYSQL_RANDOM_ROOT_PASSWORD=1

# Shell scripts
COPY bin/* /usr/local/bin/
RUN chmod 755 /usr/local/bin/*

# Create data directory
RUN mkdir -p /mnt/data \
    && chown -R mysql:mysql /mnt/data

#### DEVELOPMENT IMAGE
FROM base AS dev

COPY --chown=mysql:mysql conf/my.dev.cnf /etc/mysql/conf.d/mysql.cnf

ENTRYPOINT ["entrypoint.sh"]


#### UNIT TEST IMAGE
FROM base AS unit

COPY --chown=mysql:mysql conf/my.dev.cnf /etc/mysql/conf.d/mysql.cnf

RUN chmod -R go-rwx /etc/mysql/conf.d/*

ENTRYPOINT ["entrypoint.sh"]
