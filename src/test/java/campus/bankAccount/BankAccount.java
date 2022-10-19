package campus.bankAccount;


import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BankAccount {

    Cookies cookies;

    @BeforeClass
    public void login(){

        baseURI = "https://demo.mersys.io/";  // her metodda burası çalışacak url kalan kısmı post a eklenecek

        Map<String , String > credential = new HashMap<>();
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
                .extract().response().getDetailedCookies()
        ;
    }

    String id;
    String name;
    String iban;
    String integrationCode;
    String currency;
    String schoolId;
    @Test
    public void createBankAccount(){

        name = getRandomName();  // random metodlar sayesinde diğer fields değişlikliğine gerek kalmadı
        iban = getRandomIban();
        integrationCode = "369";
        currency = "PKR";
        schoolId = "5fe07e4fb064ca29931236a5";
        AccoundFields accoundFields = new AccoundFields();
        accoundFields.setName(name);
        accoundFields.setIban(iban);
        accoundFields.setIntegrationCode(integrationCode);
        accoundFields.setCurrency(currency);
        accoundFields.setSchoolId(schoolId);

        id =
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(accoundFields)
                .when()
                .post("school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    public String  getRandomName(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomIban(){

        return RandomStringUtils.randomAlphanumeric(24);
    }


    @Test(dependsOnMethods = "createBankAccount")
    public void updateBankAccount(){
        name = getRandomName();
        iban = getRandomIban();

        AccoundFields accoundFields = new AccoundFields();
        accoundFields.setId(id);
        accoundFields.setName(name);
        accoundFields.setIban(iban);
        accoundFields.setSchoolId(schoolId);
        accoundFields.setCurrency(currency);
        accoundFields.setIntegrationCode(integrationCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(accoundFields)
                .when()
                .put("school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateBankAccount")
    public void deleteBankAccount(){

        given()
                .cookies(cookies)
                .pathParam("id", id)
                .when()
                .delete("school-service/api/bank-accounts/{id}")
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteBankAccount")
    public void updateBankAccountNegative(){

        name = getRandomName();
        iban = getRandomIban();
        AccoundFields accoundFields = new AccoundFields();
        accoundFields.setId(id);
        accoundFields.setName(name);
        accoundFields.setIban(iban);
        accoundFields.setSchoolId(schoolId);
        accoundFields.setCurrency(currency);
        accoundFields.setIntegrationCode(integrationCode);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(accoundFields)
                .when()
                .put("school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Can't find Bank Account"));
    }

    @Test(dependsOnMethods = "updateBankAccountNegative")
    public void deleteBankAccountNegative(){

        given()
                .cookies(cookies)
                .pathParam("id", id)
                .when()
                .delete("school-service/api/bank-accounts/{id}")
                .then()
                .statusCode(400);
    }
}
