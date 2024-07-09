package id.co.sigma.mewing.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.co.sigma.mewing.API.Repository.UserRepository;
import id.co.sigma.mewing.API.VO.UserListVO;
import id.co.sigma.mewing.API.VO.UserVO;
import id.co.sigma.mewing.Model.UserModel;

public class UserViewModel extends ViewModel {

    private static final String TAG = "UserViewModel";

    private MutableLiveData<UserListVO> getAllUserResponse = new MutableLiveData<>();
    private MutableLiveData<UserVO> getUserLoginResponse = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final UserRepository mUserRepository;

    public UserViewModel (){
        mUserRepository = UserRepository.get();
    }

    public LiveData<UserListVO> getAllUserResponse(){
        return getAllUserResponse;
    }

    public LiveData<UserVO> getLoginResponse(){
        return getUserLoginResponse;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void getAllUser(){
        Log.i(TAG, "getAllUser() called");
        getAllUserResponse = mUserRepository.getAllUser();
    }

    public void getUserLogin(String username, String password){
        Log.i(TAG, "getUserLogin() called");
        getUserLoginResponse = mUserRepository.getDatalogin(username, password);
    }

    public void saveUser(UserModel user){
        Log.i(TAG, "saveUser() called");
        mUserRepository.saveUser(user, new UserRepository.messageCallback() {
            @Override
            public void onSuccess(String message) {
                successMessage.postValue(message);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    public void updateUser(UserModel updateUser){
        Log.i(TAG, "updateUser() called");
        mUserRepository.updateUser(updateUser, new UserRepository.messageCallback() {
            @Override
            public void onSuccess(String message) {
                successMessage.postValue(message);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    public void deleteUser(String username){
        Log.i(TAG, "deleteUser() called");
        mUserRepository.deleteUser(username, new UserRepository.messageCallback() {
            @Override
            public void onSuccess(String message) {
                successMessage.postValue(message);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

}
