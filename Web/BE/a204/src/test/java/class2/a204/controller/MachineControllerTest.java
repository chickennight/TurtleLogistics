package class2.a204.controller;

import class2.a204.repository.MachineRepository;
import class2.a204.service.MqttService;
import com.amazonaws.services.iot.client.AWSIotException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MachineControllerTest {
    private final MachineRepository MR;
    private final MqttService mqtt;

    @Autowired
    MachineControllerTest(MachineRepository mr, MqttService mqtt) {
        MR = mr;
        this.mqtt = mqtt;
    }

    @Test
    void getMachineStatus() {
    }

    @Test
    void getLogs() {
    }

    @Test
    void createLog() {
    }

    @Test
    void updateMachine() throws IOException {
    }

    @Test
    void machineControl() throws AWSIotException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString("hello");
        System.out.println(jsonString);
        mqtt.publish("Sup",jsonString);
    }

    @Test
    void extraTest(){
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get("src/main/resources/aws/AmazonRootCA1.pem"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            System.out.println(kf.generatePublic(spec));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}