package class2.a204.controller;

import class2.a204.dto.MessageDTO;
import class2.a204.dto.NewOrderDTO;
import class2.a204.dto.OrderUpdateDTO;
import class2.a204.entity.Log;
import class2.a204.entity.OrderDetail;
import class2.a204.entity.OrderNow;
import class2.a204.entity.Product;
import class2.a204.jwt.JwtTokenProvider;
import class2.a204.service.MachineService;
import class2.a204.service.OrderService;
import class2.a204.service.ProductService;
import class2.a204.service.SmsService;
import class2.a204.util.ErrorHandler;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OrderController(OrderService orderService, ErrorHandler errorHandler, MachineService machineService, ProductService productService, SmsService smsService, JwtTokenProvider jwtTokenProvider) {
        this.orderService = orderService;
        this.errorHandler = errorHandler;
        this.machineService = machineService;
        this.productService = productService;
        this.smsService = smsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/analysis/day")
    public ResponseEntity<?> dataAnalysisDay(@RequestParam("start_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDay, @RequestParam("end_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDay) {
        try {
            return new ResponseEntity<>(orderService.dataDay(startDay, endDay), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @GetMapping("/analysis/region")
    public ResponseEntity<?> dataAnalysisRegion(Integer year, Integer month) {
        try {
            return new ResponseEntity<>(orderService.dataRegion(year, month), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @GetMapping("/start")
    public ResponseEntity<?> packageList() {
        try {
            List<OrderNow> list = orderService.findPackageOrders();

            if (list.size() == 0)
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

//                OS.set2OrderNow(on.getOrder().getOrderNum());

                answer.add(input);
            }

            return new ResponseEntity<>(answer, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> newOrder(@RequestBody NewOrderDTO newOrderDto) {
        try {
            orderService.addNewOrder(newOrderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandler.errorMessage(e);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> orderUpdate(@RequestBody OrderUpdateDTO orderUpdateDto) {
        try {
            OrderNow orderNow = orderService.findByOrderNum(orderUpdateDto.getOrderNum());
            if (orderService.findByOrderNum(orderUpdateDto.getOrderNum()) == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (orderUpdateDto.getType() == 0) {
                if (orderUpdateDto.getResult() == 1) {
                    orderNow.changeStatus(0);
                    machineService.addLog(errorLog(orderUpdateDto, "포장 문제 발생"));
                    return new ResponseEntity<>("PROBLEM RECORD COMPLETE", HttpStatus.ACCEPTED);
                }
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
            } else if (orderUpdateDto.getType() == 1) {
                if (orderUpdateDto.getResult() == 1) {
                    orderNow.changeStatus(0);
                    machineService.addLog(errorLog(orderUpdateDto, "분류 문제 발생"));
                    return new ResponseEntity<>("PROBLEM RECORD COMPLETE", HttpStatus.ACCEPTED);
                }
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
            } else if (orderUpdateDto.getType() == 2) {
                if (orderNow.getStatus() == 4) {
                    orderNow.changeStatus(5);
                    orderService.updateStatus(orderNow);
                    smsService.sendSms(new MessageDTO(orderNow.getOrder().getCustomer().getPhoneNumber(), "주문하신 상품이 배송완료되었습니다."));
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.changeStatus(5);
                    orderService.updateStatus(orderNow);
                    machineService.addLog(errorLog(orderUpdateDto, "배송 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

}
