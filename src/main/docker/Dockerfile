FROM release.onegini.com/library/openjdk8:1.8.0.232.b09
MAINTAINER Onegini <info@onegini.com>

# Add jar file
ADD maven /

# Miscellaneous commands
RUN chown -R onegini:onegini /opt
USER onegini
CMD /usr/bin/java -Dfile.encoding=UTF-8 -jar /opt/onegini/app.jar
