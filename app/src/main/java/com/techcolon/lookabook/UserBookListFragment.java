package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class UserBookListFragment extends Fragment {

    private Button signupBtn;
    private TextView loginBtn;
    private RecyclerView rcv;
    private BookAdapter mAdapter;
    LinearLayout rcvId,noBooks;
    public UserBookListFragment() {
        // Required empty public constructor
    }


    public static UserBookListFragment newInstance(String param1, String param2) {
        UserBookListFragment fragment = new UserBookListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (User.getUser()!=null && User.getNoOfBooks() != 0) {
            mAdapter.addNewData(User.getUserBooks());
            mAdapter.notifyDataSetChanged();
            noBooks.setVisibility(View.GONE);
            rcvId.setVisibility(View.VISIBLE);
        } else {
            if(User.getUser()!=null) {
                rcvId.setVisibility(View.GONE);
                noBooks.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v;
        if (User.getUser() != null) {
            //user is not null(logged in)
            v = inflater.inflate(R.layout.fragment_user_book_list, container, false);
            rcvId = v.findViewById(R.id.rcvid);
            noBooks = v.findViewById(R.id.nobooks);
            rcv = v.findViewById(R.id.recyclerviewbook);

            mAdapter = new BookAdapter(getContext(), 1);
            rcv.setAdapter(mAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            rcv.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv.getContext(),
                    layoutManager.getOrientation());
            rcv.addItemDecoration(dividerItemDecoration);


            if (User.getUserBooks().size() == 0) {
                rcvId.setVisibility(View.GONE);
            } else {
                noBooks.setVisibility(View.GONE);
                mAdapter.addData(User.getUserBooks());
            }


        } else {
            //if user null(not logged in)
            v = inflater.inflate(R.layout.fragment_profile_nouser, container, false);
            loginBtn = v.findViewById(R.id.login);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    Navigation.findNavController(container).navigate(R.id.bookListFragment);
                }
            });

            // SignUp button working
            signupBtn = v.findViewById(R.id.signup);
            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), SignupActivity.class);
                    startActivity(i);
                    Navigation.findNavController(container).navigate(R.id.bookListFragment);
                }
            });
        }


        return v;

    }
}

