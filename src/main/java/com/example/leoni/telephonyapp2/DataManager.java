package com.example.leoni.telephonyapp2;

/**
 * Created by Leoni on 31.10.17.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;

import java.util.List;

/**
 * Created by Leoni on 31.10.17.
 */

public class DataManager {

    TelephonyManager telMan;
    ConnectivityManager connMan;

    String serial;
    String countryCode;
    String operatorNumber;
    int dataNetworkType;
    int voiceCallType;

    public List<CellInfo> getCellInfo() {
        return cellInfo;
    }

    List<CellInfo> cellInfo;


    String type;
    boolean roaming;
    String activeState;
    String bandwidth_down;
    String bandwidth_up;
    String wifi;
    boolean isAvailable;



    int baseStationLat;
    int baseStationLon;
    int baseStationId;
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
    public DataManager(Context con){

        telMan = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
        connMan = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

        TelephoneInfo(con);
        ConnectivityInfo();

    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
    private void TelephoneInfo(Context con){

        cellInfo  = telMan.getAllCellInfo();

        if(TelephonyManager.NETWORK_TYPE_CDMA == 1){
            //System.out.println("cdma!");
            CdmaCellLocation location = new CdmaCellLocation();
            baseStationId = location.getBaseStationId();
            baseStationLat = location.getBaseStationLatitude();
            baseStationLon = location.getBaseStationLongitude();


        }
        else if(TelephonyManager.NETWORK_TYPE_GSM == 1){


        }

        serial = Settings.Secure.getString(con.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        countryCode = telMan.getNetworkCountryIso();
        operatorNumber = telMan.getNetworkOperatorName();
        dataNetworkType = telMan.getNetworkType();
        voiceCallType = telMan.getVoiceNetworkType();
        //List<CellInfo> info = manager.getAllCellInfo();

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void ConnectivityInfo() {
        NetworkInfo info = connMan.getActiveNetworkInfo();
        type = info.getTypeName();

        isAvailable = info.isAvailable();
        roaming = info.isRoaming();
        activeState = info.getDetailedState().toString();
        wifi = Integer.toString(connMan.TYPE_WIFI);


        for(int i = 0; i < connMan.getAllNetworks().length; i++){
            Network[] actives = connMan.getAllNetworks();
            Network active = actives[i];
            NetworkCapabilities capa = connMan.getNetworkCapabilities(active);
            bandwidth_down = Integer.toString(capa.getLinkDownstreamBandwidthKbps()) + " kbps";
            bandwidth_up = Integer.toString(capa.getLinkUpstreamBandwidthKbps()) + " kbps";
        }
        /*NetworkCapabilities capa = connMan.getNetworkCapabilities();
        bandwidth_down = Integer.toString(capa.getLinkDownstreamBandwidthKbps()) + " kbps";
        bandwidth_up = Integer.toString(capa.getLinkUpstreamBandwidthKbps()) + " kbps";*/
    }

    public String getSerial() {
        return serial;
    }

    public TelephonyManager getManager() {
        return telMan;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOperatorNumber() {
        if(operatorNumber.isEmpty()){
            operatorNumber = "no network available";
        }
        return operatorNumber;
    }

    public void setOperatorNumber(String operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public int getDataNetworkType() {
        return dataNetworkType;
    }

    public void setDataNetworkType(int dataNetworkType) {
        this.dataNetworkType = dataNetworkType;
    }

    public int getVoiceCallType() {
        return voiceCallType;
    }

    public void setVoiceCallType(int voiceCallType) {
        this.voiceCallType = voiceCallType;
    }

    public String getType() {
        return type;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public String getActiveState() {
        return activeState;
    }

    public String getBandwidth_down() {
        return bandwidth_down;
    }

    public String getBandwidth_up() {
        return bandwidth_up;
    }

    public String getWifi() {
        return wifi;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getBaseStationLat() {
        return baseStationLat;
    }

    public int getBaseStationLon() {
        return baseStationLon;
    }

    public int getBaseStationId() {
        return baseStationId;
    }
}

