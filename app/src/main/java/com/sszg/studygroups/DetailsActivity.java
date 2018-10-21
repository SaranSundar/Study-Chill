package com.sszg.studygroups;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sszg.studygroups.data.Subject;

public class DetailsActivity extends AppCompatActivity {
    private TextView locationText;
    private ImageView profileURL;
    private Button returnBack;
    private TextView professorName, courseName, roomNumber, time;
    private ImageButton locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        locationButton = findViewById(R.id.location_button);
        locationText = findViewById(R.id.location_text);
        professorName = findViewById(R.id.professor_name);
        courseName = findViewById(R.id.course_name);
        roomNumber = findViewById(R.id.room_number);
        time = findViewById(R.id.time);
        profileURL = findViewById(R.id.profile_url);
        returnBack = findViewById(R.id.return_back);
        Subject subject = (Subject) getIntent().getSerializableExtra("serialize_data");
        fillSubject(subject);
    }

    public Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void fillSubject(final Subject subject) {
        locationText.setText(String.valueOf(" Lat: " + subject.getLatitude() + " Long: " + subject.getLongitude()));
        professorName.setText(subject.getProfessorName());
        courseName.setText(subject.getCourseName());
        roomNumber.setText(subject.getRoomNumber());
        time.setText(subject.getTime());
        Bitmap image = base64ToBitmap(subject.getProfileURL());
        profileURL.setImageBitmap(image);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse(String.valueOf("google.navigation:q=" + subject.getLatitude() + "," + subject.getLongitude() + "&mode=w"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }
}
