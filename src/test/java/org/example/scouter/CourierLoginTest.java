package org.example.scouter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierLoginTest {
    private ValidatableResponse response;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = ScouterAPI.apiBasicURL;

        // Test data initialization
        courier = new Courier();

        // Create test courier
        response = courier.apiCreate();
//        System.out.println(response.extract().body().asString());

        //Status Code check
        int statusCode = response.extract().statusCode();
        assertEquals("Courier creation failed. Response code is incorrect",SC_CREATED , statusCode);
    }

    @After
    public void cleanUp(){
        //Test data clean-up
        response = courier.apiDelete();
//        System.out.println(response.extract().body().asString());
    }

    //Positive Test
    //Courier Logged in successfully. Courier ID is returned.
    @Test
    @DisplayName("Courier login. Happy path")
    @Description("Data provided, response code 200, Courier ID is greater zero")
    public void courierLoginSuccessTest() {

        response = courier.apiLogin();
//        System.out.println(response.extract().body().asString());

        assertEquals("Courier login is not successful.", SC_OK, response.extract().statusCode());

        int id = response.extract().path("id");
        assertTrue("", id > 0);
    }


    //Negative Test
    //No login provided
    @Test
    @DisplayName("Courier login. No login")
    @Description("Login not provided, response code 400")
    public void courierNoLoginTest() {

        response = courier.apiLogin(null, courier.getPassword());
//        System.out.println(response.extract().body().asString());

        assertEquals("Courier login rejection failed.", SC_BAD_REQUEST, response.extract().statusCode());

        //Response body check
        String strExpected = "Недостаточно данных для входа";
        String strResponse = response.extract().path("message");
        assertEquals("Courier login rejection failed. Body content is incorrect", strExpected, strResponse );

    }

    //No password provided
    @Test
    @DisplayName("Courier login. No password")
    @Description("No password provided, response code 400")
    public void courierNoPasswordTest() {

        response = courier.apiLogin(courier.getLogin(), null);
//        System.out.println(response.extract().body().asString());

        assertEquals("Courier login rejection failed.", SC_BAD_REQUEST, response.extract().statusCode());

        //Response body check
        String strExpected = "Недостаточно данных для входа";
        String strResponse = response.extract().path("message");
        assertEquals("Courier login rejection failed. Body content is incorrect", strExpected, strResponse);
    }

    //Wrong Login provided
    @Test
    @DisplayName("Courier login. Wrong login")
    @Description("Wrong Login provided, response code 404")
    public void courierWrongLoginTest() {

        response = courier.apiLogin("bfgbvpoyjjnyhn", courier.getPassword());
//        System.out.println(response.extract().body().asString());

        assertEquals("Courier login rejection failed.", SC_NOT_FOUND, response.extract().statusCode());

        //Response body check
        String strExpected = "Учетная запись не найдена";
        String strResponse = response.extract().path("message");
        assertEquals("Courier login rejection failed. Body content is incorrect", strExpected, strResponse );
    }

    //Wrong password provided
    @Test
    @DisplayName("Courier login. Wrong password")
    @Description("Wrong password provided, response code 404")
    public void courierWrongPasswordTest() {

        response = courier.apiLogin(courier.getLogin(), "wgf67z4v68cs");
//        System.out.println(response.extract().body().asString());

        assertEquals("Courier login rejection failed.", SC_NOT_FOUND, response.extract().statusCode());

        //Response body check
        String strExpected = "Учетная запись не найдена";
        String strResponse = response.extract().path("message");
        assertEquals("Courier login rejection failed. Body content is incorrect", strExpected, strResponse );
    }
}
