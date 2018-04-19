package tw.org.iii.myfraghomepage;

/**
 * Created by wei-chengni on 2018/4/15.
 */

public class AttrListModel {
    private String aid;
    private String aname;
    private String aaddress;
    private String atoldesc;
    private String aopentime;
    private String atel;
    private String adescription;
    private String aimgs;

    public AttrListModel(){
    }

    public String getAid(){
        return aid;
    }
    public void setAid(String aid){
        this.aid = aid;
    }

    public String getName(){
        return aname;
    }
    public void setName(String name){
        this.aname = name;
    }

    public String getAddress(){
        return aaddress;
    }
    public void setAddress(String address){
        this.aaddress = address;
    }

    public String getToldesc(){
        return atoldesc;
    }
    public void setToldesc(String toldesc){
        this.atoldesc = toldesc;
    }

    public String getTel(){
        return atel;
    }
    public void setTel(String tel){
        this.atel = tel;
    }
    public String getImgs(){
        return aimgs;
    }
    public void setImgs(String imgs){
        this.aimgs = imgs;
    }

    public String getOpentime(){
        return aopentime;
    }
    public void setOpentime(String opentime){
        this.aopentime = opentime;
    }

    public String getDescription(){
        return adescription;
    }
    public void setDescription(String description){
        this.adescription = description;
    }
}
