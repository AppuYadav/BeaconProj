package com.example.beaconapp.Model;

public class UfoDevice {

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

  public String getDeviceType() {
    return deviceType;
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

  public void setRegisterDevice(Boolean registerDevice) {
    this.registerDevice = registerDevice;
  }

  public UfoDevice(String deviceType, String macID, String major, String minor, String UUID,
      String txPower, String RSSI, String distance, String temp, String lastUpdate,
      String scanRecord, Boolean registerDevice) {
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
}
