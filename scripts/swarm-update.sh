wget -q $1 -O docker-stack-bot.yml
echo "Deploying new na-tg-client stack"
docker stack rm na-tg-client
docker stack deploy --compose-file docker-stack-bot.yml --with-registry-auth na-tg-client