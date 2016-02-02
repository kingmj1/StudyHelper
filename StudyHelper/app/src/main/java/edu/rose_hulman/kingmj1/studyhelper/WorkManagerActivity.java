package edu.rose_hulman.kingmj1.studyhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class WorkManagerActivity extends AppCompatActivity implements TaskAdapter.TaskCallback {

    private TaskAdapter mTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.work_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter(this, recyclerView, this, null);
        recyclerView.setAdapter(mTaskAdapter);
    }

    @Override
    public void onEdit(Task task) {

    }
}
