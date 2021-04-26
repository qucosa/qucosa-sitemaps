#!/bin/bash

echo "app.db.url=$PSQL_URL" > docker.properties

if [ ! -z "$PSQL_USER" ]
then
  echo "app.db.username=$PSQL_USER" >> docker.properties
fi

if [ ! -z "$PSQL_PASSWD" ]
then
  echo "app.db.password=$PSQL_PASSWD" >> docker.properties
fi

if [ ! -z "$SERVER_PORT" ]
then
  echo "server.port=$SERVER_PORT" >> docker.properties
fi

for i in $@; do
  if [ -f "$i" ]
  then
    u="${u:+$u,}file://$i"
  fi
done

if [ ! -z "$u" ]
then
  java -jar qucosa-sitemaps-0.0.1-SNAPSHOT.jar "-Dspring.config.location=./docker.properties, $u"
else
  java -jar qucosa-sitemaps-0.0.1-SNAPSHOT.jar "-Dspring.config.location=./docker.properties"
fi