echo "Deploying Eureka Server..."
cd eurekaserver
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

cd chatbotgateway
ttab ./mvnw spring-boot:run
cd ..
