package com.app.booking_care.util;

public class UrlUtil{
    public static final String API_PREFIX ="/api";
    public static final String API_VERSION ="/v1";
    public static final String BASE_URL = API_PREFIX +  API_VERSION ;
    public static final String QUESTION_URL = BASE_URL + "/questions";
    public static final String ANSWER_URL = BASE_URL + "/answers";
    public static final String HEALTH_TEST_URL = BASE_URL + "/tests";
    public static final String USER_URL = BASE_URL + "/users";

    public static String getUrl(String domainUrl, String apiPath, String resourcePath){
        return domainUrl + apiPath + resourcePath;
    }
}
