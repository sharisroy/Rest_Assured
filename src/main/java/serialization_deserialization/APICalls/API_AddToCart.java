package serialization_deserialization.APICalls;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import serialization_deserialization.pojoClass.AddToCartRequest;
import static io.restassured.RestAssured.given;

public class API_AddToCart {
    // ✅ Request spec builder with token and base URI
    private static RequestSpecification getAddToCartRequestSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", token)
                .build();
    }

    // ✅ Response spec builder for expected success
    private static ResponseSpecification getAddToCartResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static String addToCart(String token, AddToCartRequest request) {
        return given()
                .spec(getAddToCartRequestSpec(token))
                .body(request)
                .when()
                .post("user/add-to-cart")
                .then()
                .spec(getAddToCartResponseSpec())
                .extract().asString();
    }
}
