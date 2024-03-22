package model;

import java.util.TreeMap;

public class Table {
    private int id;
    private String name;

    private String describe;
    private String stateEmpty;
    private String image;
    private TreeMap<String,MenuRestaurant> menuRestaurant=new TreeMap<>();
    public Table() {
    }
    public Table(int id, String name,String describe, String stateEmpty, String image) {
        this.id = id;
        this.name = name;
        this.stateEmpty = stateEmpty;
        this.image = image;
        this.describe=describe;
    }

    public Table(int id, String name,String describe, String stateEmpty, String image, TreeMap<String, MenuRestaurant> menuRestaurant) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.stateEmpty = stateEmpty;
        this.image = image;
        this.menuRestaurant = menuRestaurant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStateEmpty() {
        return stateEmpty;
    }

    public void setStateEmpty(String stateEmpty) {
        this.stateEmpty = stateEmpty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public TreeMap<String, MenuRestaurant> getMenuRestaurant() {
        return menuRestaurant;
    }

    public void setMenuRestaurant(TreeMap<String, MenuRestaurant> menuRestaurant) {
        this.menuRestaurant = menuRestaurant;
    }
}
