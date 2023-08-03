package class2.a204.repository;

import class2.a204.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - ?2 WHERE p.productNum = ?1")
    void updateStock(Integer productNum, Integer stock);
}
