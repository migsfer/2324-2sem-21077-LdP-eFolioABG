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
// Client.java
public class Client {
    private int id;
    private String CltName;
    private String city;
    private String district;
    private int loyaltyYears;
    private Cart cart;

    public Client(int id, String CltName, String city, String district, int loyaltyYears, Cart cart) {
        this.id = id;
        this.CltName = CltName;
        this.city = city;
        this.district = district;
        this.loyaltyYears = loyaltyYears;
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public String getCltName() {
        return CltName;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public int getLoyaltyYears() {
        return loyaltyYears;
    }

    public Cart getCart() {
        return cart;
    }

    public void ClientCartStatus() {
        System.out.println("\n");
        System.out.println("Client " + id + ", with " + loyaltyYears + " years loyalty, from " + district + ", current cart:");
        for (Item item : cart.getItems()) {
            System.out.println("Item: " + item.getItemId() + ", " + item.getItemName() + ", " + item.getItemCategory() + ", " + item.getItemPrice() + ", " + item.getItemQuantity());
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", Name'" + CltName + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", loyaltyYears=" + loyaltyYears +
                //", ListadeCompras=" + cart +
                '}';
    }
}
