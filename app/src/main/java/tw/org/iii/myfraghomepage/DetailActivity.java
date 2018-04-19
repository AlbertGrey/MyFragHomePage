package tw.org.iii.myfraghomepage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity{
    private TextView name,address,description,id;
    private ImageView img;
    private String imgs;
    private Button mes,loveadd;
    private boolean ismember = false;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailactivity_layout);

        id = findViewById(R.id.detail_id);
        name = findViewById(R.id.detail_name);
        address = findViewById(R.id.detail_address);
        img = findViewById(R.id.detail_img);
        description = findViewById(R.id.detail_description);
        mes = findViewById(R.id.detail_mes);
        loveadd = findViewById(R.id.detail_loveadd);


        mes = findViewById(R.id.detail_mes);
        loveadd = findViewById(R.id.detail_loveadd);

        Intent intent = getIntent();
        id.setText(intent.getStringExtra("id"));
        name.setText(intent.getStringExtra("name"));
        address.setText(intent.getStringExtra("addr"));
        description.setText(intent.getStringExtra("description"));
        imgs = intent.getStringExtra("img");
        GlideApp.with(this).load(imgs).into(img);
        Log.v("grey","id = "+id);

    }

    public void mes(View view) {
        if (ismember==true){

        }else {

        }

    }

    public void loveadd(View view) {
//        if (ismember==true){
//
//        }else{
//
//        }
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
    }

    private void addFavorite(String user_id,String total_id){
        String url ="http://36.234.10.186:8080/fsit04/User_favorite";

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
