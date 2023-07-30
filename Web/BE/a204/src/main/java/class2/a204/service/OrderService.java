package class2.a204.service;

import class2.a204.dto.NewOrderDTO;
import class2.a204.dto.AnalysisDayDTO;
import class2.a204.dto.AnalysisRegionDTO;
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

    public void addNewOrder(NewOrderDTO newOrderDto) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int today = (year - 2000) * 100 + month;
        int todayOrders = OR.todayCount(today);


        Long orderNum = ((long) today * 1000000 + todayOrders + 1);
        Customer customer = CR.findByCustomerNum(newOrderDto.getCustomerNum());
        Order input = new Order(orderNum, newOrderDto.getDetailAddress(), newOrderDto.getAddress(), customer);

        System.out.println(input);
        OR.save(input);

        for (Product p : newOrderDto.getProducts()) {
            OrderDetail in = new OrderDetail(input, p, p.getStock());
            ODR.save(in);
        }


        OrderNow create = new OrderNow(input, 1);
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
        temp.changeStatus(2);
        ONR.save(temp);
    }

    public Map<String, Long> dataRegion(Integer year, Integer month) {
        List<AnalysisRegionDTO> list;
        if (month == 0) list = OR.findRegionCountByYear(year);
        else list = OR.findRegionCountByYearMonth(year, month);

        Map<String, Long> result = new HashMap<>();

        for (int i = 1; i <= 17; ++i)
            result.put(String.valueOf(i), 0L);

        for (AnalysisRegionDTO dto : list) {
            result.put(String.valueOf(dto.getRegion()), dto.getCount());
        }
        return result;
    }

    public Map<String, Long> dataDay(LocalDateTime startDay, LocalDateTime endDay) {
        List<AnalysisDayDTO> list = OR.findDayCount(startDay, endDay);
        Map<String, Long> result = new TreeMap<>();
        LocalDateTime now = startDay;
        endDay = endDay.plusDays(1);
        while (now.isBefore(endDay)) {
            int input = now.getYear() * 10000 + now.getMonth().getValue() * 100 + now.getDayOfMonth();
            result.put(String.valueOf(input), 0L);
            now = now.plusDays(1);
        }
        for (AnalysisDayDTO ad : list)
            result.put(ad.getDay(), ad.getCount());

        return result;
    }
}
