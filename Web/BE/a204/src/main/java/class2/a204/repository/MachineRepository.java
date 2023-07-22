package class2.a204.repository;

import class2.a204.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Integer> {

    Machine findByMachineId(Integer machine_id);
}
