// ========================================================================
// Name        : efolioA
// Author      : 
// Description : 
// Date        : 06.05.2024
// 
// NOTAS       :
// 
// 
// efolioA-LdP
// ============================================================================
// Item.java
public class Item {
    private int ItemID;
    private String ItemName;
    private String ItemCatgry;
    private double ItemPrice;
    private int ItemQTY;

    public Item(int ItemID, String ItemName, String ItemCatgry, double ItemPrice, int ItemQTY) {
        this.ItemID = ItemID;
        this.ItemName = ItemName;
        this.ItemCatgry = ItemCatgry;
        this.ItemPrice = ItemPrice;
        this.ItemQTY = ItemQTY;
    }

    public int getItemId() {
        return ItemID;
    }

    public void setItemId(int ItemID) {
        this.ItemID = ItemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getItemCategory() {
        return ItemCatgry;
    }

    public void setItemCategory(String ItemCatgry) {
        this.ItemCatgry = ItemCatgry;
    }

    public double getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(double ItemPrice) {
        this.ItemPrice = ItemPrice;
    }

    public int getItemQuantity() {
        return ItemQTY;
    }

    public void setItemQuantity(int ItemQTY) {
        this.ItemQTY = ItemQTY;
    }

    @Override
    public String toString() {
        return "Item{" +
                "ItemID=" + ItemID +
                ", ItemName='" + ItemName + '\'' +
                ", ItemCatgry='" + ItemCatgry + '\'' +
                ", ItemPrice=" + ItemPrice +
                ", ItemQTY=" + ItemQTY +
                '}';
    }
}
