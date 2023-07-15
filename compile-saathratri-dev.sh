echo "Compiling domain projects, then checking them into Maven repository..."

echo "Comiling Chatbot Gateway"
cd chatbotgateway
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Booking Microservice"
cd bookingservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

cd bookingservicedto
./mvnw clean install
cd ..

echo "Compiling Chatbot Microservice"
cd chatbotservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

cd chatbotservicedto
./mvnw clean install
cd ..

echo "Compiling Repair Microservice"
cd repairservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

cd repairservicedto
./mvnw clean install
cd ..

echo "Compiling Customer Microservice"
cd customerservice
./mvnw clean package -Pdev,api-docs -DskipTests
cd ..

cd customerservicedto
./mvnw clean install
cd ..

echo "Completed compilation of Hotel Chatbot Development Version"
