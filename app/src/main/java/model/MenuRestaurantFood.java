package model;

public class MenuRestaurantFood extends MenuRestaurant {
    private String type;

    public MenuRestaurantFood() {
    }

    public MenuRestaurantFood(String id, String name, String description, double price, String image, String type) {
        super(id, name, description, price, image);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return "MenuFood{" +
                "type='" + type + '\'' +
                '}';
    }
}
