package class2.a204.repository;

import class2.a204.entity.OrderNow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderNowRepository extends JpaRepository<OrderNow, Long> {

    @Query("SELECT n FROM OrderNow n WHERE n.order.orderNum = ?1")
    OrderNow findByOrderNum(Long orderNum);
}
