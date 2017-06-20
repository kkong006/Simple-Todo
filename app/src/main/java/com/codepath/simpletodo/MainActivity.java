package com.codepath.simpletodo;

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

        // Add some mock items to the list
//        items.add("First todo item");
//        items.add("Next todo");

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
}