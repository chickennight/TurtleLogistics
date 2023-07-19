package class2.a204.controller;

import class2.a204.model.Machine;
import class2.a204.repository.MachineRepository;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@Api(tags = "Machine")
@RequestMapping("/machine")
public class MachineController {
    private final MachineRepository MR;

    @Autowired
    public MachineController(MachineRepository mr) {
        this.MR = mr;
    }


    @GetMapping
    public ResponseEntity<?> getAllMachines() {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMachine(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<?> createMachine(@RequestBody Machine machine) {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMachine(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
