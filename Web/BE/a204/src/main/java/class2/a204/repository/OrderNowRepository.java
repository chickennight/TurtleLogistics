package class2.a204.repository;

import class2.a204.entity.OrderNow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderNowRepository extends JpaRepository<OrderNow, Long> {


    /**
     * 주문번호에 따른 OrderNow 정보 검색
     *
     * @param orderNum OrderNow 정보를 검색할 주문 번호
     * @return 주어진 주문번호에 해당하는 OrderNow 정보
     */
    @Query("SELECT n FROM OrderNow n WHERE n.order.orderNum = ?1")
    Optional<OrderNow> findByOrderNum(Long orderNum);

    /**
     * 주문 상태에 따른 모든 OrderNow 정보 검색
     *
     * @param status OrderNow 정보를 검색할 주문 상태
     * @return 주어진 주문 상태에 해당하는 모든 OrderNow 정보의 리스트
     */
    @Query("SELECT n FROM OrderNow n WHERE n.status = ?1")
    List<OrderNow> findAllByStatus(Integer status);

    /**
     * 지정된 기간 동안에 각 지역별로 주문된 주문 수를 표시하는 배열
     *
     * @param regionCode 주문 데이터를 검색할 지역 코드
     * @return 각각의 주문현황와 해당 현황의 주문 수를 포함하는 배열의 리스트
     */
    @Query("SELECT o.status,count(o.status) FROM OrderNow o WHERE o.order.address = ?1 GROUP BY o.status")
    List<Long[]> analysisRegion(Integer regionCode);
}
