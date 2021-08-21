package sales.pipesandconduit.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("products");
    TextInputEditText nameText, codeText, priceText;
    MaterialButton addProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=  inflater.inflate(R.layout.fragment_add_products, container, false);

        getActivity().setTitle("Add product");

        addProduct = root.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = root.findViewById(R.id.et_product_name);
                String name = nameText.getText().toString();
                codeText = root.findViewById(R.id.et_product_code);
                String code = codeText.getText().toString();
                priceText = root.findViewById(R.id.et_product_price);
                String priceStr = priceText.getText().toString();
                int price = Integer.parseInt(priceStr);
                addItem(name,code,price);
            }
        });

        return root;
    }

    public void addItem(String name, String code, int price) {
        Product product = new Product(name,code,price);
        dbRef.child(code).setValue(product);
        dbRef.orderByChild("code").equalTo(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //Toast.makeText(getActivity(), "it has been added",Toast.LENGTH_LONG).show();
                    ProductsFragment fragment = new ProductsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,fragment)
                            .commitNow();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddProductFragment.this)
                        .navigate(R.id.action_AddProductFragment_to_ProductsFragment);
            }
        });
    }
}