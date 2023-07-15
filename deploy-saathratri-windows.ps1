# As an Administrator, run the following command in a PowerShell
# To run this script run:
#   Set-ExecutionPolicy RemoteSigned
#   .\deploy-saathratri-windows.ps1
Set-Variable -Name CURRENT_FOLDER -Value (Get-Location)

echo "Deploying Booking Service..."
cd "$CURRENT_FOLDER/bookingservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying Chatbot Service..."
cd "$CURRENT_FOLDER/chatbotservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying Repair Service..."
cd "$CURRENT_FOLDER/repairservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying Customer Service..."
cd "$CURRENT_FOLDER/customerservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying Chatbot Gateway..."
cd "$CURRENT_FOLDER/chatbotgateway";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Check opened terminal windows to see deployment status of Saathratri Gateway and all its microservices."