package serialization_deserialization.APICalls;

import io.restassured.RestAssured;
import serialization_deserialization.pojoClass.LoginRequest;
import serialization_deserialization.pojoClass.LoginResponse;

import static io.restassured.RestAssured.given;

public class API_Login {
    public static LoginResponse login() {

        // Set Base URI
        RestAssured.baseURI = "https://rahulshettyacademy.com/api/ecom/";

        // Serialization: Prepare login request payload
        LoginRequest loginPayload = new LoginRequest("sqa.haris@gmail.com", "H@12345bd");

        // Deserialization: Send login request and convert response into POJO
        LoginResponse loginResponse = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract().as(LoginResponse.class);

        // Use POJO getters
        System.out.println("Token   : " + loginResponse.getToken());
        System.out.println("User ID : " + loginResponse.getUserId());
        System.out.println("Message : " + loginResponse.getMessage());

        return loginResponse;
    }
}
