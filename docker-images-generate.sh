cd chatbotgateway
./mvnw -ntp -Pprod jib:dockerBuild 
cd ..

cd bookingservice
./mvnw -ntp -Pprod jib:dockerBuild 
cd ..

cd chatbotservice
./mvnw -ntp -Pprod jib:dockerBuild 
cd ..

cd customerservice
./mvnw -ntp -Pprod jib:dockerBuild 
cd ..

cd repairservice
./mvnw -ntp -Pprod jib:dockerBuild 
cd ..
