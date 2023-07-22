package class2.a204.service;

import class2.a204.entity.Log;
import class2.a204.entity.Machine;
import class2.a204.repository.LogRepository;
import class2.a204.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {

    private final LogRepository LR;
    private final MachineRepository MR;

    @Autowired
    public MachineService(LogRepository lr, MachineRepository mr) {
        LR = lr;
        MR = mr;
    }

    public List<Machine> findMachineAll() {
        return MR.findAll();
    }

    public List<Log> findLogAll() {
        return LR.findAll();
    }

    public void addLog(Log log) {
        LR.save(log);
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
        for (Integer n : brokenList) lastBrokenLogList.add(LR.findAllByMachine_MachineIdOrderByErrorDateDesc(n).get(0));
        return lastBrokenLogList;
    }

    public void updateMachine(Machine machine) {
        MR.save(machine);
    }

    public Machine findMachine(Integer machineId) {
        return MR.findByMachineId(machineId);
    }
}
