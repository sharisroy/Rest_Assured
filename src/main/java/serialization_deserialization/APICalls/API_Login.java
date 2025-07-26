package serialization_deserialization.APICalls;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import serialization_deserialization.pojoClass.LoginRequest;
import serialization_deserialization.pojoClass.LoginResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Test
public class API_Login {

    // Global Request Spec
    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .setContentType(ContentType.JSON)
//                .log(LogDetail.URI)
                .build();
    }

    // Success Response Spec (Status 200)
    public static ResponseSpecification successResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
//                .log(LogDetail.BODY)
                .build();
    }

    // Failure Response Spec (Status 400)
    public static ResponseSpecification failureResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType(ContentType.JSON)
//                .log(LogDetail.BODY)
                .build();
    }

    //  Login method - using Spec Builders
    public static LoginResponse login() {
        LoginRequest loginPayload = new LoginRequest("sqa.haris@gmail.com", "H@12345bd");

        LoginResponse loginResponse = given()
                .spec(requestSpec())
                .body(loginPayload)
                .when()
                .post("auth/login")
                .then()
                .spec(successResponseSpec())
                .extract()
                .as(LoginResponse.class);

        System.out.println("Token   : " + loginResponse.getToken());
        System.out.println("User ID : " + loginResponse.getUserId());
        System.out.println("Message : " + loginResponse.getMessage());

        return loginResponse;
    }

    //  Positive Test
    @Test
    public void testValidLogin() {
        login();
    }

    //  Invalid Email Format Test
    @Test
    public void testLoginInvalidEmailFormat() {
        LoginRequest request = new LoginRequest("invalidEmail", "H@12345bd");

        String message = given()
                .spec(requestSpec())
                .body(request)
                .when()
                .post("auth/login")
                .then()
                .spec(failureResponseSpec())
                .body("message", equalTo("Incorrect email or password."))
                .extract()
                .jsonPath().getString("message");

        System.out.println("Message: " + message);
    }

    //  Wrong Password Test
    @Test
    public void testWrongPassword() {
        LoginRequest request = new LoginRequest("sqa.haris@gmail.com", "WrongPassword");

        given()
                .spec(requestSpec())
                .body(request)
                .when()
                .post("auth/login")
                .then()
                .spec(failureResponseSpec())
                .body("message", equalTo("Incorrect email or password."));
    }

    //  Invalid Login Scenarios using DataProvider
    @Test(dataProvider = "invalidLoginData")
    public void testInvalidLoginScenarios(String email, String password, int expectedStatus) {
        LoginRequest request = new LoginRequest(email, password);

        Response response = given()
                .spec(requestSpec())
                .body(request)
                .when()
                .post("auth/login");

        // Build response spec dynamically based on expected status
        ResponseSpecification dynamicSpec = new ResponseSpecBuilder()
                .expectStatusCode(expectedStatus)
                .expectContentType(ContentType.JSON)
                .build();

        response.then()
                .spec(dynamicSpec)
                .body("message", anyOf(
                        equalTo("Incorrect email or password."),
                        equalTo("Email is required"),
                        equalTo("Password is required")
                ));
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][]{
                {"wrongemail@example.com", "H@12345bd", 400},
                {"sqa.haris@gmail.com", "wrongness", 400},
                {"", "", 400},
                {"email@", "H@12345bd", 400},
                {"sqa.haris@gmail.com", "", 400},
        };
    }
}
