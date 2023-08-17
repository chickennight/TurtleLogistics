package class2.a204.service;

import class2.a204.dto.LogDTO;
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
        return logRepository.findAllByOrderByErrorDateDesc();
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

    public List<LogDTO> lastBrokenLogs(List<Integer> brokenList) {
        List<LogDTO> lastBrokenLogList = new ArrayList<>();
        for (Integer machineId : brokenList) {
            Optional<Log> latestBrokenLog = logRepository.findFirstByMachine_MachineIdOrderByErrorDateDesc(machineId);

            if (latestBrokenLog.isPresent()) {
                Log log = latestBrokenLog.get();
                lastBrokenLogList.add(new LogDTO(log));

                if (!log.getRecorded()) {
                    log.updateRecorded();
                    logRepository.save(log);
                }
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
        if (machine.isPresent())
            return machine.get();
        else throw new DataNotFountException("기계 조회 실패");
    }

    public int getLogCnt(int machine_id) {
        return logRepository.countByMachineId(machine_id);
    }
}
