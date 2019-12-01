package com.example.beaconapp.Model.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceLocationInfoDB implements Parcelable {

  public static final Parcelable.Creator<DeviceLocationInfoDB> CREATOR = new Parcelable.Creator<DeviceLocationInfoDB>() {
    @Override
    public DeviceLocationInfoDB createFromParcel(Parcel source) {
      return new DeviceLocationInfoDB(source);
    }

    @Override
    public DeviceLocationInfoDB[] newArray(int size) {
      return new DeviceLocationInfoDB[size];
    }
  };

  protected DeviceLocationInfoDB(Parcel in) {
    this.locationName = in.readString();
    this.broadCastMessage = in.readString();
    this.locationStatus = in.readInt() != 0;
  }

  private long deviceID;
  private String locationName;
  private Boolean locationStatus;
  private String broadCastMessage;

  public long getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(long deviceID) {
    this.deviceID = deviceID;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public String getBroadCastMessage() {
    return broadCastMessage;
  }

  public void setBroadCastMessage(String broadCastMessage) {
    this.broadCastMessage = broadCastMessage;
  }

  public Boolean getLocationStatus() {
    return locationStatus;
  }

  public void setLocationStatus(Boolean locationStatus) {
    this.locationStatus = locationStatus;
  }

  public DeviceLocationInfoDB(){

  }

  public DeviceLocationInfoDB(String locationName, Boolean locationStatus, String broadCastMessage) {
    this.locationName = locationName;
    this.locationStatus = locationStatus;
    this.broadCastMessage = broadCastMessage;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.locationName);
    dest.writeString(this.broadCastMessage);
    dest.writeInt(this.locationStatus ? 1 : 0);
  }
}
