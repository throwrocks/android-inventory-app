package rocks.athrow.android_inventory_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

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
        // create Intent to take a picture and return control to the calling application
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs(); // <----
        File image = new File(imagesFolder, "image_001.jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, 0);
    }

    public void updateItem(View view) {
        EditText nameField = (EditText) view.findViewById(R.id.item_name);
        EditText quantityField = (EditText) view.findViewById(R.id.item_quantity);
        EditText priceField = (EditText) view.findViewById(R.id.item_price);
        String name = nameField.getText().toString();
        int quantity =  Integer.parseInt(quantityField.getText().toString());
        float price = Float.parseFloat(priceField.getText().toString());
        Item newItem = new Item();
        newItem.setItem(name, quantity, price);

    }

    public void saveNewItem(View view){
        Context context = getApplicationContext();
        CharSequence text = "Item Saved!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void cancelNewItem(View view){
        exitByBackKey();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
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
