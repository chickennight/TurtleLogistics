package class2.a204.repository;

import class2.a204.entity.Machine;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Integer> {

    Machine findByMachineId(Integer machine_id);
}
