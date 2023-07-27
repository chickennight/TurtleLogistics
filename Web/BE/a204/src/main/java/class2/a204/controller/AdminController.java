package class2.a204.controller;

import class2.a204.dto.MessageDTO;
import class2.a204.entity.Admin;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.service.AdminService;
import class2.a204.service.OrderService;
import class2.a204.service.SmsService;
import class2.a204.util.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService AS;
    private final OrderService OS;
    private final ErrorHandler Handler;
    private final JwtTokenProvider JP;
    private final SmsService SS;

    @Autowired
    public AdminController(AdminService as, OrderService os, ErrorHandler handler, JwtTokenProvider jp, SmsService ss) {
        AS = as;
        OS = os;
        Handler = handler;
        JP = jp;
        SS = ss;
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
            if (tokens != null) {
                return new ResponseEntity<>(tokens, HttpStatus.OK);
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

    @GetMapping("/msg")
    public ResponseEntity<?> sendMessage(@RequestParam("machine_detail") String machineDetail, ServletRequest request) {
        try {
            String token = JP.resolveToken((HttpServletRequest) request);
            MessageDTO sms = new MessageDTO(AS.getAdminPhone(token), machineDetail + " 기계 이상 발생");
            SS.sendSms(sms);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
