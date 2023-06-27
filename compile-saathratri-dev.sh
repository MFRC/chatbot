echo "Compiling domain projects, then checking them into Maven repository..."

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

echo "Completed compilation of Hotel Chatbot Development Version"
