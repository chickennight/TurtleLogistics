package class2.a204.service;

import class2.a204.entity.Customer;
import class2.a204.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository CR;
    private final PasswordEncoder encoder;

    @Autowired
    public CustomerService(CustomerRepository cr, PasswordEncoder encoder){
        this.CR = cr;
        this.encoder = encoder;
    }

    //회원가입
    public Customer registerCustomer(Customer customer){
        customer.setPassword(encoder.encode(customer.getPassword()));
        return CR.save(customer);
    }

    //로그인
    public boolean login(String id, String password){
        Customer customer = CR.findByCustomerId(id).get();
        if(customer != null){
            return encoder.matches(password, customer.getPassword());
        }
        return false;
    }

}
