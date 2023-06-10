echo "Deleting previously created directories..."
echo "Deleting docker-compose..."
rm -fr docker-compose 
echo "Deleting Booking Service..."
rm -fr bookingservice
echo "Deleting Chatbot Service..."
rm -fr chatbotservice
echo "Deleting Repair Service..."
rm -fr repairservice
echo "Customer Service Service..."
rm -fr customerservice
echo "Deleting Chatbot Gateway..."
rm -fr chatbotgateway
rm -fr node_modules
echo "Deleting package.json..."
rm package.json
rm package-lock.json
echo "Deleting .yo-repository..."
rm -fr .yo-repository
rm .yo-rc.json
echo "Deleting .jhipster..."
rm -fr .jhipster
echo "Deleting target..."
rm -fr target
