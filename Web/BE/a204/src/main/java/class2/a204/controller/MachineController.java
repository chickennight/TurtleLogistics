package class2.a204.controller;

import class2.a204.dto.LogAddDTO;
import class2.a204.dto.LogDTO;
import class2.a204.dto.PayloadDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@Api(tags = "Machine")
@RequestMapping("/machine")
public class MachineController {
    private final MachineService machineService;
    private final MqttService mqttService;
    private final ErrorHandler errorHandler;
    private final OrderService orderService;
    private final SmsService smsService;
    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MachineController(MachineService machineService, MqttService mqttService, ErrorHandler errorHandler, OrderService orderService, SmsService smsService, AdminService adminService, JwtTokenProvider jwtTokenProvider) {
        this.machineService = machineService;
        this.mqttService = mqttService;
        this.errorHandler = errorHandler;
        this.orderService = orderService;
        this.smsService = smsService;
        this.adminService = adminService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<?> getMachineStatus() {
        try {
            List<Machine> machineList = machineService.findMachineAll();
            if (machineList.size() == 0)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else {
                List<Integer> brokenList = machineService.brokenMachine(machineList);
                if (brokenList.size() == 0)
                    return new ResponseEntity<>(machineList, HttpStatus.OK);
                else {
                    Map<String, List<?>> map = new HashMap<>();
                    map.put("상태", machineList);
                    List<Log> temp = machineService.lastBrokenLogs(brokenList);
                    List<LogDTO> errorLogs = new ArrayList<>();
                    for (Log l : temp) errorLogs.add(new LogDTO(l));
                    map.put("로그", errorLogs);
                    List<Log> orderError = orderService.findOrderError();
                    List<LogDTO> processErrorLogs = new ArrayList<>();
                    for (Log l : orderError) processErrorLogs.add(new LogDTO(l));
                    map.put("인식 오류 로그", processErrorLogs);
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @GetMapping("/log")
    public ResponseEntity<?> getLogs() {
        try {
            List<Log> temp = machineService.findLogAll();
            List<LogDTO> logList = new ArrayList<>();
            for (Log l : temp) logList.add(new LogDTO(l));
            if (logList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(logList, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<?> createLog(@RequestBody LogAddDTO logAddDto) {
        try {
            Log input = new Log();
            //Log Entity에 메서드 추가
            if (logAddDto.getType() == 0)
                input.updateErrorMessage("포장 파트" + logAddDto.getMachineId() + "기기 이상 발생");
            else
                input.updateErrorMessage("분류 파트" + logAddDto.getMachineId() + "기기 이상 발생");

            input.updateMachine(machineService.findMachine(logAddDto.getMachineId()));
            machineService.addLog(input);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateMachine(@RequestBody Machine machine) {
        try {
            machineService.updateMachine(machine);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @PostMapping("/control")
    public ResponseEntity<?> MachineControl(@RequestBody PayloadDTO payload) {
        try {
            mqttService.publish(payload.getTopic(), payload.getMessage());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

}
