package tw.org.iii.myfraghomepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button loginbtn;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RequestQueue queue;
    private  SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean issign;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        queue= Volley.newRequestQueue(LoginActivity.this);

        sp = getSharedPreferences("memberdata",MODE_PRIVATE);
        editor = sp.edit();
        issign = sp.getBoolean("signin",true);
        Log.v("grey","logicboolean = "+(issign?true:false));
        callbackManager = CallbackManager.Factory.create();

        loginbtn = findViewById(R.id.login_button2);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,LoginPage.class);
                startActivity(intent);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
//        loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.v("grey","onSuccess");

                AccessToken accessToken = loginResult.getAccessToken();
                String user_id = accessToken.getUserId();
                String token = accessToken.getToken();
                Log.v("grey",user_id +":"+token);

                GraphRequest request =
                        GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday");
                                    String name = object.getString("name");
                                    Log.v("grey",name+":"+email+":"+birthday);
                                    sighin(email,name,"123","fb");
                                    editor.putBoolean("signin",true);
                                    editor.commit();
                                    Log.v("grey","logicboolean = "+(issign?true:false));
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle params = new Bundle();
                params.putString("fields","id,name,email,gender,birthday");
                request.setParameters(params);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    //

    public void newmember(View view) {
        Uri uri = Uri.parse("https://topic-timgyes123.c9users.io/phoneregister.html");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);

    }

    @Override
    public void finish() {
        super.finish();
    }

    private void sighin(String mail, String name, String password, String type){
        final String p1=mail;
        final String p2=password;
        final String p3=type;
        final String p4=name;
        String url =AttrPage.urlip+"/fsit04/app/sighin";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("grey","Responsea = "+response);
                        String res = response.trim();
                        if (res.equals("erro")){
                            Log.v("grey","error="+response);
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
}
