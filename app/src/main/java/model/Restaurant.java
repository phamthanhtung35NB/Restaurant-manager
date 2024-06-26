package model;
public class Restaurant {
    private int idTableMax;
    private int idMax;
    private String phone;
    private String username;

    //description la mot mo ta cua nha hang
    private String description;
    private String address;
    private String location;
    private String image;

    public Restaurant(int idTableMax, int idMax, String phone, String username, String description, String address, String image) {
        this.idTableMax = idTableMax;
        this.idMax = idMax;
        this.phone = phone;
        this.username = username;
        this.description = description;
    }

    public Restaurant(int idTableMax, int idMax, String phone, String username) {
        this.idTableMax = idTableMax;
        this.idMax = idMax;
        this.phone = phone;
        this.username = username;
        this.description = "";
    }

    public Restaurant(int idTableMax, int idMax, String phone, String username, String description, String address, String location, String image) {
        this.idTableMax = idTableMax;
        this.idMax = idMax;
        this.phone = phone;
        this.username = username;
        this.description = description;
        this.address = address;
        this.location = location;
        this.image = image;
    }
    public double getLatitude(){
        String[] temp = location.split("_");
            return Double.parseDouble(temp[0]);}
    public double getLongitude(){
        String[] temp = location.split("_");
        return Double.parseDouble(temp[1]);
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdTableMax() {
        return idTableMax;
    }

    public void setIdTableMax(int idTableMax) {
        this.idTableMax = idTableMax;
    }

    public int getIdMax() {
        return idMax;
    }

    public void setIdMax(int idMax) {
        this.idMax = idMax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
