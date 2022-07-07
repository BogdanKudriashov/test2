package com.bohdandroid.test;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PageFragment.OnFragmentSendDataListener {

    private static final String CHANNEL_ID = "notification_channel";
    Button btnDelete;
    Button btnCreate;
    TextView tvCount;

    private ViewPager mPager;
    private FragmentPagerAdapter mPageAdapter;
    Context context;

    private SharedPreferences sPref;
    private ArrayList<String> fragmentsList = new ArrayList<>();

    int pageNumber;
    int mNotificationId = 0;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (savedInstanceState == null) {
            fragmentsList.add(String.valueOf(fragmentsList.size() + 1));
        }

        mPager = (ViewPager) findViewById(R.id.vpPager);
        btnCreate = findViewById(R.id.btn_create);
        btnDelete = findViewById(R.id.btn_delete);
        tvCount = findViewById(R.id.tv_count);

        Intent intent = getIntent();
        pageNumber = intent.getIntExtra("page_number", 0);
        Log.d("pageNumber", getLocalClassName() + " " + String.valueOf(pageNumber));
        if (pageNumber == 0) {
            loadData();
        } else {
            if(pageNumber >= 1) {
                for (int i = 1; i < pageNumber; i++) {
                    fragmentsList.add(String.valueOf(i));
                    Log.d("result", String.valueOf(i));
                }
            }
        }

        List<Fragment> mFragments = buildFragments();

        mPageAdapter = new MyFragmentPageAdapter(context, getSupportFragmentManager(), mFragments, fragmentsList);
        mPageAdapter.notifyDataSetChanged();
        mPager.setAdapter(mPageAdapter);
        mPager.setCurrentItem(fragmentsList.size() - 1);
        tvCount.setText(String.valueOf(fragmentsList.size()));

        if (fragmentsList.size() < 2) {
            btnDelete.setVisibility(View.INVISIBLE);
        }
        if (fragmentsList.size() > 1) {
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentsList.add(String.valueOf(fragmentsList.size() + 1));
                List<Fragment> mFragments = buildFragments();

                mPageAdapter = new MyFragmentPageAdapter(context, getSupportFragmentManager(), mFragments, fragmentsList);
                mPageAdapter.notifyDataSetChanged();
                mPager.setAdapter(mPageAdapter);
                mPager.setCurrentItem(fragmentsList.size() - 1);
                tvCount.setText(String.valueOf(fragmentsList.size()));
                if (fragmentsList.size() > 1) {
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentsList.remove(fragmentsList.size() - 1);
                List<Fragment> mFragments = buildFragments();

                mPageAdapter = new MyFragmentPageAdapter(context, getSupportFragmentManager(), mFragments, fragmentsList);
                mPageAdapter.notifyDataSetChanged();
                mPager.setAdapter(mPageAdapter);
                mPager.setCurrentItem(fragmentsList.size() - 1);
                tvCount.setText(String.valueOf(fragmentsList.size()));

                if (mNotificationId > 0) {
                    mNotificationManager.cancel(mNotificationId);
                    //tvCount.setText("Delete NiD: " + mNotificationId);
                    mNotificationId = mNotificationId - 1;
                }

                if (tvCount.getText().toString().equalsIgnoreCase("0")) {
                    saveData();
                    finish();
                    System.exit(0);
                }

                if (fragmentsList.size() < 1) {
                    btnDelete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private List<Fragment> buildFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        for(int i = 0; i < fragmentsList.size(); i++) {
            fragments.add(PageFragment.newInstance(i));
        }
        return fragments;
    }

    @Override
    public void onSendData(String data) {
        mNotificationId = Integer.parseInt(data);
    }

    @Override
    public void onBackPressed() {

        saveData();
        finish();
        System.exit(0);
    }

    public void loadData() {
        sPref = getPreferences(MODE_PRIVATE);

        int valueInt = sPref.getInt("var1", 0);
        Log.d("result", "valueInt: " + String.valueOf(valueInt));

        if(valueInt > 0) {
            for (int i = 1; i <= valueInt; i++) {
                fragmentsList.add(String.valueOf(i));
                Log.d("result", String.valueOf(i));
            }
        }
        mNotificationId = sPref.getInt("var2", 0);
    }

    public void saveData() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("var1", fragmentsList.size() - 1);
        ed.putInt("var2", mNotificationId);
        ed.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }
}