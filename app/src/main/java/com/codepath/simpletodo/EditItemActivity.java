package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.codepath.simpletodo.MainActivity.ITEM_POSITION;
import static com.codepath.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // Text field with the updated item description
    EditText etItemText;
    // Item's position in the list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        // Resolve the text field from the layout
        etItemText = (EditText) findViewById(R.id.etItemText);
        // Set the text field's context from Intent
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        // Track the position of the item in the list
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        // Set the title bar to reflect purpose of the view
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v) {
        // Prepare intent to pass back to MainActivity
        Intent data = new Intent();
        // Pass updated item text and original position
        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        // Add the item's position
        data.putExtra(ITEM_POSITION, position);
        // Set the result code and bundle data for response
        setResult(RESULT_OK, data);
        // Close the activity, pass intent to main
        finish();
    }
}
