source .env

./mvnw clean package -Dmaven.test.skip=true

docker rm -f $CONTAINER_NAME || true
docker run -d \
  --name $CONTAINER_NAME \
  -p 8080:8080 \
  -v $(pwd)/target/tennis-scoreboard.war:/usr/local/tomcat/webapps/tennis-scoreboard.war \
  $IMAGE_NAME