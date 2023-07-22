package class2.a204.service;

import class2.a204.entity.Admin;
import class2.a204.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository AR;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminService(AdminRepository ar, PasswordEncoder encoder) {
        this.AR = ar;
        this.encoder = encoder;
    }



    public Admin findAdminById(String id) {
        return AR.findById(id).orElse(null);
    }

    public Admin saveAdmin(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return AR.save(admin);
    }

    public boolean login(String id, String password) {
        Admin admin = findAdminById(id);
        if (admin != null) {
            return encoder.matches(password, admin.getPassword());
        }
        return false;
    }
}
