package tw.org.iii.myfraghomepage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class AttrPage extends ListFragment {
    private LinkedList<AttrListModel> data;
    private ListView listView;
    private String jstring;
    private JSONObject jsonObject;
    private MylistAdapter adapter;
    private Button mesbtn,addbtn;
    private float screenWidth,screenHeight,newHeight;
    private boolean ismember = false;
    private RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)    {

        View v = inflater.inflate(R.layout.fragment_attr_page,container,false);
        listView=(ListView)v.findViewById(android.R.id.list);
        new attrHttpasync().execute();
        return v;
    }
    //    private void getScreen(){
//        //螢幕寬高
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        screenWidth = metrics.widthPixels;
//        screenHeight = metrics.heightPixels;
//        newHeight = screenWidth / 16 * 9;
//        Log.v("grey","手機寬高1 ＝" + metrics.widthPixels+" X "+metrics.heightPixels);
//    }
    private class attrHttpasync extends AsyncTask<String, Void, LinkedList<AttrListModel>> {

        @Override
        protected LinkedList<AttrListModel> doInBackground(String... strings) {
            JSONArray jsonArray = null;
            data = new LinkedList<>();
            jstring = JSONFuction.getJSONFromurl("http://36.235.38.228:8080/J2EE/getData.jsp?start=1&rows=15");
            try {
                jsonArray = new JSONArray(jstring);
                Log.v("grey","jason"+jsonArray);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    JSONArray imgarray = jsonObject2.getJSONArray("imgs");
                    JSONObject jsonObject3 = imgarray.getJSONObject(0);
                    AttrListModel listModel = new AttrListModel();
                    listModel.setAid(jsonObject2.getString("total_id"));
                    listModel.setName(jsonObject2.getString("stitle"));
                    listModel.setAddress(jsonObject2.getString("address"));
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
            Log.v("grey","json22 = "+jsonresult);
            adapter = new MylistAdapter(getContext(),data);
            setListAdapter(adapter);
//            SimpleAdapter adapter = new SimpleAdapter(getContext(),data,R.layout.item_layout,from,to);
            Log.v("grey","data=="+data);

//            setListAdapter(adapter);
        }
    }
    public class MylistAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private LinkedList<AttrListModel> data;
        private AttrListModel reslut = new AttrListModel();
        private TextView itemtitle;
        private TextView itemaddr;
        private ImageView itemimage;

        public MylistAdapter(Context context,
                             LinkedList<AttrListModel> linklist) {
            this.context = context;
            this.data = linklist;
            this.inflater = LayoutInflater.from(context);
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
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view==null){
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.item_layout,viewGroup,false);
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder.itemtitle = (TextView)view.findViewById(R.id.item_title);
                holder.itemaddress = (TextView)view.findViewById(R.id.item_addr);
                holder.itemimage = (ImageView)view.findViewById(R.id.item_image);
                view.setTag(holder);
                mesbtn = view.findViewById(R.id.item_message_btn);
                addbtn = view.findViewById(R.id.item_add_btn);
                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addFavorite("1",reslut.getAid());
                        Log.v("grey","result1_id = "+reslut.getAid());
                    }
                });


            }else{
                holder = (ViewHolder) view.getTag();
            }
            reslut = data.get(position);
            //set reslut to textview
            holder.itemtitle.setText(reslut.getName());
            Log.v("grey","holdername = "+reslut.getName());
            holder.itemaddress.setText(reslut.getAddress());
            Log.v("grey","holderaddr = "+data.get(position).getAddress());
            GlideApp.with(context).load(reslut.getImgs()).into(holder.itemimage);
            Log.v("grey","data.image = "+data.get(position).getImgs());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reslut = data.get(position);
                    Intent intent = new Intent(getActivity(),DetailActivity.class);
                    intent.putExtra("id",reslut.getAid());
                    Log.v("grey","attid = "+reslut.getAid());
                    intent.putExtra("name",reslut.getName());
                    intent.putExtra("addr",reslut.getAddress());
                    intent.putExtra("img",reslut.getImgs());
                    intent.putExtra("description",reslut.getDescription());
                    startActivity(intent);
                }
            });

            return view;
        }

    }
    static class ViewHolder
    {
        public ImageView itemimage;
        public TextView itemtitle;
        public TextView itemaddress;
    }
//     //* @param mail        信箱 test123@gmail.com
//     //* @param password    密碼 test123
//
//    private void sighin(String mail,String password){
//        final String p1=mail="test123@gmail.com";
//        final String p2=password="test123";
//        String url ="http://36.235.38.228:8080/fsit04/sighin.jsp";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.v("chad",response);
//                    }
//                }, null){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> m1 =new HashMap<>();
//                m1.put("mail",p1);
//                m1.put("password", p2);
//                return m1;
//            }
//        };
//
//        queue.add(stringRequest);
//    }


    private void addFavorite(String user_id,String total_id){
        String url ="http://36.235.38.228:8080/fsit04/User_favorite";

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