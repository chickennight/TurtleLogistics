package class2.a204.repository;

import class2.a204.dto.AnalysisGetDTO;
import class2.a204.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    /**
     * 특정 날짜 이후의 상품별 주문량 조회
     *
     * @param today 오늘 시작에 해당 하는 시각
     * @param weekAgo 일주일 전에 해당 하는 시각
     * @param monthAgo 한달 전에 해당 하는 시각
     * @param yearAgo 일년 전에 해당 하는 시각
     * @return 주어진 주문번호에 해당하는 모든 상품 번호와 주문량 배열
     *
     * */
//    @Query(value = "SELECT od.product_num +
//            "SUM(CASE WHEN od.order_date >= :today THEN od.amount ELSE 0 END) AS todayAmount " +
//            "SUM(CASE WHEN od.order_date >= :weekAgo THEN od.amount END) / 7 AS weekAvg" +
//            "SUM(CASE WHEN od.order_date >= :monthAgo THEN od.amount END) / 30 AS monthAvg " +
//            "SUM(CASE WHEN od.order_date >= :yearAgo THEN od.amount END) / 365 AS yearAvg " +
//            "FROM orderdetail od +
//            "GROUP BY od.product_num",nativeQuery = true)
    @Query("SELECT new class2.a204.dto.AnalysisGetDTO(od.product.productNum, SUM(CASE WHEN od.orderDate >= :today THEN od.amount ELSE 0 END), SUM(CASE WHEN od.orderDate >= :weekAgo THEN od.amount END), SUM(CASE WHEN od.orderDate >= :monthAgo THEN od.amount END), SUM(CASE WHEN od.orderDate >= :yearAgo THEN od.amount END) ) FROM OrderDetail od GROUP BY od.product.productNum")
    List<AnalysisGetDTO> findAllInfo(@Param("today") Integer today,
                                     @Param("weekAgo") Integer weekAgo,
                                     @Param("monthAgo") Integer monthAgo,
                                     @Param("yearAgo") Integer yearAgo);
}
