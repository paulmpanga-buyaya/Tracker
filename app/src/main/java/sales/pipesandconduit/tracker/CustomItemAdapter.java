package sales.pipesandconduit.tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomItemAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> data = null;
    private Context mContext;
    private OnListItemClick onListItemListener;

    public CustomItemAdapter(Context context, ArrayList<HashMap<String, Object>> data, OnListItemClick onListItemListener) {
        this.mContext = context;
        this.data = data;
        this.onListItemListener = onListItemListener;
    }

    //String.format("%,d", num)

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.cart_item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.total = (TextView) view.findViewById(R.id.total);
            viewHolder.item = (TextView) view.findViewById(R.id.item);
            viewHolder.price_and_quantity = (TextView)view.findViewById(R.id.price_and_quantity);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //viewHolder.price_and_quantity.setText(String.valueOf(data.get(i).get("quantity")) + " " + "*" + " "+ String.valueOf(data.get(i).get("price")));
        viewHolder.price_and_quantity.setText(String.valueOf(String.format("%,d",data.get(i).get("quantity"))) + " " + "*" + " "+ String.valueOf(String.format("%,d",data.get(i).get("price"))));

        if ( mContext instanceof NewSaleActivity) {
            int price = Integer.valueOf(String.valueOf(data.get(i).get("price")));

            int quantity = Integer.valueOf(String.valueOf(data.get(i).get("quantity")));

            int total = price * quantity;

            viewHolder.total.setText(String.format("%,d",total));
        }else {
            viewHolder.total.setText(String.format("%,d",data.get(i).get("total")));
        }
        viewHolder.item.setText(data.get(i).get("item").toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListItemListener.onItemClick(i);
            }
        });

        return view;
    }

    static class ViewHolder {
        TextView item;
        TextView price_and_quantity;
        TextView total;
    }

    public interface OnListItemClick {
        void onItemClick(int position);
    }

}
