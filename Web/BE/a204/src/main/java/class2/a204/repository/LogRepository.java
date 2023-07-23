package class2.a204.repository;

import class2.a204.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findAllByMachine_MachineIdOrderByErrorDateDesc(int machineId);

    List<Log> findAllByMachine_MachineId(int machineId);

}
