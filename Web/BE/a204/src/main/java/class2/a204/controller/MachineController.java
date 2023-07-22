package class2.a204.controller;

import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.dto.Payload;
import class2.a204.service.MqttService;
import class2.a204.util.ErrorHandler;
import class2.a204.service.MachineService;
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
        System.out.println("DDDDDDDDDDD");
        try {
            List<Machine> machineList = MS.findMachineAll();
            if (machineList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else {
                System.out.println("DDDDDDDDDDD");
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

    @PutMapping("/{machine_id}")
    public ResponseEntity<?> updateMachine(@RequestParam int machine_id,@RequestBody Machine machine) {
        try {
            Machine m = MS.findMachineById(machine_id);
            m.setMachineDetail(machine.getMachineDetail());
            m.setBroken(machine.getBroken());
            MS.updateMachine(m);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PostMapping("/control")
    public ResponseEntity<?> MachineControl(@RequestBody Payload payload) {
        try {
            mqtt.publish(payload.getTopic(), payload.getMessage());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

}
