FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY /target/*.jar /app.jar
COPY src/main/resources/certs/simur.crt /simur.crt
RUN keytool -import -alias simur \
    -keystore "$JAVA_HOME/lib/security/cacerts" \
    -file /simur.crt \
    -storepass changeit -noprompt
ENTRYPOINT ["java","-jar","/app.jar"]