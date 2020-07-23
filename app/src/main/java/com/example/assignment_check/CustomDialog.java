package com.example.assignment_check;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.assignment_check.interface_.OnDialogListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomDialog extends Dialog {
    private OnDialogListener listener;
    private Button mod_bt, push_data, delet_data;
    private EditText modify_title, modify_sub_title, modify_content;
    private String title, sub_title, content;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Context context;

    public CustomDialog(final Context context, final int position, Note_Data note_data)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.customdialog);
        title = note_data.getTitle();
        sub_title = note_data.getSub_title();
        content = note_data.getContent();
        this.context = context;

        //이름, 나이 EditText에 값 채우기
        modify_title = findViewById(R.id.modify_title);
        modify_title.setText(title);

        modify_sub_title = findViewById(R.id.modify_subtitle);
        modify_sub_title.setText(sub_title);

        modify_content = findViewById(R.id.modify_content);
        modify_content.setText(content);

        mod_bt = findViewById(R.id.mod_bt);
        mod_bt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { if(listener!=null)
            { //EditText의 수정된 값 가져오기
                String title = modify_title.getText().toString();
                String sub_title = modify_sub_title.getText().toString();
                String content = modify_content.getText().toString();

                Note_Data note_ = new Note_Data();

                note_.setTitle(title);
                note_.setSub_title(sub_title);
                note_.setContent(content);

                //Listener를 통해서 person객체 전달
                listener.onFinish(position, note_);
                //다이얼로그 종료
                dismiss();
            }
            }
        });

        push_data = findViewById(R.id.push_data);
        push_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Note_Save_Data").child(title).child("title").setValue(title);
                databaseReference.child("Note_Save_Data").child(title).child("sub_title").setValue(sub_title);
                databaseReference.child("Note_Save_Data").child(title).child("content").setValue(content);
                Toast.makeText(context, title + "데이터 저장", Toast.LENGTH_SHORT).show();
            }
        });

        delet_data = findViewById(R.id.delet_data);
        delet_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Note_Save_Data").child(title).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, title+"삭제", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, title+"삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }public void setDialogListener(OnDialogListener listener){
        this.listener = listener; }
}
