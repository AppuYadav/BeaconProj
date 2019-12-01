package com.example.beaconapp.Model.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class MappingBeaconDB implements Parcelable {

  public static final Creator<MappingBeaconDB> CREATOR = new Creator<MappingBeaconDB>() {
    @Override
    public MappingBeaconDB createFromParcel(Parcel source) {
      return new MappingBeaconDB(source);
    }

    @Override
    public MappingBeaconDB[] newArray(int size) {
      return new MappingBeaconDB[size];
    }
  };

  private String direction, created_date, updated_date, location;
  private Integer b1, b2, distance, numberofhops;

  public MappingBeaconDB(Integer b1, Integer b2, Integer numberofhops, Integer distance,
      String direction, String created_date, String updated_date) {
    this.b1 = b1;
    this.b2 = b2;
    this.numberofhops = numberofhops;
    this.distance = distance;
    this.direction = direction;
    this.created_date = created_date;
    this.updated_date = updated_date;
  }

  public MappingBeaconDB(Integer numberofhops, Integer distance,
      String direction, String location) {
    this.numberofhops = numberofhops;
    this.distance = distance;
    this.direction = direction;
    this.location = location;
  }

  public MappingBeaconDB(Parcel in) {
    this.b1 = in.readInt();
    this.b2 = in.readInt();
    this.distance = in.readInt();
    this.numberofhops = in.readInt();
    this.direction = in.readString();
    this.created_date = in.readString();
    this.updated_date = in.readString();
    this.location = in.readString();
  }


  public Integer getB1() {
    return b1;
  }

  public Integer getB2() {
    return b2;
  }

  public Integer getNumberofhops() {
    return numberofhops;
  }

  public Integer getDistance() {
    return distance;
  }

  public String getDirection() {
    return direction;
  }

  public void setB1(Integer b1) {
    this.b1 = b1;
  }

  public void setB2(Integer b2) {
    this.b2 = b2;
  }

  public void setNumberofhops(Integer numberofhops) {
    this.numberofhops = numberofhops;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getCreated_date() {
    return created_date;
  }

  public void setCreated_date(String created_date) {
    this.created_date = created_date;
  }

  public String getUpdated_date() {
    return updated_date;
  }

  public void setUpdated_date(String updated_date) {
    this.updated_date = updated_date;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.b1);
    dest.writeInt(this.b2);
    dest.writeInt(this.distance);
    dest.writeInt(this.numberofhops);
    dest.writeString(direction);
    dest.writeString(created_date);
    dest.writeString(updated_date);
    dest.writeString(location);
  }
}
