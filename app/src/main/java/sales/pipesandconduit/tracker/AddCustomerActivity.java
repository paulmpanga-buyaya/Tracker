package sales.pipesandconduit.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class AddCustomerActivity extends AppCompatActivity {

    CheckBox individualCheckBox, organizationCheckBox;
    LinearLayout individualLayout, organizationLayout;
    TextView selectionHint;
    MaterialButton addCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        addCustomer = findViewById(R.id.addCustomer);
        selectionHint = findViewById(R.id.selectionHint);
        individualLayout = findViewById(R.id.individualLayout);
        organizationLayout = findViewById(R.id.organizationLayout);

        organizationCheckBox = findViewById(R.id.organizationCheckBox);
        individualCheckBox = findViewById(R.id.individualCheckBox);
        //individualCheckBox.isChecked();

        individualCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                individualLayout.setVisibility(View.VISIBLE);
                organizationLayout.setVisibility(View.GONE);
                organizationCheckBox.setChecked(false);
                selectionHint.setVisibility(View.GONE);
                addCustomer.setVisibility(View.VISIBLE);

               /* if (individualCheckBox.isChecked()){
                    individualLayout.setVisibility(View.VISIBLE);
                    organizationLayout.setVisibility(View.GONE);
                    organizationCheckBox.setChecked(false);
                    selectionHint.setVisibility(View.GONE);
                    addCustomer.setVisibility(View.VISIBLE);

                } else {
                    individualLayout.setVisibility(View.GONE);
                    organizationLayout.setVisibility(View.GONE);
                    selectionHint.setVisibility(View.VISIBLE);
                    addCustomer.setVisibility(View.GONE);
                }*/
            }
        });

        individualCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        organizationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (organizationCheckBox.isChecked()){
                    organizationLayout.setVisibility(View.VISIBLE);
                    individualLayout.setVisibility(View.GONE);
                    individualCheckBox.setChecked(false);
                    selectionHint.setVisibility(View.GONE);
                    addCustomer.setVisibility(View.VISIBLE);
                } else {
                    individualLayout.setVisibility(View.GONE);
                    organizationLayout.setVisibility(View.GONE);
                    selectionHint.setVisibility(View.VISIBLE);
                    addCustomer.setVisibility(View.GONE);
                }
            }
        });
    }
}