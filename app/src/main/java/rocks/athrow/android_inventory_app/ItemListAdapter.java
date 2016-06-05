package rocks.athrow.android_inventory_app;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by josel on 6/5/2016.
 */
public class ItemListAdapter extends RealmRecyclerViewAdapter<Item, ItemListAdapter.MyViewHolder> {
    private static final String LOG_TAG = ItemListAdapter.class.getSimpleName();
    private final ItemListActivity activity;

    public ItemListAdapter(ItemListActivity activity, OrderedRealmCollection<Item> data) {
        super(activity, data, true);
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_list_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Item item = getData().get(position);
        holder.data = item;

        // Get the data from the Item object

        String item_name = item.getName();

        int item_id_int = item.getId();
        String item_id = Integer.toString(item_id_int);

        int item_qty_int = item.getQuantity();
        String item_qty = Integer.toString(item_qty_int);


        double item_price_double= item.getPrice();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        String item_price = defaultFormat.format(item_price_double);


        holder.item_id.setText(item_id);
        holder.item_name.setText(item_name);
        holder.item_qty.setText(item_qty);
        holder.item_price.setText(item_price);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView item_id;
        public TextView item_name;
        public TextView item_qty;
        public TextView item_price;
        public Item data;

        public MyViewHolder(View view) {
            super(view);
            item_id = (TextView) view.findViewById(R.id.item_id);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_qty = (TextView) view.findViewById(R.id.item_qty);
            item_price = (TextView) view.findViewById(R.id.item_price);
        }

    }


}
