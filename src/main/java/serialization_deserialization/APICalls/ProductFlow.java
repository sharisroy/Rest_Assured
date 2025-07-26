package serialization_deserialization.APICalls;

import serialization_deserialization.pojoClass.*;

import java.util.List;

public class ProductFlow {
    public static void main(String[] args) {

        // Login
        LoginResponse loginResponse = API_Login.login();
        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();

        // Get products
        ProductListResponse productListResponse = API_GetProducts.getAllProducts(token);
        List<Product> products = productListResponse.getData();
        Product firstProduct = products.get(0);

        System.out.println("First Product: " + firstProduct.getProductName());

        // Add to cart
        AddToCartRequest request = new AddToCartRequest(userId, firstProduct);
        String cartResponse = API_AddToCart.addToCart(token, request);
        System.out.println("Cart Response: " + cartResponse);
    }
}
