package class2.a204.repository;

import class2.a204.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    @Query(value = "SELECT * FROM machine WHERE machine_id = ?1",nativeQuery = true)
    List<Log> findByMachineId(int machineId);

}
