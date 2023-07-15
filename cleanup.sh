echo "Deleting previously created directories..."
echo "Deleting docker-compose..."
rm -fr docker-compose 
echo "Deleting Booking Service..."
rm -fr bookingservice
rm -fr bookingservicedto
echo "Deleting Chatbot Service..."
rm -fr chatbotservice
rm -fr chatbotservicedto
echo "Deleting Repair Service..."
rm -fr repairservice
rm -fr repairservicedto
echo "Deleting Customer Service Service..."
rm -fr customerservice
rm -fr customerservicedto
echo "Deleting Chatbot Gateway..."
rm -fr chatbotgateway
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
echo "Deleting node_modules..."
rm -fr node_modules

