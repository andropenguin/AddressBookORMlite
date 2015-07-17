package com.sarltokyo.addressbookormlite.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.sarltokyo.addressbookormlite.dao.DatabaseHelper;
import com.sarltokyo.addressbookormlite.entity.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private List<String > mList = new ArrayList<String>();
    private ListView mListView;
    private DatabaseHelper mHelper;

    public final static String TYPE = "type";
    public final static String CREATE_DATA_TYPE = "create";
    public final static String UPDATE_DATA_TYPE = "update";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = AddressApplication.getInstance().getDatabaseHelper();

        mListView = (ListView)findViewById(android.R.id.list);

        mList = getAllList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.rowdata, mList);
        mListView.setAdapter(arrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                String name = (String)listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra(TYPE, UPDATE_DATA_TYPE);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.add("NEW");
        item.setIcon(android.R.drawable.ic_input_add);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra(TYPE, CREATE_DATA_TYPE);
                startActivity(intent);

                return false;
            }
        });
        MenuItemCompat.setShowAsAction(item,
                MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

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

        return super.onOptionsItemSelected(item);
    }

    public List<String> getAllList() {
        List<String> list  = new ArrayList<String>();
        try {
            List<Person> persons =  mHelper.getPersonDao().queryForAll();
            for (Person person: persons) {
                list.add(person.getName());
            }
            return list;
        } catch (SQLException e) {
            Log.e(TAG, "例外が発生しました", e);
            return null;
        }
    }

    @Override
    protected void onRestart() {
        mListView = (ListView)findViewById(android.R.id.list);

        mList = getAllList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.rowdata, mList);
        mListView.setAdapter(arrayAdapter);

        super.onRestart();
    }
}
