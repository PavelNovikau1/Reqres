import objects.ListResource;
import objects.SingleUser;
import objects.UserData;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Tests extends BaseTest {

    @Test
    public void getRequestToListUsersTest() {
        given()
                .when()
                .get( "api/users?page=2")
                .then()
                .log().all()
                .statusCode(200)
                .body("page", equalTo(2),
                        "total", equalTo(12));
    }

    @Test
    public void getRequestToSingleUserTest() {
        SingleUser singleUser = SingleUser.builder()
                .id(2)
                .email("janet.weaver@reqres.in")
                .firstName("Janet")
                .lastName("Weaver")
                .avatar("https://reqres.in/img/faces/2-image.jpg")
                .build();
        given()
                .when()
                .get( "api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(singleUser.getId()),
                        "data.email", equalTo(singleUser.getEmail()),
                        "data.first_name", equalTo(singleUser.getFirstName()),
                        "data.last_name", equalTo(singleUser.getLastName()),
                        "data.avatar", equalTo(singleUser.getAvatar()));
    }

    @Test
    public void getRequestToSingleUserNotFoundTest() {
        given()
                .when()
                .get( "api/users/23")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    public void getRequestToListResourceTest() {
        given()
                .when()
                .get("api/unknown")
                .then()
                .log().all()
                .statusCode(200)
                .body("total", equalTo(12));
    }

    @Test
    public void getRequestToSingleResourceTest() {
        ListResource listResource = ListResource.builder()
                .id(2)
                .name("fuchsia rose")
                .year(2001)
                .color("#C74375")
                .pantoneValue("17-2031")
                .build();
        given()
                .when()
                .get("api/unknown/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(listResource.getId()),
                        "data.name", equalTo(listResource.getName()),
                        "data.year", equalTo(listResource.getYear()),
                        "data.color", equalTo(listResource.getColor()),
                        "data.pantone_value", equalTo(listResource.getPantoneValue()));
    }

    @Test
    public void getRequestToSingleResourceNotFoundTest() {
        given()
                .when()
                .get("api/unknown/23")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    public void postRequestToCreateUserTest() {
        UserData userData = UserData.builder()
                .name("morpheus")
                .job("leader")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .post("api/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("$", hasKey("id") ,
                        "job", equalTo(userData.getJob()),
                        "name", equalTo(userData.getName()),
                        "$", hasKey("createdAt"));
    }

    @Test
    public void putRequestToUpdateUserTest() {
        UserData userData = UserData.builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .put("api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("job", equalTo(userData.getJob()),
                        "name", equalTo(userData.getName()),
                        "$", hasKey("updatedAt"));
    }

    @Test
    public void patchRequestToUpdateUserTest() {
        UserData userData = UserData.builder()
                .name("morpheus")
                .job("zion resident")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .patch("api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("job", equalTo(userData.getJob()),
                        "name", equalTo(userData.getName()),
                        "$", hasKey("updatedAt"));
    }

    @Test
    public void deleteRequestUserTest() {
        given()
                .when()
                .delete("api/users/2")
                .then()
                .log().all()
                .statusCode(204)
                .body(equalTo(""));
    }

    @Test
    public void postRequestToRegisterSuccessfulTest() {
        UserData userData = UserData.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .post("api/register")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", hasKey("id"),
                        "$", hasKey("token"));
    }

    @Test
    public void postRequestToRegisterUnsuccessfulTest() {
        UserData userData = UserData.builder()
                .email("eve.holt@reqres.in")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .post("api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void postRequestToLoginSuccessfulTest() {
        UserData userData = UserData.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .post("api/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void postRequestToLoginUnsuccessfulTest() {
        UserData userData = UserData.builder()
                .email("peter@klaven")
                .build();
        given()
                .body(userData)
                .given()
                .when()
                .post("api/login")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void getRequestToDelayedResponseTest() {
        given()
                .when()
                .get("api/users?delay=3")
                .then()
                .log().all()
                .statusCode(200)
                .time(lessThan(5L), TimeUnit.SECONDS);
    }
}
