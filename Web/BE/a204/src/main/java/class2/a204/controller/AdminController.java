package class2.a204.controller;

import class2.a204.model.Admin;
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
    private final AdminService adminService;
    private final OrderService orderService;
    private final ErrorHandler errorHandler;

    @Autowired
    public AdminController(AdminService adminService, OrderService orderService, ErrorHandler errorHandler) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.errorHandler = errorHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        try {
            adminService.saveAdmin(admin);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String id, @RequestParam String password) {
        try {
            if (adminService.login(id, password)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        try {
            List<?> orders = orderService.findAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }
}
