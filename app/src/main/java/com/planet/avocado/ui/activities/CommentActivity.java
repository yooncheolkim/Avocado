package com.planet.avocado.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.avocado.R;
import com.planet.avocado.data.Product;
import com.planet.avocado.repos.ProductRepo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";

    ImageView star1, star2, star3, star4, star5;
    TextView scoreText;
    int score;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initToolBar();

        star1=(ImageView)findViewById(R.id.star1);
        star2=(ImageView)findViewById(R.id.star2);
        star3=(ImageView)findViewById(R.id.star3);
        star4=(ImageView)findViewById(R.id.star4);
        star5=(ImageView)findViewById(R.id.star5);
        scoreText=(TextView)findViewById(R.id.scoreText);

        score=0;

        star1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initStar();
                Log.e("asdf","asdf");
                star1.setImageResource(R.drawable.ic_seed_big);
                scoreText.setText("1 점");
                score=1;
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initStar();
                star1.setImageResource(R.drawable.ic_seed_big);
                star2.setImageResource(R.drawable.ic_seed_big);
                scoreText.setText("2 점");
                score=2;
            }
        });


        star3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initStar();
                star1.setImageResource(R.drawable.ic_seed_big);
                star2.setImageResource(R.drawable.ic_seed_big);
                star3.setImageResource(R.drawable.ic_seed_big);
                scoreText.setText("3 점");
                score=3;
            }
        });


        star4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initStar();
                star1.setImageResource(R.drawable.ic_seed_big);
                star2.setImageResource(R.drawable.ic_seed_big);
                star3.setImageResource(R.drawable.ic_seed_big);
                star4.setImageResource(R.drawable.ic_seed_big);
                scoreText.setText("4 점");
                score=4;
            }
        });


        star5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initStar();
                star1.setImageResource(R.drawable.ic_seed_big);
                star2.setImageResource(R.drawable.ic_seed_big);
                star3.setImageResource(R.drawable.ic_seed_big);
                star4.setImageResource(R.drawable.ic_seed_big);
                star5.setImageResource(R.drawable.ic_seed_big);
                scoreText.setText("5 점");
                score=5;
            }
        });
    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("리뷰 남기기");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_exit);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "clicking the Back!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case R.id.commentUpload:
                //클릭 했을 시
                Toast.makeText(getApplicationContext(), "clicking on email", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initStar() {
        star1.setImageResource(R.drawable.ic_seed_big_gray);
        star2.setImageResource(R.drawable.ic_seed_big_gray);
        star3.setImageResource(R.drawable.ic_seed_big_gray);
        star4.setImageResource(R.drawable.ic_seed_big_gray);
        star5.setImageResource(R.drawable.ic_seed_big_gray);
    }



}
