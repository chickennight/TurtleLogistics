package class2.a204.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper objectMapper = new ObjectMapper();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("order", "이것은 테스트 메세지 입니다.");
//        String jsonString = objectMapper.writeValueAsString(map);
        AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        String jsonString = objectMapper.writeValueAsString(message);
        client.connect();
        client.publish(topic, message);
        client.disconnect();
//        try {
//            client.connect();
//            client.publish(topic, message);
//            client.publish(topic, jsonString);
//            client.disconnect();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

    }
}