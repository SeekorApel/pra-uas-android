package id.co.sigma.mewing.API;

import id.co.sigma.mewing.API.Service.UserService;

public class ApiUtils {

    public static final String API_BASE_URL = "http://10.1.10.248:8080/";
    //public static final String API_BASE_URL = "http://10.1.6.174:8080/";

    public ApiUtils() {
    }

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_BASE_URL).create(UserService.class);
    }

}
