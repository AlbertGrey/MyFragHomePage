package tw.org.iii.myfraghomepage;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class HotPage extends ListFragment {
    private LinkedList<AttrListModel> data;
    private ListView listView;
    private String jstring;
    private JSONObject jsonObject;
    private MyhotlistAdapter adapter;
    private ImageButton mesbtn,addbtn;
    private float screenWidth,screenHeight,newHeight;
    private RequestQueue queue;
    private ViewHolder holder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)    {
        queue= Volley.newRequestQueue(getContext());
        View v = inflater.inflate(R.layout.fragment_hot_page,container,false);
        listView=(ListView)v.findViewById(android.R.id.list);
        new HotHttpasync().execute();
        return v;
    }

    private class HotHttpasync extends AsyncTask<String, Void, LinkedList<AttrListModel>>{

        @Override
        protected LinkedList<AttrListModel> doInBackground(String... strings) {
            JSONArray jsonArray = null;
            data = new LinkedList<>();
            jstring = JSONFuction.getJSONFromurl(AttrPage.urlip+"/fsit04/User_favorite?user_id=3");
            Log.v("gery","jstringhot= "+jstring);

            try {
                jsonArray = new JSONArray(jstring);
                Log.v("grey","jasonhot"+jsonArray);
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
            Log.v("grey","json22 = "+jsonresult);
            adapter = new MyhotlistAdapter(getActivity(),data);
            setListAdapter(adapter);
            Log.v("grey","data=="+data);

        }
    }

    private class MyhotlistAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater inflater;
        private LinkedList<AttrListModel> datas;
        private AttrListModel reslut = new AttrListModel();
        private TextView itemtitle;
        private TextView itemaddr;
        private ImageView itemimage;

        public MyhotlistAdapter(Context context,
                                LinkedList<AttrListModel> linklist) {
            this.context = context;
            this.datas = linklist;
            this.inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return datas.size();
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
            //set reslut to textview
            holder.itemtitle.setText(reslut.getName());
            Log.v("grey","holdername = "+reslut.getName());
            holder.itemaddress.setText(reslut.getAddress());
            Log.v("grey","holderaddr = "+datas.get(i).getAddress());
            GlideApp.with(context).load(reslut.getImgs()).into(holder.itemimage);
            Log.v("grey","data.image = "+datas.get(i).getImgs());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reslut = datas.get(i);
                    Intent intent = new Intent(getActivity(),DetailActivity.class);
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
                    MainActivity.issignin = MainActivity.sp.getBoolean("signin",true);
                    Log.v("grey","issighhot =="+MainActivity.issignin);
                    MainActivity.memberid = MainActivity.sp.getString("memberid","1");
                    Log.v("grey","memberidhot = "+MainActivity.memberid);
                    if (MainActivity.issignin==true){
                        reslut = datas.get(i);
                        addFavorite(MainActivity.memberid,reslut.getAid());
                        Log.v("grey",reslut.getAid());
                        showAletDialog();
                    }else {
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }

                }
            });
            //mesbtn
            holder.mesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(),MessagePage.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    private void showAletDialog(){
        DialogFragment newFragment = AlertDialogFragment.newInstance(1);
        newFragment.show(getFragmentManager(), "dialog");
    }

    static class ViewHolder
    {
        public ImageView itemimage;
        public TextView itemtitle;
        public TextView itemaddress;
        public ImageButton mesbtn;
        public ImageButton addbtn;
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

                        Log.v("grey","hotress = "+response);
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


