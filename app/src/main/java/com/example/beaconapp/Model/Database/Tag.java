package com.example.beaconapp.Model.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {
  private String tagID, verboseTitle, created_Date, message, updated_Date;

  public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>(){

    @Override
    public Tag createFromParcel(Parcel source) {
      return new Tag(source);
    }

    @Override
    public Tag[] newArray(int size) {
      return new Tag[size];
    }
  };

  public Tag(String tagID, String verboseTitle, String message, String created_Date,
      String updated_Date) {
    this.tagID = tagID;
    this.verboseTitle = verboseTitle;
    this.created_Date = created_Date;
    this.message = message;
    this.updated_Date = updated_Date;
  }

  public Tag(String tagID, String verboseTitle, String message) {
    this.tagID = tagID;
    this.verboseTitle = verboseTitle;
    this.message = message;
  }

  protected Tag(Parcel in){
    this.tagID = in.readString();
    this.verboseTitle = in.readString();
    this.message = in.readString();
    this.created_Date = in.readString();
    this.updated_Date = in.readString();
  }

  public String getTagID() {
    return tagID;
  }

  public void setTagID(String tagID) {
    this.tagID = tagID;
  }

  public String getVerboseTitle() {
    return verboseTitle;
  }

  public void setVerboseTitle(String verboseTitle) {
    this.verboseTitle = verboseTitle;
  }

  public String getCreated_Date() {
    return created_Date;
  }

  public void setCreated_Date(String created_Date) {
    this.created_Date = created_Date;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUpdated_Date() {
    return updated_Date;
  }

  public void setUpdated_Date(String updated_Date) {
    this.updated_Date = updated_Date;
  }

  public Tag() {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.tagID);
    dest.writeString(this.verboseTitle);
    dest.writeString(this.message);
    dest.writeString(this.created_Date);
    dest.writeString(this.updated_Date);
  }
}
