package class2.a204.controller;

import class2.a204.dto.MessageDTO;
import class2.a204.dto.NewOrderDto;
import class2.a204.dto.OrderUpdateDto;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Api(tags = "Order")
@RequestMapping("/order")
public class OrderController {

    private final OrderService OS;

    private final ErrorHandler ER;

    private final MachineService MS;

    private final ProductService PS;

    private final SmsService SS;
    private final JwtTokenProvider JP;

    @Autowired
    public OrderController(OrderService os, ErrorHandler er, MachineService ms, ProductService ps, SmsService ss, JwtTokenProvider jp) {
        OS = os;
        ER = er;
        MS = ms;
        PS = ps;
        SS = ss;
        JP = jp;
    }

    @GetMapping("/analysis/day")
    public ResponseEntity<?> dataAnalysisDay(@RequestParam("start_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDay, @RequestParam("end_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDay) {
        try {
            return new ResponseEntity<>(OS.dataDay(startDay, endDay), HttpStatus.OK);
        } catch (Exception e) {
            return ER.errorMessage(e);
        }
    }

    @GetMapping("/analysis/region")
    public ResponseEntity<?> dataAnalysisRegion(Integer year, Integer month) {
        try {
            return new ResponseEntity<>(OS.dataRegion(year, month), HttpStatus.OK);
        } catch (Exception e) {
            return ER.errorMessage(e);
        }
    }

    @GetMapping("/start")
    public ResponseEntity<?> packageList() {
        try {
            List<OrderNow> list = OS.findPackageOrders();

            if (list.size() == 0)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<Map<String, ?>> answer = new ArrayList<>();
            for (OrderNow on : list) {
                Map<String, Long> input = new HashMap<>();
                input.put("order_num", on.getOrder().getOrderNum());
                List<OrderDetail> detailList = OS.findOrderDetailsBy(on.getOrder().getOrderNum());
                List<Product> productList = PS.findAll();
                for (Product p : productList)
                    input.put(String.valueOf(p.getName()), 0L);

                for (OrderDetail od : detailList)
                    input.put(String.valueOf(od.getProduct().getName()), (long) od.getAmount());


                input.put("address", (long) on.getOrder().getAddress());

                OS.set2OrderNow(on.getOrder().getOrderNum());

                answer.add(input);
            }

            return new ResponseEntity<>(answer, HttpStatus.OK);
        } catch (Exception e) {
            return ER.errorMessage(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> newOrder(@RequestBody NewOrderDto newOrderDto) {
        try {
            OS.addNewOrder(newOrderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ER.errorMessage(e);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> orderUpdate(@RequestBody OrderUpdateDto orderUpdateDto) {
        try {
            OrderNow orderNow = OS.findByOrderNum(orderUpdateDto.getOrderNum());
            if (OS.findByOrderNum(orderUpdateDto.getOrderNum()) == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (orderUpdateDto.getType() == 0) {
                if (orderUpdateDto.getResult() == 1) {
                    orderNow.setStatus(0);
                    MS.addLog(errorLog(orderUpdateDto, "포장 문제 발생"));
                    return new ResponseEntity<>("PROBLEM RECORD COMPLETE", HttpStatus.ACCEPTED);
                }
                if (orderNow.getStatus() == 2) {
                    orderNow.setStatus(3);
                    OS.updateStatus(orderNow);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.setStatus(3);
                    OS.updateStatus(orderNow);
                    MS.addLog(errorLog(orderUpdateDto, "포장 순서 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            } else if (orderUpdateDto.getType() == 1) {
                if (orderUpdateDto.getResult() == 1) {
                    orderNow.setStatus(0);
                    MS.addLog(errorLog(orderUpdateDto, "분류 문제 발생"));
                    return new ResponseEntity<>("PROBLEM RECORD COMPLETE", HttpStatus.ACCEPTED);
                }
                if (orderNow.getStatus() == 3) {
                    orderNow.setStatus(4);
                    OS.updateStatus(orderNow);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.setStatus(4);
                    OS.updateStatus(orderNow);
                    MS.addLog(errorLog(orderUpdateDto, "분류 순서 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            } else if (orderUpdateDto.getType() == 2) {
                if (orderNow.getStatus() == 4) {
                    orderNow.setStatus(5);
                    OS.updateStatus(orderNow);
                    SS.sendSms(new MessageDTO(orderNow.getOrder().getCustomer().getPhoneNumber(), "주문하신 상품이 배송완료되었습니다."));
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    orderNow.setStatus(5);
                    OS.updateStatus(orderNow);
                    MS.addLog(errorLog(orderUpdateDto, "배송 문제 발생"));
                    return new ResponseEntity<>("OMISSION CHECK NEED", HttpStatus.ACCEPTED);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ER.errorMessage(e);
        }
    }

    private Log errorLog(OrderUpdateDto orderUpdateDto, String errorMessage) {
        Log l = new Log();
        l.setErrorMessage(orderUpdateDto.getOrderNum() + " " + errorMessage);
        l.setMachine(MS.findMachine(0));
        return l;
    }

}
