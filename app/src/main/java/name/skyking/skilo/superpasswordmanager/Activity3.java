package name.skyking.skilo.superpasswordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String subtl2 = getString(R.string.subtl2);
        final String copied = getString(R.string.copied);
        final String accountName = getString(R.string.account_name);
        final String userName = getString(R.string.user_name);
        final String password = getString(R.string.pword);

        ActionBar actionbar = getSupportActionBar();
        //actionbar.setDisplayShowHomeEnabled(true);
        //actionbar.setIcon(R.mipmap.ic_launcher);
        actionbar.setSubtitle(subtl2);
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 114, 198)));
        Button button = (Button)findViewById(R.id.button5);
        button.setBackgroundResource(android.R.drawable.btn_default);
        button.getBackground().setColorFilter(Color.argb(255, 0, 114, 198), PorterDuff.Mode.MULTIPLY);
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setTextColor(Color.BLACK);
        //get data from row and insert into textview
        String var1 = "";
        Intent intent = getIntent();
        String cname = intent.getStringExtra(var1);
        SQLiteDatabase myDB = openOrCreateDatabase("spmdb.db", MODE_PRIVATE, null);
        //get values from row
        Cursor crs = myDB.rawQuery("Select * from Passwords where Account = ?", new String[]{cname}, null);

        crs.moveToFirst();
        String v1 = crs.getString(1);
        String v2 = crs.getString(2);
        final String v3 = crs.getString(3);
        textView.setText(accountName + ": " + v1 + "\n" + "\n" + userName + ": " + v2 + "\n" + "\n" + password + ": " + v3);
        crs.close();
        myDB.close();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(password,v3);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),copied, Toast.LENGTH_LONG).show();
            }
        });
    }
}
