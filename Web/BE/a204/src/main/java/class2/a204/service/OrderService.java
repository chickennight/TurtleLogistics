package class2.a204.service;

import class2.a204.dto.NewOrderDto;
import class2.a204.entity.Log;
import class2.a204.entity.OrderNow;
import class2.a204.repository.LogRepository;
import class2.a204.repository.OrderNowRepository;
import class2.a204.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository OR;
    private final OrderNowRepository ONR;
    private final LogRepository LR;

    public OrderService(OrderRepository or, OrderNowRepository onr, LogRepository lr) {
        OR = or;
        ONR = onr;
        LR = lr;
    }

    public Object data() {
        return new Object();
    }

    public OrderNow findByOrderNum(Long orderNum) {
        return ONR.findOrderNowByOrderNum(orderNum);
    }

    public void addNewOrder(NewOrderDto newOrderDto) {
    }

    public void updateStatus(OrderNow orderNow) {
        ONR.save(orderNow);
    }

    public List<Log> findOrderError() {
        return LR.findAllByMachine_MachineId(0);
    }
}
