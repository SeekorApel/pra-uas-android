package id.co.sigma.mewing.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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

import id.co.sigma.mewing.Model.UserModel;
import id.co.sigma.mewing.R;
import id.co.sigma.mewing.ViewModel.UserViewModel;

public class UpdateFragment extends Fragment {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtNama;
    private EditText edtJabatan;
    private Button btnUpdate;
    private Button btnDelete;
    private UserViewModel mUserViewModel;
    private UserModel currentUser;

    public UpdateFragment() {
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
        if (itemId == R.id.btn_new_user) {
            // Handle item selection
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        //edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);
        edtNama = view.findViewById(R.id.edt_nama);
        edtJabatan = view.findViewById(R.id.edt_jabatan);
        btnUpdate = view.findViewById(R.id.btn_tambah);
        btnDelete = view.findViewById(R.id.btn_hapus);


        if (getArguments() != null) {
            currentUser = getArguments().getParcelable("user");
            if (currentUser != null) {
                //edtUsername.setText(currentUser.getUsername());
                edtPassword.setText(currentUser.getPassword());
                edtNama.setText(currentUser.getNama());
                edtJabatan.setText(currentUser.getJabatan());
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        // OnClickListener untuk Tombol Hapus
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        return view;
    }

    private void deleteUser() {
        if (currentUser != null) {
            mUserViewModel.deleteUser(currentUser.getUsername());

            mUserViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                navigateToListUser();
            });

            mUserViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            });
        }
    }

    private void updateUser() {
        String password = edtPassword.getText().toString();
        String nama = edtNama.getText().toString();
        String jabatan = edtJabatan.getText().toString();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(nama) || TextUtils.isEmpty(jabatan)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setPassword(password);
        currentUser.setNama(nama);
        currentUser.setJabatan(jabatan);

        mUserViewModel.updateUser(currentUser);

        mUserViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            navigateToListUser();
        });

        mUserViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });
    }

    private void navigateToListUser(){
        Fragment listFragment = new ListUserFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, listFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
