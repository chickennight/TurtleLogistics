package class2.a204.service;

import class2.a204.dto.AdminDTO;
import class2.a204.dto.AdminLoginDTO;
import class2.a204.entity.Admin;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

//    public Admin findAdminById(String id) {
//        return AR.findById(id).orElse(null);
//    }

    //회원가입
    public void registerAdmin(AdminDTO adminDto) {
        Admin admin = adminDto.toEntity();
        admin.encodePassword(passwordEncoder);
        adminRepository.save(admin);
    }

    //로그인
    public Map<String, String> login(AdminLoginDTO adminLoginDto) {
        String id = adminLoginDto.getAdminId();
        String password = adminLoginDto.getPassword();
        Optional<Admin> admin = adminRepository.findByAdminId(id);
        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
            String accessToken = jwtTokenProvider.createToken(admin.get().getAdminId(), Role.ROLE_ADMIN.name());
            String refreshToken = jwtTokenProvider.createRefreshToken(admin.get().getAdminId(), Role.ROLE_ADMIN.name());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        }
        return null;
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtTokenProvider.refreshAccessToken(refreshToken);
    }

    public String getAdminPhone(String token) {
        Optional<Admin> admin = adminRepository.findByAdminId(jwtTokenProvider.getUserID(token));
        if (admin.isPresent())
            return admin.get().getPhoneNumber();
        else
            return "";
    }
}
