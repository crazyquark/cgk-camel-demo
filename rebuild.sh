alias dc='docker-compose'
dc down | true && mvn clean && mvn package && dc build --no-cache

