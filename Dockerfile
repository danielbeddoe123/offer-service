FROM openjdk:8-jdk

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/offer-service/offer-service.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/offer-service/offer-service.jar
