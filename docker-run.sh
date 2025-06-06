source .env

./mvnw clean package

docker rm -f $CONTAINER_NAME || true
docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME
docker cp target/tennis-scoreboard.war $CONTAINER_NAME:/usr/local/tomcat/webapps/
docker exec $CONTAINER_NAME catalina.sh stop
docker exec $CONTAINER_NAME catalina.sh start