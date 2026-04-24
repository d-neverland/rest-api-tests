package com.restapitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsersCrudApiTest {

    private static final int EXISTING_USER_ID = 1;
    private static final String USER_BY_ID_URL = UsersApiTest.USERS_URL + "/" + EXISTING_USER_ID;

    @Test
    @DisplayName("POST /users creates a user")
    void shouldCreateUser() {
        Map<String, Object> newUser = Map.of(
                "name", "Daria QA",
                "username", "daria.qa",
                "email", "daria.qa@example.com"
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post(UsersApiTest.USERS_URL);

        assertEquals(201, response.statusCode(),
                "Creating a user should return HTTP 201 Created");
        assertEquals("Daria QA", response.jsonPath().getString("name"),
                "The created user response should include the provided name");
        assertEquals("daria.qa@example.com", response.jsonPath().getString("email"),
                "The created user response should include the provided email");
        assertTrue(response.jsonPath().getInt("id") > 0,
                "The created user response should include a generated id");
    }

    @Test
    @DisplayName("GET /users/1 returns an existing user")
    void shouldGetUserById() {
        Response response = given()
                .when()
                .get(USER_BY_ID_URL);

        assertEquals(200, response.statusCode(),
                "Getting a user should return HTTP 200 OK");
        assertEquals(EXISTING_USER_ID, response.jsonPath().getInt("id"),
                "The returned user should have the requested id");
    }

    @Test
    @DisplayName("PUT /users/1 updates a user")
    void shouldUpdateUser() {
        Map<String, Object> updatedUser = Map.of(
                "id", EXISTING_USER_ID,
                "name", "Updated User",
                "username", "updated.user",
                "email", "updated.user@example.com"
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when()
                .put(USER_BY_ID_URL);

        assertEquals(200, response.statusCode(),
                "Updating a user with PUT should return HTTP 200 OK");
        assertEquals("Updated User", response.jsonPath().getString("name"),
                "The updated user response should include the new name");
        assertEquals("updated.user@example.com", response.jsonPath().getString("email"),
                "The updated user response should include the new email");
    }

    @Test
    @DisplayName("PATCH /users/1 partially updates a user")
    void shouldPartiallyUpdateUser() {
        Map<String, Object> partialUpdate = Map.of(
                "email", "patched.user@example.com"
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(partialUpdate)
                .when()
                .patch(USER_BY_ID_URL);

        assertEquals(200, response.statusCode(),
                "Updating a user with PATCH should return HTTP 200 OK");
        assertEquals("patched.user@example.com", response.jsonPath().getString("email"),
                "The patched user response should include the updated email");
    }

    @Test
    @DisplayName("DELETE /users/1 deletes a user")
    void shouldDeleteUser() {
        Response response = given()
                .when()
                .delete(USER_BY_ID_URL);

        int actualStatusCode = response.statusCode();

        assertTrue(actualStatusCode == 200 || actualStatusCode == 204,
                "Deleting a user should return a successful delete status code");
    }
}
