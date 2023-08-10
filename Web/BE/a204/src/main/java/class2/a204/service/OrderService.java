package class2.a204.service;

import class2.a204.dto.NewOrderDTO;
import class2.a204.dto.AnalysisDayDTO;
import class2.a204.dto.AnalysisRegionDTO;
import class2.a204.dto.OrderNowDTO;
import class2.a204.entity.*;
import class2.a204.repository.*;
import class2.a204.util.DataNotFountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderNowRepository orderNowRepository;
    private final LogRepository logRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderNowRepository orderNowRepository, LogRepository logRepository, CustomerRepository customerRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderNowRepository = orderNowRepository;
        this.logRepository = logRepository;
        this.customerRepository = customerRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
    }

    public OrderNow findByOrderNum(Long orderNum) throws DataNotFountException {
        Optional<OrderNow> orderNow = orderNowRepository.findByOrderNum(orderNum);
        if (orderNow.isPresent())
            return orderNow.get();
        else throw new DataNotFountException("현재 진행 중인 주문 없음");
    }

    public void addNewOrder(NewOrderDTO newOrderDto) throws DataNotFountException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int today = (year - 2000) * 100 + month;
        int todayOrders = orderRepository.todayCount(today);


        Long orderNum = ((long) today * 1000000 + todayOrders + 1);
        Optional<Customer> customer = customerRepository.findByCustomerNum(newOrderDto.getCustomerNum());
        Order input;
        if (customer.isPresent())
            input = new Order(orderNum, newOrderDto.getDetailAddress(), newOrderDto.getAddress(), customer.get());
        else throw new DataNotFountException("고객 정보 조회 실패");

        orderRepository.save(input);

        for (Product p : newOrderDto.getProducts()) {
            OrderDetail in = new OrderDetail(input, p, p.getStock(), changeForm(LocalDateTime.now()));
            orderDetailRepository.save(in);
            productRepository.updateStock(p.getProductNum(), p.getStock());
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

    public void set2OrderNow(Long orderNum) throws DataNotFountException {
        Optional<OrderNow> temp = orderNowRepository.findByOrderNum(orderNum);
        if (temp.isPresent()) {
            OrderNow orderNow = temp.get();
            orderNow.changeStatus(2);
            orderNowRepository.save(orderNow);
        } else throw new DataNotFountException("현재 진행 중인 주문 없음");
    }

    public Map<String, Long> dataDay(LocalDateTime startDay, LocalDateTime endDay) {
        LocalDateTime startOfDay = startDay.toLocalDate().atStartOfDay();
        List<AnalysisDayDTO> list = orderRepository.findDayCount(startOfDay, endDay);
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

    public List<OrderNowDTO> getOrderNow() {
        List<OrderNow> list = orderNowRepository.findAll();
        List<OrderNowDTO> ans = new ArrayList<>();
        for (OrderNow on : list)
            ans.add(new OrderNowDTO(on));
        return ans;
    }

    public void deleteOrderNow(OrderNow orderNow) {
        orderNowRepository.delete(orderNow);
    }

    public AnalysisRegionDTO dataRegionCode(Integer regionCode) {
        List<Long[]> list = orderNowRepository.analysisRegion(regionCode);
        return new AnalysisRegionDTO(list);
    }

    private int changeForm(LocalDateTime ldt) {
        int year = ldt.getYear();
        int month = ldt.getMonthValue();
        int day = ldt.getDayOfMonth();
        return year * 10000 + month * 100 + day;
    }
}
