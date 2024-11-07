## 2024-10-21
- remove r2dbc as mix jpa and r2dbc is not best practice and involves lots of customization

## 2024-10-31
-  docker-compose up -d

## 2024-11-07
Steps to Apply Changes
Recreate the K6 container: If the old container still exists, remove it first:


`docker rm k6_test_container`
Rebuild the containers with Docker Compose:


`docker-compose up -d`
Verify that the K6 container is running:


`docker ps`
Enter the K6 container:


`docker exec -it k6_test_container /bin/sh`
This setup should keep the k6 container running, allowing you to access it with docker exec. Let me know how it goes!