package org.example.scouter;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.apache.http.HttpStatus.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GetOrderListTest {

    private class ScouterOrder{
        public int id;
        public int courierId;
        public String firstName;
        public String lastName;
        public String address;
        public String metroStation;
        public String phone;
        public int rentTime;
        public String deliveryDate;
        public int track;
        public String[] color;

        public String OrderToString(){
            return "id: " + id +
                    ", CourierID: " + courierId + ", " +
                    ", firstName: " + firstName +
                    ", lastName: " + lastName +
                    ", addeess: " + address +
                    ", metroStation: " + metroStation +
                    ", phone: " + phone +
                    ", rentTime: " + rentTime +
                    ", deliveryDate: " + deliveryDate +
                    ", track: " + track +
                    ", color: " + Arrays.toString(color);
        }
    }

    private class ScouterOrders{
        public ScouterOrder[] orders;
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = ScouterAPI.apiBasicURL;
    }

    @Test
    public void getOrderListTest(){
        Response response = given().header(ScouterAPI.apiPostHeaderType, ScouterAPI.apiPostHeaderValue).contentType(ContentType.JSON).accept(ContentType.JSON).get(ScouterAPI.apiCourierOrders);
        ValidatableResponse vresponse = response.then();

        assertEquals("Scouter orders list request failed. Incorrect Status Code.", SC_OK, vresponse.extract().statusCode());

        String strJson = response.getBody().asPrettyString();
        Gson gson = new Gson();
        ScouterOrders orders = gson.fromJson(strJson, ScouterOrders.class);

        assertTrue("Scouter orders list is not returned.", orders.orders.length > 0);

//        System.out.println(orders.orders[0].OrderToString());

    }
}
