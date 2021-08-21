package sales.pipesandconduit.tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Expense> expenseList;
    private List<Expense> expenseListFiltered;
    private ExpenseAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, date;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.amount);
            date = view.findViewById(R.id.date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onExpenseSelected(expenseListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ExpenseAdapter(Context context, List<Expense> expenseList, ExpenseAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.expenseList = expenseList;
        this.expenseListFiltered = expenseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Expense expense = expenseListFiltered.get(position);
        holder.name.setText(expense.getCategory_name());
        holder.amount.setText(String.valueOf(expense.getAmount()));
        final DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
        holder.date.setText(dateFormatter.format(new Date(expense.getDate())));
    }

    @Override
    public int getItemCount() {
        return expenseListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    expenseListFiltered = expenseList;
                } else {
                    List<Expense> filteredList = new ArrayList<>();
                    for (Expense row : expenseList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCategory_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDescription().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    expenseListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = expenseListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                expenseListFiltered = (ArrayList<Expense>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ExpenseAdapterListener {
        void onExpenseSelected(Expense expense);
    }

}
