package org.example.scouter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScouterCourierTest {

    // Positive Test
    // Courier created successfully
    @Before
    public void setUp() {
        RestAssured.baseURI = ScouterAPI.apiBasicURL;
    }

    @Test
    @DisplayName("Create Courier. Happy path")
    @Description("Data provided, response code 201, Login successful")
    public void courierCreateSuccessTest() {
        // Test data initialization
        Courier courier = new Courier();

        // Test run
        ValidatableResponse response = courier.apiCreate();
        System.out.println(response.extract().body().asString());

        //Status Code check
        int statusCode = response.extract().statusCode();
        assertTrue("Courier creation failed. Response code is incorrect",statusCode == 201);

        //Response body check
        boolean isCourierCreated = response.extract().path("ok");
        assertTrue("Courier creation failed. Body content is incorrect", isCourierCreated );

        //Courier verification via login
        response = courier.apiLogin();
        System.out.println(response.extract().body().asString());

        assertTrue("Courier creation is not successful. Login attempt is failed.", response.extract().statusCode() == 200);

        //Test data clean-up
        response = courier.apiDelete();
        System.out.println(response.extract().body().asString());
    }

    // Negative Tests
    // Courier duplicate rejected
    @Test
    @DisplayName("Create Courier duplicate")
    @Description("Create courier two times, response code 409")
    public void courierCreateDuplicateTest() {
        // Test data initialization
        Courier courier = new Courier();

        // Test run - create courier first time
        ValidatableResponse response = courier.apiCreate();
        System.out.println(response.extract().body().asString());

        //Make sure, that courier is created
        int statusCode = response.extract().statusCode();
        assertTrue("Courier creation failed. Response code is incorrect",statusCode == 201);

        // Test run - create courier second time
        response = courier.apiCreate();
        System.out.println(response.extract().body().asString());

        //Check Status Code
        statusCode = response.extract().statusCode();
        assertTrue("Courier duplication check failed. Response code is incorrect",statusCode == 409);

        //Check Response body
        String strExpected = "Этот логин уже используется";
        String strResponse = response.extract().path("message");
        assertEquals("Courier duplication check failed. Response message is different", strExpected, strResponse);

        //Test data clean-up
        response = courier.apiDelete();
        System.out.println(response.extract().body().asString());
    }

    // Courier Data not enough: Login
    @Test
    @DisplayName("Create Courier - no login")
    @Description("Create courier without login, response code 400")
    public void courierCreateNoLoginTest(){
        // Test data initialization
        Courier courier = new Courier(null, "12345", "fhvbd");

        // Test run
        ValidatableResponse response = courier.apiCreate(true);
        System.out.println(response.extract().body().asString());

        //Status Code check
        int statusCode = response.extract().statusCode();
        assertTrue("Courier rejection failed. Response code is incorrect",statusCode == 400);

        //Response body check
        String strExpected = "Недостаточно данных для создания учетной записи";
        String strResponse = response.extract().path("message");
        assertEquals("Courier rejection failed. Body content is incorrect", strExpected, strResponse );

    }
    // Courier Data not enough: Password
    @Test
    @DisplayName("Create Courier - no password")
    @Description("Create courier without password, response code 400")
    public void courierCreateNoPasswordTest(){
        // Test data initialization
        Courier courier = new Courier("kfhjkf", null, "fhvbd");

        // Test run
        ValidatableResponse response = courier.apiCreate(true);
        System.out.println(response.extract().body().asString());

        //Status Code check
        int statusCode = response.extract().statusCode();
        assertTrue("Courier rejection failed. Response code is incorrect",statusCode == 400);

        //Response body check
        String strExpected = "Недостаточно данных для создания учетной записи";
        String strResponse = response.extract().path("message");
        assertEquals("Courier rejection failed. Body content is incorrect", strExpected, strResponse );

    }
    // Courier Data not enough: Name
    @Test
    @DisplayName("Create Courier - no First Name")
    @Description("Create courier without First Name still should be successfull, response code 201")
    public void courierCreateNoFirstNameTest(){
        // Test data initialization
        Courier courier = new Courier("kffjfkf", "12345", null);

        // Test run
        ValidatableResponse response = courier.apiCreate(true);
        System.out.println(response.extract().body().asString());

        //Status Code check
        int statusCode = response.extract().statusCode();
        assertTrue("Courier creation failed. Response code is incorrect",statusCode == 201);

        //Response body check
        boolean isCourierCreated = response.extract().path("ok");
        assertTrue("Courier creation failed. Body content is incorrect", isCourierCreated );

        //Test data clean-up
        response = courier.apiDelete();
        System.out.println(response.extract().body().asString());
    }
    // Courier Data not enough: no data
    @Test
    @DisplayName("Create Courier - no data at all")
    @Description("Create courier without any data, response code 400")
    public void courierCreateNoDataTest() {
        // Test data initialization
        Courier courier = new Courier("null", null, "null");

        // Test run
        ValidatableResponse response = courier.apiCreate(true);
        System.out.println(response.extract().body().asString());

        //Status Code check
        int statusCode = response.extract().statusCode();
        assertTrue("Courier rejection failed. Response code is incorrect", statusCode == 400);

        //Response body check
        String strExpected = "Недостаточно данных для создания учетной записи";
        String strResponse = response.extract().path("message");
        assertEquals("Courier rejection failed. Body content is incorrect", strExpected, strResponse);
    }
}
