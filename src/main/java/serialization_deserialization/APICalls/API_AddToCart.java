package serialization_deserialization.APICalls;

import io.restassured.RestAssured;
import serialization_deserialization.pojoClass.AddToCartRequest;
import static io.restassured.RestAssured.given;

public class API_AddToCart {
    public static String addToCart(String token, AddToCartRequest request) {
        RestAssured.baseURI = "https://rahulshettyacademy.com/api/ecom/";
        return given()
                .contentType("application/json")
                .header("Authorization", token)
                .body(request)
                .when()
                .post("user/add-to-cart")
                .then()
                .statusCode(200)
                .extract().asString();
    }
}
