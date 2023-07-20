package class2.a204.Service;

import class2.a204.model.Admin;
import class2.a204.repository.AdminRepository;
import class2.a204.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AdminServiceTest {

    private final AdminService adminService;
    private final AdminRepository adminRepository;

    @Autowired
    AdminServiceTest(AdminService adminService, AdminRepository adminRepository){
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    @Test
    public void findAdminByIdTest() {
        // given
        Admin admin = new Admin();
        admin.setId("admin");
        admin.setPassword("password");
        admin = adminRepository.save(admin);

        // when
        Admin foundAdmin = adminService.findAdminById(admin.getId());

        // then
        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getId()).isEqualTo(admin.getId());
    }

    @Test
    public void saveAdminTest() {
        // given
        Admin admin = new Admin();
        admin.setId("admin");
        admin.setPassword("password");

        // when
        Admin savedAdmin = adminService.saveAdmin(admin);

        // then
        assertThat(savedAdmin).isNotNull();
//        assertThat(savedAdmin.getPassword()).isNotEqualTo(admin.getPassword()); // password is encoded
    }

    @Test
    public void loginTest() {
        // given
        Admin admin = new Admin();
        admin.setId("admin");
        admin.setPassword("password");
        admin = adminService.saveAdmin(admin);

        // when
        boolean result = adminService.login(admin.getId(), "password");

        // then
        assertThat(result).isTrue();
    }
}
