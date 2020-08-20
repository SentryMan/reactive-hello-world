
FROM openjdk:8

ENV SERVICE_NAME reactive-hello-world

ADD target/reactive-hello-world-*.jar $APP_HOME/reactive-hello-world.jar

RUN mkdir /opt/reactor-netty/

EXPOSE 9010

CMD java \
	-Dcom.sun.management.jmxremote=true \
	-Dcom.sun.management.jmxremote.local.only=false \
	-Dcom.sun.management.jmxremote.authenticate=false \
	-Dcom.sun.management.jmxremote.ssl=false \
	-Djava.rmi.server.hostname=localhost \
	-Dcom.sun.management.jmxremote.port=9010 \
	-Dcom.sun.management.jmxremote.rmi.port=9010 \ 
    -Xmx190M \
	-jar reactive-hello-world.jar
EXPOSE 8080

