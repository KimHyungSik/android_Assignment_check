package com.example.assignment_check;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_check.interface_.OnDialogListener;
import com.example.assignment_check.interface_.OnPersonItemClickListener;

import java.util.List;

public class Note_Adapter extends RecyclerView.Adapter<Note_Adapter.Note_Holder>
        implements OnPersonItemClickListener, OnDialogListener{
    List<Note_Data> note_data;
    OnPersonItemClickListener listener;
    Context context;

    public static class Note_Holder extends RecyclerView.ViewHolder
                                 {
        public TextView Title_Text;
        public TextView Sub_Title_Text;
        public TextView Content_Text;

        public Note_Holder(View v, final OnPersonItemClickListener listener){
            super(v);
            Title_Text = v.findViewById(R.id.note_title);
            Sub_Title_Text = v.findViewById(R.id.note_subtitle);
            Content_Text = v.findViewById(R.id.note_content);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(Note_Holder.this, v, position);
                    }
                }
            });

            v.setClickable(true);
            v.setEnabled(true);
        }

    }


    public void setOnClickListener(OnPersonItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onFinish(int position, Note_Data note_data) {
        this.note_data.set(position, note_data);
        notifyItemChanged(position);

    }

    @Override
    public void onItemClick(Note_Holder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);

         CustomDialog dialog = new CustomDialog(context, position, note_data.get(position));

         DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
         int width = dm.widthPixels; int height = dm.heightPixels;

         //다이얼로그 사이즈 세팅
         WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
         wm.copyFrom(dialog.getWindow().getAttributes());
         wm.width =  width;
         wm.height = height;

         //다이얼로그 Listener 세팅
         dialog.setDialogListener(this);

         //다이얼로그 띄우기
         dialog.show();
        }
    }

    //리싸이클뷰 어뎁터 호출 함수 정리
    public Note_Adapter(List<Note_Data> mNote, Context context){
        note_data = mNote;
        this.context = context;

    }

    @Override
    public Note_Adapter.Note_Holder onCreateViewHolder( ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.note_detail, parent, false);
        Note_Holder vh = new Note_Holder(v, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(Note_Holder holder, int position) {
        //리싸이클 뷰 들어갈 아이템 설정
        Note_Data myNote = note_data.get(position);
        holder.Title_Text.setText(myNote.getTitle());
        holder.Sub_Title_Text.setText(myNote.getSub_title());
        holder.Content_Text.setText(myNote.getContent());
    }

    public Note_Data getItem(int position){
        return note_data.get(position);
    }

    public void setItem(int position, Note_Data item){
        note_data.set(position,item);
    }

    @Override
    public int getItemCount() {
        return note_data == null ? 0 :note_data.size();
    }

    public Note_Data getNote(int position){
        return note_data == null ? null : note_data.get(position);
    }
}
