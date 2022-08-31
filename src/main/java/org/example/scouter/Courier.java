package org.example.scouter;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class Courier {

    private String login;
    private String password;
    private String firstName;
    private Integer id;

    private boolean isLoggedIn = false;

    public Courier(){
        int i = (int) (Math.random() * 999);
        login = "Courier" + i;
        password = "pwd" + i;
        firstName = "Ivan" + i;
    }

    public Courier(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public ValidatableResponse apiCreate(boolean dropField) {
        String strLogin;
        String strPassword;
        String strFirstName;

        if(login == null) {strLogin = "";} else strLogin = login;
        if(password == null) {strPassword = "";} else strPassword = password;
        if(firstName == null) {strFirstName = "";} else strFirstName = firstName;

        String json = "{";
        String separator = "";

        if(dropField) {
            if(!strLogin.isBlank() && !strLogin.isEmpty()) {
                json = json + "\"login\": \"" + login + "\"";
                separator = ", ";
            }
            if (!strPassword.isBlank() && !strPassword.isEmpty()){
                json = json + separator + "\"password\": \"" + password + "\"";
                separator = ", ";
            }
            if(!strFirstName.isBlank() && !strFirstName.isEmpty()){
                json = json + separator + "\"firstName\": \"" + firstName + "\"";
            }
            json = json + "}";
        } else {
            json = "{\"login\": \"" + strLogin + "\", \"password\": \"" + strPassword + "\", \"firstName\": \"" + strFirstName + "\"}";
        }
        return given().header(ScouterAPI.apiPostHeaderType, ScouterAPI.apiPostHeaderValue).and().body(json).when().post(ScouterAPI.apiCourierCreate).then();
    }

    public ValidatableResponse apiCreate() {
        return apiCreate(false);
    }

    public ValidatableResponse apiLogin(String strLogin, String strPassword){
        if(strLogin == null) {strLogin = "";}
        if(strPassword == null) {strPassword = "";}

        String json = "{\"login\": \"" + strLogin + "\", \"password\": \"" + strPassword + "\"}";

        ValidatableResponse response = given().header(ScouterAPI.apiPostHeaderType, ScouterAPI.apiPostHeaderValue).and().body(json).when().post(ScouterAPI.apiCourierLogin).then();
                if((response.extract().statusCode() == SC_OK) && ((int)response.extract().path("id") > 0)) {
                    isLoggedIn = true;
                    id = response.extract().path("id");
        }

        return response;
    }
    public ValidatableResponse apiLogin(){
        return apiLogin(login, password);
    }

    public ValidatableResponse apiDelete(){
        if(!isLoggedIn){
            ValidatableResponse response = apiLogin();
        }
        return given().and().when().delete(ScouterAPI.apiCourierDelete.replace(":id", id.toString())).then();
    }
}
