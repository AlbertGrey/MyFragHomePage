package tw.org.iii.myfraghomepage;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei-chengni on 2018/4/19.
 */

public class LoginPage extends AppCompatActivity{
    private RequestQueue queue;
    private EditText loginaccount,loginpasswd;
    private Button loginbtn,newbtn;
    private String account,passwd;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean issign;
    private String memberid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        queue= Volley.newRequestQueue(this);

        sp = getSharedPreferences("memberdata",MODE_PRIVATE);
        editor = sp.edit();
        issign = sp.getBoolean("signin",true);
        memberid = sp.getString("member","0");
        Log.v("grey","logicboolean = "+(issign?true:false));
        Log.v("grey","memberidLogin ="+memberid);

        loginaccount = findViewById(R.id.login_account);
        loginpasswd = findViewById(R.id.login_passwd);
        loginbtn = findViewById(R.id.login_button);
        newbtn = findViewById(R.id.login_newbutton);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = loginaccount.getText().toString();
                passwd = loginpasswd.getText().toString();
                sighin(account, "",passwd,"normal");
                Log.v("grey",account+":"+passwd);
                loginaccount.setText("");
                loginpasswd.setText("");
            }
        });

        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://topic-timgyes123.c9users.io/phoneregister.html");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
    private void sighin(String mail,String name,String password,String type){
        final String p1=mail;
        final String p2=password;
        final String p3=type;
        final String p4=name;
        String url =AttrPage.urlip+"/fsit04/app/sighin";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("grey",response);
                        String res = response.trim();
                        if (res.equals("erro")){
                            Log.v("grey","error="+response);
                            errortest();
                        }else{
                            try {
                                JSONObject j2 = new JSONObject(res);
                                String mid = j2.getString("id");
                                Log.v("grey","success");
                                Log.v("grey","mid = "+mid);
                                editor.putBoolean("signin",true);
                                editor.putString("memberid",mid);
                                editor.commit();
                                Log.v("grey","logicbooleanpage = "+(issign?true:false));
                                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();
                m1.put("mail",p1);
                m1.put("password", p2);
                m1.put("type",p3);
                m1.put("name",p4);
                return m1;
            }
        };


        queue.add(stringRequest);
    }

    private void errortest(){
        new AlertDialog.Builder(LoginPage.this)
                .setTitle(" ")
                .setMessage("輸入帳號或密碼錯誤")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
