package class2.a204.service;

import class2.a204.dto.AdminDTO;
import class2.a204.dto.AdminLoginDTO;
import class2.a204.dto.LogisticAnalysisDTO;
import class2.a204.entity.Admin;
import class2.a204.entity.Product;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.jwt.Role;
import class2.a204.repository.AdminRepository;
import class2.a204.repository.OrderDetailRepository;
import class2.a204.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
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
        for (Product p : productList) {
            // 기본 생성
            LogisticAnalysisDTO input = new LogisticAnalysisDTO(p);
            // 1년간 개수 조회
            LocalDateTime yearAgo = now.minusYears(1L);
            Optional<Long> yearSum = orderDetailRepository.sumAmountByTimeAndProductNum(yearAgo, input.getProductNum());
            input.setYearAvg((int) Math.ceil((double) (yearSum.isPresent() ? yearSum.get() : 0) / 365));
            // 한달간 개수 조회
            LocalDateTime monthAgo = now.minusDays(30L);
            Optional<Long> monthSum = orderDetailRepository.sumAmountByTimeAndProductNum(monthAgo, input.getProductNum());
            input.setMonthAvg((int) Math.ceil((double) (monthSum.isPresent() ? monthSum.get() : 0) / 30));
            // 주간 개수 조회
            LocalDateTime weekAgo = now.minusDays(7L);
            Optional<Long> weekSum = orderDetailRepository.sumAmountByTimeAndProductNum(weekAgo, input.getProductNum());
            input.setWeekAvg((int) Math.ceil((double) (weekSum.isPresent() ? weekSum.get() : 0) / 7));
            // 금일 개수 조회
            LocalDateTime today = now.toLocalDate().atStartOfDay();
            Optional<Long> todayCount = orderDetailRepository.sumAmountByTimeAndProductNum(today, input.getProductNum());
            input.setTodayAmount((int) (todayCount.isPresent() ? todayCount.get() : 0));

            // 주의 여부
            if (input.getStock() <= 7 * Math.max(Math.max(input.getYearAvg(), input.getWeekAvg()), input.getMonthAvg()))
                input.setErrorMessage("재고 소진 임박");

            if (input.getTodayAmount() >= 3 * Math.max(Math.max(input.getYearAvg(), input.getWeekAvg()), input.getMonthAvg())) {
                if (input.getErrorMessage().equals(""))
                    input.setErrorMessage("주문 폭주");
                else
                    input.setErrorMessage("주문 폭주! 재고 즉시 확인");
            }


            ans.add(input);
        }
        return ans;

    }
}
