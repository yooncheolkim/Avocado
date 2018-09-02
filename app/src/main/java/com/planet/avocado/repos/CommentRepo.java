package com.planet.avocado.repos;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.planet.avocado.data.Comment;
import com.planet.avocado.data.Product;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.SingleSubject;

public class CommentRepo {
    private static final String TAG = "CommentRepo";
    private static CommentRepo sInstance = null;

    public static CommentRepo getInstance() {
        if (sInstance == null) {
            synchronized (CommentRepo.class) {
                if (sInstance == null) {
                    sInstance = new CommentRepo();
                }
            }
        }

        return sInstance;
    }

    public void insert(Product product) {
        Log.d(TAG, "insert: ");

        DatabaseReference push = FirebaseDatabaseUtils.getCommentRef().push();
        product.id = push.getKey();
        push.setValue(product);
    }

    public Observable<List<Comment>> getList() {
        BehaviorSubject<List<Comment>> result = BehaviorSubject.create();

        FirebaseDatabaseUtils.getProductRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Comment> commentList = new ArrayList<>();
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            Comment commet = snapshot.getValue(Comment.class);
                            commentList.add(commet);
                        }

                        result.onNext(commentList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: ");
                    }
                });

        return result;
    }

    public Single<Comment> getOnce(String id) {
        Log.d(TAG, "getOnce() called with: id = [" + id + "]");

        SingleSubject<Comment> result = SingleSubject.create();
        FirebaseDatabaseUtils.getCommentRef()
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        result.onSuccess(comment);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return result;
    }

    public Single<List<Comment>> getListOnce() {
        Log.d(TAG, "getListOnce: ");

        SingleSubject<List<Comment>> result = SingleSubject.create();
        FirebaseDatabaseUtils.getProductRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Comment> commentList = new ArrayList<>();
                        for (DataSnapshot eachSnapshot : dataSnapshot.getChildren()) {
                            Comment comment = eachSnapshot.getValue(Comment.class);
                            commentList.add(comment);
                        }

                        result.onSuccess(commentList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return result;
    }
}
