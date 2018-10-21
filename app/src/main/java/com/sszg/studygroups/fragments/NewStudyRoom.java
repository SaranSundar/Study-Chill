package com.sszg.studygroups.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sszg.studygroups.R;
import com.sszg.studygroups.data.Subject;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;


public class NewStudyRoom extends Fragment {
    private TextView locationText;
    private ImageView profileURL;
    private Button create, autofill;
    private EditText professorName, courseName, roomNumber, time;
    private ImageButton locationButton;
    private FusedLocationProviderClient client;
    private FirebaseFirestore db;
    private double lastLong = -999;
    private double lastLat = -999;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_study_room, null);
        locationButton = view.findViewById(R.id.location_button);
        locationText = view.findViewById(R.id.location_text);
        professorName = view.findViewById(R.id.professor_name);
        courseName = view.findViewById(R.id.course_name);
        roomNumber = view.findViewById(R.id.room_number);
        time = view.findViewById(R.id.time);
        profileURL = view.findViewById(R.id.profile_url);
        autofill = view.findViewById(R.id.autofill);
        create = view.findViewById(R.id.create);
        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGPSCoords();
            }
        });
        final Context context = getContext();
        final Fragment thisFragment = this;
        profileURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(context, thisFragment);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                createStudyRoom(v);
            }
        });
        autofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professorName.setText(String.valueOf("Martin Ashwell"));
                courseName.setText(String.valueOf("Home Economics"));
                roomNumber.setText(String.valueOf("JO 2.34"));
                time.setText(String.valueOf("9AM-11AM"));
            }
        });
    }

    public void getGPSCoords() {
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "GPS Permissions need to be enabled", Toast.LENGTH_SHORT).show();
            return;
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lastLat = location.getLatitude();
                    lastLong = location.getLongitude();
                    locationText.setText(String.valueOf(" Lat: " + location.getLatitude() + " Long: " + location.getLongitude()));
                } else {
                    Toast.makeText(getActivity(), "Not able to get GPS Coordinates", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createStudyRoom(final View v) {
        profileURL.buildDrawingCache();
        Bitmap bitmap = profileURL.getDrawingCache();
        String base64URI = getEncoded64ImageStringFromBitmap(bitmap);
        String courseName = this.courseName.getText().toString();
        String profesorName = this.professorName.getText().toString();
        String roomNumber = this.roomNumber.getText().toString();
        String time = this.time.getText().toString();
        final Subject subject = new Subject(profesorName, courseName, roomNumber, time, base64URI);
        subject.setLongitude(lastLong);
        subject.setLatitude(lastLat);
        db.collection("studyrooms").add(subject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                subject.setUID(documentReference.getId());
                DocumentReference docRef = db.collection("studyrooms").document(documentReference.getId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("timestamp", FieldValue.serverTimestamp());
                docRef.update(updates);
                docRef.set(subject).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Created New Study Room", Toast.LENGTH_SHORT).show();
                        v.setEnabled(true);
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error Creating New Study Room", Toast.LENGTH_SHORT).show();
                        System.out.println("FAILED CAUSE: " + e.toString());
                        v.setEnabled(true);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error Creating New Study Room", Toast.LENGTH_SHORT).show();
                System.out.println("FAILED CAUSE: " + e.toString());
                v.setEnabled(true);
            }
        });
    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                profileURL.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                System.out.println(error.toString());
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

}
