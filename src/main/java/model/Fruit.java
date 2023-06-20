package model;


import java.util.Objects;

public class Fruit {
    private long id;
    private String fruitName;

    private int quantity;
    private double pricePerPill;
    private String productionDate;
    private String expirationDate;
    private String note;

    public Fruit() {}

    public Fruit(String raw) {
        String[] fruitInformation = raw.split("~");
        this.id = Long.parseLong(fruitInformation[0]);
        this.fruitName = fruitInformation[1];
        this.quantity = Integer.parseInt(fruitInformation[2]);
        this.pricePerPill = Double.parseDouble(fruitInformation[3]);
        this.productionDate = fruitInformation[4];
        this.expirationDate = fruitInformation[5];
        this.note = fruitInformation[6];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerPill() {
        return pricePerPill;
    }

    public void setPricePerPill(double pricePerPill) {
        this.pricePerPill = pricePerPill;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return id +
                "~" + fruitName +
                "~" + quantity +
                "~" + pricePerPill +
                "~" + productionDate +
                "~" + expirationDate +
                "~" + note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fruit drug = (Fruit) o;
//        return fruitContent == drug.fruitContent && fruitName.equalsIgnoreCase(drug.fruitName);
        return true;
    }

    @Override
    public int hashCode() {
//        return Objects.hash(fruitName, fruitContent, productionDate);
        return 1;
    }
}

