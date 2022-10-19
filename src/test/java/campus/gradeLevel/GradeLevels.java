package campus.gradeLevel;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GradeLevels {

    Cookies cookies;

    @BeforeClass
    public void login(){

        baseURI = "https://demo.mersys.io/";

        Map<String ,String> credential = new HashMap<>();

        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

        cookies =
        given()
                .contentType(ContentType.JSON)
                .body(credential)
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }

    String id;
    String name;
    String shortName;
    String nextGradeLevel;
   @Test
    public void createNewGradeLevel(){

       name = getRandomName();
       shortName = getRandomName();
       nextGradeLevel = "1";
       GradeLevelFields gradeLevelFields = new GradeLevelFields();
       gradeLevelFields.setName(name);
       gradeLevelFields.setShortName(name);

       id =
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gradeLevelFields)
                .when()
                .post("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createNewGradeLevel")
    public void updateGradeLevel(){

       GradeLevelFields gradeLevelFields = new GradeLevelFields();
       name = getRandomName();
       shortName = getRandomName();
       gradeLevelFields.setName(name);
       gradeLevelFields.setId(id);
       gradeLevelFields.setShortName(shortName);

       given()
               .cookies(cookies)
               .contentType(ContentType.JSON)
               .body(gradeLevelFields)
               .when()
               .put("school-service/api/grade-levels")
               .then()
               .log().body()
               .statusCode(200)
               .body("name", equalTo(name));
    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel(){
        given()
                .cookies(cookies)
                .pathParam("id", id)
                .when()
                .delete("school-service/api/grade-levels/{id}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "createNewGradeLevel")
    public void createGradeLevelNegative()
    {
        GradeLevelFields gradeLevelFields = new GradeLevelFields();
        gradeLevelFields.setName(name);
        gradeLevelFields.setShortName(shortName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gradeLevelFields)
                .when()
                .post("school-service/api/grade-levels")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("The Grade Level with Name \""+ name+"\" already exists."))
        ;
    }

    @Test(dependsOnMethods = "deleteGradeLevel")
    public void deleteGradeLevelNegative(){
       given()
               .cookies(cookies)
               .pathParam("id", id)
               .when()
               .delete("school-service/api/grade-levels/{id}")

               .then()
               .log().body()
               .statusCode(400);
    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void updateGradeLevelNegative(){
        GradeLevelFields gradeLevelFields = new GradeLevelFields();
        gradeLevelFields.setName(name);
        gradeLevelFields.setId(id);
        gradeLevelFields.setShortName(shortName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gradeLevelFields)
                .when()
                .put("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Grade Level not found."));

    }

    public String  getRandomName(){
        return RandomStringUtils.randomAlphabetic(8);

    }


}
