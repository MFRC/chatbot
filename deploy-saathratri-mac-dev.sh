# Before running this script, install ttab by running the command: npm -g install ttab
# Since Saathratri Orchestrator has a startup/scheduled task that depends on other microservices like Organizations Service,
# we start/deploy the orchestrator separately once all other services have been deployed.

CURRENT_FOLDER="$PWD"

echo "Deploying Eureka Server..."
ttab -t eurekaserver -d "$CURRENT_FOLDER/eurekaserver" ./mvnw clean spring-boot:run

echo "Deploying Booking Service..."
ttab -t bookingservice -d "$CURRENT_FOLDER/bookingservice" ./mvnw spring-boot:run -Dspring.profiles.active=dev,no-liquibase -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8006"

echo "Deploying Chatbot Service Service..."
ttab -t chatbotservice -d "$CURRENT_FOLDER/chatbotservice" ./mvnw spring-boot:run -Dspring.profiles.active=dev,no-liquibase -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8005"

echo "Deploying Repair Service..."
ttab -t repairservice -d "$CURRENT_FOLDER/repairservice" ./mvnw spring-boot:run -Dspring.profiles.active=dev,no-liquibase -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8007"

echo "Deploying Customer Service Service..."
ttab -t customerservice -d "$CURRENT_FOLDER/customerservice" ./mvnw spring-boot:run -Dspring.profiles.active=dev,no-liquibase -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8008"

echo "Deploying Chatbot Gateway..."
ttab -t chatbotgateway -d "$CURRENT_FOLDER/chatbotgateway" ./mvnw spring-boot:run -Dspring.profiles.active=dev,no-liquibase -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8001"

echo "Check opened tabs to see deployment status of Chatbot Gateway and all its microservices."
