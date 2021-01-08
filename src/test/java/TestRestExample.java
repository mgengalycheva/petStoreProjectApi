import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.serenitybdd.rest.RestRequests.*;
import static org.hamcrest.CoreMatchers.containsString;


public class TestRestExample {
    public Logger logger = LogManager.getLogger(TestRestExample.class);

    @Test
    public void firstGetTest() {
        logger.info("start test");

        RestAssured
                .when().get("https://petstore.swagger.io/v2/store/inventory")
                .then().assertThat().statusCode(200).
                and().body(containsString("AVAILABLE"));

        logger.info("end test");
    }

    @Test
    public void firstPostTest() {

        Random random = new Random();
        int petId = 1 + random.nextInt(1000 - 1);

        Map<String, String> pet = new HashMap<>();
        pet.put("id", "" + petId + "");
        pet.put("name", "test pet");

        logger.info("start test");

        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .prettyPrint();

        logger.info("end test");

    }

    @Test
    public void firstPutTest() {
        Random random = new Random();
        int petId = 1 + random.nextInt(1000 - 1);

        Map<String, String> pet = new HashMap<>();
        pet.put("id", "" + petId + "");
        pet.put("name", "test pet");

        logger.info("start test");

       RequestSpecification requestSpecification = new RequestSpecBuilder()
               .setBaseUri("https://petstore.swagger.io/v2")
               .addHeader("Content-Type","application/json")
               .addHeader("Accept", "application/json")
               .build();

       Response response = given().spec(requestSpecification).body(pet)
                .when().post("/pet");

       response.prettyPrint();

       int newPetId = response.then().extract().path("id");

        Map<String, String> editPet = new HashMap<>();
        editPet.put("id", Integer.toString(newPetId));
        editPet.put("name", "doggie");

        Response editResponse = given().spec(requestSpecification).body(editPet)
                .when().put("/pet");

        editResponse.prettyPrint();

        logger.info("end test");
    }
}
