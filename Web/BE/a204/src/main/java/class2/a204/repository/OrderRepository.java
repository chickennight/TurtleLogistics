package class2.a204.repository;

import class2.a204.dto.AnalysisRegionDto;
import class2.a204.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT count(*) FROM Order r WHERE r.orderNum BETWEEN ?1*1000000 AND (?1+1)*1000000")
    int todayCount(int today);

    @Query(value = "SELECT o.address AS region,COUNT(o) AS count FROM Order o WHERE YEAR(o.orderDate) = ?1 GROUP BY o.address")
    List<?> findRegionCountByYear(Integer year);

    @Query(value = "SELECT o.address AS region,COUNT(o) AS count FROM Order o WHERE YEAR(o.orderDate) = ?1 AND MONTH(o.orderDate) = ?2 GROUP BY o.address")
    List<?> findRegionCountByYearMonth(Integer year, Integer month);

    @Query(value = "SELECT DATE(o.orderDate) AS DATE,COUNT(o) AS count FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2 GROUP BY o.address")
    List<?> findDayCount(Date startDay, Date endDay);
}
