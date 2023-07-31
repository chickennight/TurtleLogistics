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
    private final OrderRepository orderRepository;
    private final OrderNowRepository orderNowRepository;
    private final LogRepository logRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderNowRepository orderNowRepository, LogRepository logRepository, CustomerRepository customerRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderNowRepository = orderNowRepository;
        this.logRepository = logRepository;
        this.customerRepository = customerRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public OrderNow findByOrderNum(Long orderNum) {
        return orderNowRepository.findByOrderNum(orderNum);
    }

    public void addNewOrder(NewOrderDTO newOrderDto) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int today = (year - 2000) * 100 + month;
        int todayOrders = orderRepository.todayCount(today);


        Long orderNum = ((long) today * 1000000 + todayOrders + 1);
        Customer customer = customerRepository.findByCustomerNum(newOrderDto.getCustomerNum());
        Order input = new Order(orderNum, newOrderDto.getDetailAddress(), newOrderDto.getAddress(), customer);

        System.out.println(input);
        orderRepository.save(input);

        for (Product p : newOrderDto.getProducts()) {
            OrderDetail in = new OrderDetail(input, p, p.getStock());
            orderDetailRepository.save(in);
        }


        OrderNow create = new OrderNow(input, 1);
        orderNowRepository.save(create);
    }

    public void updateStatus(OrderNow orderNow) {
        orderNowRepository.save(orderNow);
    }

    public List<Log> findOrderError() {
        return logRepository.findAllByMachine_MachineId(0);
    }

    public List<OrderNow> findAllOrders() {
        return orderNowRepository.findAll();
    }

    public List<OrderNow> findPackageOrders() {
        return orderNowRepository.findAllByStatus(1);
    }

    public List<OrderDetail> findOrderDetailsBy(Long orderNum) {
        return orderDetailRepository.findAllByOrderNum(orderNum);
    }

    public void set2OrderNow(Long orderNum) {
        OrderNow temp = orderNowRepository.findByOrderNum(orderNum);
        temp.changeStatus(2);
        orderNowRepository.save(temp);
    }

    public Map<String, Long> dataRegion(Integer year, Integer month) {
        List<AnalysisRegionDTO> list;
        if (month == 0) list = orderRepository.findRegionCountByYear(year);
        else list = orderRepository.findRegionCountByYearMonth(year, month);

        Map<String, Long> result = new HashMap<>();

        for (int i = 1; i <= 17; ++i)
            result.put(String.valueOf(i), 0L);

        for (AnalysisRegionDTO dto : list) {
            result.put(String.valueOf(dto.getRegion()), dto.getCount());
        }
        return result;
    }

    public Map<String, Long> dataDay(LocalDateTime startDay, LocalDateTime endDay) {
        List<AnalysisDayDTO> list = orderRepository.findDayCount(startDay, endDay);
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
