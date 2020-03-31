/*************************************************************************
 Michael Hefner
 C482 - Software 1

 Write code to implement exception controls with custom error messages for one requirement out of each of the following sets (pick one from each):

 1.  Set 1

 •  entering an inventory value that exceeds the minimum or maximum value for that part or product

 •  preventing the minimum field from having a value above the maximum field

 •  preventing the maximum field from having a value below the minimum field

 •  ensuring that a product must always have at least one part

 2.  Set 2

 •  including a confirm dialogue for all “Delete” and “Cancel” buttons

 •  ensuring that the price of a product cannot be less than the cost of the parts

 •  ensuring that a product must have a name, price, and inventory level (default 0)
 *************************************************************************/


package com.michaelhefner.Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partId){
        if(partId > -1 && partId < allParts.size()) {
            return allParts.get(partId);

        } else {
            System.out.println("lookupPart: partId index out of bounds" + partId);
            return null;
        }
    }

    public static Product lookupProduct(int productId){

        return allProducts.get(productId);
    }

    public static ObservableList<Part> lookupPart(String partName){
        return allParts;
    }

    public static ObservableList<Product> lookupProduct(String productName){

        return allProducts;
    }

    public static void updatePart(int index, Part selectedPart){
        try {
            lookupPart(index);

            if (index > -1 && index < allParts.size()) {
                allParts.set(index, selectedPart);
            } else {
                System.out.println("index out of bounds. index: " + index + " allparts size: " + allParts.size());
            }
        } catch (NullPointerException e){
            System.out.println("Part at index " + index + " does not exist.");
        }
    }

    public static void updateProduct(int index, Product newProduct){
        try {
            lookupProduct(index);

            if (index > -1 && index < allProducts.size()) {
                allProducts.set(index, newProduct);
            } else {
                System.out.println("index out of bounds. index: " + index + " allProducts size: " + allProducts.size());
            }
        } catch (NullPointerException e){
            System.out.println("Part at index " + index + " does not exist.");
        }
    }

    public static boolean deletePart(Part selectedPart){
        try{
            lookupPart(selectedPart.getName());
            return allParts.remove(selectedPart);
        } catch (Exception e){
            return false;
        }
    }

    public static boolean deleteProduct(Product selectedProduct){
        try{
            lookupPart(selectedProduct.getName());
            return allProducts.remove(selectedProduct);
        } catch (Exception e){
            return false;
        }
    }

    public static ObservableList<Part> getAIIParts(){
        return allParts;
    }

    public static ObservableList<Product> getAIIProducts(){
        return allProducts;
    }
}
