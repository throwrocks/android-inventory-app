package rocks.athrow.android_inventory_app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class ItemDetailFragment extends Fragment {

    private static final String LOG_TAG = ItemListAdapter.class.getSimpleName();
    public static final String ARG_ITEM_ID = "item_id";
    private View rootView;
    private int itemId;
    private String itemPrice;
    private String itemQtyString;
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
        double price = item.getPrice();
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        itemPrice = defaultFormat.format(price);
        int itemQty = item.getQuantity();
        itemQtyString = Integer.toString(itemQty);
        itemVendorName = item.getVendorName();
        itemVendorEmail = item.getVendorEmail();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);

        TextView itemPriceView = (TextView) rootView.findViewById(R.id.item_detail_price);
        TextView itemQtyView = (TextView) rootView.findViewById(R.id.item_detail_qty);
        ImageView itemImageView = (ImageView) rootView.findViewById(R.id.item_detail_image);
        TextView itemVendorNameView = (TextView) rootView.findViewById(R.id.item_detail_vendor_name);
        TextView itemVendorEmailView = (TextView) rootView.findViewById(R.id.item_detail_vendor_email);

        FloatingActionButton sellItemButton = (FloatingActionButton) getActivity().findViewById(R.id.item_detail_sell);

        Button itemQuantityAddButton = (Button) rootView.findViewById(R.id.item_quantity_add);
        Button itemQuantityRemoveButton = (Button) rootView.findViewById(R.id.item_quantity_remove);

        Button itemDeleteButton = (Button) rootView.findViewById(R.id.item_detail_delete);
        Button itemReorderButton = (Button) rootView.findViewById(R.id.item_detail_reorder);

        // Set the TextViews
        itemPriceView.setText(itemPrice);
        itemQtyView.setText(itemQtyString);
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
        sellItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellItem(itemId);
            }
        });
        itemQuantityAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityAdd(itemId, 1);
            }
        });
        itemQuantityRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityRemove(itemId, 1);
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


    private void quantityAdd(int itemId, int itemQty) {
        modifyQuantityOnHand("add", itemId, itemQty);
    }

    private void quantityRemove(int itemId, int itemQty) {
        modifyQuantityOnHand("remove", itemId, itemQty);
    }

    /**
     * modifyQuantityOnHand
     * This method is called from the button methods and from the sell method
     *
     * @param action  add or remove
     * @param itemId  the id of the item being adjusted
     * @param itemQty the qty to be added or removed
     */
    private void modifyQuantityOnHand(String action, int itemId, int itemQty) {


        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Item> items = realm.where(Item.class).equalTo("id", itemId).findAll();

        Item item = items.get(0);

        int newQty;

        switch (action) {
            case "add":
                newQty = item.getQuantity() + itemQty;
                item.setQuantity(newQty);
                realm.commitTransaction();
                updateQuantityView(Integer.toString(newQty));
                break;
            case "remove":
                newQty = item.getQuantity() - itemQty;
                if (newQty <= 0) {
                    newQty = 0;
                }
                item.setQuantity(newQty);
                realm.commitTransaction();
                updateQuantityView(Integer.toString(newQty));
                break;
            default:
                realm.commitTransaction();
                break;

        }

    }

    /**
     * updateQuantityView
     * This methods handles updating the Quantity on Hand View
     *
     * @param newQty the new qty to be displayed
     */
    private void updateQuantityView(String newQty) {
        if (rootView == null) {
            return;
        }
        TextView itemQtyView = (TextView) rootView.findViewById(R.id.item_detail_qty);
        itemQtyView.setText(newQty);
    }


    /**
     * sellItem
     * This method is attached to the sell FAB button
     *
     * @param itemId the id of the item being sold
     */
    private void sellItem(int itemId) {
        sellTransaction(itemId);
    }

    /**
     * sellTransaction
     * This method comples a sale transaction
     * It validates that there is enough quantity to be sold, and adjusts the item's quantity if the
     * sale is valid
     *
     * @param itemId the id of the item being sold
     */
    private void sellTransaction(final int itemId) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.item_sell_dialog, null);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Item> items = realm.where(Item.class).equalTo("id", itemId).findAll();
        final Item item = items.get(0);
        realm.commitTransaction();

        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setMessage("New Sale")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        EditText sellQtyView = (EditText) view.findViewById(R.id.item_sale_qty);

                        String sellQtyString = sellQtyView.getText().toString();
                        int sellQty = Integer.parseInt(sellQtyString);
                        int newQty = item.getQuantity() - sellQty;

                        if (newQty < 0) {
                            Context context = getContext().getApplicationContext();
                            CharSequence text = "You don't have enough to sell.";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            modifyQuantityOnHand("remove", itemId, sellQty);
                            Context context = getActivity().getApplicationContext();
                            CharSequence text = "Sold!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            //close();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
        // Automatically pop up the keyboard
        // See http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
        alertbox.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    /**
     * itemReorder
     * This method handles creating an order request to the vendor
     *
     * @param itemId the id of the item being reordered
     */
    private void itemReorder(int itemId) {

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Item> items = realm.where(Item.class).equalTo("id", itemId).findAll();
        final Item item = items.get(0);
        realm.commitTransaction();

        String itemName = item.getName();
        String vendorName = item.getVendorName();
        String vendorEmail = item.getVendorEmail();

        String[] recipients = {vendorEmail};
        String message = "Hello " + vendorName + ", " +
                "\n\n Need to reorder" +
                "\n Item: " + itemName +
                "\n Qty: " + " " +
                "\n\n Thanks!";

        String subject = "Need to reorder " + itemName;

        composeEmail(recipients, subject, message);
    }

    /**
     * composeEmail
     * This method launches an intent to open the email application with some information filled in
     *
     * @param recipients the email address that you're sending the email to
     * @param subject    the email subject
     * @param message    the email body
     */
    private void composeEmail(String[] recipients, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * itemDelete
     * This method deletes the item and finishes the Detail Activity
     *
     * @param itemId the item to be deleted from the database
     */
    private void itemDelete(final int itemId) {
        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                .setMessage("Delete Item")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
                        Realm.setDefaultConfiguration(realmConfig);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        final RealmResults<Item> items = realm.where(Item.class).equalTo("id", itemId).findAll();
                        Item item = items.get(0);
                        item.deleteFromRealm();
                        realm.commitTransaction();
                        getActivity().finish();
                        //close();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }
}
