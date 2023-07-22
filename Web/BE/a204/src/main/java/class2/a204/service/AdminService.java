package class2.a204.service;

import class2.a204.entity.Admin;
import class2.a204.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }



    public Admin findAdminById(String id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Admin saveAdmin(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public boolean login(String id, String password) {
        Admin admin = findAdminById(id);
        if (admin != null) {
            return encoder.matches(password, admin.getPassword());
        }
        return false;
    }
}
