cd chatbotgateway
npm run docker:db:up
ttab ./mvnw spring-boot:run
cd ..

cd bookingservice
npm run docker:db:up
ttab ./mvnw spring-boot:run
cd ..

cd chatbotservice
npm run docker:db:up
ttab ./mvnw spring-boot:run
cd ..

cd customerservice
npm run docker:db:up
ttab ./mvnw spring-boot:run
cd ..

cd repairservice
npm run docker:db:up
ttab ./mvnw spring-boot:run
cd ..

cd chatbotgateway
ttab docker compose -f src/main/docker/keycloak.yml up -d
ttab docker compose -f src/main/docker/jhipster-registry.yml up -d
