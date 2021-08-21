package sales.pipesandconduit.tracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BaseActivity extends AppCompatActivity {

    public void setDate(EditText et_date,Context context){
        final Calendar date = new GregorianCalendar();
        final DateFormat dateFormatter = SimpleDateFormat.getDateInstance();

        et_date.setText(dateFormatter.format(date.getTime()));
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                date.set(year, month, day);
                et_date.setText(dateFormatter.format(date.getTime()));
            }
        };
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                int day = date.get(Calendar.DATE);
                DatePickerDialog datePicker = new DatePickerDialog(context,dateSetListener, year, month, day);
                datePicker.show();
            }
        });
    }

    public long dateLong(String str_date){
        final DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
        long dateMs = 0;
        try {
            dateMs = dateFormatter.parse(str_date).getTime();
        } catch (ParseException e)
        {
            //return;
        }
        return dateMs;
    }

    public String getDate(String str_date){
        final Calendar date = new GregorianCalendar();
        final DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
        String returned_date = dateFormatter.format(new Date(str_date));
        return returned_date;
    }

}
