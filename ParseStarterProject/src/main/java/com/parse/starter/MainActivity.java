/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signInMode = true;
  TextView changeSignUpView;
  EditText passwordEditText;
  TextView chaneSignupModeTextView;
  RelativeLayout background;
  ImageView logo;

  public void showUserList(){

    Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }
  public void signUp(View view){
    EditText usernameEditText= (EditText) findViewById(R.id.editText);
    passwordEditText= (EditText) findViewById(R.id.editText2);
    if(usernameEditText.getText().toString().matches("")||passwordEditText.getText().toString() .matches("")){
      Toast.makeText(this,"username and passowrd can not left empty.",Toast.LENGTH_SHORT).show();
    }else{

      if (signInMode) {
        ParseUser user = new ParseUser();

        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Signup", "Successful");

              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }else{
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {

            if(user !=null){

              Log.i("Signup","Login Successful");
              showUserList();
            }else{
              Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setTitle("Instagram");
    setContentView(R.layout.activity_main);
    chaneSignupModeTextView =(TextView)findViewById(R.id.textView);
    chaneSignupModeTextView.setOnClickListener(this);
    background = (RelativeLayout) findViewById(R.id.background);
    passwordEditText = (EditText) findViewById(R.id.editText2);
    passwordEditText.setOnKeyListener(this);
    logo=(ImageView) findViewById(R.id.logo);
    background.setOnKeyListener(this);
    logo.setOnKeyListener(this);

    if(ParseUser.getCurrentUser()!=null){
      showUserList();
    }
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public void onClick(View view) {
    TextView changeSignUpView= (TextView) findViewById( R.id.textView);
    if(view.getId() == R.id.textView){
      Button signupButton = (Button) findViewById(R.id.button2);
      if (signInMode){
        signInMode=false;
        signupButton.setText("Login");
        changeSignUpView.setText("Or,Sign up");
      }else{
        signInMode=true;
        signupButton.setText("SignUp");
        changeSignUpView.setText("Or,Login");
      }
    } else if (view.getId() == R.id.background || view.getId() == R.id.logo){
      InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }

  @Override
  public boolean onKey(View v, int i, KeyEvent event) {

    if (i == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
      signUp(v);
    }
    return false;
  }
}