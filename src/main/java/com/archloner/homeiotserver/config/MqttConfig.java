package com.archloner.homeiotserver.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MqttConfig {

    @Value("${mqtt.broker.url}")
    private String MQTT_BROKER_URL;

    @Bean
    public MqttAsyncClient mqttAsyncClient() throws Exception {
        String broker = MQTT_BROKER_URL;
        String clientId = "homeiot-server";

        MqttAsyncClient client = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(60);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                log.warn("MQTT connection lost: {}", throwable.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                log.info("Message arrived on {}: {}", topic, new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
        });

        client.connect(options).waitForCompletion();
        return client;
    }
}
