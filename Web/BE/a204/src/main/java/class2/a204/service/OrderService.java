package class2.a204.service;

import class2.a204.model.Orders;
import class2.a204.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Orders> findAllOrders() {
        return orderRepository.findAll();
    }
}