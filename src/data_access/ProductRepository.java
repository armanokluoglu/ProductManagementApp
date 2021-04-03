package data_access;

import domain.Assembly;
import domain.Part;
import domain.Product;
import utilities.NotFoundException;

import java.util.List;

public class ProductRepository {

    private List<Product> products;

    public ProductRepository(List<Product> products) {
        this.products = products;
    }

    public Product findAssemblyByNumber(int number) throws NotFoundException {
        for(Product product:products){
            if(product instanceof Assembly && product.getNumber() == number)
                return product;
        }
        throw  new NotFoundException();
    }

    public Product findPartByNumber(int number) throws NotFoundException {
        for(Product product:products){
            if(product instanceof Part && product.getNumber() == number)
                return product;
        }
        throw  new NotFoundException();
    }

    public Product save(Product product){
        products.add(product);
        return product;
    }
}
