package class2.a204.controller;


import class2.a204.dto.CustomerDTO;
import class2.a204.dto.AdminLoginDTO;
import class2.a204.dto.CustomerLoginDTO;
import class2.a204.service.CustomerService;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@Api(tags = "Customer")
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ErrorHandler errorHandler;

    @Autowired
    public CustomerController(CustomerService customerService, ErrorHandler errorHandler){
        this.customerService = customerService;
        this.errorHandler = errorHandler;
    }

    //회원가입
    @ApiOperation(value = "사용자 등록", notes = "신규 사용자 등록")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerDTO customerDto){
        try{
            customerService.registerCustomer(customerDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return errorHandler.errorMessage(e);
        }
    }

    
    //로그인
    @ApiOperation(value = "사용자 로그인", notes = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerLoginDTO customerLoginDTO) {
        try {
            Map<String, String> tokens = customerService.login(customerLoginDTO);
            if (tokens != null) {
                return new ResponseEntity<>(tokens,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

}
