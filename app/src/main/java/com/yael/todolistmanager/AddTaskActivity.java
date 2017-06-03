package com.yael.todolistmanager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.widget.EditText;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
/**
 * Created by Nicole on 08/05/2017.
 */

public class AddTaskActivity extends Activity {
    private static final String TAG = "AddTaskActivity";
    public static final String NEW_TITLE = "com.yael.todolistmanager.NEW_TITLE";
    public static final String NEW_DESCRIPTION = "com.yael.todolistmanager.NEW_DESCRIPTION";
    public static final String NEW_DATE = "com.yael.todolistmanager.NEW_DATE";
    String DATE_FORMAT = "dd/MM/yyyy";
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    Button btnCalendar;
    private int year, month, day;
    final Context context = this;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dateView = (TextView) findViewById(R.id.dateString);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        btnCalendar = (Button) findViewById(R.id.set_date);
        btnCalendar.setOnClickListener( new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View view) {
                // Dialog onCreateDialog(int id) - Method automatically gets Called when you call showDialog() method
                showDialog(999);
                Toast.makeText(getApplicationContext(),"ca",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        @Override
        public void onDateSet( DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
            Calendar cl = Calendar.getInstance();
            cl.set( yearSelected, monthOfYear, dayOfMonth);

            // date validation - date is future date
            if (cl.after(Calendar.getInstance())) {
                showDate(yearSelected, monthOfYear+1, dayOfMonth);
            } else {
                getDialog(context, "Invalid Date", "Please Set future Date", DialogType.SINGLE_BUTTON);
            }
        }
    };


    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog date_dialog = new DatePickerDialog(this, myDateListener, year, month, day);

        switch (id) {
            case 1:
                // create a new DatePickerDialog with values you want to show
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                Date newDate = c.getTime(); // Current time
                date_dialog.getDatePicker().setMinDate(newDate.getTime());
                return date_dialog;
            // create a new TimePickerDialog with values you want to show
            case 2:
                // create a new DatePickerDialog with values you want to show
                Calendar c1 = Calendar.getInstance();
                c1.add(Calendar.DATE, 1);
                Date newDate2 = c1.getTime();
                date_dialog.getDatePicker().setMinDate(newDate2.getTime());
                return date_dialog;
            case 999:
                return date_dialog;
        }
        return null;
    }


    private void showDate( int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    public String getFormatDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void cancelOperation(View view) {
        /**
         * Send user inputs for new task to the main activity.
         */
        final Intent intent = new Intent(this, MainActivity.class);
        setResult( Activity.RESULT_CANCELED, intent);
        finish();
    }


    public void sendMessage(View view) {
        /**
         * Send user inputs for new task to the main activity.
         */
        final Intent intent = new Intent(this, MainActivity.class);
        final EditText titleText = (EditText) findViewById(R.id.edit_title);
        EditText descText = (EditText) findViewById(R.id.edit_description);
        String newTitle = titleText.getText().toString();
        String newDesc = descText.getText().toString();
        String newDate = dateView.getText().toString();
        intent.putExtra(NEW_TITLE, newTitle);
        intent.putExtra(NEW_DESCRIPTION, newDesc);
        intent.putExtra(NEW_DATE, newDate);
        setResult( Activity.RESULT_OK, intent);
        finish();
    }


    public static void getDialog(Context context,String title, String message, DialogType typeButtons) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setCancelable(true);

        if (typeButtons == DialogType.SINGLE_BUTTON) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else if (typeButtons == DialogType.DOUBLE_BUTTON) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        AlertDialog alert = builder.create();
        alert.show();
    }

    public enum DialogType {
        SINGLE_BUTTON,
        DOUBLE_BUTTON

    }
}
