FROM tomcat:9.0.96-jre21-temurin-noble

ARG APP_NAME=task-servlets

COPY /build/libs/${APP_NAME}.war /usr/local/tomcat/webapps/ROOT.war