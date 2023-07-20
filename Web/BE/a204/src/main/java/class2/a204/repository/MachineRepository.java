package class2.a204.repository;

import class2.a204.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    Machine findById(Integer id);
    List<Machine> findAll();

}
