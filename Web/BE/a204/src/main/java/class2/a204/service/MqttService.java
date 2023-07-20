package class2.a204.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    static String clientEndpoint = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com";
    static String clientId = "Esp_8266";
    static String awsAccessKeyId = "AKIA2ILMCFJQG5DYFRTR";
    static String awsSecretAccessKey = "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";


    public void publish(String topic, String payload) throws AWSIotException {
        AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, null);
        client.connect();
        client.publish(topic,payload);
        client.disconnect();
    }
}