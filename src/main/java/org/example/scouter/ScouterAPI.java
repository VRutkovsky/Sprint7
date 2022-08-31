package org.example.scouter;

public abstract class ScouterAPI {

    public static final String apiBasicURL = "http://qa-scooter.praktikum-services.ru";
    public static final String apiCourierCreate = "/api/v1/courier";
    public static final String apiCourierLogin = "/api/v1/courier/login";
    public static final String apiCourierDelete = "/api/v1/courier/:id";
    public static final String apiCourierOrders = "/api/v1/orders";
    public static final String apiOrderCreate = "/api/v1/orders";
    public static final String apiPostHeaderType = "Content-type";
    public static final String apiPostHeaderValue = "application/json";


}
