package class2.a204.controller;

import class2.a204.entity.Admin;
import class2.a204.service.AdminService;
import class2.a204.service.OrderService;
import class2.a204.util.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService AS;
    private final OrderService OS;
    private final ErrorHandler Handler;

    @Autowired
    public AdminController(AdminService as, OrderService os, ErrorHandler handler) {
        this.AS = as;
        this.OS = os;
        this.Handler = handler;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        try {
            AS.saveAdmin(admin);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String password) {
        try {
            if (AS.login(id, password)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        try {
            List<?> orders = OS.findAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }
}
