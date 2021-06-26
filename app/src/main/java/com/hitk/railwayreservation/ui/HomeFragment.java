package com.hitk.railwayreservation.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hitk.railwayreservation.R;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";

    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayoutAdmin1;
    LinearLayout linearLayoutAdmin2;
    CardView search;
    CardView book;
    CardView viewTicket;
    CardView balance;
    CardView addTrain;
    CardView removeTrain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("HOME");
        linearLayout1 = view.findViewById(R.id.linearLayout1);
        linearLayout2=view.findViewById(R.id.linearLayout2);
        linearLayoutAdmin1=view.findViewById(R.id.linearLayoutAdmin1);
        //linearLayoutAdmin2=view.findViewById(R.id.linearLayoutAdmin2);

        search=view.findViewById(R.id.cd_Search);
        book = view.findViewById(R.id.cd_Book);
        viewTicket=view.findViewById(R.id.cd_viewTickets);
        balance=view.findViewById(R.id.cd_Balance);
        addTrain=view.findViewById(R.id.cd_AddTrain);
        removeTrain=view.findViewById(R.id.cd_RemoveTrain);

        search.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment));

        book.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_bookFragment));

        viewTicket.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_viewFragment));

        balance.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_balanceFragment));

        addTrain.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addTrainFragment));

        removeTrain.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_removeTrainFragment));

    }

}