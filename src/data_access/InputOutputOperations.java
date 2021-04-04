package data_access;

import domain.*;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputOutputOperations {

    private static FileWriter file;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public InputOutputOperations(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void outputProducts(){
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
}
