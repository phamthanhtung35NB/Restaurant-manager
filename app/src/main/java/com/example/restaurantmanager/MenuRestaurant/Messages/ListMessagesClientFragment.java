package com.example.restaurantmanager.MenuRestaurant.Messages;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.restaurantmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListMessagesClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ListMessagesClientFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_messages_client, container, false);
        init(view);
        addEvents(view);
        return view;
    }
    void init(View view){
        System.out.println("đầu init");
    }
    void addEvents(View view){

    }
}