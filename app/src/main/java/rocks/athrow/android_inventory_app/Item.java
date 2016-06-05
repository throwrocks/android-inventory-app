package rocks.athrow.android_inventory_app;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by josel on 6/5/2016.
 */
public class Item extends RealmObject {
    private static final String LOG_TAG = Item.class.getSimpleName();
    public static final String ITEM = "item";

    @PrimaryKey
    private int id;
    private String name;
    private int quantity;
    private double price;


    public void newItem(int id, String name, int quantity, float price) {
        Log.e(LOG_TAG, "set item " + name);
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId(){ return this.id;}

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }
}
