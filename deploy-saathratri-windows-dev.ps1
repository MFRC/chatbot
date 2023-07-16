# As an Administrator, run the following command in a PowerShell
# To run this script run:
#   Set-ExecutionPolicy RemoteSigned
#   .\deploy-saathratri-windows.ps1
Set-Variable -Name CURRENT_FOLDER -Value (Get-Location)

echo "Deploying Eureka Server..."
cd "$CURRENT_FOLDER/eurekaserver";
invoke-expression 'cmd /c start powershell -NoExit -Command {
  ./mvnw clean spring-boot:run;
}';
cd ..

echo "Deploying Organizations Service..."
cd "$CURRENT_FOLDER/organizationsservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying Saathratri Maintenance Service..."
cd "$CURRENT_FOLDER/saathratrimaintenanceservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying TAJ Vote Service..."
cd "$CURRENT_FOLDER/tajvoteservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..


echo "Deploying Geonames Service..."
cd "$CURRENT_FOLDER/geonamesservice";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Deploying Saathratri Orchestrator..."
cd "$CURRENT_FOLDER/saathratriorchestrator";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev' spring-boot:run;
}";
cd ..

echo "Deploying Saathratri Message Sender..."
cd "$CURRENT_FOLDER/saathratrimessagesender";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev' spring-boot:run;
}";
cd ..

echo "Deploying Saathratri Gateway..."
cd "$CURRENT_FOLDER/saathratrigateway";
invoke-expression "cmd /c start powershell -NoExit -Command {
  ./mvnw '-Dspring.profiles.active=dev,no-liquibase' spring-boot:run;
}";
cd ..

echo "Check opened terminal windows to see deployment status of Saathratri Gateway and all its microservices."