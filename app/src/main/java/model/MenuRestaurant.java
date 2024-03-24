
package model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MenuRestaurant implements Serializable{
    private String id;
    private String name;
    //miêu tả món ăn
    private String description;
    //giá tiền
    private double price;
    //hình ảnh
    private String image;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    //constructor
    public MenuRestaurant() {
    }
    public Map<String, Object> toMap() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", this.name);
        dataMap.put("description", this.description);
        dataMap.put("price", this.price);
        dataMap.put("image", this.image);
        return dataMap;
    }
    public MenuRestaurant(String id, String name, String description, double price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public MenuRestaurant(String name, String description, double price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    //getter and setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public String getImage() {
        return image;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setImage(String image) {
        this.image = image;
    }
    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }

}
