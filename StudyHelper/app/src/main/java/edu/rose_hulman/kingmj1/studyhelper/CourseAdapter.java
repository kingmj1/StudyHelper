package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    public static final String COURSE_KEY_EXTRA_KEY = "course_key";

    private static final String FIREBASE_REPO = "study-helper-rose";
    private static final String FIREBASE_URL = "https://" + FIREBASE_REPO + ".firebaseio.com";
    private static final String COURSES_PATH = FIREBASE_URL + "/courses";

    private Context mContext;
    private CourseCallback mCallback;
    private ArrayList<Course> mCourses = new ArrayList<>();
    private RecyclerView mRecyclerView;

    private Firebase mCourseRef;

    public CourseAdapter(Context context, CourseCallback callback, RecyclerView recyclerView) {
        mContext = context;
        mCallback = callback;
        mRecyclerView = recyclerView;
        mCourseRef = new Firebase(COURSES_PATH);
        mCourseRef.keepSynced(true);
        mCourseRef.addChildEventListener(new CourseChildListener());
        //createTestCourses();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        final Course course = mCourses.get(position);
        holder.courseNameView.setText(course.getName());
        //int courseCount = course.getTaskAdapter().getItemCount();
        int courseCount = 0;
        holder.courseCountView.setText(mContext.getResources().getQuantityString(R.plurals.task_count_text, courseCount, courseCount));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tasksIntent = new Intent(mContext, TaskActivity.class);
                tasksIntent.putExtra(COURSE_KEY_EXTRA_KEY, course.getKey());
                mContext.startActivity(tasksIntent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCallback.onCourseEdit(course);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView courseNameView;
        private TextView courseCountView;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseNameView = (TextView)itemView.findViewById(R.id.course_name_text);
            courseCountView = (TextView)itemView.findViewById(R.id.course_count_text);
        }
    }

    private void createTestCourses() {
        mCourses.add(new Course("MA223- Engineering Statistics"));
        mCourses.add(new Course("ES205- ADES"));
    }

    public void add(Course newCourse) {
        mCourseRef.push().setValue(newCourse);
    }

    public void update(Course course, String newName) {
        course.setName(newName);
        mCourseRef.child(course.getKey()).setValue(course);
    }

    public void remove(Course course) {
        mCourseRef.child(course.getKey()).removeValue();
    }

    class CourseChildListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Course course = dataSnapshot.getValue(Course.class);
            course.setKey(dataSnapshot.getKey());
            mCourses.add(0, course);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Course newCourse = dataSnapshot.getValue(Course.class);
            for (Course c : mCourses) {
                if(c.getKey().equals(key)) {
                    c.setValues(newCourse);
                    notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Course c : mCourses) {
                if (key.equals(c.getKey())) {
                    mCourses.remove(c);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //Not needed
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("Course", firebaseError.getMessage());
        }
    }

    public interface CourseCallback {
        void onCourseEdit(Course course);
    }
}
