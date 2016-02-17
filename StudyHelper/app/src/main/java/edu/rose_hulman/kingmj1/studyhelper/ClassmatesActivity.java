package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by reynolpt on 1/18/2016.
 */
public class ClassmatesActivity extends AppCompatActivity implements ClassmatesAdapter.ClassmatesCallback {

    private ClassmatesAdapter mClassmatesAdapter;
    private String mCourseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //TODO: Create a new Classmate
            @Override
            public void onClick(View view) {
                showAddEditDialog(null);
            }
        });
        Intent intent = getIntent();
        mCourseKey = intent.getStringExtra(Constants.COURSE_KEY_EXTRA_KEY);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.classmates_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mClassmatesAdapter = new ClassmatesAdapter(this, recyclerView, this, mCourseKey);
        recyclerView.setAdapter(mClassmatesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //TODO: Not sure if we need a menu yet
        //getMenuInflater().inflate(R.menu.menu_classmates, menu);
        return false;
    }

    private void showAddEditDialog(final Classmate classmate) {
        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(classmate == null ? R.string.dialog_add_classmate_title : R.string.dialog_edit_classmate_title));
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_classmate, null, false);
                builder.setView(view);
                final EditText nameEditText = (EditText) view.findViewById(R.id.classmate_name_entry);
                final EditText emailEditText = (EditText) view.findViewById(R.id.classmate_email_entry);
                if (classmate != null) {
                    // pre-populate
                    nameEditText.setText(classmate.getName());

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
                            String email = emailEditText.getText().toString();
                            mClassmatesAdapter.update(classmate, name, email);
                        }
                    };

                    nameEditText.addTextChangedListener(textWatcher);
                    emailEditText.setText(classmate.getEmailAddress());
                    emailEditText.addTextChangedListener(textWatcher);
                }

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (classmate == null) {
                            String name = nameEditText.getText().toString();
                            String emailAddress = emailEditText.getText().toString();
                            mClassmatesAdapter.add(new Classmate(name, emailAddress, mCourseKey));
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);

                if (classmate != null) {
                    builder.setNeutralButton(R.string.delete_title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mClassmatesAdapter.remove(classmate);
                        }
                    });
                }

                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "add");
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClassmateEdit(Classmate classmate) {
        showAddEditDialog(classmate);
    }
}
