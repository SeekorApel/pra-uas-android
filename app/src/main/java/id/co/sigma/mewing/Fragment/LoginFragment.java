package id.co.sigma.mewing.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.co.sigma.mewing.API.Repository.UserRepository;
import id.co.sigma.mewing.API.VO.UserVO;
import id.co.sigma.mewing.Model.UserModel;
import id.co.sigma.mewing.R;
import id.co.sigma.mewing.ViewModel.UserViewModel;

public class LoginFragment extends Fragment {

    private final String TAG = "LoginFragment";

    private EditText mEdtUsername, mEdtPassword;

    private Button mBtnLogin;

    private UserViewModel mUserViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepository.initialize(requireContext());
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEdtUsername = view.findViewById(R.id.edt_username);
        mEdtPassword = view.findViewById(R.id.edt_password);
        mBtnLogin = view.findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEdtUsername.getText().toString().trim();
                String password = mEdtPassword.getText().toString().trim();

                mUserViewModel.getUserLogin(username, password);
                mUserViewModel.getLoginResponse().observe(getViewLifecycleOwner(), new Observer<UserVO>() {
                    @Override
                    public void onChanged(UserVO userVO) {
                        if (userVO != null && userVO.getData() != null) {
                            if ("Customer".equals(userVO.getData().getJabatan())) {
                                Toast.makeText(getContext(), "Anda tidak memiliki akses", Toast.LENGTH_SHORT).show();
                            } else if ("Admin".equals(userVO.getData().getJabatan())) {
                                navigateToList();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Login salah, Akun Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        return view;
    }

    private void navigateToList(){
        Fragment listUserFragment = new ListUserFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, listUserFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}