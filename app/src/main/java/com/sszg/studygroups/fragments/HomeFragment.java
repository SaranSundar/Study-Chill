package com.sszg.studygroups.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sszg.studygroups.R;
import com.sszg.studygroups.data.Subject;
import com.sszg.studygroups.data.SubjectAdapter;

import static android.support.constraint.Constraints.TAG;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        intializeRecyclerView(view);
        populateList();
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
        db = FirebaseFirestore.getInstance();
        db.collection("studyrooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Subject subject = document.toObject(Subject.class);
                                subjectAdapter.addSubject(subject);
                                subjectAdapter.notifyDataSetChanged();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                System.out.println("SUBJECT DATE IS " + subject.getTimestamp().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
