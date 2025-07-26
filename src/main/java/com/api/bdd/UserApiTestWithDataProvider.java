package com.api.bdd;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UserApiTestWithDataProvider {

    @DataProvider(name = "userData")
    public Object[][] provideUserData() {
        return new Object[][] {
                {"1", 200},
                {"2", 200},
                {"9999", 404}  // example of not found
        };
    }

    @Test(dataProvider = "userData")
    public void testGetUserById(String userId, int expectedStatusCode) {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}");

        System.out.println("Response: " + response.asPrettyString());
        assertEquals(response.getStatusCode(), expectedStatusCode);
    }
}
