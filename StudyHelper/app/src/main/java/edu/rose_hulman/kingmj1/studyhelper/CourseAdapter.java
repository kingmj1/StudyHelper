package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context mContext;
    private ArrayList<Course> mCourses = new ArrayList<>();
    private RecyclerView mRecyclerView;


    public CourseAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        createTestCourses();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = mCourses.get(position);
        holder.courseNameView.setText(course.getName());
        int courseCount = course.getTaskAdapter().getTaskCount();
        holder.courseCountView.setText(mContext.getResources().getQuantityString(R.plurals.task_count_text, courseCount, courseCount));
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView courseNameView;
        private TextView courseCountView;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseNameView = (TextView)itemView.findViewById(R.id.course_name_text);
            courseCountView = (TextView)itemView.findViewById(R.id.course_count_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: Launch Task RecyclerView
        }
    }

    private void createTestCourses() {
        mCourses.add(new Course("MA223- Engineering Statistics"));
        mCourses.add(new Course("ES205- ADES"));
    }
}
