package class2.a204.controller;

import class2.a204.entity.Admin;
import class2.a204.service.AdminService;
import class2.a204.service.OrderService;
import class2.a204.util.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println(admin.getAdminId());
        try {
            AS.registerAdmin(admin);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> Info) {
        try {
            String id = Info.get("admin_id");
            String password = Info.get("password");
            Map<String, String> tokens = AS.login(id, password);
            if(tokens != null){
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> tokenMap) {
        try {
            String refreshToken = tokenMap.get("refreshToken");
            String newAccessToken = AS.refreshAccessToken(refreshToken);
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
