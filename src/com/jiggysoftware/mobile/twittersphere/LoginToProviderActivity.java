package com.jiggysoftware.mobile.twittersphere;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginToProviderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences prefs = getSharedPreferences("TWITTERSPHERE", MODE_PRIVATE);
		String userName = prefs.getString("twitter.username", "NOTSET~~");
		String password = prefs.getString("twitter.password", "NOTSET~~");
		
		if((!userName.equals("NOTSET~~")) && (!password.equals("NOTSET~~"))){
		    setResult(0);
		    finish();
		}else{
		    setContentView(R.layout.loginview);
		    Button btnLogin = (Button) findViewById(R.id.BtnLogin);
		    btnLogin.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					EditText txtUserName = (EditText) findViewById(R.id.EdtUserName);
					EditText txtPassword = (EditText) findViewById(R.id.EdtPassword);
					CheckBox chkRememberMe = (CheckBox) findViewById(R.id.ChkRememberMe);
					String userName = txtUserName.getText().toString();
					String password = txtPassword.getText().toString();
					boolean rememberMe = chkRememberMe.isChecked();

					if (rememberMe) {
						Editor prefsEditor = prefs.edit();
						prefsEditor.putBoolean(Constants.SNAGGME_PREFERENCE_CONFIGURED, true);
						prefsEditor.putString("twitter.username", userName);
						prefsEditor.putString("twitter.password", password);
						prefsEditor.commit();
					}

					setResult(0);
					finish();
				}

			});
		}
	}

}
