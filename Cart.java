// ========================================================================
// Name        : 2200169efolioA
// Author      : bmfernandes
// Description : BrunoMiguelFernandes_2200169_efolioA
// Date        : 06.05.2024
// 
// NOTAS       :
// 
// 
// 2200169efolioA-LdP
// ============================================================================
// Cart.java
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Item> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void addItem(int quantity, String itemName, List<Item> storeItems) {
        for (Item item : storeItems) {
            if (item.getItemName().equals(itemName)) {
                int availableQuantity = item.getItemQuantity();
                if (availableQuantity >= quantity) {
                    // Reduce the quantity in the store
                    item.setItemQuantity(availableQuantity - quantity);
                    // Add the item to the cart
                    cartItems.add(new Item(item.getItemId(), item.getItemName(), item.getItemCategory(), item.getItemPrice(), quantity));
                } else {
                    System.out.println("Insufficient quantity in store for " + itemName);
                }
                return; // Stop searching after adding the item
            }
        }
        System.out.println("Item not found in the store: " + itemName);
    }

    // Getters for item attributes
    public List<Item> getItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

}
