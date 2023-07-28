package class2.a204.controller;

import class2.a204.dto.LogAddDto;
import class2.a204.dto.LogDto;
import class2.a204.dto.MessageDTO;
import class2.a204.dto.Payload;
import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.service.*;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@Api(tags = "Machine")
@RequestMapping("/machine")
public class MachineController {
    private final MachineService MS;
    private final MqttService mqtt;
    private final ErrorHandler Handler;
    private final OrderService OS;
    private final SmsService SS;
    private final AdminService AS;
    private final JwtTokenProvider JP;

    @Autowired
    public MachineController(MachineService ms, MqttService mqtt, ErrorHandler handler, OrderService os, SmsService ss, AdminService as, JwtTokenProvider jp) {
        MS = ms;
        this.mqtt = mqtt;
        Handler = handler;
        OS = os;
        SS = ss;
        AS = as;
        JP = jp;
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
                    Map<String, List<?>> map = new HashMap<>();
                    map.put("상태", machineList);
                    List<Log> temp = MS.lastBrokenLogs(brokenList);
                    List<LogDto> errorLogs = new ArrayList<>();
                    for (Log l : temp) errorLogs.add(new LogDto(l));
                    map.put("로그", errorLogs);
                    List<Log> orderError = OS.findOrderError();
                    List<LogDto> processErrorLogs = new ArrayList<>();
                    for (Log l : orderError) processErrorLogs.add(new LogDto(l));
                    map.put("인식 오류 로그", processErrorLogs);
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
    public ResponseEntity<?> createLog(@RequestBody LogAddDto logAddDto) {
        try {
            Log input = new Log();
            if (logAddDto.getType() == 0)
                input.setErrorMessage("포장 파트" + logAddDto.getMachineId() + "기기 이상 발생");
            else
                input.setErrorMessage("분류 파트" + logAddDto.getMachineId() + "기기 이상 발생");

            input.setMachine(MS.findMachine(logAddDto.getMachineId()));
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
            return new ResponseEntity<>(HttpStatus.CREATED);
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
