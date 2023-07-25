package class2.a204.jwt;

import class2.a204.entity.Admin;
import class2.a204.entity.Customer;
import class2.a204.repository.AdminRepository;
import class2.a204.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final AdminRepository AR;
    private final CustomerRepository CR;

    @Autowired
    public UserDetailsServiceImpl(AdminRepository ar, CustomerRepository cr) {
        this.AR = ar;
        this.CR = cr;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Admin> adminOptional = AR.findByAdminId(userId);
        Optional<Customer> customerOptional = CR.findByCustomerId(userId);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            // 여기에서 Admin의 상세 정보를 UserDetails에 설정
            return new User(admin.getAdminId(), admin.getPassword(), AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        } else if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            // 여기에서 Customer의 상세 정보를 UserDetails에 설정
            return new User(customer.getCustomerId(), customer.getPassword(), AuthorityUtils.createAuthorityList("ROLE_CUSTOMER"));
        } else {
            throw new UsernameNotFoundException(userId + "사용자 없음");
        }
    }

}