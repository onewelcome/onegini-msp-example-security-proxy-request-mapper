FROM docker.io/library/openjdk:11.0.8-jre-slim

# Add jar file
ADD --chown=1621 maven /

USER 1621
CMD java -Dfile.encoding=UTF-8 -jar /opt/onegini/app.jar
