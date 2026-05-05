#!/bin/sh
#
# Copyright © 2015-2021 the original authors.
# Licensed under the Apache License, Version 2.0
#

# Attempt to set APP_HOME
APP_HOME=$(cd "$(dirname "$0")" && pwd -P) || exit

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# Determine the OS
OS_TYPE=$(uname -s)

JAVACMD=java
if ! command -v java >/dev/null 2>&1; then
    die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Setup the CLASSPATH
GRADLE_WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
    # Download the wrapper jar if missing
    if command -v curl >/dev/null 2>&1; then
        curl -fsSLo "$GRADLE_WRAPPER_JAR" "https://raw.githubusercontent.com/gradle/gradle/v8.8.0/gradle/wrapper/gradle-wrapper.jar" \
            || die "Could not download gradle-wrapper.jar"
    else
        die "gradle-wrapper.jar is missing and curl is not available to download it."
    fi
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
    "-Dorg.gradle.appname=gradlew" \
    -classpath "$GRADLE_WRAPPER_JAR" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
