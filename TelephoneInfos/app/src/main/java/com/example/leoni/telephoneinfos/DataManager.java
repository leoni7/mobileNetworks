package com.example.leoni.telephoneinfos;

/**
 * Created by Leoni on 07.11.17.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;

import java.util.ArrayList;
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


    List<String> cellIds;
    List<List<String > > cellInfos;


    String type;
    boolean roaming;
    String activeState;
    String bandwidth_down;
    String bandwidth_up;
    String wifi;
    boolean isAvailable;

    String interfaceName;



    private int baseStationLat = 0;
    private int baseStationLon = 0;
    private int baseStationId = 0;
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
    public DataManager(Context con){

        telMan = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
        connMan = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

        cellInfos = new ArrayList<List<String>>();
        cellIds = new ArrayList<String>();

        TelephoneInfo(con);
        ConnectivityInfo();

    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
    private void TelephoneInfo(Context con){

        //if no cdma cell = location, location listener and via gps last seen location
        boolean depricated_function = true;
        try{
            List<CellInfo> infos  = telMan.getAllCellInfo();
            depricated_function = false;
            for(CellInfo info_cell : infos){

                if(info_cell instanceof CellInfoCdma){

                    if(info_cell.isRegistered()) {
                        CellInfoCdma registered = (CellInfoCdma) info_cell;
                        CellIdentityCdma cellIdentity = registered.getCellIdentity();
                        baseStationLon = cellIdentity.getLongitude();
                        baseStationLat = cellIdentity.getLatitude();
                        baseStationId = cellIdentity.getBasestationId();
                    }else{
                        CellInfoCdma registered = (CellInfoCdma) info_cell;
                        CellIdentityCdma cellIdentity = registered.getCellIdentity();
                        List<String> strings = new ArrayList<String>();
                        strings.add("CDMA");
                        strings.add(Integer.toString(cellIdentity.getLongitude()));
                        strings.add(Integer.toString(cellIdentity.getLatitude()));
                        strings.add(Integer.toString( cellIdentity.getBasestationId()));
                    }

                    cellIds.add("CDMA");

                }else if(info_cell instanceof CellInfoGsm){
                    CellInfoGsm gsm = (CellInfoGsm) info_cell;
                    CellIdentityGsm id_gsm = (CellIdentityGsm) ((CellInfoGsm) info_cell).getCellIdentity();

                    List<String> strings = new ArrayList<String>();
                    strings.add("GSM");
                    strings.add(Integer.toString(id_gsm.getCid()));
                    strings.add(Integer.toString(id_gsm.getLac()));
                    strings.add(Integer.toString(id_gsm.getMnc()));

                    cellInfos.add(strings);

                    cellIds.add("GSM " + Integer.toString(id_gsm.getCid()) + Integer.toString(id_gsm.getLac()) + Integer.toString(id_gsm.getMnc()));


                }else if(info_cell instanceof CellInfoLte){
                    CellInfoLte lte = (CellInfoLte) info_cell;
                    cellIds.add("LTE");
                    cellInfos.add(new ArrayList<String>(){{add("not defined yet");}});
                    //not defined yet

                    cellInfos.add(new ArrayList<String>(){{add("not defined yet");}});
                }else if(info_cell instanceof CellInfoWcdma){
                    CellInfoWcdma wcdma = (CellInfoWcdma) info_cell;
                    CellIdentityWcdma id_wcdma = wcdma.getCellIdentity();
                    List<String> strings = new ArrayList<String>();

                    strings.add(Integer.toString(id_wcdma.getCid()));
                    strings.add(Integer.toString(id_wcdma.getLac()));
                    strings.add(Integer.toString(id_wcdma.getMnc()));

                    cellIds.add(id_wcdma.toString());
                    cellInfos.add(new ArrayList<String>(){{add("not defined yet");}});
                    //not defined yet
                }else{
                    //no cells
                    cellIds.add("no cells");
                    cellInfos.add(new ArrayList<String>(){{add("no information");}});
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }

        if(depricated_function == true) {
            try {
                List<NeighboringCellInfo> infos = telMan.getNeighboringCellInfo();

                for (int i = 0; i < infos.size(); i++) {

                    List<String> strings = new ArrayList<String>();
                    strings.add("Cid: " + Integer.toString(infos.get(i).getCid()));
                    strings.add("Lac: " + Integer.toString(infos.get(i).getLac()));
                    strings.add("Psc: " + Integer.toString(infos.get(i).getPsc()));
                    //add more

                }

                CdmaCellLocation location = (CdmaCellLocation) telMan.getCellLocation();
                if (location != null) {
                    baseStationId = location.getBaseStationId();
                    baseStationLat = location.getBaseStationLatitude();
                    baseStationLon = location.getBaseStationLongitude();
                }
            } catch (Exception e1) {
                List<String> strings = new ArrayList<String>();
                strings.add("no cell information available! ");
                cellInfos.add(strings);
            }
        }
        serial = telMan.getSimSerialNumber();
        //serial = Settings.Secure.getString(con.getContentResolver(),
        //        Settings.Secure.ANDROID_ID);
        countryCode = telMan.getNetworkCountryIso();
        operatorNumber = telMan.getNetworkOperatorName();
        dataNetworkType = telMan.getNetworkType();
        //voiceCallType = telMan.getVoiceNetworkType();
        //List<CellInfo> info = manager.getAllCellInfo();

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void ConnectivityInfo() {
        try {
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

                interfaceName = connMan.getLinkProperties(active).getInterfaceName();
            }
        }catch(Exception e){
            type = "no active network available";
        }

    }

    public String getSerial() {
        return serial;
    }

    public TelephonyManager getManager() {
        return telMan;
    }

    public String getCountryCode() {
        if(countryCode.isEmpty()){
            countryCode = "not available";
        }
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

    public String getVoiceCallType() {
        return Integer.toString(voiceCallType);
    }

    public void setVoiceCallType(int voiceCallType) {
        this.voiceCallType = voiceCallType;
    }

    public String getType() {
        return type;
    }

    public String isRoaming() {
        String val = "";
        if(roaming == true){

            val = "on";
        }else{
            val = "off";
        }
        return val;
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

    public List<String> getCellIds() {
        return cellIds;
    }

    public List<List<String>> getCellInfos() {
        return cellInfos;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

}


