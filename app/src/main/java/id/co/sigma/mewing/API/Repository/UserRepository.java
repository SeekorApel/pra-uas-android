package id.co.sigma.mewing.API.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import id.co.sigma.mewing.API.ApiUtils;
import id.co.sigma.mewing.API.Service.UserService;
import id.co.sigma.mewing.API.VO.UserListVO;
import id.co.sigma.mewing.API.VO.UserVO;
import id.co.sigma.mewing.Model.UserModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private static UserRepository INSTANCE;
    private UserService mUserService;

    private UserRepository(Context context) {
        mUserService = ApiUtils.getUserService();
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository(context);
        }
    }

    public static UserRepository get() {
        return INSTANCE;
    }

    public MutableLiveData<UserVO> getDatalogin(String username, String password){
        Log.d(TAG, "getDatalogin().called");
        MutableLiveData<UserVO> data = new MutableLiveData<>();
        Call<UserVO> call = mUserService.login(username, password);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "getDatalogin().onResponse");
                    data.setValue(response.body());
                }else {
                    Log.e(TAG, "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable throwable) {
                Log.e(TAG, "Error API Call : " + throwable.getMessage());
            }
        });

        return data;
    }

    public MutableLiveData<UserListVO> getAllUser(){
        MutableLiveData<UserListVO> data = new MutableLiveData<>();
        Call<UserListVO> call = mUserService.getAllUser();

        call.enqueue(new Callback<UserListVO>() {
            @Override
            public void onResponse(Call<UserListVO> call, Response<UserListVO> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "getAllUser().onResponse");
                    data.setValue(response.body());
                }else {
                    Log.e(TAG, "Mengambil data gagal");
                }
            }

            @Override
            public void onFailure(Call<UserListVO> call, Throwable throwable) {
                Log.e(TAG, "Error API Call : " + throwable.getMessage());
            }
        });
        return data;
    }

    public void saveUser(UserModel user, final messageCallback callback){
        Log.i(TAG, "saveUser() called");
        Call<UserVO> call = mUserService.saveUser(user);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "saveUser().onResponse");
                    callback.onSuccess(response.body().getMessage());
                }else {
                    callback.onError("Menyimpan data gagal");
                }
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable throwable) {
                Log.e(TAG, "Error API Call : " + throwable.getMessage());
            }
        });
    }

    public void updateUser(UserModel updateUser, final messageCallback callback){
        Log.i(TAG, "updateUser() called");
        Call<UserVO> call = mUserService.updateUser(updateUser);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "updateUser().onResponse");
                    callback.onSuccess(response.body().getMessage());
                }else {
                    callback.onError("Mengubah data gagal");
                }
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable throwable) {
                Log.e(TAG, "Error API Call : " + throwable.getMessage());
            }
        });
    }

    public void deleteUser(String username, final messageCallback callback){
        Log.i(TAG, "deleteUser() called");
        Call<UserVO> call = mUserService.deleteUser(username);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d(TAG, "deleteUser().onResponse");
                    callback.onSuccess(response.body().getMessage());
                }else {
                    callback.onError("Menghapus data gagal");
                }
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable throwable) {
                Log.e(TAG, "Error API Call : " + throwable.getMessage());
            }
        });
    }

    public interface messageCallback {
        void onSuccess(String message);
        void onError(String error);
    }

}
