package com.harsh.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Intialising varaible
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variables
        recyclerView = findViewById(R.id.recycle);

        //Check permission
        checkPermission();
    }

    private void checkPermission() {
        //Checking condition
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //when permission is not granted request for permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},100);
        }
        else {
            //When permission is granted create method
            getContactList();

        }
    }

    private void getContactList() {
        //Intialise URL
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        //Sorting in ascending
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";

        //Intialize cursor
        Cursor cursor = getContentResolver().query(uri, null,null,null,sort);

        //Check Condition
        if(cursor.getCount() > 0)
        {
            //When count is greater than zero
            //Use while loop
            while(cursor.moveToNext()){
                //Cursor move to next
                //Getting the next contact id
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                ));

                //Get Contact name
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));

                //Initialising Phone uri
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                //Initialising Selection
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";

                //Initalising phone cursor
                Cursor phoneCursor = getContentResolver().query(
                        uriPhone,null,selection,new String[]{id},null
                );

                //Check the condition
                if(phoneCursor.moveToNext())
                {
                    //When phone cursor move to next
                    @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                    //Initialize contact model
                    ContactModel model = new ContactModel();
                    //Set Name
                    model.setName(name);
                    //Set Number
                    model.setNumber(number);
                    //Add model in array list
                    arrayList.add(model);
                    //Close phone cursor
                    phoneCursor.close();
                }
            }
            //Close cursor
            cursor.close();
        }
        //Set Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialise adapter
        mainAdapter = new MainAdapter(this,arrayList);
        //Set adapter
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Check condition
        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            //When permission is granted call method
            getContactList();
        }
        else
        {
            //When permission is denied Displaying the toast
            Toast.makeText(MainActivity.this,"Permission Denied.",Toast.LENGTH_SHORT).show();
            //Call check permission method
            checkPermission();
        }
    }
}