package id.co.sigma.mewing.API.Service;

import id.co.sigma.mewing.API.VO.UserListVO;
import id.co.sigma.mewing.API.VO.UserVO;
import id.co.sigma.mewing.Model.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @GET("user/login")
    Call<UserVO> login (@Query("username") String username, @Query("password") String password);

    @GET("user/getAllUser")
    Call<UserListVO> getAllUser();

    @POST("user/saveUser")
    Call<UserVO> saveUser(@Body UserModel user);

    @POST("user/updateUser")
    Call<UserVO> updateUser(@Body UserModel updateUser);

    @POST("user/deleteUser")
    Call<UserVO> deleteUser(@Query("username") String username);

}
