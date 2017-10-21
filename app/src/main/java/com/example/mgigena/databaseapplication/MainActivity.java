package com.example.mgigena.databaseapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView mListView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create the DBHelper and get all saved items, also create the adapter for the list item
        myDb = new DBHelper(this);
        ArrayList<String> array_list = myDb.getAllContacts();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                array_list);
        //Locate the ListView1 object, and set the adapter to arrayAdapter
        mListView = (ListView) findViewById(R.id.listView1);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_to_search = position + 1;
                Bundle dataBundle = new Bundle();
                //Into the dataBundle object put the id to search
                dataBundle.putInt(FeedReaderContract.FeedEntry._ID, id_to_search);
                //Launch the DisplayActivity class
                Intent intent = new Intent(getApplicationContext(), DisplayContact.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    /**
     * This method is used to load the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This method is launched when, from the main menu, the option Add New is clicked.
     *
     * @param item MenuItem
     * @return success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.item1:
                Bundle dataBundle = new Bundle();
                //An _id of 0 is given to the dataBundle to signal that is an insert and not an
                // update.
                dataBundle.putInt(FeedReaderContract.FeedEntry._ID, 0);
                //Launch the DisplayActivity class
                Intent intent = new Intent(getApplicationContext(), DisplayContact.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method will be launched when, from the main menu, the back button is pressed.
     *
     * @param keyCode int
     * @param event KeyEvent
     * @return success
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
