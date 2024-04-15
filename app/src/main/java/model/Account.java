package model;
import java.io.Serializable;
//import MenuRestaurant;
import java.util.ArrayList;
import java.util.TreeMap;

public class Account {
    private String type;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String address;
    //tọa độ của người dùng
    private String location;
    //link ảnh đại diện
    private String profilePic;
    private TreeMap<String,MenuRestaurant> menuRestaurant=new TreeMap<>();
    //constructor
    public Account() {
    }
    public Account( String type,String username, String password, String phone, String email, String address,String id2, String name, String description, double price, String image) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.address = address;
        MenuRestaurant menuRestaurant1 = new MenuRestaurant(id2, name, description, price, image);
        this.menuRestaurant.put(id2,menuRestaurant1);
    }
    public Account(String type,String username, String password, String phone, String email, String address,String location,String profilePic) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.location=location;
        this.profilePic=profilePic;
//        MenuRestaurant menuRestaurant1 = new MenuRestaurant(id2, name, description, price, image);
//        this.menuRestaurant.put(id2,menuRestaurant1);
        this.menuRestaurant = new TreeMap<>();
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address ;
    }

}
