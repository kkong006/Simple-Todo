package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;

    // Keys used for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    // Declaring stateful objects here; these will be null before onCreate is called
    ArrayList<String> items; // List of items to display
    ArrayAdapter<String> itemsAdapter; // Adapter for the list view
    ListView lvItems; // List view of items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to listview in the layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        // Initialize the items list
        readItems();
        // Initialize the adapter using the items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // Wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        // Set up the listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v) {
        // Reference to the EditText in the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // Grab the EditText's content as a String
        String itemText = etNewItem.getText().toString();
        // Add the item to the list via the adapter
        itemsAdapter.add(itemText);
        // Store the update list
        writeItems();
        // Clear the EditText
        etNewItem.setText("");

        // Alert the user that the item has been added
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    public void setupListViewListener() {
        // Set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Remove the item in the list at the index position
                items.remove(position);
                // Notify the adapter of the change
                itemsAdapter.notifyDataSetChanged();
                // Store the updated list
                writeItems();
                // Log the system change
//                Log.i("Main Activity", "Removed item " + position);
                // Return true to tell the framework the long click was consumed
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Parameters: this context, class of activity to launch
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // Put extras into the bundle for access in the edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                Toast.makeText(getApplicationContext(), "position " + position, Toast.LENGTH_LONG).show();
                // Brings up the edit activity with the expectation of a result
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    // Returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // Read the items from the file system
    private void readItems() {
        try {
            // Create array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e) {
            // Print the error to the console
            e.printStackTrace();
            // Load an empty list
            items = new ArrayList<>();
        }
    }

    // Write the items to the filesystem
    private void writeItems() {
        try {
            // Save the items as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch(IOException e) {
            // Print the error to the console
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // EDIT_REQUEST_CODE defined with constants
        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // Extract updated item value from result extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            // Get the position of the item which was edited
            int position = data.getExtras().getInt(ITEM_POSITION, 0);
            // Update model with new item text at edited position
            items.set(position, updatedItem);
            // Notify the adapter of model change
            itemsAdapter.notifyDataSetChanged();
            // Notify the user the operation completed
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
    }
}