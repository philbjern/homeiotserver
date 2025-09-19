# Home IoT Server

A self-hosted Spring Boot application for collecting and visualizing sensor data from ESP8266/ESP32 devices.

## ✨ Features:

* REST API for storing sensor readings (temperature, humidity, light, etc.).

* PostgreSQL backend with support for raw data and time-bucketed aggregation.

* Grafana integration for dashboards and visualization.

* Configurable alerts via Discord webhooks (thresholds stored in DB).

* API key authentication for devices.

* Systemd service support for easy deployment on Linux servers.

## 🛠️ Requirements

* Java 21+

* Maven 3.8+

* PostgreSQL 14+

* Grafana (optional, for dashboards)

## 🚀 Setup

###  1. Clone & Build

```
git clone https://github.com/yourusername/homeiotserver.git
cd homeiotserver
mvn clean package -DskipTests
```

### 2. Database

Create database and user in PostgreSQL:

```
CREATE DATABASE homeiot;
CREATE USER homeiotuser WITH ENCRYPTED PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE homeiot TO homeiotuser;
```

Schema example (simplified):

```
CREATE TABLE readings (
    id SERIAL PRIMARY KEY,
    device_id TEXT NOT NULL,
    timestamp_utc TIMESTAMPTZ NOT NULL,
    epoch_ms BIGINT NOT NULL,
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    light DOUBLE PRECISION
); 
```

### 3. Configuration

Set database credentials in application.yml or environment variables:

```
spring:
datasource:
url: jdbc:postgresql://localhost:5432/homeiot
username: homeiotuser
password: yourpassword
```

### 4. Run

```
java -jar target/homeiotserver-0.0.1-SNAPSHOT.jar
```

Or as a systemd service (recommended):

```
sudo cp homeiotserver.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable homeiotserver
sudo systemctl start homeiotserver
```

### 5. Grafana Integration

Use PostgreSQL as a Grafana datasource and run queries like:

```
SELECT
  extract(epoch from timestamp_utc) * 1000 AS time,
  temperature,
  humidity,
  light
FROM readings
WHERE $__timeFilter(timestamp_utc)
ORDER BY timestamp_utc
```

## 📡 API Example

Send sensor data from ESP8266:

```
POST /api/sensors
Content-Type: application/json
Authorization: ApiKey your-api-key

{
  "deviceId": "esp8266-e07757",
  "temperature": 21.5,
  "humidity": 55.2,
  "light": 0.76
}
```