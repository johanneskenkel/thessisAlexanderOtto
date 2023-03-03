# Test Applications

## Database

The applications need a running database on the port 5432 to start. To start postgres SQL in docker, use the command:
````
docker run -d --name thesis_postgres -p "5432:5432" -e POSTGRES_PASSWORD=12345 postgres:alpine3.17
````

## Prometheus

To read the metrics from the app, prometheus is used. To start prometheus in docker, use the command:
````
// the prometheus config has the location <Project>/prometheus_config
docker run -d --name thesis_prometheus -p "9090:9090" -v /path/to/config:/etc/prometheus prom/prometheus:v2.42.0
````