echo "Criando jar..."
./mvnw assembly:single

echo "Movendo jar gerado para pasta deploy..."
mkdir deploy
sudo cp ./target/gateway_server-1.0-alpha-jar-with-dependencies.jar ./deploy/gateway_server.jar