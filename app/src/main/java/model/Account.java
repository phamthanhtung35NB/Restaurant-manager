package model;
import java.io.Serializable;
//import MenuRestaurant;
import java.util.ArrayList;
import java.util.TreeMap;

public class Account {
    private String id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String address;
    private TreeMap<String,MenuRestaurant> menuRestaurant=new TreeMap<>();
    //constructor
    public Account() {
    }
    public Account(String id, String username, String password, String phone, String email, String address,String id2, String name, String description, double price, String image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.address = address;
        MenuRestaurant menuRestaurant1 = new MenuRestaurant(id2, name, description, price, image);
        MenuRestaurant menuRestaurant4 = new MenuRestaurant("4", "Cơm chiên", "Cơm chiên hải sản", 50000, "https://cdn.tgdd.vn/Files/2020/07/14/1276873/food-1_800x450.jpg");
        MenuRestaurant menuRestaurant2 = new MenuRestaurant("2", "Cơm trắng", "Cơm  hải sản", 10000, "https://cdn.tgdd.vn/Files/2020/07/14/1276873/food-1_800x450.jpg");
        MenuRestaurant menuRestaurant3 = new MenuRestaurant("3", "Cá", "Cơm chiên hải sản", 500000, "https://cdn.tgdd.vn/Files/2020/07/14/1276873/food-1_800x450.jpg");

        this.menuRestaurant.put(id2,menuRestaurant1);
        this.menuRestaurant.put("2",menuRestaurant2);
        this.menuRestaurant.put("3",menuRestaurant3);
        this.menuRestaurant.put("4",menuRestaurant4);
    }
//    TreeMap<String,
//            MenuRestaurant> menuRestaurant
//    public Account(String username, String password, String phone, String email, String address, MenuRestaurant menuRestaurant) {
//        this.username = username;
//        this.password = password;
//        this.phone = phone;
//        this.email = email;
//        this.address = address;
//        this.menuRestaurant.getDescription()=menuRestaurant.getDescription();
//    }
    //getter and setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public TreeMap<String, MenuRestaurant> getMenuRestaurant() {
        return menuRestaurant;
    }
    public void setMenuRestaurant(TreeMap<String, MenuRestaurant> menuRestaurant) {
        this.menuRestaurant = menuRestaurant;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", menuRestaurant=" + menuRestaurant +
                '}';
    }

}
