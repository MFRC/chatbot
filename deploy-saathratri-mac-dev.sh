export "JAVA_HOME=\$(/usr/libexec/java_home -v 1.7)"

cd chatbotgateway
ttab ./mvnw spring-boot:run
cd ..

cd bookingservice
ttab ./mvnw spring-boot:run
cd ..

cd chatbotservice
ttab ./mvnw spring-boot:run
cd ..

cd customerservice
ttab ./mvnw spring-boot:run
cd ..

cd repairservice
ttab ./mvnw spring-boot:run
cd ..
