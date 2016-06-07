package rocks.athrow.android_inventory_app;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class ItemDetailFragment extends Fragment {

    private static final String LOG_TAG = ItemListAdapter.class.getSimpleName();
    public static final String ARG_ITEM_ID = "item_id";

    private int itemId;
    private String itemName;
    private String itemPrice;
    private String itemQty;
    private String itemVendorName;
    private String itemVendorEmail;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        itemId = intent.getIntExtra("item_id", 0);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Item> items = realm.where(Item.class).equalTo("id", itemId).findAll();
        realm.commitTransaction();


        Item item = items.get(0);
        itemName = item.getName();
        double price = item.getPrice();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        itemPrice = defaultFormat.format(price);
        int qty = item.getQuantity();
        itemQty = Integer.toString(qty);
        itemVendorName = item.getVendorName();
        itemVendorEmail = item.getVendorEmail();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        TextView itemPriceView = (TextView) rootView.findViewById(R.id.item_detail_price);
        TextView itemQtyView = (TextView) rootView.findViewById(R.id.item_detail_qty);
        ImageView itemImageView = (ImageView) rootView.findViewById(R.id.item_detail_image);
        TextView itemVendorNameView = (TextView) rootView.findViewById(R.id.item_detail_vendor_name);
        TextView itemVendorEmailView = (TextView) rootView.findViewById(R.id.item_detail_vendor_email);

        Button itemQuantityAddButton = (Button)  rootView.findViewById(R.id.item_quantity_add);
        Button itemQuantityRemoveButton = (Button)  rootView.findViewById(R.id.item_quantity_remove);

        Button itemDeleteButton = (Button) rootView.findViewById(R.id.item_detail_delete);
        Button itemReorderButton = (Button) rootView.findViewById(R.id.item_detail_reorder);

        // Set the TextViews
        itemPriceView.setText(itemPrice);
        itemQtyView.setText(itemQty);
        itemVendorNameView.setText(itemVendorName);
        itemVendorEmailView.setText(itemVendorEmail);

        // Get the app's files directory
        ContextWrapper cw = new ContextWrapper(getActivity());
        File filesDir = cw.getFilesDir();
        // Load the item image
        String itemImageDir = filesDir.toString();
        String itemImagePath = itemImageDir + "/" + itemId;
        Log.e(LOG_TAG, itemImageDir);
        Bitmap bitmap = BitmapFactory.decodeFile(itemImagePath);
        // Set the image view
        itemImageView.setImageBitmap(bitmap);

        // Set the button click listeners
        itemQuantityAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityAdd(itemId);
            }
        });
        itemQuantityRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityRemove(itemId);
            }
        });
        itemReorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemReorder(itemId);
            }
        });
        itemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDelete(itemId);
            }
        });

        return rootView;
    }

    public void quantityAdd(int itemId){
        modifyQuantityOnHand("add", itemId);
    }
    public void quantityRemove(int itemId){
        modifyQuantityOnHand("remove", itemId);
    }

    public void modifyQuantityOnHand(String action, int itemId) {

        Context context = getActivity().getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        Toast toast;

        switch (action) {
            case "add":
                text = "added from" + itemId;
                toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
            case "remove":
                text =  "removed from" + itemId;
                toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
            default:
                break;

        }
    }

    public void itemReorder(int itemId){
        Context context = getActivity().getApplicationContext();
        CharSequence text = "Reorder " + itemId;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void itemDelete(int itemId){
        Context context = getActivity().getApplicationContext();
        CharSequence text = "Delete " + itemId;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
