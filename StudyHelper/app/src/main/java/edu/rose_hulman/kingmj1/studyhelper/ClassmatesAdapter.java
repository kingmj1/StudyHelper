package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by reynolpt on 1/18/2016.
 */
public class ClassmatesAdapter extends RecyclerView.Adapter<ClassmatesAdapter.ClassmatesViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<Classmates> mClassmates = new ArrayList<>();

    public ClassmatesAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public ClassmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.classmate_view, parent, false);
        return new ClassmatesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassmatesViewHolder holder, int position) {
        Classmates classmate = mClassmates.get(position);
        holder.classmateNameView.setText(classmate.getName());
    }

    @Override
    public int getItemCount() {
        return mClassmates.size();
    }

    class ClassmatesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView classmateNameView;

        public ClassmatesViewHolder(View itemView) {
            super(itemView);
            classmateNameView = (TextView) itemView.findViewById(R.id.classmate_name_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: Launch the email Classmate Dialog
        }
    }

}
