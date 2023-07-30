package class2.a204.repository;

import class2.a204.dto.AnalysisDayDTO;
import class2.a204.dto.AnalysisRegionDTO;
import class2.a204.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 특정 일자의 모든 주문 수 카운트
     *
     * @param today 주문 데이터를 검색할 일자
     * @return 해당 일자의 총 주문 수
     *
     * */
    @Query("SELECT count(r) FROM Order r WHERE r.orderNum BETWEEN ?1*1000000 AND (?1+1)*1000000")
    int todayCount(int today);

    /**
     *
     * 특정 년도의 지역별 모든 주문 수를 표시하는 AnalysisRegionDTO 객체 리스트
     *
     * @param year 주문 데이터를 검색할 년도
     * @return 각각 지역번호와 해당 지역의 주문수를 포함하는 AnalysisRegionDTO객체 리스트
     *
     * */
    @Query(value = "SELECT new class2.a204.dto.AnalysisRegionDTO(o.address, COUNT(o)) FROM Order o WHERE YEAR(o.orderDate) = ?1 GROUP BY o.address")
    List<AnalysisRegionDTO> findRegionCountByYear(Integer year);

    /**
     *
     * 특졍 년도 월의 지역별 모든 주문 수를 표시하는 AnalysisRegionDTO 객체 리스트
     *
     * @param year 주문 데이터를 검색할 년도
     * @param month 주문 데이터를 검색할 월
     * @return 각각 지역번호와 해당 지역의 주문수를 포함하는 AnalysisRegionDTO객체 리스트
     *
     * */
    @Query(value = "SELECT new class2.a204.dto.AnalysisRegionDTO(o.address, COUNT(o)) FROM Order o WHERE YEAR(o.orderDate) = ?1 AND MONTH(o.orderDate) = ?2 GROUP BY o.address")
    List<AnalysisRegionDTO> findRegionCountByYearMonth(Integer year, Integer month);

    /**
     * 지정된 기간 동안에 각 일자별로 주문된 주문 수를 표시하는 AnalysisDayDTO 객체 리스트
     *
     * @param startDay 주문 데이터를 검색할 시작 일자
     * @param endDay 주문 데이터를 검색할 종료 일자
     * @return 각각의 일자와 해당 일자의 주문 수를 포함하는 AnalysisDayDTO 객체의 리스트
     */
    @Query(value = "SELECT new class2.a204.dto.AnalysisDayDTO(DATE_FORMAT(o.orderDate, '%Y%m%d'),COUNT(o)) FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2 GROUP BY DATE_FORMAT(o.orderDate, '%Y%m%d')")
    List<AnalysisDayDTO> findDayCount(LocalDateTime startDay, LocalDateTime endDay);
}
