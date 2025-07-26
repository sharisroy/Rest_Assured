package com.api.bdd;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.payload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class APICalls {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com/api/ecom/";

        Response login_response = given()
//                .log().all()
                .header("Content-Type", "application/json")
                .body(payload.loginPayload())
                .when()
                .post("auth/login")
                .then()
//                .log().all()
                .assertThat().statusCode(200)
                .body("message", equalTo("Login Successfully"))
                .header("Server", equalTo("Apache/2.4.52 (Ubuntu)"))
//                .assertThat().statusCode(201)
//                .body("message", equalTo("Login Successfully"))
//                .header("Server", equalTo("Apache/2.4.52 ()"))
                .extract().response();

        // ✅ Print body as string
        String login_responseBody = login_response.asString();
        System.out.println("========= Response Body =========");
        System.out.println(login_responseBody);

        // ✅ Extract values using JsonPath
        JsonPath login_jsonPath = new JsonPath(login_responseBody);
        System.out.println("Token   : " + login_jsonPath.getString("token"));
        System.out.println("UserId  : " + login_jsonPath.getString("userId"));
        System.out.println("Message : " + login_jsonPath.getString("message"));

        // ✅ Print headers
        System.out.println("========= Response Headers =========");
        login_response.getHeaders().forEach(System.out::println);

// Get Product List
        Response getProductList_response = given()
//                .log().all()
                .headers("Content-Type", "application/json","Authorization", login_jsonPath.getString("token") )
                .when().post("product/get-all-products")
                .then()
                .assertThat().statusCode(200)
//                .log().all()
                .extract().response();

        // Step 3: Extract product ID
        String getProductList = getProductList_response.asString();
        JsonPath productJson = new JsonPath(getProductList);
        System.out.println("All Products : " + getProductList);

        Map<String, Object> firstProduct = productJson.getMap("data[0]");
        System.out.println("First Product JSON: " + firstProduct);


//        Add to Cart

        Map<String, Object> addToCart_payload = new HashMap<>();
        addToCart_payload.put("_id", login_jsonPath.getString("userId"));
        addToCart_payload.put("product", firstProduct); // another Map

        Response addToCard_response = given()
//                .log().all()
                .body(addToCart_payload)
                .headers("Content-Type", "application/json",
                        "Authorization", login_jsonPath.getString("token"))
                .when()
                .post("user/add-to-cart")
                .then()
//                .log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // ✅ Print body as string
        String addToCart = addToCard_response.asString();
        System.out.println("========= Response Body =========");
        System.out.println(addToCart);


        // Get Cart

        Response getCart_response = given()
//                .log().all()
                .headers("Content-Type", "application/json",
                        "Authorization", login_jsonPath.getString("token"))
                .get("user/get-cart-products/" + login_jsonPath.getString("userId"))
                .then()
//                .log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // ✅ Print body as string
        String getCart = getCart_response.asString();
//        System.out.println("========= Response Body =========");
//        System.out.println(getCart);

        JsonPath getCart_jsonPath = new JsonPath(getCart);
        System.out.println("Message : " + getCart_jsonPath.getString("message"));
        System.out.println("Product ID : " + getCart_jsonPath.getString("products[0]._id"));


//        Create Order

        // Create the order entry
        Map<String, String> orderItem = new HashMap<>();
        orderItem.put("country", "India");
        orderItem.put("productOrderedId", getCart_jsonPath.getString("products[0]._id"));

        List<Map<String, String>> ordersList = new ArrayList<>();
        ordersList.add(orderItem);

        // Wrap in parent payload map
        Map<String, Object> placeOrderPayload = new HashMap<>();
        placeOrderPayload.put("orders", ordersList);


        Response createOrder_response = given()
                .body(placeOrderPayload)
                .headers("Content-Type", "application/json",
                        "Authorization", login_jsonPath.getString("token"))
                .when()
                .post("order/create-order")
                .then()
                .assertThat().statusCode(201)
                .extract().response();


        // ✅ Print body as string
        String createOrder = createOrder_response.asString();
        JsonPath createOrder_jsonPath = new JsonPath(createOrder);
        System.out.println("Message : " + createOrder_jsonPath.getString("message"));
        System.out.println("Message : " + createOrder_jsonPath.getString("message"));

//        Get Orders
        Response getOrder_response = given()
//                .log().all()
                .headers("Content-Type", "application/json",
                        "Authorization", login_jsonPath.getString("token"))
                .get("order/get-orders-for-customer/" + login_jsonPath.getString("userId"))
                .then()
//                .log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // ✅ Print body as string
        String getOrder = getOrder_response.asString();
//        System.out.println("========= Response Body =========");
//        System.out.println(getOrder);

        JsonPath getOrder_jsonPath = new JsonPath(getOrder);
        System.out.println("Message : " + getOrder_jsonPath.getString("message"));
        System.out.println("Order ID : " + getOrder_jsonPath.getString("data[0]._id"));


//        Get Order Details
        Response getOrderDetails_response = given()
//                .log().all()
                .headers("Content-Type", "application/json",
                        "Authorization", login_jsonPath.getString("token"))
                .get("order/get-orders-details?id=" + getOrder_jsonPath.getString("data[0]._id"))
                .then()
//                .log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // ✅ Print body as string
        String getOrderDetails = getOrderDetails_response.asString();
//        System.out.println("========= Response Body =========");
//        System.out.println(getOrderDetails);

        JsonPath getOrderDetails_jsonPath = new JsonPath(getOrderDetails);
        System.out.println("Message : " + getOrderDetails_jsonPath.getString("message"));
        System.out.println("Order Details : " + getOrderDetails_jsonPath.getString("data"));


//        Delete Order
        Response deleteOrder_response = given()
//                .log().all()
                .headers("Content-Type", "application/json",
                        "Authorization", login_jsonPath.getString("token"))
                .delete("order/delete-order/" + getOrder_jsonPath.getString("data[0]._id"))
                .then()
//                .log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // ✅ Print body as string
        String deleteOrder = deleteOrder_response.asString();
//        System.out.println("========= Response Body =========");
//        System.out.println(deleteOrder);

        JsonPath deleteOrder_jsonPath = new JsonPath(deleteOrder);
//        Assert.assertEquals(deleteOrder_jsonPath.getString("message"), "Orders Deleted Successfully");
        System.out.println("Message : " + deleteOrder_jsonPath.getString("message"));

    }
}
