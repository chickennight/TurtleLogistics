//package class2.a204.service;
//
//import class2.a204.repository.OrderRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Calendar;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@Transactional
//class OrderServiceTest {
//
//    private final OrderRepository OR;
//
//    @Autowired
//    OrderServiceTest(OrderRepository or) {
//        OR = or;
//    }
//
//    @Test
//    void data() {
//    }
//
//    @Test
//    void findByOrderNum() {
//    }
//
//    @Test
//    void addNewOrder() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int today = (year - 2000) * 100 + month;
//
//        System.out.println(OR.todayCount(today));
//    }
//
//    @Test
//    void updateStatus() {
//    }
//
//    @Test
//    void findOrderError() {
//    }
//}