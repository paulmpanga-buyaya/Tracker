package sales.pipesandconduit.tracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.crypto.CipherSpi;

public class AddCustomerFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbCustomers = database.getReference("customers");

    CheckBox individualCheckBox, organizationCheckBox;
    LinearLayout individualLayout, organizationLayout, buttonLayout;
    TextView selectionHint;
    MaterialButton addCustomer;

    //Individual details
    EditText individualName, individualContact, individualTIN;
    //Organisation details
    EditText organizationName, organizationContactPersonName, organizationContact, organizationTIN;

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_customer, container, false);
        getActivity().setTitle("Add customer");

        addCustomer = root.findViewById(R.id.addCustomer);
        selectionHint = root.findViewById(R.id.selectionHint);
        buttonLayout = root.findViewById(R.id.buttonLayout);
        individualLayout = root.findViewById(R.id.individualLayout);
        organizationLayout = root.findViewById(R.id.organizationLayout);
        organizationCheckBox = root.findViewById(R.id.organizationCheckBox);
        individualCheckBox = root.findViewById(R.id.individualCheckBox);
        //individualCheckBox.isChecked();

        individualCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (individualCheckBox.isChecked()){
                    individualLayout.setVisibility(View.VISIBLE);
                    organizationLayout.setVisibility(View.GONE);
                    organizationCheckBox.setChecked(false);
                    selectionHint.setVisibility(View.GONE);
                    addCustomer.setVisibility(View.VISIBLE);
                    buttonLayout.setVisibility(View.VISIBLE);

                } else {
                    individualLayout.setVisibility(View.GONE);
                    organizationLayout.setVisibility(View.GONE);
                    selectionHint.setVisibility(View.VISIBLE);
                    addCustomer.setVisibility(View.GONE);
                    buttonLayout.setVisibility(View.GONE);
                    selectionHint.setVisibility(View.VISIBLE);
                }
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
                    buttonLayout.setVisibility(View.VISIBLE);
                } else {
                    individualLayout.setVisibility(View.GONE);
                    organizationLayout.setVisibility(View.GONE);
                    selectionHint.setVisibility(View.VISIBLE);
                    addCustomer.setVisibility(View.GONE);
                    buttonLayout.setVisibility(View.GONE);
                    selectionHint.setVisibility(View.VISIBLE);
                }
            }
        });

        root.findViewById(R.id.addCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Customer(String key, String businessName, String name, String phoneOrEmail, String tin)
                //Individual details
                individualName = root.findViewById(R.id.individualName);
                String name = individualName.getText().toString();
                individualContact = root.findViewById(R.id.individualContact);
                String contact = individualContact.getText().toString();
                individualTIN = root.findViewById(R.id.individualTIN);
                String tin = individualTIN.getText().toString();
                //Organization details
                organizationName = root.findViewById(R.id.organizationName);
                String org_name = organizationName.getText().toString();
                organizationContactPersonName = root.findViewById(R.id.organizationContactPersonName);
                String org_contact_name = organizationContactPersonName.getText().toString();
                organizationContact = root.findViewById(R.id.organizationContact);
                String org_contact = organizationContact.getText().toString();
                organizationTIN = root.findViewById(R.id.organizationTIN);
                String org_tin = organizationTIN.getText().toString();

                if (individualCheckBox.isChecked()){
                    addCustomer("none",name,contact,tin);
                } else if (organizationCheckBox.isChecked()){
                    addCustomer(org_name,org_contact_name,org_contact,org_tin);
                }
            }
        });

        return root;
    }

    //Customer(String key, String businessName, String name, String phoneOrEmail, String tin)
    public void addCustomer(String businessName, String name, String phoneOrEmail, String tin) {
        String key = dbCustomers.push().getKey();
        Customer customer = new Customer(key,businessName,name,phoneOrEmail,tin);
        dbCustomers.child(key).setValue(customer);
        dbCustomers.orderByChild("key").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    NavHostFragment.findNavController(AddCustomerFragment.this)
                            .navigate(R.id.action_addCustomerFragment_to_customersFragment);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(AddCustomerFragment.this)
                        .navigate(R.id.action_addCustomerFragment_to_customersFragment);

            }
        });
    }

    public void selectedIndividual(){
    }
}