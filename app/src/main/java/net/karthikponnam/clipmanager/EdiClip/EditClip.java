package net.karthikponnam.clipmanager.EdiClip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import net.karthikponnam.clipmanager.Database.DataQuery;
import net.karthikponnam.clipmanager.R;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditClip extends AppCompatActivity {

    String TAG = getClass().getName();
    DataQuery dataQuery;
    EditText clip_editText;
    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataQuery = new DataQuery(EditClip.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        clip_editText = (EditText) findViewById(R.id.edit_clip);

        Intent i = getIntent();
        _id = i.getIntExtra("id", -1);
        JSONObject jsonObject = dataQuery.getClipsById(_id);
        _id = jsonObject.optInt("id");

        clip_editText.setText(jsonObject.optString("data"));

        findViewById(R.id.save_clip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataQuery.updateClipById(clip_editText.getText().toString(), _id);
                Snackbar.make(v, "Data Updated.",Snackbar.LENGTH_SHORT).show();
                hideKeyboard();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save :
                dataQuery.updateClipById(clip_editText.getText().toString(), _id);
                hideKeyboard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(clip_editText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
