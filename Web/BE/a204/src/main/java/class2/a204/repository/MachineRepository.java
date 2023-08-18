package class2.a204.repository;

import class2.a204.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Integer> {

    Optional<Machine> findByMachineId(Integer machine_id);
}
