package class2.a204.service;

import class2.a204.dto.AdminDTO;
import class2.a204.dto.LoginRequestDTO;
import class2.a204.entity.Admin;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    //회원가입
    public Admin registerAdmin(AdminDTO adminDto) {
        Admin admin = adminDto.toEntity();
        admin.encodePassword(encoder);
        return AR.save(admin);
    }

    //로그인
    public Map<String, String> login(LoginRequestDTO loginRequestDto) {
        String id = loginRequestDto.getId();
        String password = loginRequestDto.getPassword();
        Admin admin = AR.findByAdminId(id).get();
        if (admin != null && encoder.matches(password, admin.getPassword())) {
            String accessToken = JP.createToken(admin.getAdminId(), Role.ROLE_ADMIN.name());
            String refreshToken = JP.createRefreshToken(admin.getAdminId(), Role.ROLE_ADMIN.name());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        }
        return null;
    }

    public String refreshAccessToken(String refreshToken) {
        return JP.refreshAccessToken(refreshToken);
    }

    public String getAdminPhone(String token) {
        return AR.findByAdminId(JP.getUserID(token)).get().getPhoneNumber();
    }
}
