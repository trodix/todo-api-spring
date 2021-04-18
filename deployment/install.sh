#!/bin/sh

cp -p /tmp/jenkins_tmp/target/*.jar /opt/todoapi
chown -R todoapi:todoapi /opt/todoapi
chmod +x /opt/todoapi/todoapi.jar

service todoapi restart