package data_access;

import domain.*;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utilities.CatalogueEntry;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputOutputOperations {

    private static FileWriter file;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public InputOutputOperations() {
        this.productRepository = inputProducts();
        this.userRepository = inputUsers();
    }

    public void outputProducts(){
        JSONObject productsJSON = new JSONObject();
        JSONObject assembliesAndPartsJson = getProductJson();
        JSONObject catalogEntriesJson = getCatalogueEntriesJson();
        productsJSON.put("assembliesAndParts",assembliesAndPartsJson);
        productsJSON.put("catalogEntries",catalogEntriesJson);
        try {

            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter("products.json");
            file.write(productsJSON.toString());
            CrunchifyLog("Successfully Copied JSON Object to File...");
            CrunchifyLog("\nJSON Object: " + productsJSON);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private JSONObject getProductJson(){
        JSONObject productsJSON = new JSONObject();
        List<JSONObject> assembliesJson = new ArrayList<>();
        List<JSONObject> partsJson = new ArrayList<>();

        List<Product> assemblies = productRepository.findAllAssemblies();
        List<Product> lonelyParts = productRepository.findLonelyParts();
        for(Product product:assemblies){
            assembliesJson.add(((Assembly)product).getProductTree());
        }
        for(Product product:lonelyParts){
            partsJson.add(((Part)product).getJson());
        }
        productsJSON.put("ASSEMBLIES",assembliesJson);
        productsJSON.put("PARTS",partsJson);
        return productsJSON;
    }

    private JSONObject getCatalogueEntriesJson(){
        JSONObject entriesJson = new JSONObject();
        List<CatalogueEntry> entries = productRepository.getEntries();
        List<JSONObject> entriesJsonList = new ArrayList<>();
        for(CatalogueEntry entry:entries){
            entriesJsonList.add(entry.getJson());
        }
        entriesJson.put("CatalogEntries",entriesJsonList);
        return entriesJson;
    }

    public void outputUsers(){
        JSONObject usersJson = new JSONObject();
        List<User> admins = userRepository.findAdmins();
        List<JSONObject> adminsJson = new ArrayList<>();
        for(User admin:admins){
            adminsJson.add(admin.getJson());
        }
        usersJson.put("ALLUSERS",adminsJson);
        try {

            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter("users.json");
            file.write(usersJson.toString());
            CrunchifyLog("Successfully Copied JSON Object to File...");
            CrunchifyLog("\nJSON Object: " + usersJson);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    static public void CrunchifyLog(String str) {
        System.out.println("str");
    }



    public ProductRepository inputProducts(){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("products.json"))
        {
            Object obj = jsonParser.parse(reader);
            org.json.simple.JSONObject productsAndCatalogEntries = (org.json.simple.JSONObject) obj;
            List<CatalogueEntry> catalogueEntries = inputCatalogEntries((org.json.simple.JSONObject) productsAndCatalogEntries.get("catalogEntries"));
            List<Product> products = inputProducts((org.json.simple.JSONObject) productsAndCatalogEntries.get("assembliesAndParts"));
            return new ProductRepository(products,catalogueEntries);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CatalogueEntry> inputCatalogEntries(org.json.simple.JSONObject catalogEntriesJson){
        List<CatalogueEntry> catalogueEntries = new ArrayList<>();
        org.json.simple.JSONArray entries = (org.json.simple.JSONArray)catalogEntriesJson.get("CatalogEntries");
        entries.forEach( entry -> catalogueEntries.add(CatalogueEntry.parseJson( (org.json.simple.JSONObject) entry ) ));
        return catalogueEntries;

    }
    private List<Product> inputProducts(org.json.simple.JSONObject productsJson){
        List<Product> products = new ArrayList<>();
        org.json.simple.JSONArray assemblies = (org.json.simple.JSONArray)productsJson.get("ASSEMBLIES");
        org.json.simple.JSONArray parts = (org.json.simple.JSONArray)productsJson.get("PARTS");
        assemblies.forEach( entry -> products.add(Assembly.parseJson( (org.json.simple.JSONObject) entry ) ));
        parts.forEach( entry -> products.add(Part.parseJson( (org.json.simple.JSONObject) entry ) ));
        return products;
    }

    public UserRepository inputUsers(){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("users.json"))
        {
            Object obj = jsonParser.parse(reader);
            org.json.simple.JSONObject usersJson = (org.json.simple.JSONObject) obj;
            org.json.simple.JSONArray admins = (org.json.simple.JSONArray)usersJson.get("ALLUSERS");
            List<User> users = parseUserArray(admins);
            return new UserRepository(users);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<User> parseUserArray(org.json.simple.JSONArray usersJson){
        List<User> admins = new ArrayList<>();
        List<User> users = new ArrayList<>();
        usersJson.forEach( entry -> admins.add(Admin.parseJson( (org.json.simple.JSONObject) entry ) ));
        for (User admin:admins){
            users.addAll(((Admin)admin).getAllManagers());
            users.addAll(((Admin)admin).getAllEmployees());
            users.add(admin);
        }

        return users;
    }


}
