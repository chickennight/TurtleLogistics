package class2.a204.service;

import class2.a204.dto.MessageDTO;
import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.repository.LogRepository;
import class2.a204.repository.MachineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MachineServiceTest {


    private final LogRepository logRepository;
    private final MachineRepository machineRepository;
    private final MachineService machineService;
    private final SmsService SS;

    @Autowired
    public MachineServiceTest(LogRepository logRepository, MachineRepository machineRepository, MachineService machineService, SmsService ss) {
        this.logRepository = logRepository;
        this.machineRepository = machineRepository;
        this.machineService = machineService;
        SS = ss;
    }

    @Test
    public void testFindMachineAll() {
        List<Machine> machineList = new ArrayList<>();
        for (int i = 0; i < 100; ++i)
            machineList.add(new Machine());

        System.out.println(machineList.size());
        machineRepository.saveAll(machineList);
        System.out.println(machineList.size());

        List<Machine> result = machineService.findMachineAll();
        System.out.println(result.size());

        assertEquals(machineList, result);
    }

    @Test
    public void testFindMachineById() {
        Machine machine = new Machine(1, "바보", true);

        machine = machineRepository.save(machine);

    }

    @Test
    public void testBrokenMachine() throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
//        List<Machine> machineList = new ArrayList<>();
//        machineList.add(new Machine(1, "1", true));
//        machineList.add(new Machine(2, "1"));
//        machineList.add(new Machine(3, "1", true));
//        machineList.add(new Machine(4, "1"));
//        machineList.add(new Machine(5, "1", true));
//        machineList.add(new Machine(6, "1"));
//        machineRepository.saveAll(machineList);
//
//        List<Machine> temp = machineRepository.findAll();
//        System.out.println(temp.size());
//        List<Integer> result = machineService.brokenMachine(temp);
//
//        assertEquals(3, result.size());
        MessageDTO sms = new MessageDTO("01047368221", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!권도현 바보!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        SS.sendSms(sms);
    }

    @Test
    public void testLastBrokenLogs() {
        List<Integer> machineIds = List.of(1, 2, 3);

        // Assuming some logs are present for each machine

        List<Log> result = machineService.lastBrokenLogs(machineIds);

        // Verify that the result contains only the last log for each machine
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getLogNum());
        assertEquals(2, result.get(1).getLogNum());
        assertEquals(0, result.get(2).getLogNum()); // Empty list, so the log ID should be 0
    }

    @Test
    public void testFindLogAll() {
        List<Log> logList = new ArrayList<>();
        // Add some mock Log objects to logList
        // ...

        when(logRepository.findAll()).thenReturn(logList);

        List<Log> result = machineService.findLogAll();

        assertEquals(logList, result);
    }

    @Test
    public void testAddLog() {
        Log log = new Log();
        // Set properties of the mock Log object
        // ...

        machineService.addLog(log);

        // Verify that the save method of LogRepository was called with the log object
        verify(logRepository, times(1)).save(log);
    }

    @Test
    public void testUpdateMachine() {
        Machine machine = new Machine();
        // Set properties of the mock Machine object
        // ...

        machineService.updateMachine(machine);

        // Verify that the save method of MachineRepository was called with the machine object
        verify(machineRepository, times(1)).save(machine);
    }
}
