package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity
        implements LoginActivity.OnLoginListener,
        GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1;
    private static final String TAG = "FPK";
    GoogleApiClient mGoogleApiClient;
    private boolean returningFromLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        returningFromLogout = false;
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        if(firebase.getAuth() == null || isExpired(firebase.getAuth())) {
            switchToLoginFragment();
        } else {
            //switchToPasswordFragment(Constants.FIREBASE_URL + "/users/" + firebase.getAuth().getUid());

            showCourseActivity();
        }


    }

    private void showCourseActivity() {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        String uid = firebase.getAuth().getUid();
        Intent courseIntent = new Intent(this, CourseActivity.class);
        courseIntent.putExtra(Constants.UID_EXTRA_KEY, uid);
        startActivityForResult(courseIntent, Constants.RC_DID_LOGOUT);
    }

    private void switchToLoginFragment() {
//        Intent loginIntent = new Intent(this, LoginActivity.class);
//        startActivity(loginIntent);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new LoginActivity(), "Login");
        ft.commit();
    }

    private boolean isExpired(AuthData authData) {
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }

    public void onLogout() {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.unauth();
        switchToLoginFragment();
    }


    class MyAuthResultHandler implements Firebase.AuthResultHandler {

        @Override
        public void onAuthenticated(AuthData authData) {
            //switchToPasswordFragment(FIREBASE_URL + "/users/" + authData.getUid());
            showCourseActivity();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            showLoginError(firebaseError.getMessage());
        }
    }


    @Override
    public void onLogin(String email, String password) {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.authWithPassword(email, password, new MyAuthResultHandler());
    }

    @Override
    public void onGoogleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                //TODO         account.getDisplayName();
                String emailAddress = account.getEmail();
                getGoogleOAuthToken(emailAddress);
            }

        } else if (requestCode == Constants.RC_DID_LOGOUT) {
            if(resultCode == Activity.RESULT_OK) {
                returningFromLogout = true;
            } else {
                Log.d("SH", "Came back using back key, naughty");
                showCourseActivity();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getGoogleOAuthToken(final String emailAddress) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    String scope = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(MainActivity.this, emailAddress, scope);
                } catch (IOException transientEx) {
                /* Network or server error */
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                /* We probably need to ask for permissions, so start the intent if there is none pending */
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, REQUEST_CODE_GOOGLE_SIGN_IN);
                } catch (GoogleAuthException authEx) {
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }
            @Override
            protected void onPostExecute(String token) {
                Log.d("FPK", "onPostExecute");
                //TODO
                if(token == null) {
                    showLoginError(errorMessage);
                } else {
                    onGoogleLoginWithToken(token);
                }
            }
        };
        task.execute();
    }


    private void onGoogleLoginWithToken(String oAuthToken) {
        //TODO: Log user in with Google OAuth Token
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.authWithOAuthToken("google", oAuthToken, new MyAuthResultHandler());
    }

    private void showLoginError(String message) {
        LoginActivity loginActivity = (LoginActivity) getSupportFragmentManager().findFragmentByTag("Login");
        loginActivity.onLoginError(message);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(returningFromLogout) {
            onLogout();
        }
        returningFromLogout = false;
    }

}
