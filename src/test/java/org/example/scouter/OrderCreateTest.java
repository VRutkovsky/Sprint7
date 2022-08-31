package org.example.scouter;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.*;

import static io.restassured.RestAssured.given;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = ScouterAPI.apiBasicURL;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        Object[][] object = new Object[][]{
                {"Иван",  "Иванов", "Москва 1", "Сокол",    "+79998880001", 1, "09.09.2022", "test black",         new String[]{"BLACK"}},
                {"Петр",  "Петров", "Москва 2", "Лубянка",  "+79998880002", 2, "10.09.2022", "test grey",          new String[]{"GREY"}},
                {"Ольга", "Белых",  "Москва 3", "Строгино", "+79998880003", 3, "11.09.2022", "test blac and grey", new String[]{"BLACK", "GREY"}},
                {"Ирина", "Черных", "Москва 4", "ВДНХ",     "+79998880004", 5, "12.09.2022", "test no color",      new String[]{}}
        };

        return object;
    }
    @Test

    public void orderCreateTest(){
        ValidatableResponse response = given().header(ScouterAPI.apiPostHeaderType, ScouterAPI.apiPostHeaderValue).and().body(this).when().post(ScouterAPI.apiOrderCreate).then();

        assertEquals("Scouter Order failed. Incorrect Status Code.", SC_CREATED, response.extract().statusCode());

        int track = response.extract().path("track");
        assertTrue("Scouter Order failed. Incorrect track number", track > 0);
    }
}
