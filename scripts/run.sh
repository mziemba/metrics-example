#!/bin/bash

# Script that builds and starts this application

ROOT_DIR=`dirname $0`/..
JAVA_ROOT_DIR=$ROOT_DIR

JAR_FILE=$JAVA_ROOT_DIR/target/metrics-example-1.0-SNAPSHOT-jar-with-dependencies.jar
CLASS_NAME=example.Main

CONFIG_PATH=src/main/resources
CLASSPATH=$JAR_FILE

# Enable JMX for reporting
VM_ARGS=""\
"-Dcom.sun.management.jmxremote "\
"-Dcom.sun.management.jmxremote.port=51111 "\
"-Dcom.sun.management.jmxremote.authenticate=false "\
"-Dcom.sun.management.jmxremote.ssl=false"

mvn clean assembly:assembly -DskipTests
java $* $VM_ARGS -cp $CLASSPATH $CLASS_NAME
