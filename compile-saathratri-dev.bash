# As an Administrator, run the following command in a PowerShell
# To run this script run:
#   Set-ExecutionPolicy RemoteSigned
#   .\deploy-saathratri-windows.ps1
Set-Variable -Name CURRENT_FOLDER -Value (Get-Location)

echo "Compiling chatbotgateway..."
cd "$CURRENT_FOLDER/chatbotgateway";
./mvnw clean package -Pdev,api-docs -DskipTests;
cd ..

echo "Compiling bookingservice..."
cd "$CURRENT_FOLDER/bookingservice";
./mvnw clean package -Pdev,api-docs -DskipTests;
cd ..

echo "Compiling chatbotservice..."
cd "$CURRENT_FOLDER/chatbotservice";
./mvnw clean package -Pdev,api-docs -DskipTests;
cd ..

echo "Compiling repairservice..."
cd "$CURRENT_FOLDER/repairservice";
./mvnw clean package -Pdev,api-docs -DskipTests;
cd ..


echo "Compiling customerservice..."
cd "$CURRENT_FOLDER/customerservice";
./mvnw clean package -Pdev,api-docs -DskipTests;
cd ..

echo "Chatbot Gateway and Microservice should all be compiled now."