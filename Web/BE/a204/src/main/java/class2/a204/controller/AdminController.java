package class2.a204.controller;

import class2.a204.dto.AdminDTO;
import class2.a204.dto.AdminLoginDTO;
import class2.a204.dto.MessageDTO;
import class2.a204.dto.RefreshTokenDTO;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.service.AdminService;
import class2.a204.service.ImageService;
import class2.a204.service.OrderService;
import class2.a204.service.SmsService;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@Api(tags = "Admin")
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final OrderService orderService;
    private final ErrorHandler errorHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final SmsService smsService;
    private final ImageService imageService;

    public AdminController(AdminService adminService, OrderService orderService, ErrorHandler errorHandler, JwtTokenProvider jwtTokenProvider, SmsService smsService, ImageService imageService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.errorHandler = errorHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.smsService = smsService;
        this.imageService = imageService;
    }

    @ApiOperation(value = "관리자 등록", notes = "신규 관리자 등록")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminDTO adminDto) {
        try {
            adminService.registerAdmin(adminDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "관리자 로그인", notes = "관리자 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginDTO adminLoginDto) {
        try {
            Map<String, String> tokens = adminService.login(adminLoginDto);
            if (tokens != null) {
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "주문 정보 가져오기", notes = "전체 주문정보 가져오기")
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        try {
            List<?> orders = orderService.findAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "Access Token 갱신", notes = "Refresh Token을 이용하여 Access Token 갱신")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            String newAccessToken = adminService.refreshAccessToken(refreshTokenDTO.getRefreshToken());
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "기기 이상 알림", notes = "기기 이상 발생시 메세지 전송")
    @PostMapping("/msg")
    public ResponseEntity<?> sendMessage(@RequestBody int logNum, ServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

            MessageDTO sms = new MessageDTO(adminService.getAdminPhone(token));
            sms.setContent(smsService.createMessage(logNum));
            smsService.sendSms(sms);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "물류 분석 자료", notes = "상품별 소모량 제공")
    @GetMapping("/logistic")
    public ResponseEntity<?> logisticAnalysis() {
        try {
            return new ResponseEntity<>(adminService.getLogisticAnalysis(), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "사진 저장", notes = "문제 발생시 해당 상황 사진 저장")
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile image, @RequestParam("log_num") int logNum) {
        try {
            adminService.uploadImage(image, logNum);
            return new ResponseEntity<>(adminService.getLogisticAnalysis(), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "사진 조회", notes = "로그 번호와 매칭 되는 사진 조회")
    @GetMapping("/image/{name}")
    public ResponseEntity<?> downloadImage(@PathVariable("name") int name) {
        try {
            return imageService.downloadImage(name);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }
}
