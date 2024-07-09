package id.co.sigma.mewing.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.sigma.mewing.API.Repository.UserRepository;
import id.co.sigma.mewing.API.VO.UserListVO;
import id.co.sigma.mewing.Model.UserModel;
import id.co.sigma.mewing.R;
import id.co.sigma.mewing.ViewModel.UserViewModel;

public class CreateFragment extends Fragment {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtNama;
    private EditText edtJabatan;
    private Button btnTambah;
    private UserViewModel mUserViewModel;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.btn_new_user){
            //navigateToAddUser();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepository.initialize(requireContext());
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);
        edtNama = view.findViewById(R.id.edt_nama);
        edtJabatan = view.findViewById(R.id.edt_jabatan);
        btnTambah = view.findViewById(R.id.btn_tambah);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String nama = edtNama.getText().toString().trim();
                String jabatan = edtJabatan.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nama) || TextUtils.isEmpty(jabatan)) {
                    Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserModel createUser = new UserModel();
                createUser.setUsername(username);
                createUser.setPassword(password);
                createUser.setNama(nama);
                createUser.setJabatan(jabatan);

                mUserViewModel.saveUser(createUser);

                mUserViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    navigateToListUser();
                });

                mUserViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                });
            }
        });

        return view;
    }
    private void navigateToListUser(){
        Fragment listFragment = new ListUserFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, listFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}