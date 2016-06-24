alias dc='docker-compose'
d-c down | true && mvn clean && mvn package && d-c build --no-cache

