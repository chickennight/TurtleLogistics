package class2.a204.service;

import class2.a204.entity.Product;
import class2.a204.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository PR;

    @Autowired
    public ProductService(ProductRepository pr) {
        PR = pr;
    }

    public List<Product> findAll() {
        return PR.findAll();
    }
}
