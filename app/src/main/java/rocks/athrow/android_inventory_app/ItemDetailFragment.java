package rocks.athrow.android_inventory_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rocks.athrow.android_inventory_app.dummy.DummyContent;


public class ItemDetailFragment extends Fragment {

    private static final String LOG_TAG = ItemListAdapter.class.getSimpleName();
    public static final String ARG_ITEM_ID = "item_id";

    private Realm realm;


    private DummyContent.DummyItem mItem;
    private final String imageInSD = "/data/user/0/rocks.athrow.android_inventory_app/files/profile.jpg";

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        int itemId = intent.getIntExtra("item_id",0);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Item> item = realm.where(Item.class).equalTo("id", itemId).findAll();
        realm.commitTransaction();


        String item_name = item.get(0).getName();



        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                getActivity().setTitle(item_name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        ImageView myImageView = (ImageView) getActivity().findViewById(R.id.item_image);
        myImageView.setImageBitmap(bitmap);




        return rootView;
    }
}
