package com.planet.avocado.repos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.planet.avocado.data.Product;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.SingleSubject;

public class ProductRepo {
    private static final String TAG = "ProductRepo";
    private static ProductRepo sInstance = null;

    public static ProductRepo getInstance() {
        if (sInstance == null) {
            synchronized (ProductRepo.class) {
                if (sInstance == null) {
                    sInstance = new ProductRepo();
                }
            }
        }

        return sInstance;
    }

    public void insert(Product product) {
        Log.d(TAG, "insert: ");

        DatabaseReference push = FirebaseDatabaseUtils.getProductRef().push();
        product.id = push.getKey();
        push.setValue(product);
    }

    public Observable<List<Product>> getList() {
        BehaviorSubject<List<Product>> result = BehaviorSubject.create();

        FirebaseDatabaseUtils.getProductRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Product> productList = new ArrayList<>();
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            Product product = snapshot.getValue(Product.class);
                            productList.add(product);
                        }

                        result.onNext(productList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: ");
                    }
                });

        return result;
    }

    public Single<Product> getOnce(String id) {
        Log.d(TAG, "getOnce() called with: id = [" + id + "]");

        SingleSubject<Product> result = SingleSubject.create();
        FirebaseDatabaseUtils.getProductRef()
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Product product = dataSnapshot.getValue(Product.class);
                        result.onSuccess(product);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return result;
    }

    public Single<List<Product>> getListOnce() {
        Log.d(TAG, "getListOnce: ");

                    SingleSubject<List<Product>> result = SingleSubject.create();
        FirebaseDatabaseUtils.getProductRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Product> productList = new ArrayList<>();
                            for (DataSnapshot eachSnapshot : dataSnapshot.getChildren()) {
                                Product product = eachSnapshot.getValue(Product.class);
                                productList.add(product);
                            }

                            result.onSuccess(productList);
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return result;
    }
}
