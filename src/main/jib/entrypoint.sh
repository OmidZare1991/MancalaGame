#!/bin/sh

echo "The application will start in ${DOCKER_INIT_SLEEP}s..." && sleep ${DOCKER_INIT_SLEEP}
exec java ${JAVA_OPTS} -noverify -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/*:. ${MAIN_CLASS_PATH}  "$@"




