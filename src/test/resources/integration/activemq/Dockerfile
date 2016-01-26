FROM java:8

RUN curl http://www.mirrorservice.org/sites/ftp.apache.org/activemq/5.13.0/apache-activemq-5.13.0-bin.tar.gz | tar -xz

EXPOSE 61612 61613 61616 8161

RUN mv apache-activemq-5.13.0/conf/activemq.xml apache-activemq-5.13.0/conf/activemq.xml.orig
RUN awk '/.*stomp.*/{print "            <transportConnector name=\"stomp+ssl\" uri=\"stomp+ssl://0.0.0.0:61612\"/>"}1' apache-activemq-5.13.0/conf/activemq.xml.orig >> apache-activemq-5.13.0/conf/activemq.xml

CMD java -Xms1G -Xmx1G -Djava.util.logging.config.file=logging.properties -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote -Djava.io.tmpdir=apache-activemq-5.13.0/tmp -Dactivemq.classpath=apache-activemq-5.13.0/conf -Dactivemq.home=apache-activemq-5.13.0 -Dactivemq.base=apache-activemq-5.13.0 -Dactivemq.conf=apache-activemq-5.13.0/conf -Dactivemq.data=apache-activemq-5.13.0/data -jar apache-activemq-5.13.0/bin/activemq.jar start
