package class2.a204.repository;

import class2.a204.entity.OrderNow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNowRepository extends JpaRepository<OrderNow, Long> {

    OrderNow findOrderNowByOrderNum(Long orderNum);
}
