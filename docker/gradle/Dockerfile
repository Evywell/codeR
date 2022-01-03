FROM gradle:6.9.0-jdk11 as base

ARG USER_ID
ARG GROUP_ID

USER $USER_ID:$GROUP_ID
COPY jstatd.sh /home/gradle/jstatd.sh
CMD /home/gradle/jstatd.sh
WORKDIR /mnt/app

EXPOSE 9010

FROM base as dev
CMD tail -f /dev/null

FROM base as debug
