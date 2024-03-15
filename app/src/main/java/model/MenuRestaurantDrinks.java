package model;

public class MenuRestaurantDrinks extends MenuRestaurant {
    private String size;

    public MenuRestaurantDrinks() {
    }
    public MenuRestaurantDrinks(String id, String name, String description, double price, String image, String size) {
        super(id, name, description, price, image);
        this.size = size;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    @Override
    public String toString() {
        return "MenuDrinks{" +
                "size='" + size + '\'' +
                '}';
    }
}
