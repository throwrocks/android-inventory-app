package rocks.athrow.android_inventory_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;



/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ItemListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getTitle());
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ItemDetailDataEntry.class);
                    startActivity(intent);

                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Item> items = realm.where(Item.class).findAll();
        realm.commitTransaction();
        int size = items.size();
        Log.e(LOG_TAG, "size " + size);
        if (size > 0) {
            recyclerView.setAdapter(new ItemListAdapter(this, realm.where(Item.class).findAllAsync()));
            recyclerView.setHasFixedSize(true);
        }

    }


}
