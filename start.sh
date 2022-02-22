mvn clean package -DskipTests
docker-compose -f docker-compose.yml up --build
