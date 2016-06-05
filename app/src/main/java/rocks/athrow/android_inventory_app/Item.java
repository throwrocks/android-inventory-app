package rocks.athrow.android_inventory_app;

import io.realm.RealmObject;

/**
 * Created by josel on 6/5/2016.
 */
public class Item extends RealmObject {
    private static final String LOG_TAG = Item.class.getSimpleName();
    public static final String ITEM = "item";
    private String name;
    private int quantity;
    private float price;


    public void setItem(String name, int quantity, float price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public float getPrice() {
        return this.price;
    }
}
