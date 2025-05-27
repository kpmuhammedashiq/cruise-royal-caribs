# cruise-royal-caribs


**Maven:**
mvn clean package
docker network create proxy-net


**ship:**
docker build -t ship .
docker run -d --name ship --network proxy-net -p 8080:8080 ship


**offshore-server:**
docker build -t offshore-server .
docker run -d --name offshore-server --network proxy-net -p 8082:8082 offshore-server
