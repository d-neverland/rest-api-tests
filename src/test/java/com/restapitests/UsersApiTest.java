package com.restapitests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UsersApiTest {

    static final String USERS_URL = "https://jsonplaceholder.typicode.com/users";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String EXPECTED_CONTENT_TYPE = "application/json; charset=utf-8";

    @Test
    @DisplayName("GET /users returns HTTP 200 OK")
    void shouldReturnOkStatusWhenGettingUsers() {
        sendGetUsersRequest()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET /users returns the expected content type header")
    void shouldReturnJsonContentTypeHeader() {
        Response response = sendGetUsersRequest();

        String contentType = response.getHeader(CONTENT_TYPE_HEADER);

        assertFalse(contentType == null || contentType.isBlank(),
                "The content-type header should be present in the response");
        assertEquals(EXPECTED_CONTENT_TYPE, contentType,
                "The content-type header value should match the expected JSON content type");
    }

    @Test
    @DisplayName("GET /users returns an array of 10 users")
    void shouldReturnArrayOfTenUsers() {
        Response response = sendGetUsersRequest();

        int actualNumberOfUsers = response.jsonPath().getList("$").size();

        assertEquals(10, actualNumberOfUsers,
                "The response body should contain an array of 10 users");
    }

    private Response sendGetUsersRequest() {
        return given()
                .when()
                .get(USERS_URL);
    }
}
