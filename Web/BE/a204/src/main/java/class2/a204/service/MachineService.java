package class2.a204.service;

import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.repository.LogRepository;
import class2.a204.repository.MachineRepository;
import class2.a204.util.DataNotFountException;
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
        for (Integer n : brokenList) {
            List<Log> logs = logRepository.findAllByMachine_MachineIdOrderByErrorDateDesc(n);
            if (!logs.isEmpty()) {
                lastBrokenLogList.add(logs.get(0));
            }
        }

        return lastBrokenLogList;
    }

    public void updateMachine(boolean broken, Integer machineId) {
        Optional<Machine> foundMachine = machineRepository.findById(machineId);
        if (foundMachine.isEmpty()) {
            throw new RuntimeException("등록되지 않은 기기 : " + machineId);
        }
        Machine machine = foundMachine.get();
        machine.changeBroken(broken);
        machineRepository.save(machine);
    }

    public Machine findMachine(Integer machineId) throws DataNotFountException {
        Optional<Machine> machine = machineRepository.findByMachineId(machineId);
        if(machine.isPresent())
        return machine.get();
        else throw new DataNotFountException("기계 조회 실패");
    }
}
