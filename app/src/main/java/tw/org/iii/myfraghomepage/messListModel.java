package tw.org.iii.myfraghomepage;

/**
 * Created by wei-chengni on 2018/4/15.
 */

public class messListModel {
    private String mid;
    private String mname;
    private String mmessage;

    public messListModel(){
    }

    public String getMid(){
        return mid;
    }
    public void setMid(String mid){
        this.mid = mid;
    }

    public String getMname(){
        return mname;
    }
    public void setMname(String mname){
        this.mname = mname;
    }

    public String getMmessage(){
        return mmessage;
    }

    public void setMmessage(String mmessage){
        this.mmessage = mmessage;
    }


}
