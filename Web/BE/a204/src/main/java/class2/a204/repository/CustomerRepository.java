package class2.a204.repository;

import class2.a204.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByCustomerId(String customerId);

    /**
     * 특정 고객 번호를 이용해 고객 정보 반환
     *
     * @param customerNum 고객을 찾기 위한 고객 번호
     * @return 찾아진 고객 객체, 해당 번호를 가진 고객이 없을 경우 null 반환
     */
    @Query("SELECT c FROM Customer c WHERE c.customerNum = ?1")
    Optional<Customer> findByCustomerNum(Integer customerNum);
}
