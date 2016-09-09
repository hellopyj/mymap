package com.example.hello.mymap.map.modle;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Lovepyj on 2016/7/15.
 */
@JsonObject
public class Properties {
    @JsonField
    private double mag;
    @JsonField
    private String place;
    @JsonField
    private long time;
    @JsonField
    private String updated;
    @JsonField
    private int tz;
    @JsonField
    private String url;
    @JsonField
    private String detail;
    @JsonField
    private String felt;
    @JsonField
    private String cdi;
    @JsonField
    private String mmi;
    @JsonField
    private String alert;
    @JsonField
    private String status;
    @JsonField
    private int tsunami;
    @JsonField
    private int sig;
    @JsonField
    private String net;
    @JsonField
    private String code;
    @JsonField
    private String ids;
    @JsonField
    private String sources;
    @JsonField
    private String types;
    @JsonField
    private int nst;
    @JsonField
    private double dmin;
    @JsonField
    private double rms;
    @JsonField
    private int gap;
    @JsonField
    private String magType;
    @JsonField
    private String type;
    @JsonField
    private String title;

    public void setMag(double mag){
        this.mag = mag;
    }
    public double getMag(){
        return this.mag;
    }
    public void setPlace(String place){
        this.place = place;
    }
    public String getPlace(){
        return this.place;
    }
    public void setTime(long time){
        this.time = time;
    }
    public long getTime(){
        return this.time;
    }
    public void setUpdated(String updated){
        this.updated = updated;
    }
    public String getUpdated(){
        return this.updated;
    }
    public void setTz(int tz){
        this.tz = tz;
    }
    public int getTz(){
        return this.tz;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setDetail(String detail){
        this.detail = detail;
    }
    public String getDetail(){
        return this.detail;
    }
    public void setFelt(String felt){
        this.felt = felt;
    }
    public String getFelt(){
        return this.felt;
    }
    public void setCdi(String cdi){
        this.cdi = cdi;
    }
    public String getCdi(){
        return this.cdi;
    }
    public void setMmi(String mmi){
        this.mmi = mmi;
    }
    public String getMmi(){
        return this.mmi;
    }
    public void setAlert(String alert){
        this.alert = alert;
    }
    public String getAlert(){
        return this.alert;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setTsunami(int tsunami){
        this.tsunami = tsunami;
    }
    public int getTsunami(){
        return this.tsunami;
    }
    public void setSig(int sig){
        this.sig = sig;
    }
    public int getSig(){
        return this.sig;
    }
    public void setNet(String net){
        this.net = net;
    }
    public String getNet(){
        return this.net;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setIds(String ids){
        this.ids = ids;
    }
    public String getIds(){
        return this.ids;
    }
    public void setSources(String sources){
        this.sources = sources;
    }
    public String getSources(){
        return this.sources;
    }
    public void setTypes(String types){
        this.types = types;
    }
    public String getTypes(){
        return this.types;
    }
    public void setNst(int nst){
        this.nst = nst;
    }
    public int getNst(){
        return this.nst;
    }
    public void setDmin(double dmin){
        this.dmin = dmin;
    }
    public double getDmin(){
        return this.dmin;
    }
    public void setRms(double rms){
        this.rms = rms;
    }
    public double getRms(){
        return this.rms;
    }
    public void setGap(int gap){
        this.gap = gap;
    }
    public int getGap(){
        return this.gap;
    }
    public void setMagType(String magType){
        this.magType = magType;
    }
    public String getMagType(){
        return this.magType;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
}
