package class2.a204.service;

import class2.a204.dto.NewOrderDto;
import class2.a204.entity.*;
import class2.a204.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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

    public String data() {
        return "결과물";
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
}
