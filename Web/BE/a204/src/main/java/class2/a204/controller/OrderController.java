package class2.a204.controller;

import class2.a204.dto.MessageDTO;
import class2.a204.dto.NewOrderDTO;
import class2.a204.dto.OrderUpdateDTO;
import class2.a204.entity.Log;
import class2.a204.entity.OrderDetail;
import class2.a204.entity.OrderNow;
import class2.a204.entity.Product;
import class2.a204.service.MachineService;
import class2.a204.service.OrderService;
import class2.a204.service.ProductService;
import class2.a204.service.SmsService;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@Api(tags = "Order")
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final ErrorHandler errorHandler;

    private final MachineService machineService;

    private final ProductService productService;

    private final SmsService smsService;

    @Autowired
    public OrderController(OrderService orderService, ErrorHandler errorHandler, MachineService machineService, ProductService productService, SmsService smsService) {
        this.orderService = orderService;
        this.errorHandler = errorHandler;
        this.machineService = machineService;
        this.productService = productService;
        this.smsService = smsService;
    }

    @ApiOperation(value = "일자별 주문 분석", notes = "입력 기간에 따른 주문의 일자별 자료를 반환")
    @GetMapping("/analysis/day")
    public ResponseEntity<?> dataAnalysisDay(@RequestParam("start_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDay, @RequestParam("end_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDay) {
        try {
            return new ResponseEntity<>(orderService.dataDay(startDay, endDay), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

//    @ApiOperation(value = "지역별 주문 분석", notes = "입력 기간에 따른 주문의 지역별 자료를 반환")
//    @GetMapping("/analysis/region")
//    public ResponseEntity<?> dataAnalysisRegion(Integer year, Integer month) {
//        try {
//            return new ResponseEntity<>(orderService.dataRegion(year, month), HttpStatus.OK);
//        } catch (Exception e) {
//            return errorHandler.errorMessage(e);
//        }
//    }

    @ApiOperation(value = "포장이 필요한 주문 반환", notes = "포장 시스템에서 필요로 하는 주문 반환")
    @GetMapping("/start")
    public ResponseEntity<?> packageList() {
        try {
            List<OrderNow> list = orderService.findPackageOrders();

            if (list.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<Map<String, ?>> answer = new ArrayList<>();
            for (OrderNow on : list) {
                Map<String, Long> input = new HashMap<>();
                input.put("order_num", on.getOrder().getOrderNum());
                List<OrderDetail> detailList = orderService.findOrderDetailsBy(on.getOrder().getOrderNum());
                List<Product> productList = productService.findAll();
                for (Product p : productList)
                    input.put(String.valueOf(p.getName()), 0L);

                for (OrderDetail od : detailList)
                    input.put(String.valueOf(od.getProduct().getName()), (long) od.getAmount());


                input.put("address", (long) on.getOrder().getAddress());

                orderService.set2OrderNow(on.getOrder().getOrderNum());

                answer.add(input);
            }

            return new ResponseEntity<>(answer, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "새로운 주문 입력", notes = "홈페이지에서 사용될 새로운 주문 입력")
    @PostMapping
    public ResponseEntity<?> newOrder(@RequestBody NewOrderDTO newOrderDto) {
        try {
            orderService.addNewOrder(newOrderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }


    @ApiOperation(value = "주문 진행 현황 업데이트", notes = "기기에서 주문이 진행되는 과정에 따른 현황 업데이트")
    @PutMapping("/update")
    public ResponseEntity<?> orderUpdate(@RequestBody OrderUpdateDTO orderUpdateDto) {
        try {
            OrderNow orderNow = orderService.findByOrderNum(orderUpdateDto.getOrderNum());
            if (orderService.findByOrderNum(orderUpdateDto.getOrderNum()) == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (orderUpdateDto.getType() == 0) { // 분류 과정일 경우
                // 포장 과정 이상 발생
                if (orderUpdateDto.getResult() == 1) {
                    orderNow.changeStatus(0);
                    machineService.addLog(errorLog(orderUpdateDto, "포장 문제 발생"));
                    return new ResponseEntity<>("PROBLEM RECORD COMPLETE", HttpStatus.ACCEPTED);
                }

                // 포장 과정 이상 없음
                if (orderNow.getStatus() == 2) {
                    orderNow.changeStatus(3);
                    orderService.updateStatus(orderNow);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.changeStatus(3);
                    orderService.updateStatus(orderNow);
                    machineService.addLog(errorLog(orderUpdateDto, "포장 순서 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            } else if (orderUpdateDto.getType() == 1) { // 분류 파트
                // 분류 과정 이상 발생
                if (orderUpdateDto.getResult() == 1) {
                    orderNow.changeStatus(0);
                    machineService.addLog(errorLog(orderUpdateDto, "분류 문제 발생"));
                    return new ResponseEntity<>("PROBLEM RECORD COMPLETE", HttpStatus.ACCEPTED);
                }
                //분류 과정 이상 없음
                if (orderNow.getStatus() == 3) {
                    orderNow.changeStatus(4);
                    orderService.updateStatus(orderNow);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.changeStatus(4);
                    orderService.updateStatus(orderNow);
                    machineService.addLog(errorLog(orderUpdateDto, "분류 순서 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            } else if (orderUpdateDto.getType() == 2) { // 배송파트
                if (orderNow.getStatus() == 4) {
                    orderNow.changeStatus(5);
                    orderService.updateStatus(orderNow);
                    smsService.sendSms(new MessageDTO(orderNow.getOrder().getCustomer().getPhoneNumber(), "주문하신 상품이 배송시작되었습니다."));
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.changeStatus(5);
                    orderService.updateStatus(orderNow);
                    machineService.addLog(errorLog(orderUpdateDto, "배송 순서 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            } else if (orderUpdateDto.getType() == 3) { // 배송 완료
                if (orderNow.getStatus() == 5) {
                    orderService.deleteOrderNow(orderNow);
                    smsService.sendSms(new MessageDTO(orderNow.getOrder().getCustomer().getPhoneNumber(), "주문하신 상품이 배송완료되었습니다."));
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderService.deleteOrderNow(orderNow);
                    machineService.addLog(errorLog(orderUpdateDto, "배송 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @ApiOperation(value = "현재 진행중인 주문 목록", notes = "주문이 진행되는 과정에 있는 모든 주문의 현황을 반환한다")
    @GetMapping("/now")
    public ResponseEntity<?> OrderNow() {
        try {
            return new ResponseEntity<>(orderService.getOrderNow(), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    private Log errorLog(OrderUpdateDTO orderUpdateDto, String errorMessage) {
        Log l = new Log();
        //Log entity에 메서드 추가
        l.updateErrorMessage(orderUpdateDto.getOrderNum() + " " + errorMessage);
        l.updateMachine(machineService.findMachine(0));
        return l;
    }

    @ApiOperation(value = "지역코드별 주문 분석", notes = "지역별 주문 정보 반환")
    @GetMapping("/analysis/regioncode")
    public ResponseEntity<?> dataAnalysisRegionCode(Integer regionCode) {
        try {
            return new ResponseEntity<>(orderService.dataRegionCode(regionCode), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

}
