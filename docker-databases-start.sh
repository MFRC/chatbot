cd chatbotgateway
npm run docker:db:up
docker compose -f src/main/docker/keycloak.yml up -d
docker compose -f src/main/docker/jhipster-registry.yml up -d
cd ..

cd bookingservice
npm run docker:db:up
cd ..

cd chatbotservice
npm run docker:db:up
cd ..

cd customerservice
npm run docker:db:up
cd ..

cd repairservice
npm run docker:db:up
cd ..
