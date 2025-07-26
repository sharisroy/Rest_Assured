package serialization_deserialization.APICalls;

import io.restassured.RestAssured;
import serialization_deserialization.pojoClass.ProductListResponse;
import static io.restassured.RestAssured.given;

public class API_GetProducts {
    public static ProductListResponse getAllProducts(String token) {
        RestAssured.baseURI = "https://rahulshettyacademy.com/api/ecom/";
        return given()
                .contentType("application/json")
                .header("Authorization", token)
                .when()
                .post("product/get-all-products")
                .then()
                .statusCode(200)
                .extract().as(ProductListResponse.class);
    }
}
