package name.skyking.skilo.superpasswordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    public String randomString(int len){
        final String AB = getString(R.string.chrs);
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public void doToast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String acctName = getString(R.string.account_name);
        final String usrName = getString(R.string.user_name);
        final String pWord = getString(R.string.pword);
        final String accex = getString(R.string.acctExists);
        final String _saved = getString(R.string.saved);
        final String noblank = getString(R.string.noBlank);
        final String expt = getString(R.string.expt);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_launcher);
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 114, 198)));
        Button savebtn = (Button)findViewById(R.id.button);
        savebtn.setBackgroundResource(android.R.drawable.btn_default);
        savebtn.getBackground().setColorFilter(Color.argb(255,0,114,198), PorterDuff.Mode.MULTIPLY);
        Button generatebtn = (Button)findViewById(R.id.button2);
        generatebtn.setBackgroundResource(android.R.drawable.btn_default);
        generatebtn.getBackground().setColorFilter(Color.argb(255, 0, 114, 198), PorterDuff.Mode.MULTIPLY);
        Button viewsavepassbtn = (Button)findViewById(R.id.button3);
        viewsavepassbtn.setBackgroundResource(android.R.drawable.btn_default);
        viewsavepassbtn.getBackground().setColorFilter(Color.argb(255,0,114,198), PorterDuff.Mode.MULTIPLY);
        Button exportbtn = (Button)findViewById(R.id.button4);
        exportbtn.setBackgroundResource(android.R.drawable.btn_default);
        exportbtn.getBackground().setColorFilter(Color.argb(255,0,114,198), PorterDuff.Mode.MULTIPLY);
        final EditText accountName = (EditText)findViewById(R.id.editText);
        final EditText userName = (EditText)findViewById(R.id.editText2);
        final EditText passwd = (EditText)findViewById(R.id.editText3);

        final Intent intent = new Intent(this, Activity2.class);

        //open or create the database to prevent errors when no DB exists
        SQLiteDatabase mydb = openOrCreateDatabase("spmdb.db", MODE_PRIVATE, null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS Passwords(id integer primary key, Account TEXT, Username TEXT,Password TEXT);");
        mydb.close();

        generatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwd.setText(randomString(15));
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text of editText
                String accountNameValue = accountName.getText().toString();
                String userNameValue = userName.getText().toString();
                String passValue = passwd.getText().toString();
                SQLiteDatabase mydb = openOrCreateDatabase("spmdb.db", MODE_PRIVATE, null);
                if (accountNameValue.isEmpty()) {
                    doToast(acctName + " " + noblank);
                    accountName.setText(acctName);
                } else if (userNameValue.isEmpty()) {
                    doToast(usrName + " " + noblank);
                    userName.setText(usrName);
                } else if (passValue.isEmpty()) {
                    doToast(pWord + " " + noblank);
                    passwd.setText(pWord);
                } else {
                    //check to see if account already exists
                    Cursor crs = mydb.rawQuery("Select * from Passwords where Account=?", new String[]{accountNameValue});
                    if (crs.getCount() <= 0) {
                        mydb.execSQL("INSERT INTO Passwords (Account, Username, Password) " + " Values ('" + accountNameValue + "', '" + userNameValue + "', '" + passValue + "');");
                        accountName.setText(acctName);
                        userName.setText(usrName);
                        passwd.setText(pWord);
                        crs.close();
                        mydb.close();
                        doToast(_saved);
                    } else {
                        doToast(accex);
                        crs.close();
                        mydb.close();
                    }
                }
            }

        });

        viewsavepassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountName.setText("");
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setText("");
            }
        });

        passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwd.setText("");
            }
        });

        exportbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String g = Environment.getExternalStorageDirectory().toString();
                    File dbFile = new File(String.valueOf(getDatabasePath("spmdb.db")));
                    InputStream in = new FileInputStream(dbFile); // Memory card path
                    File myFile = new File(g + "/spmdb_backup.db"); //
                    OutputStream out = new FileOutputStream(myFile);
                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    doToast(expt + g + "/spmdb_backup.db");

                } catch (FileNotFoundException e) {
                    doToast(e.getMessage());
                    //e.printStackTrace();
                } catch (IOException e) {
                    doToast(e.getMessage());
                    //e.printStackTrace();
                }
            }
        });
    }
}
