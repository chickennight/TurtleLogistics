package class2.a204.controller;

import class2.a204.dto.LogAddDTO;
import class2.a204.dto.LogDTO;
import class2.a204.dto.PayloadDTO;
import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.service.MachineService;
import class2.a204.service.MqttService;
import class2.a204.service.OrderService;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    public MachineController(MachineService machineService, MqttService mqttService, ErrorHandler errorHandler, OrderService orderService) {
        this.machineService = machineService;
        this.mqttService = mqttService;
        this.errorHandler = errorHandler;
        this.orderService = orderService;
    }

    @ApiOperation(value = "기기 상태 정보 반환", notes = "모든 기기 상태 정보를 반환하고 에러가 있는 기기가 있다면 에러 기기 로그 기록도 함께 반환")
    @GetMapping
    public ResponseEntity<?> getMachineStatus() {
        try {
            List<Machine> machineList = machineService.findMachineAll();
            if (machineList.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else {
                List<Integer> brokenList = machineService.brokenMachine(machineList);
                Map<String, List<?>> map = new HashMap<>();
                map.put("상태", machineList);
                List<Log> temp = machineService.lastBrokenLogs(brokenList);
                List<LogDTO> errorLogs = new ArrayList<>();
                for (Log l : temp) errorLogs.add(new LogDTO(l));
                map.put("로그", errorLogs);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "모든 로그 가져오기", notes = "저장된 모든 로그를 반환")
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

    @ApiOperation(value = "로그 생성", notes = "새 로그를 생성하고 저장")
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

    @ApiOperation(value = "기기 정보 업데이트", notes = "기기의 정보를 업데이트")
    @PutMapping("/{machineId}")
    public ResponseEntity<?> updateMachine(@PathVariable Integer machineId, @RequestBody boolean status) {
        try {
            machineService.updateMachine(status, machineId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "기기 제어", notes = "특정 기기를 제어하기 위한 MQTT 메시지를 발행")
    @PostMapping("/control")
    public ResponseEntity<?> controlMachine(@RequestBody PayloadDTO payloadDto) {
        try {
            mqttService.publish(payloadDto.getTopic(), payloadDto.getMessage());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

}
