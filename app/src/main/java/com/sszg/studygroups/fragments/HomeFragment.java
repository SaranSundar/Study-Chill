package com.sszg.studygroups.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sszg.studygroups.R;
import com.sszg.studygroups.data.SubjectAdapter;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        intializeRecyclerView(view);
        return view;
    }

    private void intializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        subjectAdapter = new SubjectAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subjectAdapter);
    }

    private void populateList() {
        //Fill subject list
        //subjectAdapter.addSubject(null);
        //subjectAdapter.notifyDataSetChanged();
    }
}
