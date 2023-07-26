package class2.a204.controller;


import class2.a204.entity.Customer;
import class2.a204.service.CustomerService;
import class2.a204.util.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService CS;
    private final ErrorHandler Handler;

    @Autowired
    public CustomerController(CustomerService cs, ErrorHandler handler){
        this.CS = cs;
        this.Handler = handler;
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer customer){
        try{
            CS.registerCustomer(customer);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return Handler.errorMessage(e);
        }
    }

    
    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> Info) {
        try {
            String id = Info.get("customer_id");
            String password = Info.get("password");
            Map<String, String> tokens = CS.login(id,password);
            if (tokens != null) {
                return new ResponseEntity<>(tokens,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return Handler.errorMessage(e);
        }
    }

}
