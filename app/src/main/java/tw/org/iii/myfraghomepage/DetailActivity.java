package tw.org.iii.myfraghomepage;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity{
    private TextView name,address,description,id,opentime,phone;
    private ImageView img;
    private String imgs,aid;
    private boolean ismember = false;
    private RequestQueue queue;
    private Context context;

    private Toolbar toolbar;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean issign;
    private String memberid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailactivity_layout);

        context = this;
        queue= Volley.newRequestQueue(this);

        //member
        sp = getSharedPreferences("memberdata",MODE_PRIVATE);
        editor = sp.edit();
        issign = sp.getBoolean("signin",true);
        memberid = sp.getString("memberid","2");
        Log.v("grey","detailsign = "+(issign?true:false));

        toolbar = findViewById(R.id.detail_toolbar);
        toolbar.inflateMenu(R.menu.mes_toolbar_menu);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        id = findViewById(R.id.detail_id);
        name = findViewById(R.id.detail_name);
        address = findViewById(R.id.detail_address);
        img = findViewById(R.id.detail_img);
        description = findViewById(R.id.detail_description);
        opentime = findViewById(R.id.detail_memotime);
        phone = findViewById(R.id.detail_phone);



        Intent intent = getIntent();
        id.setText(intent.getStringExtra("total_id"));
        aid = id.getText().toString();
        name.setText(intent.getStringExtra("name"));
        address.setText(intent.getStringExtra("addr"));
        opentime.setText(intent.getStringExtra("opentime"));
        description.setText(intent.getStringExtra("description"));
        phone.setText(intent.getStringExtra("phone"));
        imgs = intent.getStringExtra("img");
        GlideApp.with(this).load(imgs).into(img);
        Log.v("grey","id = "+id);
        Log.v("grey","aid = "+aid);
        Log.v("grey","name = ?"+name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mes_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_button:
                if(issign==true){
                    addFavorite(memberid,aid);
                    Log.v("grey","aid"+aid);
                    showAletDialog();
                }else{
                    Intent intent = new Intent(DetailActivity.this,LoginActivity.class);
                    startActivity(intent);

                }
                break;
            case R.id.mes_button:
                Intent intent = new Intent(DetailActivity.this,MessagePage.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAletDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = new AlertDialogFragment();
        newFragment.show(ft,"dialog");
    }

    private void addFavorite(String user_id,String total_id){
        String url =AttrPage.urlip+"/fsit04/User_favorite";

        final String p1 =user_id;
        final String p2=total_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("chad",response);
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
