package com.example.beaconapp.Model.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class UfoDeviceDB implements Parcelable {

  public static final Creator<UfoDeviceDB> CREATOR = new Creator<UfoDeviceDB>() {
    @Override
    public UfoDeviceDB createFromParcel(Parcel source) {
      return new UfoDeviceDB(source);
    }

    @Override
    public UfoDeviceDB[] newArray(int size) {
      return new UfoDeviceDB[size];
    }
  };
  private String deviceType;
  private String macID;
  private String major;
  private String minor;
  private String UUID;
  private String txPower;
  private String RSSI;
  private String distance;
  private String temp;
  private String lastUpdate;
  private String scanRecord;
  private Boolean registerDevice;
  private DeviceLocationInfoDB deviceLocationInfoDB;

  public String getCreated_date() {
    return created_date;
  }

  public void setCreated_date(String created_date) {
    this.created_date = created_date;
  }

  private String created_date;

  public DeviceLocationInfoDB getDeviceLocationInfoDB() {
    return deviceLocationInfoDB;
  }

  public void setDeviceLocationInfoDB(
      DeviceLocationInfoDB deviceLocationInfoDB) {
    this.deviceLocationInfoDB = deviceLocationInfoDB;
  }

  public UfoDeviceDB(String deviceType, String macID, String major, String minor, Boolean registerDevice, DeviceLocationInfoDB deviceLocationInfoDB, String created_date, String rssi){
    this.deviceType = deviceType;
    this.macID = macID;
    this.major = major;
    this.minor = minor;
    this.registerDevice = registerDevice;
    this.deviceLocationInfoDB = deviceLocationInfoDB;
    this.created_date = created_date;
    this.RSSI = rssi;
  }

  public UfoDeviceDB(){

  }

  public UfoDeviceDB(String deviceType, String macID, String major, String minor,
      String UUID, String txPower, String RSSI, String distance, String temp,
      String lastUpdate, String scanRecord, Boolean registerDevice, DeviceLocationInfoDB deviceLocationInfoDB) {
    this.deviceType = deviceType;
    this.macID = macID;
    this.major = major;
    this.minor = minor;
    this.UUID = UUID;
    this.txPower = txPower;
    this.RSSI = RSSI;
    this.distance = distance;
    this.temp = temp;
    this.lastUpdate = lastUpdate;
    this.scanRecord = scanRecord;
    this.registerDevice = registerDevice;
    this.deviceLocationInfoDB = deviceLocationInfoDB;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public UfoDeviceDB(Parcel in) {
    this.deviceType = in.readString();
    this.macID = in.readString();
    this.major = in.readString();
    this.minor = in.readString();
    this.registerDevice = in.readInt() != 0;
    this.deviceLocationInfoDB = in.readParcelable(DeviceLocationInfoDB.class.getClassLoader());
    this.created_date = in.readString();
    this.RSSI = in.readString();
  }

  public String getMacID() {
    return macID;
  }

  public String getMajor() {
    return major;
  }

  public String getMinor() {
    return minor;
  }

  public String getUUID() {
    return UUID;
  }

  public String getTxPower() {
    return txPower;
  }

  public String getRSSI() {
    return RSSI;
  }

  public String getDistance() {
    return distance;
  }

  public String getTemp() {
    return temp;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public String getScanRecord() {
    return scanRecord;
  }

  public Boolean getRegisterDevice() {
    return registerDevice;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public void setMacID(String macID) {
    this.macID = macID;
  }

  public void setMajor(String major) {
    this.major = major;
  }

  public void setMinor(String minor) {
    this.minor = minor;
  }

  public void setUUID(String UUID) {
    this.UUID = UUID;
  }

  public void setTxPower(String txPower) {
    this.txPower = txPower;
  }

  public void setRSSI(String RSSI) {
    this.RSSI = RSSI;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public void setScanRecord(String scanRecord) {
    this.scanRecord = scanRecord;
  }

  public void setRegisterDevice(Boolean registerDevice) {
    this.registerDevice = registerDevice;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.deviceType);
    dest.writeString(this.macID);
    dest.writeString(this.major);
    dest.writeString(this.minor);
    dest.writeInt(this.registerDevice ? 1 : 0);
    dest.writeParcelable(this.deviceLocationInfoDB, flags);
    dest.writeString(this.created_date);
    dest.writeString(this.RSSI);
  }
}
