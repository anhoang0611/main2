package com.example.customnavigationdrawer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.customnavigationdrawer.R;
import com.example.customnavigationdrawer.User;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.type.DateOrBuilder;
import com.google.type.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class HistoryFragment extends Fragment {

    View mView;

    Button btnBack;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    User user;
    //    ArrayList<String> list;
//   // ArrayList<String> listTemp;
//    ArrayAdapter<String> adapter;
    int sum = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        user = new User();
        String DeviceID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        Calendar calendar = Calendar.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User/"+DeviceID+"/quet");



        TextView tvSum = mView.findViewById(R.id.tv_sum);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    sum = (int) snapshot.getChildrenCount();
                    tvSum.setText("Tổng số lượt tiếp xúc: " + Integer.toString(sum) + " người");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
                Query oldBug = ref.orderByChild("timestamp").endAt(cutoff);



        oldBug.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });






        return mView;
    }
}
