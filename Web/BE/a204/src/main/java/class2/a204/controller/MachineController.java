package class2.a204.controller;

import class2.a204.model.Log;
import class2.a204.model.Machine;
import class2.a204.repository.LogRepository;
import class2.a204.repository.MachineRepository;
import class2.a204.service.ErrorHandler;
import class2.a204.service.MachineService;
import class2.a204.service.MqttService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "Machine")
@RequestMapping("/machine")
public class MachineController {
    private final MachineService MS;
    private final MqttService mqtt;
    private final ErrorHandler Handler;

    @Autowired
    public MachineController(MachineService ms, MqttService mqtt, ErrorHandler handler) {
        this.MS = ms;
        this.mqtt = mqtt;
        this.Handler = handler;
    }

    @GetMapping
    public ResponseEntity<?> getMachineStatus() {
        try {
            List<Machine> machineList = MS.findMachineAll();
            if (machineList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else {
                List<Integer> brokenList = MS.brokenMachine(machineList);
                if (brokenList.isEmpty())
                    return new ResponseEntity<>(machineList, HttpStatus.OK);
                else {
                    HashMap<String, List<?>> map = new HashMap<>();
                    map.put("status", machineList);
                    List<Log> errorLogs = MS.lastBrokenLogs(brokenList);
                    map.put("logs", errorLogs);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @GetMapping("/log")
    public ResponseEntity<?> getLogs() {
        try {
            List<Log> logList = MS.findLogAll();
            if (logList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(logList, HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<?> createLog(@RequestBody Log log) {
        try {
            MS.addLog(log);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateMachine(@RequestBody Machine machine) {
        try {
            MS.updateMachine(machine);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PostMapping("/control")
    public ResponseEntity<?> MachineControl(String order) {
        try {
            String topic = "Sup";
            mqtt.publish(topic, order);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

}
