package class2.a204.repository;

import class2.a204.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findAllByMachine_MachineId(int machineId);

    Optional<Log> findFirstByMachine_MachineIdOrderByErrorDateDesc(Integer machineId);

    List<Log> findAllByOrderByErrorDateDesc();
}
