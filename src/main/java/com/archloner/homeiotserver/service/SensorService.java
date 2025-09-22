package com.archloner.homeiotserver.service;

import com.archloner.homeiotserver.dto.SensorReadingRequest;
import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {

    private final SensorRepository sensorRepository;
    private final MqttClient mqttClient;

    public SensorReading saveReading(SensorReadingRequest request) {
        SensorReading reading = new SensorReading();
        reading.setDeviceId(request.deviceId());
        reading.setTemperature(request.temperature());
        reading.setHumidity(request.humidity());
        reading.setLight(request.light());

        reading = sensorRepository.save(reading);
        log.info("Received a sensor reading: {}", reading);

        publishReadingsToMqtt(reading);
        return reading;
    }

    private void publishReadingsToMqtt(SensorReading reading) {
        try {
            publish("homeiot/" + reading.getDeviceId() + "/temperature", String.valueOf(reading.getTemperature()));
            publish("homeiot/" + reading.getDeviceId() + "/humidity", String.valueOf(reading.getHumidity()));
            publish("homeiot/" + reading.getDeviceId() + "/light", String.valueOf(Math.floor(reading.getLight() * 10000.0) / 100.0));
        } catch (Exception e) {
            log.error("Error publishing readings to MQTT broker: {}", e.getMessage());
        }
    }

    private void publish(String topic, String payload) throws Exception {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        mqttClient.publish(topic, message);
    }

    public List<SensorReading> getAllReadings() {
        return sensorRepository.findAll();
    }

    public List<SensorReading> getReadingsForDeviceId(String deviceId) {
        return sensorRepository.findByDeviceId(deviceId);
    }

}
