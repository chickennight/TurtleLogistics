package class2.a204.service;

import class2.a204.entity.Admin;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AdminServiceTest {

    private MockMvc mockMvc;

    private final AdminService AS;
    private final AdminRepository AR;

    private final OrderService OS;

    private final JwtTokenProvider JP;

    @Autowired
    AdminServiceTest(AdminService as, AdminRepository ar, OrderService os, JwtTokenProvider jp) {
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
        String token = AS.login(admin.getAdminId(), "1234");

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
        String token = AS.login(admin.getAdminId(), "1234");

        //then
        boolean isVaildToken = JP.validateToken(token);
        assertThat(isVaildToken).isTrue();

        //토큰에서 사용자 가져오기
        String adminId = JP.getUserID(token);
        System.out.println("토큰으로 사용자 아이디 가져오기");
        System.out.println(adminId);
        assertThat(admin.getAdminId()).isEqualTo(adminId);


        //토큰에서 권한 정보 가져오기
        String Role = JP.getRoleFromToken(token);
        System.out.println(Role);
        assertThat(Role).isEqualTo("ROLE_ADMIN");

    }
}



