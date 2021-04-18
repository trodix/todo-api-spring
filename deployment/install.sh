#!/bin/sh

# Get the project artifactId based on the .jar name
project_name=$(basename -s .jar $(ls /tmp/jenkins_tmp/target/*.jar | sort -V | tail -n 1));

# Create a workspace directory for this script
mkdir /tmp/jenkins_tmp/workspace

# Create a system user for the application
useradd -r ${project_name}

# Install the .jar and set the permissions
cp -p /tmp/jenkins_tmp/target/*.jar "/opt/${project_name}"
chown -R ${project_name}:${project_name} "/opt/${project_name}"
chmod +x "/opt/${project_name}/${project_name}.jar"

# Generate a systemd service for the application
cp /tmp/jenkins_tmp/deployment/templates/app.service.dist /tmp/jenkins_tmp/workspace/${project_name}.service
service_file="/tmp/jenkins_tmp/workspace/${project_name}.service"

## Replace the template variables for the systemd service
sed -i "s/<SERVICE_DESCRIPTION>/App ${project_name}/g" $service_file
sed -i "s/<APP_USER>/${project_name}/g" $service_file
sed -i "s/<PROJECT_FOLDER>/${project_name}/g" $service_file
sed -i "s/<JAR_NAME>/${project_name}/g" $service_file

## Copy the new generated service into the systemd services directory
cp $service_file /etc/systemd/system

# Reload the service configuration
systemctl daemon-reload

# Start / Restart the application
service ${project_name} restart
