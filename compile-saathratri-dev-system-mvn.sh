echo "Compiling domain projects, then checking them into Maven repository..."

echo "Comiling Chatbot Gateway"
cd chatbotgateway
mvn clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Booking Microservice"
cd bookingservice
mvn clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Chatbot Microservice"
cd chatbotservice
mvn clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Repair Microservice"
cd repairservice
mvn clean package -Pdev,api-docs -DskipTests
cd ..

echo "Compiling Customer Microservice"
cd customerservice
mvn clean package -Pdev,api-docs -DskipTests
cd ..

echo "Completed compilation of Hotel Chatbot Development Version"
