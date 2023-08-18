package class2.a204.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MqttService {
    @Value("${aws.iot.clientEndpoint}")
    private String clientEndpoint;

    @Value("${aws.iot.clientId}")
    private String clientId;

    @Value("${aws.iot.awsAccessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.iot.awsSecretAccessKey}")
    private String awsSecretAccessKey;

    public void publish(String topic, String message) throws JsonProcessingException, AWSIotException {
        AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();
        client.publish(topic, message);
        client.disconnect();
    }
}