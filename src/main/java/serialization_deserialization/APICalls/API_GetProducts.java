package serialization_deserialization.APICalls;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import serialization_deserialization.pojoClass.ProductListResponse;
import static io.restassured.RestAssured.given;

public class API_GetProducts {

// ✅ Build request specification with Authorization and base URI
private static RequestSpecification getProductRequestSpec(String token) {
    return new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
            .setContentType(ContentType.JSON)
            .addHeader("Authorization", token)
            .build();
}

    // ✅ Build response specification for success case
    private static ResponseSpecification getProductResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
@Test
    public static ProductListResponse getAllProducts(String token) {
        return given()
                .spec(getProductRequestSpec(token))
                .when()
                .post("product/get-all-products")
                .then()
                .spec(getProductResponseSpec())
                .extract().as(ProductListResponse.class);
    }
}
