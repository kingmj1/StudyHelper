package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
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
import com.firebase.client.Query;

import java.util.ArrayList;

/**
 * Created by reynolpt on 1/18/2016.
 */
public class ClassmatesAdapter extends RecyclerView.Adapter<ClassmatesAdapter.ClassmatesViewHolder> {

    private static final String FIREBASE_REPO = "study-helper-rose";
    private static final String FIREBASE_URL = "https://" + FIREBASE_REPO + ".firebaseio.com";
    private static final String CLASSMATES_PATH = FIREBASE_URL + "/classmates";

    private Firebase mClassmatesRef;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<Classmate> mClassmates = new ArrayList<>();
    private ClassmatesCallback mCallback;

    public ClassmatesAdapter(Context context, RecyclerView recyclerView, ClassmatesCallback callback, String courseKey) {
        mContext = context;
        mRecyclerView = recyclerView;
        mClassmatesRef = new Firebase(CLASSMATES_PATH);
        mClassmatesRef.keepSynced(true);
        Query classmatesQuery = mClassmatesRef.orderByChild(Classmate.COURSE_KEY).equalTo(courseKey);
        classmatesQuery.addChildEventListener(new ClassmatesChildEventListener());
        mCallback = callback;
    }

    @Override
    public ClassmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.classmate_view, parent, false);
        return new ClassmatesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassmatesViewHolder holder, int position) {
        Classmate classmate = mClassmates.get(position);
        holder.classmateNameView.setText(classmate.getName());
        holder.classmateEmailView.setText(classmate.getEmailAddress());
    }

    @Override
    public int getItemCount() {
        return mClassmates.size();
    }

    public void add(Classmate newClassmate) {
        mClassmatesRef.push().setValue(newClassmate);
    }

    public void update(Classmate classmate, String newName, String newEmailAddress) {
        classmate.setName(newName);
        classmate.setEmailAddress(newEmailAddress);
        mClassmatesRef.child(classmate.getKey()).setValue(classmate);
    }

    public void remove(Classmate classmate) {
        mClassmatesRef.child(classmate.getKey()).removeValue();
    }

    class ClassmatesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView classmateNameView;
        private TextView classmateEmailView;

        public ClassmatesViewHolder(View itemView) {
            super(itemView);
            classmateNameView = (TextView) itemView.findViewById(R.id.classmate_name_text);
            classmateEmailView = (TextView) itemView.findViewById(R.id.classmate_email_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: Launch the email Classmate Dialog
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            mCallback.onClassmateEdit(mClassmates.get(position));
            return true;
        }
    }

    class ClassmatesChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Classmate classmate = dataSnapshot.getValue(Classmate.class);
            classmate.setKey(dataSnapshot.getKey());
            mClassmates.add(0, classmate);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Classmate newClassmate = dataSnapshot.getValue(Classmate.class);
            for (Classmate c : mClassmates) {
                if(c.getKey().equals(key)) {
                    c.setValues(newClassmate);
                    notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Classmate c : mClassmates) {
                if (key.equals(c.getKey())) {
                    mClassmates.remove(c);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //not used
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("SH", firebaseError.getMessage());
        }
    }

    public interface ClassmatesCallback {
        void onClassmateEdit(Classmate classmate);
    }

}
