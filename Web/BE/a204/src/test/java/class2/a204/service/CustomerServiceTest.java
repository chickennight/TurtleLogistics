//package class2.a204.service;
//
//import class2.a204.entity.Customer;
//import class2.a204.jwt.JwtTokenProvider;
//import class2.a204.repository.CustomerRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@Transactional
//@AutoConfigureMockMvc
//class CustomerServiceTest {
//
//    private final MockMvc mockMvc;
//    private final CustomerService CS;
//    private final CustomerRepository CR;
//
//    private final JwtTokenProvider JP;
//
//    @Autowired
//    CustomerServiceTest(MockMvc mockMvc, CustomerService cs, CustomerRepository cr, JwtTokenProvider jp){
//        this.mockMvc = mockMvc;
//        this.CS = cs;
//        this.CR = cr;
//        JP = jp;
//    }
//
//    @Test
//    public void findByCustomerIdTest() {
//        // given
//        Customer customer = new Customer();
//        customer.setCustomerId("admin");
//        customer.setPassword("password");
//        customer.setAddress("123");
//        customer.setPhoneNumber("01011111111");
//        CR.save(customer);
//
//        // when
//        Optional<Customer> foundCustomer = CR.findByCustomerId(customer.getCustomerId());
//
//        // then
//        assertThat(foundCustomer).isNotNull();
//    }
//
//    @Test
//    public void registerCustomerTest() {
//        // given
//        Customer customer = new Customer();
//        customer.setCustomerId("test");
//        customer.setPassword("password");
//        customer.setAddress("123");
//        customer.setPhoneNumber("01011111111");
//
//        // when
//        Customer regustedCustomer = CS.registerCustomer(customer);
//
//        // then
//        assertThat(regustedCustomer).isNotNull();
//    }
//
//    @Test
//    public void loginTest() {
//        // given
//        Customer customer = new Customer();
//        customer.setCustomerId("admin");
//        customer.setPassword("password");
//        customer.setAddress("123");
//        customer.setPhoneNumber("01011111111");
//        CS.registerCustomer(customer);
//
//        // when
//        Map<String, String> tokens = CS.login(customer.getCustomerId(), "password");
//
//        // then
//        assertThat(tokens).isNotNull();
//        assertThat(tokens.get("accessToken")).isNotNull();
//    }
//
//    @Test
//    public void TokenVaidationTest(){
//        // given
//        Customer customer = new Customer();
//        customer.setCustomerId("admin");
//        customer.setPassword("password");
//        customer.setAddress("123");
//        customer.setPhoneNumber("01011111111");
//        CS.registerCustomer(customer);
//
//        //when
//        Map<String, String> tokens = CS.login(customer.getCustomerId(), "password");
//        String accessToken = tokens.get("accessToken");
//
//        //then
//        boolean isVaildToken = JP.validateToken(accessToken);
//        assertThat(isVaildToken).isTrue();
//
//        //토큰에서 사용자 가져오기
//        String customerId = JP.getUserID(accessToken);
//        assertThat(customer.getCustomerId()).isEqualTo(customerId);
//
//        //토큰에서 권한 정보 가져오기
//        String Role = JP.getRoleFromToken(accessToken);
//        assertThat(Role).isEqualTo("ROLE_CUSTOMER");
//    }
//}