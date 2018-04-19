package tw.org.iii.myfraghomepage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager pager;
    private FragmentManager manager;
    private HotPage page1;
    private AttrPage page2;
    private FoodPage page3;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //詢問權限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            initdata();
        }

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //因為toolbar 而使用 tablayout
        tabLayout = findViewById(R.id.tablayout);
        //viewpager 滑動
        pager = findViewById(R.id.container);
        page1 = new HotPage();
        page2 = new AttrPage();
        page3 = new FoodPage();
        //fragment頁面
        manager = getSupportFragmentManager();
        MyAdapter adapter = new MyAdapter(manager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        //與tablayout連動
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        pager.setOffscreenPageLimit(5);
        inittablayout();
//        initActionBar();
        //螢幕寬高
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        Log.v("grey","手機寬高 ＝" + metrics.widthPixels+" X "+metrics.heightPixels);
    }

    //詢問權限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            initdata();
        }else{
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void initdata() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new HotPage());
        fragments.add(new AttrPage());
        fragments.add(new FoodPage());
    }

    private void inittablayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //使tab按鍵和pager連動
                if(pager!=null){
                    pager.setCurrentItem(tab.getPosition());
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayout.addTab(tabLayout.newTab().setText("熱門"));
        tabLayout.addTab(tabLayout.newTab().setText("景點"));
        tabLayout.addTab(tabLayout.newTab().setText("美食"));
    }



    public void topage1(View view) {
    }

    public void topage2(View view) {
    }

    public void topage3(View view) {
    }

    public void topage4(View view) {
    }

    public void topage5(View view) {
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            if (fragments != null) {
                return fragments.size();
            }
            return 0;
        }

    }
}

