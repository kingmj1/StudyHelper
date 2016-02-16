package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

public class CourseActivity extends AppCompatActivity
        implements CourseAdapter.CourseCallback {

    private CourseAdapter mCourseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog(null);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mCourseAdapter = new CourseAdapter(this, this, recyclerView);
        recyclerView.setAdapter(mCourseAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_work) {
            Intent workModeIntent = new Intent(this, WorkManagerActivity.class);
            startActivity(workModeIntent);
        }

        if (id == R.id.action_logout) {
            Firebase firebase = new Firebase(Constants.FIREBASE_URL);
            firebase.unauth();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddEditDialog(final Course course) {
        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(course == null ? R.string.dialog_add_course_title : R.string.dialog_edit_course_title));
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_course, null, false);
                builder.setView(view);
                final EditText nameEditText = (EditText) view.findViewById(R.id.course_name_edit_text);
                if (course != null) {
                    // pre-populate
                    nameEditText.setText(course.getName());

                    TextWatcher textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // empty
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // empty
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String name = nameEditText.getText().toString();
                            mCourseAdapter.update(course, name);
                        }
                    };

                    nameEditText.addTextChangedListener(textWatcher);
                }

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (course == null) {
                            String name = nameEditText.getText().toString();
                            mCourseAdapter.add(new Course(name));
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);

                if (course != null) {
                    builder.setNeutralButton(R.string.delete_title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCourseAdapter.remove(course);
                        }
                    });
                }

                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "add");
    }

    @Override
    public void onCourseEdit(Course course) {
        showAddEditDialog(course);
    }


}
