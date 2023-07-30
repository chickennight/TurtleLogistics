package class2.a204.repository;

import class2.a204.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    /**
     * 주문번호에 따른 모든 주문 상세 정보 검색
     *
     * @param orderNum 주문 상세 정보를 검색할 주문 번호
     * @return 주어진 주문번호에 해당하는 모든 주문 상세 정보의 리스트
     *
     * */
    @Query("SELECT od FROM OrderDetail od WHERE od.order.orderNum = ?1")
    List<OrderDetail> findAllByOrderNum(Long orderNum);
}
