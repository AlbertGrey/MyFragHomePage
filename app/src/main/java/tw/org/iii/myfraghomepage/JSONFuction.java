package tw.org.iii.myfraghomepage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wei-chengni on 2018/4/10.
 */

public class JSONFuction {
    public static String getJSONFromurl(String url){
        String jsonstring=null;
        StringBuffer sb= new StringBuffer();
        String result=null;
        JSONObject jsonObject = null;
        try {

            URL murl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)murl.openConnection();
            conn.connect();

            BufferedReader breader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Log.v("grey", "reader = " + breader);
            while ((jsonstring = breader.readLine()) != null) {
                sb.append(jsonstring+"\n");
            }
            Log.v("grey", "sb= " + sb);
            breader.close();
            result = sb.toString();
            Log.v("grey", "result = " + result);
        }catch(Exception e){
            Log.v("grey","error333="+e.toString());
        }
//        try {
//            jsonObject = new JSONObject(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return result;
    }
}
