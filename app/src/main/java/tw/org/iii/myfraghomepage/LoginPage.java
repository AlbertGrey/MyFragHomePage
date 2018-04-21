package tw.org.iii.myfraghomepage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        queue= Volley.newRequestQueue(this);

        loginaccount = findViewById(R.id.login_account);
        loginpasswd = findViewById(R.id.login_passwd);
        loginbtn = findViewById(R.id.login_button);
        newbtn = findViewById(R.id.login_newbutton);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = loginaccount.getText().toString();
                passwd = loginpasswd.getText().toString();
                sighin(account,passwd);
                Log.v("grey",account+":"+passwd);
            }
        });
    }
    private void sighin(String mail,String password){
        final String p1=mail;
        final String p2=password;
        String url ="http://36.234.10.186:8080/fsit04/sighin.jsp";
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
                m1.put("mail",p1);
                m1.put("password", p2);
                return m1;
            }
        };
        queue.add(stringRequest);
    }


}
