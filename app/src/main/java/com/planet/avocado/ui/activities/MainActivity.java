package com.planet.avocado.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.planet.avocado.R;
import com.planet.avocado.adapter.ViewPagerAdapter;
import com.planet.avocado.consts.Consts;
import com.planet.avocado.data.Product;
import com.planet.avocado.fragment.SnackFragment;
import com.planet.avocado.fragment.ToyFragment;
import com.planet.avocado.fragment.LipsticFragment;
import com.planet.avocado.managers.LoginManager;
import com.planet.avocado.repos.ProductRepo;
import com.planet.avocado.ui.base.BaseActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable mProductDisposable = new CompositeDisposable();
    private ViewPager viewPager;
    public BottomNavigationView bottomNavigationView;
    ToyFragment chatFragment;
    SnackFragment callsFragment;
    LipsticFragment contactsFragment;
    private MenuItem prevMenuItem;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 2018. 9. 2. kujyp remove
        temporalCheckUserInfo();
        initViews();
        loadData();
    }

    private void temporalCheckUserInfo() {
        Log.d(TAG, "temporalCheckUserInfo: ");
        Toast.makeText(this, LoginManager.getInstance().getUser().toString(), Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        Log.d(TAG, "initViews: ");
        /*findViewById(R.id.btn_detail).setOnClickListener(v -> {
            gotoDetailActivity("1");
        });*/

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_snack:
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.navigation_toy:
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.navigation_lipstick:
                                viewPager.setCurrentItem(2);
                                return true;
                        }
                        return false;
                    }
                });


        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        View logoutBtn = findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(v -> {
            signout();
        });
    }

    private void signout() {
        Log.d(TAG, "signout: ");
        LoginManager.getInstance()
                .signout();

        gotoLoginActivity();
    }

    private void gotoLoginActivity() {
        Log.d(TAG, "gotoLoginActivity: ");
        Intent intent = new Intent(this, LoginActivity.class);
        ContextCompat.startActivity(this,
                intent,
                null);
        finish();
    }

    private void gotoDetailActivity(String productId) {
        Log.d(TAG, "gotoDetailActivity() called with: productId = [" + productId + "]");
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(Consts.Bundle.PRODUCT_ID, productId);
        ContextCompat.startActivity(this,
                intent,
                null);
    }

    private void loadData() {
        Log.d(TAG, "loadData: ");

        Observable<List<Product>> productList = ProductRepo.getInstance()
                .getList();
        changeProductSource(productList);
    }

    private void changeProductSource(Observable<List<Product>> productList) {
        Log.d(TAG, "changeProductSource: ");

        mProductDisposable.dispose();
        mProductDisposable = new CompositeDisposable();
        mProductDisposable.add(
                productList.subscribe(this::updateProductList)
        );
    }

    private void updateProductList(List<Product> productList) {
        Log.d(TAG, "updateProductList() called with: productList = [" + productList + "]");
        this.productList = productList;
        callsFragment.setFirebaseData(productList);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        callsFragment = new SnackFragment();
        chatFragment = new ToyFragment();
        contactsFragment = new LipsticFragment();
        adapter.addFragment(callsFragment);
        adapter.addFragment(chatFragment);
        adapter.addFragment(contactsFragment);
        viewPager.setAdapter(adapter);
    }
}
