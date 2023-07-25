package class2.a204.repository;

import class2.a204.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT od FROM OrderDetail od WHERE od.order.orderNum = ?1")
    List<OrderDetail> findAllByOrderNum(Long orderNum);
}
