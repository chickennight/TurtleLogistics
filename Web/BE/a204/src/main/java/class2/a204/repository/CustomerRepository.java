package class2.a204.repository;

import class2.a204.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByCustomerId(String customerId);

    @Query("SELECT c FROM Customer c WHERE c.customerNum = ?1")
    Customer findByCustomerNum(Integer customerNum);
}
