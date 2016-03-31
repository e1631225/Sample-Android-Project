package com.example.infolinetestapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infolinetestapplication.R;
import com.example.infolinetestapplication.utils.DatabaseHelper;
import com.example.infolinetestapplication.utils.Variables;
import com.example.infolinetestapplication.entity.Login;
import com.example.infolinetestapplication.entity.Person;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class MainActivity extends Activity implements View.OnClickListener {
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        databaseHelper= getHelper();
        databaseHelper.initializeDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSessionTimeout();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.loginButton:
                loginButtonClickHandler();
            break;
        }
    }

    private void loginButtonClickHandler() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        Person person = databaseHelper.queryPerson(username, password);

        if(person == null) { // failed login
            Toast.makeText(getApplicationContext(), R.string.loginFailedMessage, Toast.LENGTH_SHORT).show();
        } else { // successful login
            databaseHelper.updateLoginTime(System.currentTimeMillis(), person);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            prefs.edit().putLong(Login.PERSON_ID_TEXT_FIELD, person.getId()).commit(); // successful loginde person id yi setliyoruz sonraki loginlerde kontrol için
            redirectToListActivity();
        }

    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    // login timeout a göre login yönlendirmesi yapar
    private void checkSessionTimeout() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        Long personId = prefs.getLong(Login.PERSON_ID_TEXT_FIELD, -1L);
        if(personId >= 0) { // daha önce login olunmuş demektir
            Login login = databaseHelper.queryLogin(personId);
            Person person = databaseHelper.queryPerson(personId.intValue());
            Long currentTime = System.currentTimeMillis();
            if(currentTime - login.getLoginTime() < Variables.TIMEOUT) { // Timeout olmamış list activity ye yönlendir
                databaseHelper.updateLoginTime(currentTime, person);
                redirectToListActivity();
            } else { // timeout olmuş username setle şifre boş bırak
                usernameEditText.setText(person.getUsername());
            }
        }
        // ilk kez login olunmuş initial durumda login başlatılacak
    }

    // success loginde ListActivity ye yönlendirme
    private void redirectToListActivity() {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.close();
        return true;
    }
}
