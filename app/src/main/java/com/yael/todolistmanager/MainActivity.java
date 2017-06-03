package com.yael.todolistmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.content.Intent;
import android.view.Menu;
import android.net.Uri;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DefaultItemAnimator;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 1;
    RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addBtn;
    final Context context = this;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent appIntent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("TODO List Manager");

        }

        // returns View, but Button extends View
        addBtn = (FloatingActionButton) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        List<ItemData> data = fill_with_data();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // 2. set layoutManger
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( mLayoutManager);
        // 3. create an mAdapter
        mAdapter = new MyAdapter( data, getApplication());
        // 4. set mAdapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(this, recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemData item = mAdapter.getItem( position);
                recyclerView.setFocusableInTouchMode(true);
                recyclerView.requestFocus();
                //Toast.makeText(getApplicationContext(), "Clicked at " + position + " " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick( View view, int position) {
                ItemData item = mAdapter.getItem( position);
                // Toast.makeText(getApplicationContext(), "LongClicked at item: " + position + " - " + item.getTitle(), Toast.LENGTH_SHORT).show();
                alertDeleteItem( position);
            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String newTitle = data.getStringExtra( AddTaskActivity.NEW_TITLE);
            String newDesc = data.getStringExtra( AddTaskActivity.NEW_DESCRIPTION);
            String newDate = data.getStringExtra( AddTaskActivity.NEW_DATE);
            if (!newTitle.isEmpty()) {
                mAdapter.add( new ItemData( newTitle, newDesc, newDate));
            }
        }
    }


    public void alertDeleteItem( final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Delete Item");
        alert.setMessage("Are you sure you want to delete this task?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.remove(position);
                recyclerView.removeViewAt(position);
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        // Inflate the menu. this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        /* The action bar will automatically handle clicks on the Home/Up button,
        must specify a parent activity in AndroidManifest.xml.
         */
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Create a list of Data objects
    public List<ItemData> fill_with_data() {
        List<ItemData> data = new ArrayList<>();
        data.add( new ItemData("Help","David with something","10/06/2017"));
        data.add( new ItemData("Calender","birthday event","10/06/2017"));
        data.add( new ItemData("Call","054-4914-286","12/06/2017"));
        data.add( new ItemData("Call","052-869-1536","13/06/2017"));
        data.add( new ItemData("Dial","0544432220","15/05/2017"));
        data.add( new ItemData("Gmail","nicolsa4@gmail.com","15/05/2017"));
        return data;
    }


    /**
     * Creates a Popup message to the user when there is alert or an error.
     * @param message- the message to the user.
     */
    private void alertView( String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage( message); // set the message to user
        alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    /**
     * Add a new task object - starts new activity with new layout for user input.
     * @return true if was successful.
     */
    private boolean addItem( View view) {
        Intent addItemIntent = new Intent(this, AddTaskActivity.class);
        startActivityForResult( addItemIntent, REQUEST_CODE);
        return true;
    }

}
