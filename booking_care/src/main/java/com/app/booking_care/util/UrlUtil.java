package com.app.booking_care.util;

public class UrlUtil{
    public static final String API_PREFIX ="/api";
    public static final String API_VERSION ="/v1";
    public static final String BASE_URL = API_PREFIX +  API_VERSION ;
    public static String getUrl(String domainUrl, String apiPath, String resourcePath){
        return domainUrl + apiPath + resourcePath;
    }
}
