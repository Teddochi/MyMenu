package com.Cse110.view;

import java.util.List;

import com.Cse110.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	private String AppId = "PFeVg2evGtAWYkKlIe11myOGl2Wsw4bpygimtWcT";
	private String ClientKey = "AmbMohWlGph7KEZfZMl4LnbVBj8M20fThtOIzn7P";
	public static ParseObject user;
	EditText regEmail;
	EditText regPass;
	EditText regConfirm;
	Context context;
	Button signup;

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		try {
			Intent intent = getIntent();
			System.out.println("intent is " + intent);

			// Add your initialization code here
			Parse.initialize(this, AppId, ClientKey);
			ParseUser.enableAutomaticUser();
			ParseACL defaultACL = new ParseACL();

			// If you would like all objects to be private by default, remove
			// this
			// line.
			defaultACL.setPublicReadAccess(true);
			ParseACL.setDefaultACL(defaultACL, true);
			ParseAnalytics.trackAppOpened(getIntent()); // MIGHT NEED TO COMMENT
														// OUT
														// IF CRASHES

		} catch (Exception e) {
			// Parse already initialized, continuing.
			System.out.println("Parse already initialized");
		}

		setContentView(R.layout.activity_login);

		regEmail = (EditText) findViewById(R.id.emailreg);
		regPass = (EditText) findViewById(R.id.password1);
		regConfirm = (EditText) findViewById(R.id.password2);
		signup = (Button) findViewById(R.id.signUpButton);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		if (haveNetworkConnection()) {
			System.out.println("INTERNET FOUND");
		} else {
			System.out.println("NO INTERNET");
			CharSequence text = "Please connect to the internet";
			int duration = Toast.LENGTH_SHORT;
			Toast t = Toast.makeText(context.getApplicationContext(), text,
					duration);
			t.show();
			return;
		}
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			ParseQuery<ParseObject> userQuery = ParseQuery
					.getQuery("UserAccount");
			userQuery.whereEqualTo("UserName", mEmail);
			try {
				if (userQuery.count() == 0) {
					CharSequence text = "Username not found";
					int duration = Toast.LENGTH_SHORT;
					Toast t = Toast.makeText(this.getApplicationContext(),
							text, duration);
					t.show();
				} else {
					user = userQuery.find().get(0);
					if (user.getString("Password").equals(mPassword)) {
						// Show a progress spinner, and kick off a background
						// task
						// to perform the user login attempt.
						mLoginStatusMessageView
								.setText(R.string.login_progress_signing_in);
						showProgress(true);
						mAuthTask = new UserLoginTask();
						mAuthTask.execute((Void) null);

						Intent intent = new Intent(this, HomePageActivity.class);
						intent.putExtra("user", user.getObjectId());
						// PASS IN USER HERE
						startActivity(intent);

					} else {
						CharSequence text = "Incorrect Password";
						int duration = Toast.LENGTH_SHORT;
						Toast t = Toast.makeText(this.getApplicationContext(),
								text, duration);
						t.show();
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	public void register(View v) {
		regEmail.setVisibility(View.VISIBLE);
		regPass.setVisibility(View.VISIBLE);
		regConfirm.setVisibility(View.VISIBLE);
		signup.setVisibility(View.VISIBLE);
	}

	public void signUp(View v) {
		if (haveNetworkConnection()) {
			System.out.println("INTERNET FOUND");
		} else {
			System.out.println("NO INTERNET");
			CharSequence text = "Please connect to the internet";
			int duration = Toast.LENGTH_SHORT;
			Toast t = Toast.makeText(context.getApplicationContext(), text,
					duration);
			t.show();
			return;
		}
		ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("UserAccount");
		userQuery.whereEqualTo("UserName", regEmail.getText().toString());
		userQuery.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> users, com.parse.ParseException e) {
				if (e == null && users.size() == 0) {
					if (regPass.getText().toString().length() < 4) {
						CharSequence text = "Password must be at least 4 characters";
						int duration = Toast.LENGTH_SHORT;
						Toast t = Toast.makeText(
								context.getApplicationContext(), text, duration);
						t.show();
					} else if (regEmail.getText().toString().length() == 0) {
						CharSequence text = "Please enter an email";
						int duration = Toast.LENGTH_SHORT;
						Toast t = Toast.makeText(
								context.getApplicationContext(), text, duration);
						t.show();
					} else if (!regEmail.getText().toString().contains("@")) {
						CharSequence text = "Must be valid email";
						int duration = Toast.LENGTH_SHORT;
						Toast t = Toast.makeText(
								context.getApplicationContext(), text, duration);
						t.show();
					}

					else {

						if (regPass.getText().toString()
								.equals(regConfirm.getText().toString())) {

							ParseObject newUser = new ParseObject("UserAccount");
							newUser.put("UserName", regEmail.getText()
									.toString());
							newUser.put("Password", regPass.getText()
									.toString());
							try {
								newUser.save();

								CharSequence text = "Success! Account Created.";
								int duration = Toast.LENGTH_SHORT;
								Toast t = Toast.makeText(
										context.getApplicationContext(), text,
										duration);
								t.show();
								regEmail.setVisibility(View.INVISIBLE);
								regPass.setVisibility(View.INVISIBLE);
								regConfirm.setVisibility(View.INVISIBLE);
								signup.setVisibility(View.INVISIBLE);
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							CharSequence text = "Passwords don't match";
							int duration = Toast.LENGTH_SHORT;
							Toast t = Toast.makeText(
									context.getApplicationContext(), text,
									duration);
							t.show();
						}

					}
				} else {
					CharSequence text = "Username already taken";
					int duration = Toast.LENGTH_SHORT;
					Toast t = Toast.makeText(context.getApplicationContext(),
							text, duration);
					t.show();
				}

			}
		});

	}

	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	@Override
	public void onBackPressed() {

	}
}
