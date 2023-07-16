echo "Compiling domain projects, then checking them into Maven repository..."

echo "Compile and Install all DTO JARs..."

cd bookingservicedto
./mvnw clean install
cd ..

cd chatbotservicedto
./mvnw clean install
cd ..

cd repairservicedto
./mvnw clean install
cd ..

cd customerservicedto
./mvnw clean install
cd ..

echo "Comiling Chatbot Gateway"
cd chatbotgateway
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Booking Microservice"
cd bookingservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Chatbot Microservice"
cd chatbotservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Repair Microservice"
cd repairservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Customer Microservice"
cd customerservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Chatbot Orchestrator"
cd chatbotorchestrator
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Completed compilation of Hotel Chatbot Development Version"
