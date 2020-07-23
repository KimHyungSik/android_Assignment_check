package com.example.assignment_check;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.assignment_check.interface_.OnPersonItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Note_Adapter note_Adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context mcontext;

    private static ArrayList<Note_Data> Lnote_data = new ArrayList<>();
    private Note_Data note_data = new Note_Data();

    private FloatingActionButton add_note;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private static boolean First = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.main_recycler_view);
        mcontext = this;
        //리싸이클뷰 설정
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        //fab버튼으로 리사이클뷰 추가
        add_note = findViewById(R.id.add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Input_Note.class);
                startActivity(intent);
            }
        });
        if(First == true)
        {
            String save_note = PreferenceManager.getString(mcontext, "Note_Save_Data");
            jsonParsing(save_note);

            setAdapter(Lnote_data);

            First=false;
        }
        Note_Data input_note = (Note_Data)intent.getSerializableExtra("input_note");
        if(input_note != null){
            mkNote(input_note);
        }

    }

    //앱이 완전이 죽을때 호출 함수
    @Override
    protected void onDestroy() {
        PreferenceManager.clear(mcontext);
        PreferenceManager.setString(mcontext, "Note_Save_Data", trJson());
        super.onDestroy();
    }
    //뒤로가기 누른후 재시작시 호출 함수
    @Override
    protected void onResume() {
        setAdapter(Lnote_data);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        setAdapter(Lnote_data);
        super.onRestart();
    }
    @Override
    public void onBackPressed() {

    }    //뒤로가기 누를때 호출 함수


    //키다운 확인
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK :   finishAffinity();
            case KeyEvent.KEYCODE_APP_SWITCH: finishAffinity(); break;
        }
        return super.onKeyDown(keyCode, event);
    }
    //홈 키다운 확인
    @Override
    protected void onUserLeaveHint() {
        finishAffinity();
        Log.d("tlqkf","error tlqkf");
        super.onUserLeaveHint();
    }

    //노트 만들기
    public void mkNote(Note_Data inptut_note_data){//리사이클뷰 아이템 추가
        note_data.setTitle(inptut_note_data.getTitle());
        note_data.setSub_title(inptut_note_data.getSub_title());
        note_data.setContent(inptut_note_data.getContent());

        Lnote_data.add(note_data);

        setAdapter(Lnote_data);
    }
    //어뎁터
    private void setAdapter(ArrayList<Note_Data> note_data){
        note_Adapter = new Note_Adapter(note_data, MainActivity.this);

        note_Adapter.setOnClickListener(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(Note_Adapter.Note_Holder holder, View view, int position) {
            }
        });

        recyclerView.setAdapter(note_Adapter);
    }
    //Lnote_data를 Json으로 변경
    private String trJson(){
        JSONObject obj = new JSONObject();
        try{
            JSONArray jArray = new JSONArray();
            for(int i = 0; i< Lnote_data.size();i++){
                JSONObject sObject= new JSONObject();
                sObject.put("title", Lnote_data.get(i).getTitle());
                sObject.put("sub_title", Lnote_data.get(i).getSub_title());
                sObject.put("content", Lnote_data.get(i).getContent());

                jArray.put(sObject);
            }
            obj.put("note_data", jArray);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
    //Json을 Lnote_data로 변경
    private void jsonParsing(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jArray = jsonObject.getJSONArray("note_data");

            for(int i=0; i<jArray.length(); i++)
            {
                JSONObject movieObject = jArray.getJSONObject(i);

                Note_Data note_data = new Note_Data();

                note_data.setTitle(movieObject.getString("title"));
                note_data.setSub_title(movieObject.getString("sub_title"));
                note_data.setContent(movieObject.getString("content"));

                Lnote_data.add(note_data);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.datbase_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.get_data:
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("Note_Save_Data");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Note_Data snpshot = snapshot.getValue(Note_Data.class);
                            Lnote_data.add(snpshot);
                        }
                        setAdapter(Lnote_data);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
               break;
        }
        return super.onOptionsItemSelected(item);
    }

    //swipe 기능 구현
    ItemTouchHelper.Callback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    Lnote_data.remove(viewHolder.getAdapterPosition());
                    note_Adapter.notifyDataSetChanged();
                }
            };
}
