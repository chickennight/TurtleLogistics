package class2.a204.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    static String clientEndpoint = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com";
    static String clientId = "Supervisor";
    static String awsAccessKeyId = "AKIA2ILMCFJQG5DYFRTR";
    static String awsSecretAccessKey = "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";


    public void publish(String topic, String message) throws AWSIotException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("order", "이것은 테스트 메세지 입니다.");
//        String jsonString = objectMapper.writeValueAsString(map);
        String jsonString = objectMapper.writeValueAsString(message);
        AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();
        client.publish(topic, jsonString);
        client.disconnect();
    }
}