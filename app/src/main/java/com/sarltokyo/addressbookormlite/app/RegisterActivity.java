package com.sarltokyo.addressbookormlite.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sarltokyo.addressbookormlite.dao.DatabaseHelper;
import com.sarltokyo.addressbookormlite.entity.Address;
import com.sarltokyo.addressbookormlite.entity.Person;
import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by osabe on 15/07/17.
 */
public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener {
    private final static String TAG = RegisterActivity.class.getSimpleName();

    private DatabaseHelper mHelper;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mHelper = AddressApplication.getInstance().getDatabaseHelper();

        mBtn = (Button)findViewById(R.id.btn);
        mBtn.setOnClickListener(this);

        String type = getIntent().getStringExtra(MainActivity.TYPE);

        Resources resources = getResources();
        if (type.equals(MainActivity.CREATE_DATA_TYPE)) {
            mBtn.setText(resources.getString(R.string.register));
        } else if (type.equals(MainActivity.UPDATE_DATA_TYPE)) {
            mBtn.setText(resources.getString(R.string.update));
            showAddress();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn) {
            String type = getIntent().getStringExtra(MainActivity.TYPE);
            if (type.equals(MainActivity.CREATE_DATA_TYPE)) {
                registerAddressBook();
            } else if (type.equals(MainActivity.UPDATE_DATA_TYPE)) {
                updateAddressBook();
            }
        }
    }

    public void registerAddressBook() {
        String name = ((EditText)findViewById(R.id.nameEt)).getText().toString();
        String zipcode = ((EditText)findViewById(R.id.zipcodeEt)).getText().toString();
        String prefecture = ((EditText)findViewById(R.id.prefectureEt)).getText().toString();
        String city = ((EditText)findViewById(R.id.cityEt)).getText().toString();
        String other = ((EditText)findViewById(R.id.otherEt)).getText().toString();

        if (!checkDatum(name, zipcode, prefecture, city, other)) {
            return;
        }

        if (isExists(name)) {
            Toast.makeText(this, name + " has been registered already or some error occurs.", Toast.LENGTH_LONG).show();
            return;
        }

        Address address = new Address();
        address.setZipcode(zipcode);
        address.setPrefecture(prefecture);
        address.setCity(city);
        address.setOther(other);
        Person person = new Person();
        person.setName(name);
        person.setAddress(address);
        try {
            mHelper.getPersonDao().create(person);
            finish();
        } catch (SQLException e) {
            Toast.makeText(this, "cannot register address.", Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }
    }

    public void updateAddressBook() {
        String name = ((EditText)findViewById(R.id.nameEt)).getText().toString();
        String zipcode = ((EditText)findViewById(R.id.zipcodeEt)).getText().toString();
        String prefecture = ((EditText)findViewById(R.id.prefectureEt)).getText().toString();
        String city = ((EditText)findViewById(R.id.cityEt)).getText().toString();
        String other = ((EditText)findViewById(R.id.otherEt)).getText().toString();

        if (!checkDatum(name, zipcode, prefecture, city, other)) {
            return;
        }

        try {
            Person person = mHelper.getPersonDao().queryForEq("name", name).get(0);
            Address address = person.getAddress();
            address.setZipcode(zipcode);
            address.setPrefecture(prefecture);
            address.setCity(city);
            address.setOther(other);
            mHelper.getAddressDao().update(address);
            mHelper.getPersonDao().update(person);
            finish();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void showAddress() {
        String type = getIntent().getStringExtra(MainActivity.TYPE);
        if (!type.equals(MainActivity.UPDATE_DATA_TYPE)) return;

        String name = getIntent().getStringExtra("name");

        List<Person> list = null;
        Person person;
        Address address;

        try {
            list = mHelper.getPersonDao().queryForEq("name", name);
            person = list.get(0);
        } catch (SQLException e) {
            // ありえない
            Log.e(TAG, e.getMessage());
            return;
        }

        address = person.getAddress();

        String zipcode = address.getZipcode();
        String prefecture = address.getPrefecture();
        String city = address.getCity();
        String other = address.getOther();

        ((EditText)findViewById(R.id.nameEt)).setText(name);
        ((EditText)findViewById(R.id.nameEt)).setEnabled(false);
        ((EditText)findViewById(R.id.zipcodeEt)).setText(zipcode);
        ((EditText)findViewById(R.id.prefectureEt)).setText(prefecture);
        ((EditText)findViewById(R.id.cityEt)).setText(city);
        ((EditText)findViewById(R.id.otherEt)).setText(other);
    }

    public boolean isExists(String name) {
        List<Person> list = null;
        try {
            list = mHelper.getPersonDao().queryForEq("name", name);
            if (list.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }

    }

    public boolean checkDatum(String name, String zipcode, String prefecture,
                           String city, String other) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "name is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(this, "zipcode is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(prefecture)) {
            Toast.makeText(this, "prefecture is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "city is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(other)) {
            Toast.makeText(this, "other is empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
