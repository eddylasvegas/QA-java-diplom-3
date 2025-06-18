package api;

import data.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class AuthApi {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String API_PATH = "/api";
    private static final String AUTH_PATH = "/auth";

    private static final String REGISTER_ENDPOINT = API_PATH + AUTH_PATH + "/register";
    private static final String LOGIN_ENDPOINT = API_PATH + AUTH_PATH + "/login";
    private static final String USER_ENDPOINT = API_PATH + AUTH_PATH + "/user";

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public static Response registerUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format(
                        "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
                        user.getEmail(),
                        user.getPassword(),
                        user.getName()
                ))
                .when()
                .post(REGISTER_ENDPOINT);
    }

    public static Response loginUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format(
                        "{\"email\":\"%s\",\"password\":\"%s\"}",
                        user.getEmail(),
                        user.getPassword()
                ))
                .when()
                .post(LOGIN_ENDPOINT);
    }

    public static void deleteUser(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }
}