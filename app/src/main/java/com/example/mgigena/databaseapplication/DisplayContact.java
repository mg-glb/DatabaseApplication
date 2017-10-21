package com.example.mgigena.databaseapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Activity displays the menu that creates, edits and deletes contacts.
 */
public class DisplayContact extends AppCompatActivity {
    private DBHelper myDb;
    //These are the Views that are in the layout file
    TextView name;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    int id_To_Update = 0;

    /**
     * This method will instantiate the Views, and will retrieve data from the extras if necessary.
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        //Here we set up the Views
        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        email = (TextView) findViewById(R.id.editTextStreet);
        street = (TextView) findViewById(R.id.editTextEmail);
        place = (TextView) findViewById(R.id.editTextCity);
        //Here we bring the DataBase and the extras from the previous activity.
        myDb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        //Depending on whether we are inserting or updating, we will retrieve the values
        //of the contact
        if (extras != null) {
            int value = extras.getInt(FeedReaderContract.FeedEntry._ID);
            if (value > 0) {
                Cursor rs = myDb.getData(value);
                id_To_Update = value;
                if (rs.moveToFirst()) {
                    //Here we retrieve the values of the fields of the contact.
                    String nameString = rs.getString(
                            rs.getColumnIndex(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME));
                    String phoneString = rs.getString(
                            rs.getColumnIndex(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_PHONE));
                    String emailString = rs.getString(
                            rs.getColumnIndex(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_EMAIL));
                    String streetString = rs.getString(
                            rs.getColumnIndex(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_STREET));
                    String placeString = rs.getString(
                            rs.getColumnIndex(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_CITY));
                    //Since the Cursor is an expensive object to maintain, and since we no longer
                    //need it, we close it.
                    if (!rs.isClosed()) {
                        rs.close();
                    }
                    //Here we enable the confirm contact button
                    Button b = (Button) findViewById(R.id.button1);
                    b.setVisibility(View.INVISIBLE);
                    //Here we make the name field editable
                    name.setText(nameString);
                    name.setFocusable(false);
                    name.setClickable(false);
                    //Same with phone
                    phone.setText(phoneString);
                    phone.setFocusable(false);
                    phone.setClickable(false);
                    //And email
                    email.setText(emailString);
                    email.setFocusable(false);
                    email.setClickable(false);
                    //And street
                    street.setText(streetString);
                    street.setFocusable(false);
                    street.setClickable(false);
                    //And place(city)
                    place.setText(placeString);
                    place.setFocusable(false);
                    place.setClickable(false);
                }
            }
        }
    }

    /**
     * This method is called when the DisplayContact menu object is created.
     * Its behavior will vary depending on whether we are inserting or updating an object.
     *
     * @param menu Menu
     * @return success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt(FeedReaderContract.FeedEntry._ID);
            if (value > 0) {
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else {
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    /**
     * This method handles the options of the menu of the DisplayContact activity.
     *
     * @param item MenuItem
     * @return success
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            //Here we detect whether the item clicked was the Edit or Delete Contact.
            case R.id.Edit_Contact:
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);

                place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);

                return true;
            case R.id.Delete_Contact:
                //If the user selects Delete, we start an AlertDialog. This object will be
                //composed of three parts: the message per se, the positive button config,
                //and the negative button config.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //Here we define what happens when the user clicks yes.
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.deleteContact(id_To_Update);
                        Toast.makeText(getApplicationContext(),
                                "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent);
                    }
                };
                //If the user clicks no, nothing should happen.
                DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                };
                builder.setMessage(R.string.deleteContact).setPositiveButton(R.string.yes, positive)
                        .setNegativeButton(R.string.no, negative);
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method, called from the button1 object, uses the DBHelper to record the object into
     * the database
     *
     * @param view View
     */
    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt(FeedReaderContract.FeedEntry._ID);
            //If the _id is more than zero, then the operation is an update.
            if (value > 0) {
                if (myDb.updateContact(id_To_Update,
                        name.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        street.getText().toString(),
                        place.getText().toString())) {
                    //If the operation succeeds, notify the user
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    //If the operation fails, also notify the user
                    Toast.makeText(getApplicationContext(), "not Updated",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (value == 0) {
                //If the _id is zero, perform an insert
                if (myDb.insertContact(name.getText().toString(), phone.getText().toString(),
                        email.getText().toString(), street.getText().toString(),
                        place.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                //If the value is different from both, throw an IllegalArgumentException
                throw new IllegalArgumentException("_id should not be less than zero");
            }
        }
    }
}
