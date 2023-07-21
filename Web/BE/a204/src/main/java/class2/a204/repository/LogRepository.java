package class2.a204.repository;

import class2.a204.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
//    @Query("SELECT l FROM Log l WHERE l.machine = :machine_id ORDER BY l.errorDate DESC")
//    List<Log> findByMachineId(int machineId);

}
