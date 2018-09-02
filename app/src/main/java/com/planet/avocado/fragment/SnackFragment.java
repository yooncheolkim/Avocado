package com.planet.avocado.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.planet.avocado.R;
import com.planet.avocado.ui.activities.ProductDetailActivity;
import com.planet.avocado.adapter.GridViewAdapter;
import com.planet.avocado.consts.Consts;
import com.planet.avocado.data.ImageItem;
import com.planet.avocado.data.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SnackFragment extends Fragment {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private View view;
    private Context context;
    private List<Product> firebaseData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_snack, container, false);
        context = container.getContext();

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplication(), ProductDetailActivity.class);
                intent.putExtra(Consts.Bundle.PRODUCT_ID, firebaseData.get(position).id);
                startActivity(intent);
            }
        });

        return view;
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

    public void setFirebaseData(List<Product> firebaseData) {
        this.firebaseData = firebaseData;
    }
}
