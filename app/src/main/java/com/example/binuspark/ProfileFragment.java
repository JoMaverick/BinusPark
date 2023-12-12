package com.example.binuspark;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class ProfileFragment extends Fragment {
    ImageButton backBtn;
    EditText name;
    EditText email;
    EditText phone;
    EditText password;
    Button updateProfileBtn;
    TextView keluar;

    UserModel currentUserModel;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        name = view.findViewById(R.id.full_name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone_number);
        password = view.findViewById(R.id.password);
        backBtn = view.findViewById(R.id.back);
        updateProfileBtn = view.findViewById(R.id.profile_update);
        keluar = view.findViewById(R.id.logout_button);

        getUserData();

        updateProfileBtn.setOnClickListener(v->{
            editBtnClick();
        });

        keluar.setOnClickListener(v->{
            FirebaseUtil.logout();
            Intent intent = new Intent(getContext(), Register.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;

    }

    void editBtnClick(){
        String newFullName = name.getText().toString();
        if(newFullName.isEmpty()||newFullName.length()<2){
            name.setError("Full name must be at least 2 characters");
            return;
        }
        currentUserModel.setFullname(newFullName);

        updateToFirebase();
    }

    void updateToFirebase(){
        FirebaseUtil.currentUserDetails().set(currentUserModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                AndroidUtil.showToast(getContext(),"Updated successfully");
            }else{
                AndroidUtil.showToast(getContext(),"Edit failed");
            }
        });

    }

    void getUserData(){
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                currentUserModel = task.getResult().toObject(UserModel.class);
                name.setText(currentUserModel.getFullname());
                email.setText(currentUserModel.getEmail());
                phone.setText(currentUserModel.getPhonenum());

            }
        });
    }


}