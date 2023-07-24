package class2.a204.service;

import class2.a204.dto.NewOrderDto;
import class2.a204.dto.AnalysisDayDto;
import class2.a204.dto.AnalysisRegionDto;
import class2.a204.entity.*;
import class2.a204.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository OR;
    private final OrderNowRepository ONR;
    private final LogRepository LR;
    private final CustomerRepository CR;
    private final OrderDetailRepository ODR;

    @Autowired
    public OrderService(OrderRepository or, OrderNowRepository onr, LogRepository lr, CustomerRepository cr, OrderDetailRepository odr) {
        OR = or;
        ONR = onr;
        LR = lr;
        CR = cr;
        ODR = odr;
    }

    public OrderNow findByOrderNum(Long orderNum) {
        return ONR.findByOrderNum(orderNum);
    }

    public void addNewOrder(NewOrderDto newOrderDto) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int today = (year - 2000) * 100 + month;
        int todayOrders = OR.todayCount(today);

        System.out.println(newOrderDto);
        Order input = new Order();
        input.setOrderNum((long) today * 1000000 + todayOrders + 1);
        input.setDetailAddress(newOrderDto.getDetailAddress());
        input.setAddress(newOrderDto.getAddress());
        Optional<Customer> customer = CR.findById(newOrderDto.getCustomerNum());
        input.setCustomer(customer.orElseGet(Customer::new));

        System.out.println(input);
        OR.save(input);

        for (Product p : newOrderDto.getProducts()) {
            OrderDetail in = new OrderDetail();
            in.setOrder(input);
            in.setProduct(p);
            in.setAmount(p.getStock());
            ODR.save(in);
        }

        OrderNow create = new OrderNow();
        create.setOrder(input);
        create.setStatus(1);
        ONR.save(create);
    }

    public void updateStatus(OrderNow orderNow) {
        ONR.save(orderNow);
    }

    public List<Log> findOrderError() {
        return LR.findAllByMachine_MachineId(0);
    }

    public List<OrderNow> findAllOrders() {
        return ONR.findAll();
    }

    public List<OrderNow> findPackageOrders() {
        return ONR.findAllByStatus(1);
    }

    public List<OrderDetail> findOrderDetailsBy(Long orderNum) {
        return ODR.findAllByOrderNum(orderNum);
    }

    public void set2OrderNow(Long orderNum) {
        OrderNow temp = ONR.findByOrderNum(orderNum);
        temp.setStatus(2);
        ONR.save(temp);
    }

    public List<AnalysisRegionDto> dataRegion(Integer year, Integer month) {
        if (month == 0)
            return OR.findRegionCountByYear(year);
        else
            return OR.findRegionCountByYearMonth(year, month);
    }

    public List<AnalysisDayDto> dataDay(LocalDateTime startDay, LocalDateTime endDay) {
        return OR.findDayCount(startDay, endDay);
    }
}
