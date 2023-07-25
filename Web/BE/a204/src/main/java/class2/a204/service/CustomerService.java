package class2.a204.service;

import class2.a204.entity.Customer;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository CR;

    private final JwtTokenProvider JP;
    private final PasswordEncoder encoder;

    @Autowired
    public CustomerService(CustomerRepository cr,JwtTokenProvider jp, PasswordEncoder encoder){
        this.CR = cr;
        this.JP = jp;
        this.encoder = encoder;
    }

    //회원가입
    public Customer registerCustomer(Customer customer){
        customer.setPassword(encoder.encode(customer.getPassword()));
        return CR.save(customer);
    }

    //로그인
    public String login(String id, String password){
        Customer customer = CR.findByCustomerId(id).get();
        if(customer != null && encoder.matches(password, customer.getPassword())){
            String token = JP.createToken(customer.getCustomerId(), Role.ROLE_CUSTOMER.name());

            return token;
        }
        return null;
    }

}
