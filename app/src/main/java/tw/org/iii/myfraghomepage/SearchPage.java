package tw.org.iii.myfraghomepage;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei-chengni on 2018/4/22.
 */

public class SearchPage extends AppCompatActivity {
    private Toolbar toolbar;
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        toolbar = findViewById(R.id.search_toolbar);
        toolbar.inflateMenu(R.menu.search_menu);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_real_btn).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("grey","submit = "+query);
                doSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("grey","change ="+newText);
                return false;
            }
        });


        return true;
    }

    private void doSearch(String param) {

        final String p1 =param;
        String url = String.format("http://36.235.38.228:8080/fsit04/Allviews");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseGetFavorite(response);

                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();

                m1.put("param",p1);


                return m1;
            }
        };

        queue.add(stringRequest);
        Log.v("grey","queue = "+stringRequest);
    }

    private void parseGetFavorite(String response){
        try {
            JSONArray array1 = new JSONArray(response);
            Log.v("chad",array1.length()+"");
            for(int i= 0;i<array1.length();i++) {
                JSONObject ob1 =array1.getJSONObject(i);
                //地點ID
                String total_id = ob1.getString("total_id");
                Log.v("chad",total_id);
                //地點名稱
                String name = ob1.getString("name");
                Log.v("chad",name);
                //地點類型
                String type= ob1.getString("type");
                Log.v("chad",type);
                //分類
                String CAT2 = ob1.getString("CAT2");
                Log.v("chad",CAT2);
                //營業時間
                String MEMO_TIME = ob1.getString("MEMO_TIME");
                Log.v("chad",MEMO_TIME);
                //地址
                String address = ob1.getString("address");
                Log.v("chad",address);
                //簡介
                String xbody = ob1.getString("xbody");
                Log.v("chad",xbody);
                //緯度
                String lat = ob1.getString("lat");
                Log.v("chad",lat);
                //經度
                String lng = ob1.getString("lng");
                Log.v("chad",lng);


                JSONArray imgs =ob1.getJSONArray("Img");
                for(int y= 0;y<imgs.length();y++){
                    String description =imgs.getJSONObject(y).getString("description");
                    Log.v("chad",description);
                    String imgUrl = imgs.getJSONObject(y).getString("url");
                    Log.v("chad",imgUrl);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
