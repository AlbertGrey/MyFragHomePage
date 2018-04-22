package tw.org.iii.myfraghomepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by wei-chengni on 2018/4/20.
 */

public class MessagePage extends AppCompatActivity {

    private WebView webView;
    private LinkedList<messListModel> data;
    private String jstring;
    private MymeslistAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);

        webView = findViewById(R.id.mes_webview);
        toolbar = (Toolbar)findViewById(R.id.mes_toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.mes_toolbar_menu);
        setSupportActionBar(toolbar);
    }




    public class MymeslistAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private LinkedList<messListModel> data;
        private messListModel reslut = new messListModel();


        public MymeslistAdapter(Context context,
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
