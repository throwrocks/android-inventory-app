package rocks.athrow.android_inventory_app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by josel on 6/5/2016.
 */
public class ItemDetailDataEntry extends AppCompatActivity {
    private static final String LOG_TAG = ItemDetailDataEntry.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG, "onCreate" + true);
        setContentView(R.layout.item_data_entry);
    }



    public void uploadImage(View view) {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 1000);


        // create Intent to take a picture and return control to the calling application
        /*Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getDataDirectory(), "InventoryAppItems");
        imagesFolder.mkdirs(); // <----
        File image = new File(imagesFolder, "image_001.jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, 0);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.item_image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * updateItem
     * This method is attached to the Save button onClick attribute
     * It grans all the new item's data, validates it, and saves it to the Real database
     *
     * @param view the DetailDataEntry Layout where the new item information is set in EditText Views
     */
    public void saveNewItemButton(View view) {

        Context context = getApplicationContext();
        CharSequence toastText;
        int duration = Toast.LENGTH_SHORT;


        String name = null;
        Integer quantity = null;
        Float price = null;

        EditText nameField = (EditText) this.findViewById(R.id.item_name);
        EditText quantityField = (EditText) this.findViewById(R.id.item_quantity);
        EditText priceField = (EditText) this.findViewById(R.id.item_price);
        ImageView imageField = (ImageView) this.findViewById(R.id.item_image);

        Log.e(LOG_TAG, "nameField " + nameField);

        if (nameField != null) {
            name = nameField.getText().toString();
        }
        if (quantityField != null) {
            String quantityString = quantityField.getText().toString();
            if (!quantityString.isEmpty()) {
                quantity = Integer.parseInt(quantityString);
            }

        }
        if (priceField != null) {
            String priceString = priceField.getText().toString();
            if (!priceString.isEmpty()) {
                price = Float.parseFloat(priceString);
            }
        }

        if (name == null || name.isEmpty()) {
            toastText = "Name is empty";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
        } else if (quantity == null) {
            toastText = "Quantity is empty";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
        } else if (price == null) {
            toastText = "Price is empty";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();

        }  else if (imageField == null) {
            toastText = "Price is empty";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
        }
            else {


            imageField.buildDrawingCache();
            Bitmap bm = imageField.getDrawingCache();


          saveToInternalStorage(bm);


            toastText = "Item Saved!";
            Toast toast = Toast.makeText(context, toastText, duration);
            toast.show();
            
            Item newItem = new Item();

            RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
            Realm.setDefaultConfiguration(realmConfig);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            final RealmResults<Item> items = realm.where(Item.class).findAll();
            int size = items.size();
            // Increment index
            int nextID = (size + 1);

            newItem.newItem(nextID, name, quantity, price);
            realm.copyToRealmOrUpdate(newItem);
            realm.commitTransaction();

            finish();

        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getFilesDir();
        Log.e(LOG_TAG, "nameField " + directory);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return directory.getAbsolutePath();
    }


    public void cancelNewItemButton(View view) {
        cancelNewItem();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelNewItem();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void cancelNewItem() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Cancel the New Item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

}
