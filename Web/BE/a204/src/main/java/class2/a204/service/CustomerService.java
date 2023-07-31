package class2.a204.service;

import class2.a204.dto.CustomerDTO;
import class2.a204.dto.AdminLoginDTO;
import class2.a204.dto.CustomerLoginDTO;
import class2.a204.entity.Customer;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder){
        this.customerRepository = customerRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    //회원가입
    public Customer registerCustomer(CustomerDTO customerDto){
        Customer customer = customerDto.toEntity();
        customer.encodePassword(passwordEncoder);
        return customerRepository.save(customer);
    }

    //로그인
    public Map<String, String> login(CustomerLoginDTO customerLoginDTO){
        String id = customerLoginDTO.getCustomerId();
        String password = customerLoginDTO.getPassword();
        Customer customer = customerRepository.findByCustomerId(id).get();
        if(customer != null && passwordEncoder.matches(password, customer.getPassword())){
            String accessToken = jwtTokenProvider.createToken(customer.getCustomerId(), Role.ROLE_CUSTOMER.name());
            String refreshToken = jwtTokenProvider.createRefreshToken(customer.getCustomerId(), Role.ROLE_CUSTOMER.name());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        }
        return null;
    }

}
