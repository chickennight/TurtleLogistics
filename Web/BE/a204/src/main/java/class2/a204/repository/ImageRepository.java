package class2.a204.repository;


import class2.a204.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query("SELECT i FROM Image i WHERE i.log.logNum = ?1")
    Optional<Image> findByLogNum(String logNum);
}
