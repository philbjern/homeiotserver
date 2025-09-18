#include "DHT.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

#define LED_PIN 5
#define DHTPIN 4
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

// Wi-Fi credentials
const char* ssid = "WIFI_SSID";
const char* password = "WIFI_PASSWORD";
const char* serverName = "SERVER_URL";

String deviceId;

void blinkLED(int times, int duration) {
  for (int i = 0; i < times; i ++) {
    digitalWrite(LED_PIN, HIGH);
    delay(duration);
    digitalWrite(LED_PIN, LOW);
    delay(duration);
  }
}

void blinkFast(int times) {
  for (int i = 0; i < times; i++) {
    digitalWrite(LED_PIN, HIGH);
    delay(100);
    digitalWrite(LED_PIN, LOW);
    delay(100);
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(LED_PIN, OUTPUT);

  deviceId = "esp8266-" + String(ESP.getChipId(), HEX);
  Serial.println("Device ID: " + deviceId);

  dht.begin();

  Serial.print("Connecting to WiFi");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Connected!");
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    float h = dht.readHumidity();
    float t = dht.readTemperature();

    int rawLight = analogRead(A0);
    float normLight = rawLight / 1023.0; // normalized 0-1

    if (isnan(h) || isnan(t)) {
      Serial.print("Failed to read from the DHT sensor!");
      delay(2000);
      return;
    }

    Serial.print("Temp: "); Serial.print(t);
    Serial.print(" *C  Hum: "); Serial.print(h);
    Serial.print(" %  Light: "); Serial.println(normLight);

    WiFiClient client;
    HTTPClient http;

    http.begin(client, serverName);
    http.addHeader("Content-Type", "application/json");

    // Build JSON
    StaticJsonDocument<256> doc;
    doc["deviceId"] = deviceId;
    doc["temperature"] = t;
    doc["humidity"] = h;
    doc["light"] = normLight;

    String requestBody;
    serializeJson(doc, requestBody);

    blinkFast(10);
    int httpResponseCode = http.POST(requestBody);

    if (httpResponseCode > 0) {
      Serial.print("Response: ");
      Serial.println(httpResponseCode);
    } else {
      Serial.print("Error: ");
      Serial.println(httpResponseCode);
    }

    http.end();
  }

  delay(60000); // every 60s
}
