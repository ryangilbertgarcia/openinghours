mvn clean test install
docker build --tag=wildfly-openinghours .
docker run -it -p 8080:8080 -p 9990:9990 wildfly-openinghours

