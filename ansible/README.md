# Workflow / Installs
 - openJDK8
 - GIT
 - Maven3
 - Postgres-DB
 - setup postgres-db and user
 - get latest sitemap-project from git
 - build it using maven
 - deploy springboot-application(jar) as service / daemon
# Run (only works with ssh-key stored for user)
ansible-playbook deploy_sitemap.yml -u tolksdorf --ask-become-pass
# Run (works with password auth)
ansible-playbook deploy_sitemap.yml -u tolksdorf --ask-become-pass -i hosts.ini --extra-vars "host=sitemapprod ssh_user=tolksdorf"
ansible-playbook deploy_sitemap.yml -u tolksdorf --ask-become-pass -i hosts.ini --extra-vars "host=sitemapprod ssh_user=tolksdorf gitlab_user=tolksdorf gitlab_pw=xxx application_properties_file=application-prod.properties"

Use application_properties_file={filename} to use different environments.
    - Production: application_properties_file=application-prod.properties
    - Development: application_properties_file=application-dev.properties

# Run with Vagrant
ansible-playbook --private-key=~/Projekte/sitemap-vagrant/.vagrant/machines/standalone/virtualbox/private_key -u vagrant deploy_sitemap.yml -vvv