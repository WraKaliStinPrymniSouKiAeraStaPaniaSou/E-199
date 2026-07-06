FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Fix pom.xml for JDK 17 compatibility
RUN sed -i \
  's|<source>1.7</source>|<source>11</source>|g; \
   s|<target>1.7</target>|<target>11</target>|g; \
   s|<version>3.1</version>|<version>3.11.0</version>|g; \
   /<execution>/,/<\/execution>/d' pom.xml

RUN mvn clean package -DskipTests

FROM tomcat:10-jdk17
WORKDIR /usr/local/tomcat

COPY --from=build /app/target/E-199-1.0-SNAPSHOT.war webapps/E-199.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
