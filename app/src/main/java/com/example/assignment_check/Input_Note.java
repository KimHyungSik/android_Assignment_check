package com.example.assignment_check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Input_Note extends AppCompatActivity {
    private EditText iput_title;
    private EditText iput_subtitle;
    private EditText iput_content;

    private FloatingActionButton check_fab;

    private Note_Data note_data = new Note_Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input__note);

        iput_title = findViewById(R.id.input_title);
        iput_subtitle = findViewById(R.id.input_subtitle);
        iput_content = findViewById(R.id.input_content);

        check_fab = findViewById(R.id.check_note);
        check_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFab();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("input_note", note_data);
                startActivity(intent);
            }
        });
    }

    public void onClickFab(){
        note_data.setTitle(iput_title.getText().toString());
        note_data.setSub_title(iput_subtitle.getText().toString());
        note_data.setContent(iput_content.getText().toString());
    }


}
