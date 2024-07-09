package id.co.sigma.mewing.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.sigma.mewing.API.Repository.UserRepository;
import id.co.sigma.mewing.API.VO.UserListVO;
import id.co.sigma.mewing.Model.UserModel;
import id.co.sigma.mewing.R;
import id.co.sigma.mewing.ViewModel.UserViewModel;

public class ListUserFragment extends Fragment {

    private ListUserAdapter userAdapter;
    private RecyclerView userRecyclerView;
    private UserViewModel mUserViewModel;

    public ListUserFragment() {
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
            navigateToAddUser();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepository.initialize(requireContext());
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setHasOptionsMenu(true);

        // Load initial user data
        mUserViewModel.getAllUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user, container, false);

        userRecyclerView = view.findViewById(R.id.list_data_user);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userAdapter = new ListUserAdapter();
        userRecyclerView.setAdapter(userAdapter);

        userAdapter.setOnItemClickListener(user -> navigateToUpdateUser(user));

        mUserViewModel.getAllUser();
        mUserViewModel.getAllUserResponse().observe(getViewLifecycleOwner(), new Observer<UserListVO>() {
            @Override
            public void onChanged(UserListVO userListVO) {
                if (userListVO != null) {
                    userAdapter.setUserList(userListVO.getData());
                } else {
                    userAdapter.setUserList(new ArrayList<>());
                }
            }
        });

        return view;
    }

    private class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ListUserHolder> {

        private List<UserModel> userList = new ArrayList<>();
        private OnItemClickListener onItemClickListener;

        @NonNull
        @Override
        public ListUserAdapter.ListUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list_user, parent, false);
            return new ListUserHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListUserAdapter.ListUserHolder holder, int position) {
            UserModel user = userList.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public void setUserList(List<UserModel> userList) {
            this.userList.clear();
            this.userList.addAll(userList);
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public class ListUserHolder extends RecyclerView.ViewHolder {

            private TextView mTxtUsername, mTxtNama, mTxtJabatan;

            public ListUserHolder(@NonNull View itemView) {
                super(itemView);
                mTxtUsername = itemView.findViewById(R.id.txt_username);
                mTxtNama = itemView.findViewById(R.id.txt_nama_user);
                mTxtJabatan = itemView.findViewById(R.id.txt_jabatan);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(userList.get(getAdapterPosition()));
                        }
                    }
                });
            }

            public void bind(UserModel user) {
                mTxtUsername.setText("Username : " + user.getUsername());
                mTxtNama.setText("Nama : " + user.getNama());
                mTxtJabatan.setText(user.getJabatan());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(UserModel user);
    }

    private void navigateToAddUser() {
        Fragment addFragment = new CreateFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, addFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Modifikasi di navigateToUpdateUser
    private void navigateToUpdateUser(UserModel user) {
        Fragment updateFragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        updateFragment.setArguments(args);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, updateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
