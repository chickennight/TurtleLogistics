package class2.a204.controller;

import class2.a204.dto.LogDto;
import class2.a204.dto.Payload;
import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.service.MachineService;
import class2.a204.service.MqttService;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            if (machineList.size() == 0)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else {
                List<Integer> brokenList = MS.brokenMachine(machineList);
                if (brokenList.size() == 0)
                    return new ResponseEntity<>(machineList, HttpStatus.OK);
                else {
                    HashMap<String, List<?>> map = new HashMap<>();
                    map.put("status", machineList);
                    List<Log> temp = MS.lastBrokenLogs(brokenList);
                    List<LogDto> errorLogs = new ArrayList<>();
                    for (Log l : temp) errorLogs.add(new LogDto(l));
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
            List<Log> temp = MS.findLogAll();
            List<LogDto> logList = new ArrayList<>();
            for (Log l : temp) logList.add(new LogDto(l));
            if (logList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(logList, HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<?> createLog(@RequestBody LogDto logDto) {
        try {
            Log input = new Log();
            input.setErrorMessage(logDto.getErrorMessage());
            input.setMachine(MS.findMachine(logDto.getMachineId()));
            MS.addLog(input);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateMachine(@RequestBody Machine machine) {
        try {
            MS.updateMachine(machine);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
