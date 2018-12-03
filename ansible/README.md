# Workflow / Installs
 - openJDK8
 - GIT
 - Maven3
 - Postgres-DB
 - setup postgres-db and user
 - get latest sitemap-project from git
 - build it using maven
 - deploy springboot-application(jar) as service / daemon
# Run
ansible-playbook sitemap_app04.yml --ask-become-pass