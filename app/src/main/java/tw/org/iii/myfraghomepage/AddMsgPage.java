package tw.org.iii.myfraghomepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by wei-chengni on 2018/4/26.
 */

public class AddMsgPage extends AppCompatActivity {
    private TextView username,totalid;
    private EditText editText;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmsgpage_layout);

        username = findViewById(R.id.addmsg_user_name);
        totalid = findViewById(R.id.addmsg_totalid);
        editText = findViewById(R.id.addmsg_et);

        toolbar  = findViewById(R.id.addmsg_toolbar);
        toolbar.setTitle("留言");
        setSupportActionBar(toolbar);

    }
}
