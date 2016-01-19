package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by reynolpt on 1/18/2016.
 */
public class ClassmatesAdapter extends RecyclerView.Adapter<ClassmatesAdapter.ClassmatesViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<Classmates> mClassmates = new ArrayList<>();

    public void ClassmatesAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public ClassmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ClassmatesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mClassmates.size();
    }

    class ClassmatesViewHolder extends RecyclerView.ViewHolder {
        public ClassmatesViewHolder(View itemView) {
            super(itemView);
        }
    }

}
