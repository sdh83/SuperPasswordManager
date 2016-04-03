package name.skyking.skilo.superpasswordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Activity2 extends AppCompatActivity {

    private class customAdapter extends ArrayAdapter<String> {

        public customAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View view =  super.getView(position, convertView, parent);

            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setTextColor(Color.BLACK);

            return view;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String subtl = getString(R.string.subtl);
        final String cancel = getString(R.string.canceled);
        final String del = getString(R.string.delete);
        final String y = getString(R.string.yes);
        final String n = getString(R.string.no);
        final String entryDel = getString(R.string.entryDeleted);

        ActionBar actionbar = getSupportActionBar();
        //actionbar.setDisplayShowHomeEnabled(true);
        //actionbar.setIcon(R.mipmap.ic_launcher);
        actionbar.setSubtitle(subtl);
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 0, 114, 198)));
        final Intent intent = new Intent(Activity2.this, Activity3.class);
        final SQLiteDatabase myDB = openOrCreateDatabase("spmdb.db", MODE_PRIVATE, null);
        final Cursor crs = myDB.rawQuery("Select * from Passwords", null);
        String[] array = new String[crs.getCount()];
        int i = 0;
        while(crs.moveToNext()){
            String uname = crs.getString(crs.getColumnIndex("Account"));
            array[i] = uname;
            i++;
        }
        Arrays.sort(array);
        final ListAdapter myAdapter = new customAdapter(this, android.R.layout.simple_list_item_1, array);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(myAdapter);
        crs.close();


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String k = String.valueOf(parent.getItemAtPosition(position));
                        String var1 = "";
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(var1, k);
                        //crs.close();
                        myDB.close();
                        startActivity(intent);
                    }
                }
        );

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?>parent, View view, final int position, long id) {
                //dialog box to confirm deletion
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                try {
                                    //Yes button clicked
                                    String p = String.valueOf(parent.getItemAtPosition(position));
                                    myDB.delete("Passwords", "Account=?", new String[]{p});
                                    Toast.makeText(getApplicationContext(), entryDel, Toast.LENGTH_LONG).show();
                                    myDB.close();
                                    finish();
                                    startActivity(getIntent());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(getApplicationContext(), cancel, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity2.this);
                builder.setMessage(del).setPositiveButton(y, dialogClickListener)
                        .setNegativeButton(n, dialogClickListener).show();
                return true;
            }
        });
    }
}
