package class2.a204.service;

import class2.a204.dto.MachineDTO;
import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.repository.LogRepository;
import class2.a204.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MachineService {

    private final LogRepository logRepository;
    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(LogRepository logRepository, MachineRepository machineRepository) {
        this.logRepository = logRepository;
        this.machineRepository = machineRepository;
    }

    public List<Machine> findMachineAll() {
        return machineRepository.findAll();
    }

    public List<Log> findLogAll() {
        return logRepository.findAll();
    }

    public void addLog(Log log) {
        logRepository.save(log);
    }

    public List<Integer> brokenMachine(List<Machine> list) {
        List<Integer> brokenList = new ArrayList<>();
        for (Machine m : list)
            if (m.getBroken())
                brokenList.add(m.getMachineId());
        return brokenList;
    }

    public List<Log> lastBrokenLogs(List<Integer> brokenList) {
        List<Log> lastBrokenLogList = new ArrayList<>();
        for (Integer n : brokenList) lastBrokenLogList.add(logRepository.findAllByMachine_MachineIdOrderByErrorDateDesc(n).get(0));
        return lastBrokenLogList;
    }

    public void updateMachine(MachineDTO machineDto, Integer machineId) {
        Optional<Machine> foundMachine = machineRepository.findById(machineId);
        if(foundMachine.isEmpty()) {
            throw new RuntimeException("등록되지 않은 기기 : " + machineId);
        }
        Machine machine = foundMachine.get();
        if(machineDto.getMachineDetail() != null){
            machine.changeDetail(machineDto.getMachineDetail());
        }
        machine.changeBroken(machineDto.getBroken());
        machineRepository.save(machine);
    }

    public Machine findMachine(Integer machineId) {
        return machineRepository.findByMachineId(machineId);
    }
}
