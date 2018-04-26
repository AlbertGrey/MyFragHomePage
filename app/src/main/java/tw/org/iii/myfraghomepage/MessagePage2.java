package tw.org.iii.myfraghomepage;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import static tw.org.iii.myfraghomepage.AttrPage.urlip;

/**
 * Created by wei-chengni on 2018/4/25.
 */

public class MessagePage2 extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private MeslistAdapter adapter;
    private LinkedList<messListModel> data;
    private String jstring;
    private Intent intent;
    private String totalid,name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagepage2_layout);

        listView = findViewById(R.id.msg2_lv);
        toolbar = (Toolbar)findViewById(R.id.msg2_toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.msgpage_item_menu);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        totalid = intent.getStringExtra("total_id");
        name = intent.getStringExtra("name");
        new MsgAsync().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.msgpage_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.msg_edit:
                if (MainActivity.issignin==true){
                    Log.v("grey","留言頁");
                    Intent intent = new Intent(MessagePage2.this,AddMsgPage.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MessagePage2.this,LoginActivity.class);
                    startActivity(intent);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private class MsgAsync extends AsyncTask<String,Void,LinkedList<messListModel>>{

        @Override
        protected LinkedList<messListModel> doInBackground(String... strings) {
            JSONArray jsonArray = null;
            data = new LinkedList<>();
            jstring = JSONFuction.getJSONFromurl(AttrPage.urlip+"/fsit04/Views_message?total_id="+totalid);
            Log.v("gery","jstringa = "+jstring);

            try {
                jsonArray = new JSONArray(jstring);
                Log.v("grey","jason"+jsonArray);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                    messListModel listModel = new messListModel();

                    listModel.setMid(jsonObject2.getString("total_id"));
                    listModel.setMname(jsonObject2.getString("user_name"));
                    listModel.setMmessage(jsonObject2.getString("msg"));
                }
                Log.v("grey","json = "+jsonArray);
                Log.v("grey","data = "+data);
                return data;
            } catch (Exception e) {
                Log.v("grey","errormsg = " + e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(LinkedList jsonresult) {
            super.onPostExecute(jsonresult);
            Log.v("grey","jsonmsg = "+jsonresult);
            adapter = new MeslistAdapter(MessagePage2.this,data);
            listView.setAdapter(adapter);
            Log.v("grey","data=="+data);
        }
    }


    public class MeslistAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private LinkedList<messListModel> data;
        private messListModel reslut = new messListModel();


        public MeslistAdapter(Context context,
                                LinkedList<messListModel> linklist) {
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
            messViewHolder viewHolder;
            if(view==null){
                viewHolder = new messViewHolder();
                view = inflater.inflate(R.layout.message_item_layout,viewGroup,false);
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                viewHolder.messitemid = view.findViewById(R.id.mes_item_total_id);
                viewHolder.messitemname = view.findViewById(R.id.mes_item_username);
                viewHolder.messitemmessage = view.findViewById(R.id.mes_item_mes);
                view.setTag(viewHolder);
            }else{
                viewHolder = (messViewHolder)view.getTag();
            }
            reslut = data.get(position);
            //set reslut to textview
            viewHolder.messitemname.setText(reslut.getMname());
            viewHolder.messitemid.setText(reslut.getMid());
            viewHolder.messitemmessage.setText(reslut.getMmessage());

            return view;
        }

    }

    static class messViewHolder
    {
        public TextView messitemname;
        public TextView messitemid;
        public TextView messitemmessage;
    }


}
