package class2.a204.repository;

import class2.a204.entity.OrderNow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderNowRepository extends JpaRepository<OrderNow, Long> {


    /**
     * 주문번호에 따른 OrderNow 정보 검색
     *
     * @param orderNum OrderNow 정보를 검색할 주문 번호
     * @return 주어진 주문번호에 해당하는 OrderNow 정보
     *
     * */
    @Query("SELECT n FROM OrderNow n WHERE n.order.orderNum = ?1")
    OrderNow findByOrderNum(Long orderNum);

    /**
     * 주문 상태에 따른 모든 OrderNow 정보 검색
     *
     * @param status OrderNow 정보를 검색할 주문 상태
     * @return 주어진 주문 상태에 해당하는 모든 OrderNow 정보의 리스트
     *
     * */
    @Query("SELECT n FROM OrderNow n WHERE n.status = ?1")
    List<OrderNow> findAllByStatus(Integer status);
}
