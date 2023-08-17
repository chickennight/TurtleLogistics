package class2.a204.repository;

import class2.a204.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findAllByMachine_MachineId(int machineId);

    Optional<Log> findFirstByMachine_MachineIdOrderByErrorDateDesc(Integer machineId);

    List<Log> findAllByOrderByErrorDateDesc();

    @Query("SELECT COUNT(l) FROM Log l WHERE l.logNum/10000 = ?1")
    int countByMachineId(int machineId);
}
