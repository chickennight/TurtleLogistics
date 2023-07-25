package class2.a204.service;

import class2.a204.entity.Admin;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AdminServiceTest {

    private final MockMvc mockMvc;

    private final AdminService AS;
    private final AdminRepository AR;

    private final OrderService OS;

    private final JwtTokenProvider JP;

    @Autowired
    AdminServiceTest(MockMvc mockMvc, AdminService as, AdminRepository ar, OrderService os, JwtTokenProvider jp) {
        this.mockMvc = mockMvc;
        this.AS = as;
        this.AR = ar;
        this.OS = os;
        this.JP = jp;
    }

    @Test
    public void findByAdminIdTest() {
        // given
        Admin admin = new Admin();
        admin.setAdminId("test");
        admin.setPassword("1234");
        admin.setPhoneNumber("01011111111");
        AR.save(admin);

        // when
        Optional<Admin> foundAdmin = AR.findByAdminId(admin.getAdminId());

        // then
        assertThat(foundAdmin).isNotNull();
        System.out.println(foundAdmin.get().getAdminId());
    }

    //Admin 회원가입
    @Test
    public void registerAdminTest() {
        // given
        Admin admin = new Admin();
        admin.setAdminId("test");
        admin.setPassword("1234");
        admin.setPhoneNumber("01011111111");

        // when
        Admin registedAdmin = AS.registerAdmin(admin);

        // then
        assertThat(registedAdmin).isNotNull();

    }

    //Admin 로그인
    @Test
    public void loginTest() {
        // given
        Admin admin = new Admin();
        admin.setAdminId("test");
        admin.setPassword("1234");
        admin.setPhoneNumber("01011111111");
        AS.registerAdmin(admin);

        // when
        Map<String, String> token = AS.login(admin.getAdminId(), "1234");

        // then
        assertThat(token).isNotNull();
    }

    //토큰 검증
    @Test
    public void TokenVaidationTest() {
        //given
        Admin admin = new Admin();
        admin.setAdminId("test");
        admin.setPassword("1234");
        admin.setPhoneNumber("01011111111");
        AS.registerAdmin(admin);

        //when
        Map<String, String> tokens = AS.login(admin.getAdminId(), "1234");
        String accessToken = tokens.get("accessToken");

        //then
        boolean isVaildToken = JP.validateToken(accessToken);
        assertThat(isVaildToken).isTrue();

        //토큰에서 사용자 가져오기
        String adminId = JP.getUserID(accessToken);
        assertThat(admin.getAdminId()).isEqualTo(adminId);

        //토큰에서 권한 정보 가져오기
        String Role = JP.getRoleFromToken(accessToken);
        assertThat(Role).isEqualTo("ROLE_ADMIN");
    }

    //주문정보 불러오기
    @Test
    public void getOrdersTest() throws Exception{
        //given
        Admin admin = new Admin();
        admin.setAdminId("test");
        admin.setPassword("1234");
        admin.setPhoneNumber("01011111111");
        AS.registerAdmin(admin);

        //when
        Map<String, String> tokens = AS.login(admin.getAdminId(), "1234");
        String accessToken = tokens.get("accessToken");

        //then
        mockMvc.perform(get("/admin/orders")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}



