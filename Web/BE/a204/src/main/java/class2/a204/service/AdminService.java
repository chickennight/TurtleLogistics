package class2.a204.service;

import class2.a204.entity.Admin;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository AR;
    private final JwtTokenProvider JP;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminService(AdminRepository ar,JwtTokenProvider jp, PasswordEncoder encoder) {
        this.AR = ar;
        this.JP = jp;
        this.encoder = encoder;
    }

//    public Admin findAdminById(String id) {
//        return AR.findById(id).orElse(null);
//    }

    public Admin registerAdmin(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return AR.save(admin);
    }

    public String login(String id, String password) {
        Admin admin = AR.findByAdminId(id).get();
        if (admin != null && encoder.matches(password, admin.getPassword())) {
            String token = JP.createToken(admin.getAdminId(), Role.ROLE_ADMIN.name());

            return token;
        }
        return null;
    }
}
