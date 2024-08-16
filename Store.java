// ========================================================================
// Name        : efolioB
// Author      : 
// Description : 
// Date        : 26.05.2024
// 
// NOTAS       :
// 
// 
// efolioB-LdP
// ============================================================================
// Store.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jpl7.*;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;


public class Store {
    private List<Client> clients;
    private List<Item> storeItems;

    public Store() {
        this.clients = new ArrayList<>();
        this.storeItems = new ArrayList<>();
    }
    
    public void addClient(Client client) {
        clients.add(client);
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Item> getStoreItems() {
        return storeItems;
    }

    // Add a method to reduce the quantity of an item in the store
    public void reduceItemQuantity(String itemName, int quantity) {
        for (Item item : storeItems) {
            if (item.getItemName().equals(itemName)) {
                int currentQuantity = item.getItemQuantity();
                item.setItemQuantity(currentQuantity - quantity);
                break; // Stop searching once the item is found
            }
        }
    }

    public void currentStoreStatus() {
        System.out.println("Current Store Items:");
        for (Item item : storeItems) {
            System.out.println("Item: " + item.getItemId() + ", " + item.getItemName() + ", " + item.getItemCategory() + ", " + item.getItemPrice() + ", " + item.getItemQuantity());
        }
        //System.out.println("\n");
    }

    public void currentStoreClts() {
        System.out.println("Current Clients:");
        for (Client clt : clients) {
            //System.out.println("Item: " + item.getItemId() + ", " + item.getItemName() + ", " + item.getItemCategory() + ", " + item.getItemPrice() + ", " + item.getItemQuantity());
            System.out.println("Client: " + clt.getId() + ", " + clt.getCltName() + ", " + clt.getCity() + ", " + clt.getDistrict() + ", " + clt.getLoyaltyYears());
        }
        System.out.println("\n");
    }

    public double formatDouble(double value, int precision) {
        DecimalFormat df = new DecimalFormat("#." + new String(new char[precision]).replace('\0', '0'));
        return Double.parseDouble(df.format(value));
    }

    // commented out after efolioA
    // public void populateStoreItemsFromOCaml() {
    //     try {
    //         ProcessBuilder builder = new ProcessBuilder("./lojaMago");
    //         builder.redirectErrorStream(true); // Redirect error stream to input stream
    //         Process process = builder.start();
    //         
    //         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             // Skip lines that don't start with "Item:"
    //             if (!line.startsWith("Item:")) {
    //                 continue;
    //             }
    //             // the output format is: "Item: id, name, category, price, quantity"
    //             String[] parts = line.split(", ");
    //             int id = Integer.parseInt(parts[0].split(": ")[1]);
    //             String name = parts[1];
    //             String category = parts[2];
    //             double price = Double.parseDouble(parts[3]);
    //             double quantity = Double.parseDouble(parts[4]); // Parse as double
    //             storeItems.add(new Item(id, name, category, price, (int) quantity)); // Cast to int
    //         }
    //         reader.close();
    //         
    //         // Wait for the process to complete
    //         int exitCode = process.waitFor();

    //         // Print the result
    //         //System.out.println("Exit code: " + exitCode);
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }

    public double getCheckoutPriceWoDiscountsFromOCaml(Cart cart) {
        try {
            // Convert the cart items to a string
            String cartItems = cartToString(cart);
    
            //System.out.println(cartItems);
            // Remove the trailing semicolon
            if (cartItems.endsWith(";")) {
                cartItems = cartItems.substring(0, cartItems.length() - 1);
            }
            //System.out.println(cartItems);
            // Command to execute
            String[] command = {"./lojaMago", "checkout_Wo_dscnts", cartItems};
            
            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
            Process process = processBuilder.start();
    
            // Read output from the OCaml process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line);
            }
            reader.close();
            
            // Extract the numeric part of the output string
            String output = outputBuilder.toString().trim();
            String numericPart = output.substring(output.indexOf(":") + 1).trim();

            // Parse the numeric part of the output string as a double
            double checkoutPriceWoDiscounts = Double.parseDouble(numericPart);

            // Wait for the process to complete
            int exitCode = process.waitFor();
    
            // Print the result
            //System.out.println("Exit code: " + exitCode);
            //System.out.println("Checkout Price without Discounts: " + checkoutPriceWoDiscounts);
    
            // return checkoutPriceWoDiscounts;
            return formatDouble(checkoutPriceWoDiscounts, 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0.0; // Handle error gracefully
        }
    }


    public double getCtgryDscntsFromOCaml(Cart cart) {
        try {
            // Convert the cart items to a string
            String cartItems = cartToString(cart);
    
            // Remove the trailing semicolon
            if (cartItems.endsWith(";")) {
                cartItems = cartItems.substring(0, cartItems.length() - 1);
            }
    
            // Command to execute
            String[] command = {"./lojaMago", "checkout_Ctgry_dscnts", cartItems};
    
            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
            Process process = processBuilder.start();
    
            // Read output from the OCaml process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line);
            }
            reader.close();
    
            // Extract the numeric part of the output string
            String output = outputBuilder.toString().trim();
            String numericPart = output.substring(output.indexOf(":") + 1).trim();
    
            // Parse the numeric part of the output string as a double
            double checkoutPriceWithCtgryDscnts = Double.parseDouble(numericPart);
    
            // Wait for the process to complete
            int exitCode = process.waitFor();
    
            // Print the result
            //System.out.println("Exit code: " + exitCode);
            //System.out.println("Checkout Price with category discounts: " + checkoutPriceWithCtgryDscnts);
    
            // return checkoutPriceWithCtgryDscnts;
            return formatDouble(checkoutPriceWithCtgryDscnts, 2);
        } catch (IOException | InterruptedException e) {
            //e.rintStackTrace();
            return 0.0; // Handle error gracefully
        }
    }


    public double getLyltyDscntFromOCaml(Cart cart, Client client) {
        try {
            // Get checkout price with category discounts
            // double checkoutCtgryDscnt = getCtgryDscntsFromOCaml(cart);
            //System.out.println(checkoutCtgryDscnt);
            // double checkoutWoDscnt = getCheckoutPriceWoDiscountsFromOCaml(cart);
            double checkoutAfterCtgryDscnt = getCheckoutPriceWoDiscountsFromOCaml(cart) - getCtgryDscntsFromOCaml(cart);
    
            // Convert client loyalty years and checkout category discount to string
            String clientAndCheckoutStr = clientAndCheckoutToString(client, checkoutAfterCtgryDscnt);   
            //System.out.println(clientAndCheckoutStr);

            // Command to execute
            String[] command = {"./lojaMago", "checkout_Loyalty_dscnt", clientAndCheckoutStr};
    
            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
            Process process = processBuilder.start();
    
            // Read output from the OCaml process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line);
            }
            reader.close();
    
            // Extract the numeric part of the output string
            String output = outputBuilder.toString().trim();
            String numericPart = output.substring(output.indexOf(":") + 1).trim();
    
            // Parse the numeric part of the output string as a double
            double checkoutPriceWithLyltyDscnt = Double.parseDouble(numericPart);
    
            // Wait for the process to complete
            int exitCode = process.waitFor();
    
            // Print the result
            // System.out.println("Exit code: " + exitCode);
            // System.out.println("Checkout Price with loyalty discounts: " + checkoutPriceWithLyltyDscnt);
    
            // return checkoutPriceWithLyltyDscnt;
            return formatDouble(checkoutPriceWithLyltyDscnt, 2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0.0; // Handle error gracefully
        }
    }



    public double getShippingCost(Client client) {
        try {
            // Get the client's district
            String district = client.getDistrict();
    
            // Command to execute
            String[] command = {"./lojaMago", "shipcost_by_distrito", district};
    
            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
            Process process = processBuilder.start();
    
            // Read output from the OCaml process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line);
            }
            reader.close();
    
            // Extract the numeric part of the output string
            String output = outputBuilder.toString().trim();
            String numericPart = output.substring(output.indexOf(":") + 1).trim();
    
            // Parse the numeric part of the output string as a double
            double shippingCost = Double.parseDouble(numericPart);
    
            // Wait for the process to complete
            int exitCode = process.waitFor();
    
            // Print the result
            // System.out.println("Exit code: " + exitCode);
            // System.out.println("Shipping Cost: " + shippingCost);
    
            // return shippingCost;
            return formatDouble(shippingCost, 2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0.0; // Default shipping cost if there's an error
        }
    }


    public double getCheckoutPriceFinal(Cart cart, Client client) {
        try {
            // Get the checkout price with loyalty discount
            //double checkoutAfterCtgryLyltyDscnt = getCheckoutPriceWoDiscountsFromOCaml(cart) - getCtgryDscntsFromOCaml(cart) - getLyltyDscntFromOCaml(cart, client);

            // Get the shipping cost
            //double shippingCost = getShippingCost(client);

            // Combine checkout price and shipping cost
            //double totalWithShipping = checkoutPrice + shippingCost;

            // Convert checkout price with shipping and loyalty discount to string
            //String checkoutWithShippingAndDscnts = CheckoutLyltyAndShippingToString(totalWithShipping, shippingCost);
            //String checkoutWithShippingAndDscnts = CheckoutLyltyAndShippingToString(checkoutAfterCtgryLyltyDscnt, shippingCost);
            String cartItems = cartToString(cart);
            // Remove the trailing semicolon
            if (cartItems.endsWith(";")) {
                cartItems = cartItems.substring(0, cartItems.length() - 1);
            }
            String district = client.getDistrict();
            String lyltyYears = String.format("%d", client.getLoyaltyYears());

            String checkoutWithShippingAndDscnts = cartItems + "-" + lyltyYears + "-" + district; 
            // System.out.println(checkoutWithShippingAndDscnts);
            // Command to execute
            String[] command = {"./lojaMago", "checkout_with_shipcost", checkoutWithShippingAndDscnts};

            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
            Process process = processBuilder.start();

            // Read output from the OCaml process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line);
            }
            reader.close();

            // Extract the numeric part of the output string
            String output = outputBuilder.toString().trim();
            String numericPart = output.substring(output.indexOf(":") + 1).trim();
    
            // Parse the numeric part of the output string as a double
            double totalPriceWithShipping = Double.parseDouble(numericPart);

            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Print the result
            // System.out.println("Exit code: " + exitCode);
            // System.out.println("Total price with shipping: " + totalPriceWithShipping);

            // return totalPriceWithShipping;
            return formatDouble(totalPriceWithShipping, 2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0.0; // Handle error gracefully
        }
    }

    public String getSortedCartCartNameFromOCaml(Cart cart) {
        try {
            // Convert the cart to a string
            String cartItems = cartToString(cart);

            // Remove the trailing semicolon
            if (cartItems.endsWith(";")) {
                cartItems = cartItems.substring(0, cartItems.length() - 1);
            }

            // Command to execute
            String[] command = {"./lojaMago", "sort_cart", cartItems};

            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream
            Process process = processBuilder.start();

            // Read output from the OCaml process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] itemParts = line.split(";");
                for (String item : itemParts) {
                    outputBuilder.append("Item: ").append(item.replace(",", ", ")).append("\n");
                }
            }
            reader.close();
            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Print the result
            // System.out.println("Exit code: " + exitCode);
            // System.out.println("Sorted cart by category and name:");
            // System.out.println(outputBuilder.toString());

            return outputBuilder.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ""; // Handle error gracefully
        }
    }

    private String cartToString(Cart cart) {
        StringBuilder sb = new StringBuilder();
        for (Item item : cart.getItems()) {
            sb.append(String.format("%d,%s,%s,%.2f,%d;", 
                item.getItemId(), 
                item.getItemName().replace(",", ""), // Remove any commas from the name
                item.getItemCategory(), 
                item.getItemPrice(), 
                item.getItemQuantity()));
        }
        return sb.toString();
    }

    private String clientAndCheckoutToString(Client client, double checkoutCtgryDscnt) {
        return String.format("%d;%.2f", client.getLoyaltyYears(), checkoutCtgryDscnt);
    }

    // comment ed out after efolioA
    // private String CheckoutLyltyAndShippingToString(double totalWithShipping, double shippingCost) {
    //     return String.format("%.2f;%.2f", totalWithShipping, shippingCost);
    // }

    public static boolean clientIdExists(int clientId) {
        Query cltQuery = new Query("clients", new Term[]{new org.jpl7.Integer(clientId), new Variable("_"), new Variable("_"), new Variable("_")});
        return cltQuery.hasSolution();
    }

    public static boolean itemIdExists(int itemId) {
        Query itemQuery = new Query("item", new Term[]{new org.jpl7.Integer(itemId), new Variable("_"), new Variable("_"), new Variable("_"), new Variable("_")});
        return itemQuery.hasSolution();
    }

    public static boolean categoryExists(String category) {
        ArrayList<String> catList = new ArrayList<>();
        boolean found = false;

        Query itemCatAllQuery = new Query("display_categories", new Term[]{new Variable("categories")});
        if (itemCatAllQuery.hasSolution()) {
            Map<String, Term>[] solutions = itemCatAllQuery.allSolutions();
            for(Map<String, Term> solution : solutions){
                Term purchasesTerm = solution.get("categories");
                for(Term purchTerm : purchasesTerm.toTermArray()){
                    catList.add(purchTerm.toString());
                }
            }
            for (String str : catList) {
                if (str.equals(category)) {
                    found = true;
                    return true;
                }
            }
        } else {
            System.out.println("Zero categorias");
            return false;
        }
        return false;
    }

    private static void addremeditclient() {
        char choice = '\0';
        String name = "";
        String distrito = "";
        int customerYears = 0;
        int clientId = 0;
        String prologDBFile = "store.pl";
        Query assertQuery;
        Query cltQuery;
        Query sortClientsByIdQuery;

        do {
            System.out.println("\nMenu Adiciona/Editar/Remover cliente");
            System.out.println("1. Adicionar cliente");
            System.out.println("2. Editar cliente");
            System.out.println("3. Remover cliente");
            System.out.println("V. (V)oltar ao Menu Gestão de Clientes");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    clientId = getIntInput("Indique Id cliente para adicionar: ");
                    if (clientIdExists(clientId)) {
                        System.out.println("Client ID existente: " + clientId);
                    }else{
                        name = getStringInput("\nIntroduza o nome: ");
                        distrito = getStringInput("\nIntroduza o distrito: ");
                        customerYears = getIntInput("Indique anos lealdade: ");
                        assertQuery = new Query("assertz", new Term[]{new Compound("clients", new Term[]{new org.jpl7.Integer(clientId), new Atom(name), new Atom(distrito), new org.jpl7.Integer(customerYears)})});
                        assertQuery.hasSolution();
                        sortClientsByIdQuery = new Query("sortClientsById");
                        sortClientsByIdQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                    }
                    break;
                case '2':
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    cltQuery = new Query("clients", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Distrito"), new Variable("Lealdade")});
                    while (cltQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = cltQuery.nextSolution();
                        int id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        String namedsply = solution.get("Name").name();
                        String district = solution.get("Distrito").name();
                        int lylty = java.lang.Integer.parseInt(solution.get("Lealdade").toString());
                        // System.out.println(id, name, district, (int) lylty);
                        System.out.println(id + ". " + id + " " + namedsply + " " + district + " " + lylty);
                    }
                    clientId = getIntInput("Indique Id cliente para editar: ");
                    if (clientIdExists(clientId)) {
                        assertQuery = new Query("retract", new Term[]{new Compound("clients", new Term[]{new org.jpl7.Integer(clientId), new Variable("_"), new Variable("_"), new Variable("_")})});
                        assertQuery.hasSolution();
                        name = getStringInput("\nIntroduza o nome: ");
                        distrito = getStringInput("\nIntroduza o distrito: ");
                        customerYears = getIntInput("Indique anos lealdade: ");
                        assertQuery = new Query("assertz", new Term[]{new Compound("clients", new Term[]{new org.jpl7.Integer(clientId), new Atom(name), new Atom(distrito), new org.jpl7.Integer(customerYears)})});
                        assertQuery.hasSolution();
                        sortClientsByIdQuery = new Query("sortClientsById");
                        sortClientsByIdQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                    }else{
                        System.out.println("Client ID não existe: " + clientId);
                    }
                    break;
                case '3':
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    cltQuery = new Query("clients", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Distrito"), new Variable("Lealdade")});
                    while (cltQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = cltQuery.nextSolution();
                        int id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        String namedsply = solution.get("Name").name();
                        String district = solution.get("Distrito").name();
                        int lylty = java.lang.Integer.parseInt(solution.get("Lealdade").toString());
                        // System.out.println(id, name, district, (int) lylty);
                        System.out.println(id + ". " + id + " " + namedsply + " " + district + " " + lylty);
                    }
                    clientId = getIntInput("Indique Id cliente para remover: ");

                    if (clientIdExists(clientId)) {
                        assertQuery = new Query("retract", new Term[]{new Compound("clients", new Term[]{new org.jpl7.Integer(clientId), new Variable("_"), new Variable("_"), new Variable("_")})});
                        assertQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                    }else{
                        System.out.println("Client ID Não existente: " + clientId);
                    }
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void editremeditshipcost() {
        char choice;
        do {
            System.out.println("\nMenu Adiciona/Editar/Remover custo de envio");
            System.out.println("1. Adicionar custo de envio");
            System.out.println("2. Editar custo de envio");
            System.out.println("3. Remover custo de envio");
            System.out.println("V. (V)oltar ao Menu Gestão de Descontos");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    break;
                case '2':
                    break;
                case '3':
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void editremeditlyltydscnt() {
        char choice;
        do {
            System.out.println("\nMenu Adiciona/Editar/Remover desconto lealdade");
            System.out.println("1. Adicionar desconto lealdade");
            System.out.println("2. Editar desconto lealdade");
            System.out.println("3. Remover desconto lealdade");
            System.out.println("V. (V)oltar ao Menu Gestão de Descontos");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    break;
                case '2':
                    break;
                case '3':
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void editremeditcatdscnt() {
        char choice;
        String categoria;
        Double catdiscount = 0.0;
        String prologDBFile = "store.pl";
        Query CatDscntQuery;
        do {
            System.out.println("\nMenu Adiciona/Editar/Remover categoria");
            System.out.println("1. Adicionar categoria");
            System.out.println("2. Editar categoria");
            System.out.println("3. Remover categoria");
            System.out.println("V. (V)oltar ao Menu Gestão de Inventário");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    categoria = getStringInput("\nIntroduza a categoria a adicionar: ");
                    categoria = categoria.toLowerCase();
                    if(!categoryExists(categoria)){
                        catdiscount = getDoubleInput("Introduza o desconto da categoria" + categoria + " : ");
                        while (catdiscount < 1 || catdiscount > 100) {
                            System.out.println("Desconto Inválido. Escolha novamente[0-100].");
                            catdiscount = getDoubleInput("Introduza o desconto da categoria" + categoria + " : ");
                        }

                        Query assertQuery = new Query("assert_discount", new Term[]{new Atom(categoria), new org.jpl7.Float(catdiscount/100)});
                        assertQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                        
                    }else{
                        System.out.println("Categoria Existente...");
                    }
                    break;
                case '2':
                    System.out.println("\n[ Categoria, DescontoCategoria ]");
                    CatDscntQuery = new Query("discount", new Term[]{new Variable("CatName"), new Variable("DescontoCat")});
                    while (CatDscntQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = CatDscntQuery.nextSolution();
                        String catname = solution.get("CatName").name();
                        double dscntcat = java.lang.Double.parseDouble(solution.get("DescontoCat").toString());
                        System.out.println(" " + catname + " " + dscntcat*100 + "%" );
                    }
                    categoria = getStringInput("\nIntroduza a categoria a editar: ");
                    categoria = categoria.toLowerCase();
                    if(categoryExists(categoria)){
                        Query retractQuery = new Query("retract", new Term[]{new Compound("discount", new Term[]{new Atom(categoria), new Variable("_")})});
                        retractQuery.hasSolution();
                        catdiscount = getDoubleInput("Introduza o desconto da categoria" + categoria + " : ");
                        while (catdiscount < 1 || catdiscount > 100) {
                            System.out.println("Opção Inválida. Escolha novamente.");
                            catdiscount = getDoubleInput("Introduza o desconto da categoria" + categoria + " : ");
                        }

                        Query assertQuery = new Query("assert_discount", new Term[]{new Atom(categoria), new org.jpl7.Float(catdiscount/100)});
                        assertQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                    }else{
                        System.out.println("Categoria Existente...");
                    }
                    break;
                case '3':
                    System.out.println("\n[ Categoria, DescontoCategoria ]");
                    CatDscntQuery = new Query("discount", new Term[]{new Variable("CatName"), new Variable("DescontoCat")});
                    while (CatDscntQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = CatDscntQuery.nextSolution();
                        String catname = solution.get("CatName").name();
                        double dscntcat = java.lang.Double.parseDouble(solution.get("DescontoCat").toString());
                        System.out.println(" " + catname + " " + dscntcat*100 + "%" );
                    }
                    categoria = getStringInput("\nIntroduza a categoria a remover: ");
                    categoria = categoria.toLowerCase();
                    if(categoryExists(categoria)){
                        Query retractQuery = new Query("retract", new Term[]{new Compound("discount", new Term[]{new Atom(categoria), new Variable("_")})});
                        retractQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                        
                    }else{
                        System.out.println("Categoria Inexistente...");
                    }
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void editremedititem() {
        char choice;
        int itemId = 0;
        int itemQty = 0;
        double itemPrice = 0.0;
        String name = "";
        String categoria = "";
        String prologDBFile = "store.pl";
        Query itemQuery;
        Query sortItemsByIdQuery;

        do {
            System.out.println("\nMenu Adiciona/Editar/Remover item");
            System.out.println("1. Adicionar item");
            System.out.println("2. Editar item");
            System.out.println("3. Remover item");
            System.out.println("V. (V)oltar ao Menu Gestão de Inventário");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    itemId = getIntInput("Indique Id item para adicionar: ");

                    if (itemIdExists(itemId)) {
                        System.out.println("Item ID existente: " + itemId);
                    }else{
                        name = getStringInput("\nIntroduza o nome: ");
                        categoria = getStringInput("\nIntroduza a categoria: ");
                        categoria = categoria.toLowerCase();
                        if(categoryExists(categoria)){
                            itemPrice = getDoubleInput("Indique Preço item: ");
                            itemQty = getIntInput("Indique Quantidade de items para adicionar à Loja: ");
                            Query assertQuery = new Query("assert_item", new Term[]{new org.jpl7.Integer(itemId), new Atom(name), new Atom(categoria), new org.jpl7.Float(itemPrice), new org.jpl7.Integer(itemQty)});
                            assertQuery.hasSolution();
                            sortItemsByIdQuery = new Query("sortItemsById");
                            sortItemsByIdQuery.hasSolution();
                            saveToPrologFile(prologDBFile);
                        }else{
                            System.out.println("Categoria Inexistente.... Operação Cancelada..");
                        }
                    }
                    break;
                case '2':
                    System.out.println("\n[ ItemId, Nome, Categoria, Preço, Quantidade ]");
                    itemQuery = new Query("item", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Ctgry"), new Variable("Price"), new Variable("Qty")});
                    while (itemQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = itemQuery.nextSolution();
                        int id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        name = solution.get("Name").name();
                        String category = solution.get("Ctgry").name();
                        double price = java.lang.Double.parseDouble(solution.get("Price").toString());
                        int quantity = java.lang.Integer.parseInt(solution.get("Qty").toString());
                        System.out.println(id + ". " + id + " " + name + " - " + category + " " + price + " " + quantity);
                    }
                    itemId = getIntInput("Indique Id item para editar: ");

                    if (itemIdExists(itemId)) {
                        Query retractQuery = new Query("retract", new Term[]{new Compound("item", new Term[]{new org.jpl7.Integer(itemId), new Variable("_"), new Variable("_"), new Variable("_"), new Variable("_")})});
                        retractQuery.hasSolution();
                        name = getStringInput("\nIntroduza o nome: ");
                        categoria = getStringInput("\nIntroduza a categoria: ");
                        categoria = categoria.toLowerCase();
                        if(categoryExists(categoria)){
                            itemPrice = getDoubleInput("Indique Preço item: ");
                            itemQty = getIntInput("Indique Quantidade de items para adicionar à Loja: ");
                            Query assertQuery = new Query("assert_item", new Term[]{new org.jpl7.Integer(itemId), new Atom(name), new Atom(categoria), new org.jpl7.Float(itemPrice), new org.jpl7.Integer(itemQty)});
                            assertQuery.hasSolution();
                            sortItemsByIdQuery = new Query("sortItemsById");
                            sortItemsByIdQuery.hasSolution();
                            saveToPrologFile(prologDBFile);
                        }else{
                            System.out.println("Categoria Inexistente.... Operação Cancelada..");
                        }
                    }else{
                        System.out.println("Item ID existente: " + itemId);
                    }
                    break;
                case '3':
                    System.out.println("\n[ ItemId, Nome, Categoria, Preço, Quantidade ]");
                    itemQuery = new Query("item", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Ctgry"), new Variable("Price"), new Variable("Qty")});
                    while (itemQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = itemQuery.nextSolution();
                        int id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        name = solution.get("Name").name();
                        String category = solution.get("Ctgry").name();
                        double price = java.lang.Double.parseDouble(solution.get("Price").toString());
                        int quantity = java.lang.Integer.parseInt(solution.get("Qty").toString());
                        System.out.println(id + ". " + id + " " + name + " - " + category + " " + price + " " + quantity);
                    }
                    itemId = getIntInput("Indique Id item para remover: ");

                    if (itemIdExists(itemId)) {
                        Query retractQuery = new Query("retract", new Term[]{new Compound("item", new Term[]{new org.jpl7.Integer(itemId), new Variable("_"), new Variable("_"), new Variable("_"), new Variable("_")})});
                        retractQuery.hasSolution();
                        saveToPrologFile(prologDBFile);
                    }else{
                        System.out.println("item ID Não existente: " + itemId);
                    }
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }
    private static void showPurchaseHistMenu() {
        char choice;
        String distrito = "";
        String date = "";
        int id=0;
        do {
            System.out.println("\nMenu Histórico de vendas:");
            System.out.println("1. Ver todas as vendas por data");
            System.out.println("2. Ver todas as vendas por cliente");
            System.out.println("3. Ver todas as vendas por distrito");
            System.out.println("4. Ver totais por distrito");
            System.out.println("5. Ver totais por data");
            System.out.println("6. Ver Distrito com mais descontos");
            System.out.println("V. (V)oltar ao Menu Gestão");

            // public void getAllItems() {
            //     Query itemQuery = new Query("item", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Ctgry"), new Variable("Price"), new Variable("Qty")});
            //     while (itemQuery.hasMoreSolutions()) {
            //         Map<String, Term> solution = itemQuery.nextSolution();
            //         int id = java.lang.Integer.parseInt(solution.get("Id").toString());
            //         String name = solution.get("Name").name();
            //         String category = solution.get("Ctgry").name();
            //         double price = java.lang.Double.parseDouble(solution.get("Price").toString());
            //         int quantity = java.lang.Integer.parseInt(solution.get("Qty").toString());
            //         storeItems.add(new Item(id, name, category, (double) price, (int) quantity));
            //     }
            // }

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    // Ask for the date from the user
                    date = getStringInput("\nIntroduza a data (dd/MM/yyyy): ");
                    System.out.println("Vendas em " + date + ": ");
                    System.out.println("[ ClientId, Data, TotalWoDscnt, CtgryDscnt, LyltyDscnt, ShipCost, Total ]");
                    Query purchasesOnDateQuery = new Query("purch_hist_by_date", new Term[]{new Atom(date), new Variable("Purchases")});
                    if (purchasesOnDateQuery.hasSolution()) {
                        Map<String, Term>[] solutions = purchasesOnDateQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Purchases");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero vendas na data: " + date);
                    }
                    break;
                case '2':
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    Query cltQuery = new Query("clients", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Distrito"), new Variable("Lealdade")});
                    while (cltQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = cltQuery.nextSolution();
                        id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        String name = solution.get("Name").name();
                        String district = solution.get("Distrito").name();
                        int lylty = java.lang.Integer.parseInt(solution.get("Lealdade").toString());
                        // System.out.println(id, name, district, (int) lylty);
                        System.out.println(id + ". " + id + " " + name + " " + district + " " + lylty);
                    }
                    int cltchoice = getIntInput("Selecione cliente para ver vendas: ");
                    // Validate user input
                    while (cltchoice < 1 || cltchoice > id) {
                        System.out.println("Opção Inválida. Escolha novamente.");
                        cltchoice = getIntInput("Selecione opção: ");
                    }
                    System.out.println("[ ClientId, Data, TotalWoDscnt, CtgryDscnt, LyltyDscnt, ShipCost, Total ]");
                    Query purchasesOnCltQuery = new Query("purch_hist_by_clt", new Term[]{new org.jpl7.Integer(cltchoice), new Variable("Purchases")});
                    if (purchasesOnCltQuery.hasSolution()) {
                        Map<String, Term>[] solutions = purchasesOnCltQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Purchases");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero vendas para clienteID: " + cltchoice);
                    }
                    break;
                case '3':
                    distrito = getStringInput("\nIntroduza o distrito: ");
                    System.out.println("Vendas em " + distrito + ": ");
                    System.out.println("[ ClientId, Data, TotalWoDscnt, CtgryDscnt, LyltyDscnt, ShipCost, Total ]");
                    Query purchsOnDistritoQuery = new Query("purch_hist_by_distrito", new Term[]{new Atom(distrito), new Variable("Purchases")});
                    if (purchsOnDistritoQuery.hasSolution()) {
                        Map<String, Term>[] solutions = purchsOnDistritoQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Purchases");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero vendas no distrito: " + distrito);
                    }
                    break;
                case '4':
                    distrito = getStringInput("\nIntroduza o distrito: ");
                    System.out.println("Totais do " + distrito + ": ");
                    System.out.println("[ SomaTotalWoDscnt, SomaCtgryDscnt, SomaLyltyDscnt, SomaShipCost, SomaTotal ]");
                    Query purchsSumDistritoQuery = new Query("purch_hist_sum_distrito", new Term[]{new Atom(distrito), new Variable("Purchases")});
                    if (purchsSumDistritoQuery.hasSolution()) {
                        Map<String, Term>[] solutions = purchsSumDistritoQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Purchases");
                            System.out.println(purchasesTerm.toString());
                            // for(Term purchTerm : purchasesTerm.toTermArray()){
                            //     System.out.println(purchTerm.toString());
                            // }
                        }
                    } else {
                        System.out.println("Zero Totais de vendas no distrito: " + distrito);
                    }
                    break;
                case '5':
                    date = getStringInput("\nIntroduza a data (dd/MM/yyyy): ");
                    System.out.println("Totais Vendas em " + date + ": ");
                    System.out.println("[ ClientId, Data, TotalWoDscnt, CtgryDscnt, LyltyDscnt, ShipCost, Total ]");
                    Query purchasesSumDateQuery = new Query("purch_hist_sum_date", new Term[]{new Atom(date), new Variable("Purchases")});
                    if (purchasesSumDateQuery.hasSolution()) {
                        Map<String, Term>[] solutions = purchasesSumDateQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Purchases");
                            System.out.println(purchasesTerm.toString());
                        }
                    } else {
                        System.out.println("Zero Totais de vendas na data: " + date);
                    }
                    break;
                case '6':
                    Query HighestSumDistrictQuery = new Query("district_with_highest_discount", new Term[]{new Variable("Distrito")});
                    if (HighestSumDistrictQuery.hasSolution()) {
                        Map<String, Term> solution = HighestSumDistrictQuery.oneSolution();
                        System.out.println("Distrito com a soma de descontos mais alta: " + solution.get("Distrito").name());
                    } else {
                        System.out.println("Zero resultados");
                    }

                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void showInventoryMenu() {
        char choice;
        String categoria = "";
        do {
            System.out.println("\nMenu Gestão de Inventário:");
            System.out.println("1. Ver inventário completo");
            System.out.println("2. Ver inventário por categoria");
            System.out.println("3. Ver categorias");
            System.out.println("4. Add,Edit,Rem categoria");
            System.out.println("5. Add,Edit,Rem item");
            System.out.println("V. (V)oltar ao Menu Gestão");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    System.out.println("\n[ ItemId, Nome, Categoria, Preço, Quantidade ]");
                    Query itemQuery = new Query("item", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Ctgry"), new Variable("Price"), new Variable("Qty")});
                    while (itemQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = itemQuery.nextSolution();
                        int id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        String name = solution.get("Name").name();
                        String category = solution.get("Ctgry").name();
                        double price = java.lang.Double.parseDouble(solution.get("Price").toString());
                        int quantity = java.lang.Integer.parseInt(solution.get("Qty").toString());
                        System.out.println(id + ". " + id + " " + name + " - " + category + " " + price + " " + quantity);
                    }
                    break;
                case '2':
                    categoria = getStringInput("\nIntroduza a categoria: ");
                    System.out.println("\nItems de " + categoria + ": ");
                    System.out.println("[ ItemId, Nome, Categoria, Preço, Quantidade ]");
                    Query itemCatQuery = new Query("items_by_category", new Term[]{new Atom(categoria), new Variable("ItemsCat")});
                    if (itemCatQuery.hasSolution()) {
                        Map<String, Term>[] solutions = itemCatQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("ItemsCat");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero items da categoria distrito: " + categoria);
                    }
                    break;
                case '3':
                    System.out.println("\n");
                    Query itemCatAllQuery = new Query("display_categories", new Term[]{new Variable("categories")});
                    if (itemCatAllQuery.hasSolution()) {
                        Map<String, Term>[] solutions = itemCatAllQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("categories");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero categorias");
                    }
                    break;
                case '4':
                    editremeditcatdscnt();
                    break;
                case '5':
                    editremedititem();
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void showDiscountMenu() {
        char choice;
        do {
            System.out.println("\nMenu Gestão de Descontos:");
            System.out.println("1. Ver custos de  envio");
            System.out.println("2. Ver descontos categoria");
            System.out.println("3. Ver descontos lealdade");
            System.out.println("4. Add,Edit,Rem custo envio");
            System.out.println("5. Add,Edit,Rem desconto categoria");
            System.out.println("6. Add,Edit,Rem desconto lealdade");
            System.out.println("V. (V)oltar ao Menu Gestão");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    System.out.println("\n[ Distrito, CustoEnvio ]");
                    Query ShipCostQuery = new Query("shipping_cost", new Term[]{new Variable("Distrito"), new Variable("CustoEnvio")});
                    while (ShipCostQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = ShipCostQuery.nextSolution();
                        String distrito = solution.get("Distrito").name();
                        double cost = java.lang.Double.parseDouble(solution.get("CustoEnvio").toString());
                        System.out.println(" " + distrito + " " + cost );
                    }
                    break;
                case '2':
                    System.out.println("\n[ Categoria, DescontoCategoria ]");
                    Query CatDscntQuery = new Query("discount", new Term[]{new Variable("CatName"), new Variable("DescontoCat")});
                    while (CatDscntQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = CatDscntQuery.nextSolution();
                        String catname = solution.get("CatName").name();
                        double dscntcat = java.lang.Double.parseDouble(solution.get("DescontoCat").toString());
                        System.out.println(" " + catname + " " + dscntcat*100 + "%" );
                    }
                    break;
                case '3':
                    System.out.println("\n[ AnosLealdade, DescontoLealdade ]");
                    Query LyltyDscntQuery = new Query("loyalty_discount", new Term[]{new Variable("LyltyYears"), new Variable("DescontoLylty")});
                    while (LyltyDscntQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = LyltyDscntQuery.nextSolution();
                        int anoslealdade = java.lang.Integer.parseInt(solution.get("LyltyYears").toString());
                        double dscntcat = java.lang.Double.parseDouble(solution.get("DescontoLylty").toString());
                        System.out.println(" " + anoslealdade + " " + dscntcat*100 + "%"  );
                    }
                    break;
                case '4':
                    editremeditshipcost();
                    break;
                case '5':
                    editremeditcatdscnt();
                    break;
                case '6':
                    editremeditlyltydscnt();
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }

    private static void showClientMenu() {
        char choice;
        String distrito = "";
        int id=0;
        do {
            System.out.println("\nMenu Gestão de Clients:");
            System.out.println("1. Ver todos os clients");
            System.out.println("2. Ver todos os clientes do distrito");
            System.out.println("3. Ver todas as vendas por cliente");
            System.out.println("4. Ver todos os clientes com maior lealdade");
            System.out.println("5. Adiciona/Editar/Remover cliente");
            System.out.println("V. (V)oltar ao Menu Gestão");

            choice = getCharInput("Escolha opção: ");
            switch (choice) {
                case '1':
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    Query clientQuery = new Query("clients", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Distrito"), new Variable("Lealdade")});
                    while (clientQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = clientQuery.nextSolution();
                        id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        String name = solution.get("Name").name();
                        String district = solution.get("Distrito").name();
                        int lylty = java.lang.Integer.parseInt(solution.get("Lealdade").toString());
                        // System.out.println(id, name, district, (int) lylty);
                        System.out.println(id + " " + name + " " + district + " " + lylty);
                    }
                    break;
                case '2':
                    distrito = getStringInput("\nIntroduza o distrito: ");
                    System.out.println("Clientes em " + distrito + ": ");
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    Query cltByDistrictQuery = new Query("client_by_district", new Term[]{new Atom(distrito), new Variable("Clients")});
                    if (cltByDistrictQuery.hasSolution()) {
                        Map<String, Term>[] solutions = cltByDistrictQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Clients");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero vendas no distrito: " + distrito);
                    }
                    break;
                case '3':
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    Query cltQuery = new Query("clients", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Distrito"), new Variable("Lealdade")});
                    while (cltQuery.hasMoreSolutions()) {
                        Map<String, Term> solution = cltQuery.nextSolution();
                        id = java.lang.Integer.parseInt(solution.get("Id").toString());
                        String name = solution.get("Name").name();
                        String district = solution.get("Distrito").name();
                        int lylty = java.lang.Integer.parseInt(solution.get("Lealdade").toString());
                        // System.out.println(id, name, district, (int) lylty);
                        System.out.println(id + ". " + id + " " + name + " " + district + " " + lylty);
                    }
                    int cltchoice = getIntInput("Selecione cliente para ver vendas: ");
                    // Validate user input
                    while (cltchoice < 1 || cltchoice > id) {
                        System.out.println("Opção Inválida. Escolha novamente.");
                        cltchoice = getIntInput("Selecione opção: ");
                    }
                    System.out.println("[ ClientId, Data, TotalWoDscnt, CtgryDscnt, LyltyDscnt, ShipCost, Total ]");
                    Query purchasesOnCltQuery = new Query("purch_hist_by_clt", new Term[]{new org.jpl7.Integer(cltchoice), new Variable("Purchases")});
                    if (purchasesOnCltQuery.hasSolution()) {
                        Map<String, Term>[] solutions = purchasesOnCltQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Purchases");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero vendas para clienteID: " + cltchoice);
                    }
                    break;
                case '4':
                    int yearchoice = getIntInput("Introduza anos lealdade: ");
                    System.out.println("\n[ Id, Nome, Distrito, Lealdade ]");
                    Query CltYearQuery = new Query("clients_with_more_years", new Term[]{new org.jpl7.Integer(yearchoice), new Variable("Clients")});
                    if (CltYearQuery.hasSolution()) {
                        Map<String, Term>[] solutions = CltYearQuery.allSolutions();
                        for(Map<String, Term> solution : solutions){
                            Term purchasesTerm = solution.get("Clients");
                            for(Term purchTerm : purchasesTerm.toTermArray()){
                                System.out.println(purchTerm.toString());
                            }
                        }
                    } else {
                        System.out.println("Zero vendas para clienteID: " + yearchoice);
                    }
                    break;
                case '5':
                    addremeditclient();
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Gestão...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (choice != 'V' && choice != 'v');
    }



    public static void showGestaoMenu() {
        char gestaoChoice;
        do {
            System.out.println("\nMenu Gestão:");
            System.out.println("(1). Histórico de vendas");
            System.out.println("(2). Gestão de Inventário");
            System.out.println("(3). Gestão de Descontos");
            System.out.println("(4). Gestão de Clients");
            System.out.println("V. (V)oltar ao Menu Principal");
            //System.out.print("Enter your choice: ");
            
            gestaoChoice = getCharInput("Escolha opção: ");
            switch (gestaoChoice) {
                case '1':
                    System.out.println("PurchaseHist");
                    showPurchaseHistMenu();
                    break;
                case '2':
                    System.out.println("Inventory");
                    showInventoryMenu();
                    break;
                case '3':
                    System.out.println("Discounts");
                    showDiscountMenu();
                    break;
                case '4':
                    System.out.println("Clients");
                    showClientMenu();
                    break;
                case 'V':
                case 'v':
                    System.out.println("Regressar ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        } while (gestaoChoice != 'V' && gestaoChoice != 'v');
    }

    public void getAllItems() {
        storeItems.clear();
        Query itemQuery = new Query("item", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("Ctgry"), new Variable("Price"), new Variable("Qty")});
        while (itemQuery.hasMoreSolutions()) {
            Map<String, Term> solution = itemQuery.nextSolution();
            int id = java.lang.Integer.parseInt(solution.get("Id").toString());
            String name = solution.get("Name").name();
            String category = solution.get("Ctgry").name();
            double price = java.lang.Double.parseDouble(solution.get("Price").toString());
            int quantity = java.lang.Integer.parseInt(solution.get("Qty").toString());
            storeItems.add(new Item(id, name, category, (double) price, (int) quantity));
        }
    }


    public void getAllClients() {
        clients.clear();
        Query cltQuery = new Query("clients", new Term[]{new Variable("Id"), new Variable("Name"), new Variable("District"), new Variable("LyltyY")});
        while (cltQuery.hasMoreSolutions()) {
            Map<String, Term> solution = cltQuery.nextSolution();
            int id = java.lang.Integer.parseInt(solution.get("Id").toString());
            String name = solution.get("Name").name();
            String district = solution.get("District").name();
            int loyaltyYears = java.lang.Integer.parseInt(solution.get("LyltyY").toString());
            clients.add(new Client(id, name, district, district, loyaltyYears, new Cart()));
        }
    }


    private static int getIntInput(String promptMessage) {
        int userInput = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.print(promptMessage);
        try {
            userInput = java.lang.Integer.parseInt(scanner.next());
            if(userInput <= 0) {
                System.out.println("Número Inválido. Introduza um inteiro maior que zero.");
                return getIntInput(promptMessage);
            }
        } catch (NumberFormatException e) {
            System.out.println("Número Inválido. Introduza um inteiro.");
            return getIntInput(promptMessage);
        }
        return userInput;
    }

    public static double getDoubleInput(String promptMessage) {
        double userInput = 0.0;
        Scanner scanner = new Scanner(System.in);
        System.out.print(promptMessage);
        try {
            userInput = Double.parseDouble(scanner.next());
            if(userInput <= 0) {
                System.out.println("Número Inválido. Introduza um inteiro maior que zero.");
                return getDoubleInput(promptMessage);
            }
        } catch (NumberFormatException e) {
            System.out.println("Número Inválido. Introduza um número real.");
            // Recursive call to try again
            return getDoubleInput(promptMessage);
        }
        return userInput;
    }

    private static char getCharInput(String promptMessage) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(promptMessage);
        char userInput = scanner.next().charAt(0);
        return userInput;
    }

    private static String getStringInput(String promptMessage) {
        String userInput = "";
        Scanner scanner = new Scanner(System.in);
        System.out.print(promptMessage);
        userInput = scanner.nextLine();
        return userInput;
    }


    public static <T> T MenuFromLst(List<T> menuItems) {

        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i).toString());
        }
        System.out.println("\n");

        int choice = getIntInput("Selecione opção: ");

        // Validate user input
        while (choice < 1 || choice > menuItems.size()) {
            System.out.println("Opção Inválida. Escolha novamente.");
            choice = getIntInput("Selecione opção: ");
        }

        return menuItems.get(choice - 1);
    }


    public static void saveToPrologFile(String prologDBFile) {
        Query saveQuery = new Query("save_store_to_file", new Term[]{new Atom(prologDBFile)});
        if (saveQuery.hasSolution()) {
            System.out.println("Alterações Guardadas na BD " + prologDBFile +" com sucesso.");
        } else {
            System.out.println("FALHA de Gravação para a BD " + prologDBFile + " .");
        }
    }

    public static void updatePrologStoreItems(List<Item> Items) {
        // Retract all existing item facts first
        Query retractAllQuery = new Query("retract_all_items");
        retractAllQuery.hasSolution();

        // Assert new facts
        for (Item itm : Items) {
            // Query assertQuery = new Query("assertz", new Term[]{new Compound("item", new Term[]{new Atom(itm.getItemId()), new Atom(itm.getItemName()), new Atom(itm.getItemCategory()), new org.jpl7.Float(itm.getItemPrice()), new org.jpl7.Integer(itm.getItemQuantity())})});
            Query assertQuery = new Query("assert_item", new Term[]{new org.jpl7.Integer(itm.getItemId()), new Atom(itm.getItemName()), new Atom(itm.getItemCategory()), new org.jpl7.Float(itm.getItemPrice()), new org.jpl7.Integer(itm.getItemQuantity())});
            assertQuery.hasSolution();
        }
    }

    public static void updatePrologPurchHist(Client client1, double tmpChkWoDscnt, double tmpChkCtgryDscnts, double tmpChkLyltyDscnt, double tmpShipCost, double tmpChkTotal) {
        // Assert new facts
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);
        Cart tempCart = client1.getCart();
        Query addPurchaseQuery = new Query("add_purch_hist", new Term[]{
            new org.jpl7.Integer(client1.getId()), 
                new Atom(formattedDate), 
                new org.jpl7.Integer((int) tmpChkWoDscnt), 
                new org.jpl7.Float(tmpChkCtgryDscnts), 
                new org.jpl7.Float(tmpChkLyltyDscnt),
                new org.jpl7.Float(tmpShipCost), 
                new org.jpl7.Float(tmpChkTotal)}
                );
        addPurchaseQuery.hasSolution();
    }


    public static void main(String[] args) {
        // Create the store
        Store store = new Store();
        double checkoutPrice = 0;

        Scanner scanner = new Scanner(System.in);
        char mainChoice;
        char LojaChoice;
        String prologPredsFile = "predstore.pl";
        String prologDBFile = "store.pl";

        Query q1 = new Query("consult", new Term[]{new Atom(prologPredsFile)});
        System.out.println("Consult " + prologPredsFile + (q1.hasSolution() ? " succeeded." : " FAILED."));
        // Populate store fom prologPredsFile->consult('store.pl')


        do{
            store.getAllItems();
            store.getAllClients();


            System.out.println("\nMenu Principal:");
            System.out.println("L. Processar Encomenda na (L)oja");
            System.out.println("G. (G)estão de Loja");
            System.out.println("Q. (Q)uit");
            // System.out.print("Enter your choice: ");
            
            //mainChoice = scanner.next().charAt(0);
            mainChoice = getCharInput("Escolha opção: ");
            switch (Character.toUpperCase(mainChoice)) {
                case 'L':
                    System.out.println("You selected Loja");
                    boolean checkoutSuccess = false;

                    Client selectedClient = MenuFromLst(store.clients);
                    System.out.println(selectedClient);
                    Cart cartLoja = selectedClient.getCart();
                    do{
                        LojaChoice = getCharInput("\nA. (A)dicionar Item(s) ao Carrinho, \nM. (M)ostrar Carrinho de Compras, \nC. Ir para (C)heckout, \nQ. (Q)uit e Cancelar esta Encomenda . \n? ");
                        switch(Character.toUpperCase(LojaChoice)) {
                            case 'A':
                                System.out.println("A. (A)dicionar Item(s) ao Carrinho");
                                Item selectedItem = MenuFromLst(store.storeItems);
                                int quantiItem = getIntInput("Adicionar Quantos " + selectedItem.getItemName() + "? ");

                                cartLoja.addItem(quantiItem, selectedItem.getItemName(), store.getStoreItems());
                                break;
                            case 'M':
                                if(!cartLoja.getItems().isEmpty()){
                                    System.out.println("\nCarrinho Actual:\n\n" + store.getSortedCartCartNameFromOCaml(cartLoja));
                                }
                                break;
                            case 'C':
                                System.out.println("C. Ir para (C)heckout");
                                if(!cartLoja.getItems().isEmpty()){
                                    // cartLoja.getItems();
                                    updatePrologStoreItems(store.getStoreItems());

                                    updatePrologPurchHist(
                                            selectedClient, 
                                            store.getCheckoutPriceWoDiscountsFromOCaml(cartLoja), 
                                            store.getCtgryDscntsFromOCaml(cartLoja), 
                                            store.getLyltyDscntFromOCaml(cartLoja, selectedClient), 
                                            store.getShippingCost(selectedClient), 
                                            store.getCheckoutPriceFinal(cartLoja, selectedClient)
                                            );

                                    saveToPrologFile(prologDBFile);
                                    cartLoja.clearCart();
                                    checkoutSuccess = true;
                                }
                                break;
                            case 'Q':
                                System.out.println("Q. (Q)uit e Cancelar esta Encomenda");
                                cartLoja.clearCart();
                                break;
                            default:
                                System.out.println("Opção Inválida");
                        }


                    }while(!checkoutSuccess && Character.toUpperCase(LojaChoice) != 'Q');

                    System.out.println("\n");
                    break;
                case 'G':
                    showGestaoMenu();
                    break;
                case 'Q':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Opção Inválida. Escolha novamente.");
                    break;
            }
        }while(Character.toUpperCase(mainChoice) != 'Q');
        
        scanner.close();


        // Populate store items from OCaml
        // store.populateStoreItemsFromOCaml();
        // store.currentStatus();

        // commented out after efolioA
        // Create carts for clients
        // Cart cart1 = new Cart(); // Create an empty cart for client1
        // Cart cart2 = new Cart(); // Create an emtpy cart for client2

        // // Create clients and add them to the store
        // Client client1 = new Client(1, "Aveiro", "Aveiro", 3, cart1);
        // Client client2 = new Client(2, "Lisboa", "Lisboa", 6, cart2);

        // // Add clients to the store
        // store.addClient(client1);
        // store.addClient(client2);

        // // Add items to the cart
        // cart1.addItem(10, "Potion of Healing", store.getStoreItems());
        // cart1.addItem(2, "Wand of Fireball", store.getStoreItems());
        // cart1.addItem(3, "Enchanted Spellbook", store.getStoreItems());
        // cart1.addItem(4, "Crystal of Clairvoyance", store.getStoreItems());
        // cart1.addItem(20, "Amulet of Protection", store.getStoreItems());
        // cart1.addItem(5, "Potion of Healing", store.getStoreItems());

        // cart2.addItem(15, "Potion of Healing", store.getStoreItems());
        // cart2.addItem(35, "Crystal of Clairvoyance", store.getStoreItems());

        // client1.ClientCartStatus();
        // System.out.println("Checkout Total w/o Discounts: " + store.getCheckoutPriceWoDiscountsFromOCaml(cart1));
        // System.out.println("Category discounts: " + store.getCtgryDscntsFromOCaml(cart1));
        // System.out.println("Loyalty discount: " + store.getLyltyDscntFromOCaml(cart1,client1));
        // System.out.println("Shipping Cost: " + store.getShippingCost(client1));
        // System.out.println("Total price with shipping: " + store.getCheckoutPriceFinal(cart1,client1));
        // System.out.println("Sorted cart by category and name:\n" + store.getSortedCartCartNameFromOCaml(cart1));

        // client2.ClientCartStatus();
        // System.out.println("Checkout Total w/o Discounts: " + store.getCheckoutPriceWoDiscountsFromOCaml(cart2));
        // System.out.println("Category discounts: " + store.getCtgryDscntsFromOCaml(cart2));
        // System.out.println("Loyalty discount: " + store.getLyltyDscntFromOCaml(cart2,client2));
        // System.out.println("Shipping Cost: " + store.getShippingCost(client2));
        // System.out.println("Total price with shipping: " + store.getCheckoutPriceFinal(cart2,client2));
        // System.out.println("Sorted cart by category and name:\n" + store.getSortedCartCartNameFromOCaml(cart2));

        //store.currentStoreStatus();
        //store.currentStoreClts();
    }
}
