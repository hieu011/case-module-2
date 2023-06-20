package model;

public class Order {
    private long id;
    private long userId;
    private String name;
    private String phoneNumber;
    private String address;
    private double grandTotal;
    private long creationTime;

    public Order(){}

    public Order(String record) {
        String[] orders = record.split("~");
        id = Long.parseLong(orders[0]);
        userId = Long.parseLong(orders[1]);
        name = orders[2];
        phoneNumber = orders[3];
        address = orders[4];
        grandTotal = Double.parseDouble(orders[5]);
        creationTime = Long.parseLong(orders[6]);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    @Override
    public String toString() {
        return  id +
                "~" + userId +
                "~" + name +
                "~" + phoneNumber +
                "~" + address +
                "~" + grandTotal +
                "~" + creationTime;
    }
}

