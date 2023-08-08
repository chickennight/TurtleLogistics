package class2.a204.service;

import class2.a204.dto.AdminDTO;
import class2.a204.dto.AdminLoginDTO;
import class2.a204.dto.AnalysisGetDTO;
import class2.a204.dto.LogisticAnalysisDTO;
import class2.a204.entity.Admin;
import class2.a204.entity.Image;
import class2.a204.entity.Product;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final LogRepository logRepository;
    private final ImageRepository imageRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Autowired
    public AdminService(AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, ProductRepository productRepository, OrderDetailRepository orderDetailRepository, LogRepository logRepository, ImageRepository imageRepository, ImageService imageService) {
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.logRepository = logRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
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

    public List<LogisticAnalysisDTO> getLogisticAnalysis() {
        List<LogisticAnalysisDTO> ans = new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        // 기본 생성
        for (Product p : productList)
            ans.add(new LogisticAnalysisDTO(p));

        // 1년간 개수 조회
        LocalDateTime yearAgo = now.minusYears(1L);
        // 한달간 개수 조회
        LocalDateTime monthAgo = now.minusDays(30L);
        // 주간 개수 조회
        LocalDateTime weekAgo = now.minusDays(7L);
        // 금일 개수 조회
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        List<AnalysisGetDTO> list = orderDetailRepository.findAllInfo(changeForm(today), changeForm(weekAgo), changeForm(monthAgo), changeForm(yearAgo));
        for (LogisticAnalysisDTO la : ans)
            for (AnalysisGetDTO ag : list)
                if (la.getProductNum().equals(ag.getProductNum()))
                    la.addStatus(ag);

        return ans;

    }

    static int changeForm(LocalDateTime ldt) {
        int year = ldt.getYear();
        int month = ldt.getMonthValue();
        int day = ldt.getDayOfMonth();
        return year * 10000 + month * 100 + day;
    }

    public void uploadImage(MultipartFile image, int logNum) throws IOException {
        imageService.uploadImage(image);
        Image input = new Image(logRepository.findById(logNum).get(), image.getContentType());
        imageRepository.save(input);
    }
}
