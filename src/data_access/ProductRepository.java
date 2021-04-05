package data_access;

import domain.Assembly;
import domain.Part;
import domain.Product;
import utilities.CatalogueEntry;
import utilities.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private List<Product> products;
    private List<CatalogueEntry> entries;

	public ProductRepository(List<Product> products, List<CatalogueEntry> entries) {
        this.products = products;
        this.entries = entries;
    }

    public Product findAssemblyByNumber(int number) throws NotFoundException {
        for(Product product:products){
            if(product instanceof Assembly && product.getNumber() == number)
                return product;
        }
        throw new NotFoundException("Assembly with the number not found.");
    }

    public Product findPartByNumber(int number) throws NotFoundException {
        for(Product product:products){
            if(product instanceof Part && product.getNumber() == number)
                return product;
        }
        throw new NotFoundException("Part with the number not found.");
    }

    public List<Product> findAllAssemblies(){
        List<Product> assemblies = new ArrayList<>();
        for(Product product:products){
            if(product instanceof  Assembly)
                assemblies.add(product);
        }
        return assemblies;
    }

    public List<Product> findAllParts(){
        List<Product> assemblies = new ArrayList<>();
        for(Product product:products){
            if(product instanceof  Part)
                assemblies.add(product);
        }
        return assemblies;
    }

    public List<Product> findAssemblyParts(Product assembly){
        List<Product> parts = new ArrayList<>();
        for(Product product:((Assembly)assembly).getProducts()){
            if(product instanceof  Part)
                parts.add(product);
            else
                parts.addAll(findAssemblyParts(product));
        }
        return parts;
    }

    public List<Product> findLonelyParts(){
        List<Product> allParts = findAllParts();
        List<Product> notAloneParts = new ArrayList<>();
        for(Product product:products){
            if(product instanceof Assembly)
                notAloneParts.addAll(findAssemblyParts(product));
        }
        allParts.removeAll(notAloneParts);
        return allParts;
    }

    public CatalogueEntry findCatalogueEntryByNumber(int number) throws NotFoundException {
		for (CatalogueEntry entry : entries) {
			if(entry.getNumber() == number) {
				return entry;
			}
		}
		throw new NotFoundException("Catalogue entry with the number not found.");
	}
    
    public List<CatalogueEntry> getEntries() {
		return entries;
	}
    
    public Product save(Product product){
        products.add(product);
        return product;
    }
    
    public CatalogueEntry saveEntry(CatalogueEntry entry){
    	entries.add(entry);
        return entry;
    }
}
