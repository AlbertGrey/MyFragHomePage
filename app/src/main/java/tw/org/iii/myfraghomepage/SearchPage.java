package tw.org.iii.myfraghomepage;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by wei-chengni on 2018/4/22.
 */

public class SearchPage extends AppCompatActivity {
    private Toolbar toolbar;
    private RequestQueue queue;
    private ListView listView;
    private SearchAdapter adapter;
    private LinkedList<AttrListModel> data;
    private String jstring;
    private SearchAsync searchAsync;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean issign;
    private String memberid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        toolbar = findViewById(R.id.search_toolbar);
        toolbar.inflateMenu(R.menu.search_menu);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        queue= Volley.newRequestQueue(SearchPage.this);
        listView = findViewById(R.id.search_list);

        //member
        sp = getSharedPreferences("memberdata",MODE_PRIVATE);
        editor = sp.edit();
        issign = sp.getBoolean("signin",true);
        memberid = sp.getString("memberid","2");
        Log.v("grey","detailsign = "+(issign?true:false));

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
                doSearch(query);
                Log.v("grey","submit = "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("grey","change ="+newText);
                doSearch(newText);
                return false;
            }
        });


        return true;
    }



    private void doSearch(String param) {

        final String p1 =param;
        String url = String.format(AttrPage.urlip+"/fsit04/Allviews");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new SearchAsync().execute(response);
                        Log.v("grey","ressss = "+response);
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();

                m1.put("param",p1);
                Log.v("grey","param + "+m1);
                return m1;


            }
        };

        queue.add(stringRequest);
        Log.v("grey","queue = "+stringRequest);
    }

    private class SearchAsync extends AsyncTask <String ,Void,LinkedList<AttrListModel>>{

        @Override
        protected LinkedList<AttrListModel> doInBackground(String... strings) {
            String jstring = strings[0];
            data = new LinkedList<>();
            Log.v("grey","data = "+jstring);
            try {
                JSONArray jsonArray = new JSONArray(jstring);
                Log.v("grey","jason"+jsonArray);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    JSONArray imgarray = jsonObject2.getJSONArray("Img");
                    JSONObject jsonObject3 = imgarray.getJSONObject(0);

                    AttrListModel listModel = new AttrListModel();

                    listModel.setAid(jsonObject2.getString("total_id"));
                    listModel.setName(jsonObject2.getString("name"));
                    listModel.setAddress(jsonObject2.getString("address"));
                    listModel.setOpentime(jsonObject2.getString("MEMO_TIME"));
                    listModel.setDescription(jsonObject2.getString("xbody"));
                    listModel.setImgs(jsonObject3.getString("url"));
                    data.add(listModel);
                }
                Log.v("grey","json = "+jsonArray);
                Log.v("grey","data = "+data);
                return data;
            } catch (Exception e) {
                Log.v("grey","error22 = " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(LinkedList jsonresult) {
            super.onPostExecute(jsonresult);
            Log.v("grey","jsonsearch = "+jsonresult);
            adapter = new SearchAdapter(SearchPage.this,data);
            listView.setAdapter(adapter);
            Log.v("grey","data=="+data);

        }
    }

    public class SearchAdapter extends BaseAdapter{

        private Context context;
        private LayoutInflater inflater;
        private LinkedList<AttrListModel> datas;
        private AttrListModel reslut = new AttrListModel();
        private TextView itemtitle;
        private TextView itemaddr;

        public SearchAdapter(Context context,
                             LinkedList<AttrListModel> linklist){
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.datas = linklist;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            reslut = datas.get(i);
            if(view==null){
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.item_layout,viewGroup,false);
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder.itemtitle = (TextView)view.findViewById(R.id.item_title);
                holder.itemaddress = (TextView)view.findViewById(R.id.item_addr);
                holder.itemimage = (ImageView)view.findViewById(R.id.item_image);


                view.setTag(holder);
                holder.mesbtn = view.findViewById(R.id.item_message_btn);
                holder.addbtn = view.findViewById(R.id.item_add_btn);
                Log.v("grey","resaid = "+reslut.getAid());
            }else{
                holder = (ViewHolder) view.getTag();
            }
            reslut = datas.get(i);
            //set reslut to textview
            holder.itemtitle.setText(reslut.getName());
            Log.v("grey","holdername = "+reslut.getName());
            holder.itemaddress.setText(reslut.getAddress());
            Log.v("grey","holderaddr = "+data.get(i).getAddress());
            GlideApp.with(context).load(reslut.getImgs()).into(holder.itemimage);
            Log.v("grey","data.image = "+data.get(i).getImgs());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reslut = datas.get(i);
                    Intent intent = new Intent(SearchPage.this,DetailActivity.class);
                    intent.putExtra("total_id",reslut.getAid());
                    Log.v("grey","attid = "+reslut.getAid());
                    intent.putExtra("name",reslut.getName());
                    intent.putExtra("addr",reslut.getAddress());
                    intent.putExtra("img",reslut.getImgs());
                    intent.putExtra("description",reslut.getDescription());
                    intent.putExtra("opentime",reslut.getOpentime());
                    startActivity(intent);
                }
            });
            //addbtn
            holder.addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (issign==true){
                        reslut = datas.get(i);
                        addFavorite(memberid,reslut.getAid());
                        Log.v("grey",reslut.getAid());
                        addtest();
                    }else {
                        Intent intent = new Intent(SearchPage.this,LoginActivity.class);
                        startActivity(intent);
                    }

                }
            });
            //mesbtn
            holder.mesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(SearchPage.this,MessagePage.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    static class ViewHolder
    {
        public TextView itemtitle;
        public TextView itemaddress;
        public ImageView itemimage;
        public ImageButton mesbtn;
        public ImageButton addbtn;
    }


    private void addtest(){
        new AlertDialog.Builder(SearchPage.this)
                .setTitle(" ")
                .setMessage("成功")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
                    }
                }).show();
    }

    private void addFavorite(String user_id,String total_id){
        String url =AttrPage.urlip+"/fsit04/User_favorite";
        Log.v("grey","user_id = "+ user_id);
        Log.v("grey","total_id="+total_id);
        final String p1 =user_id;
        final String p2=total_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("grey","attress = "+response);
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();
                m1.put("user_id",p1);
                m1.put("total_id", p2);
                return m1;
            }
        };
        queue.add(stringRequest);

    }

}
