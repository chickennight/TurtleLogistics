package class2.a204.service;

import class2.a204.entity.Customer;
import class2.a204.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CustomerServiceTest {

    private final CustomerService CS;
    private final CustomerRepository CR;

    @Autowired
    CustomerServiceTest(CustomerService cs, CustomerRepository cr){
        this.CS = cs;
        this.CR = cr;
    }

    @Test
    public void findByCustomerIdTest() {
        // given
        Customer customer = new Customer();
        customer.setCustomerId("admin");
        customer.setPassword("password");
        customer.setAddress("123");
        customer.setPhoneNumber("01011111111");
        customer = CR.save(customer);

        // when
        Customer foundCustomer = CR.findByCustomerId(customer.getCustomerId()).get();

        // then
        assertThat(foundCustomer).isNotNull();
    }

    @Test
    public void registerAdminTest() {
        // given
        Customer customer = new Customer();
        customer.setCustomerId("admin");
        customer.setPassword("password");
        customer.setAddress("123");
        customer.setPhoneNumber("01011111111");

        // when
        Customer savedCustomer = CS.registerCustomer(customer);

        // then
        assertThat(savedCustomer).isNotNull();
    }

    @Test
    public void loginTest() {
        // given
        Customer customer = new Customer();
        customer.setCustomerId("admin");
        customer.setPassword("password");
        customer.setAddress("123");
        customer.setPhoneNumber("01011111111");
        customer = CS.registerCustomer(customer);

        // when
        boolean result = CS.login(customer.getCustomerId(), "password");

        // then
        assertThat(result).isTrue();
    }
}