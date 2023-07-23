package class2.a204.repository;

import class2.a204.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT count(*) FROM Order r WHERE r.orderNum BETWEEN ?1*1000000 AND (?1+1)*1000000")
    int todayCount(int today);

}
