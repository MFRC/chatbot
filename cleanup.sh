echo "Deleting previously created directories..."
echo "Deleting docker-compose..."
rm -r docker-compose 
echo "Deleting Booking Service..."
rm -r bookingservice
echo "Deleting Chatbot Service..."
rm -r chatbotservice
echo "Deleting Repair Service..."
rm -r repairservice
echo "Deleting Customer Service Service..."
rm -r customerservice
echo "Deleting Chatbot Gateway..."
rm -r chatbotgateway
rm -r node_modules
echo "Deleting package.json..."
rm package.json
rm package-lock.json
echo "Deleting .yo-repository..."
rm -r .yo-repository
rm .yo-rc.json
echo "Deleting .jhipster..."
rm -r .jhipster
echo "Deleting target..."
rm -r target
