package rocks.athrow.android_inventory_app;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Item
 * A custom class of Items based on RealmObjects
 */
public class Item extends RealmObject {
    private static final String LOG_TAG = Item.class.getSimpleName();

    @PrimaryKey
    private int id;
    private String name;
    private int quantity;
    private double price;
    private String vendor_name;
    private String vendor_email;


    public void newItem(int id, String name, int quantity, float price,
                        String vendor_name, String vendor_email) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.vendor_name = vendor_name;
        this.vendor_email = vendor_email;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public String getVendorName() {
        return this.vendor_name;
    }

    public String getVendorEmail() {
        return this.vendor_email;
    }
}
