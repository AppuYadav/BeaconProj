package com.example.beaconapp.Model;

public class DestInfo {

  private String location_name, rssi, message_broadcast, direction, step, hops, message_mapping, mac_id;

  public DestInfo(){

  }

  public String getMessage_mapping() {
    return message_mapping;
  }

  public void setMessage_mapping(String message_mapping) {
    this.message_mapping = message_mapping;
  }

  public DestInfo(String location_name, String rssi, String message_broadcast,
      String direction, String step, String hops, String mac_id) {
    this.location_name = location_name;
    this.mac_id = mac_id;
    this.rssi = rssi;
    this.message_broadcast = message_broadcast;
    this.direction = direction;
    this.step = step;
    this.hops = hops;
  }

  public String getMac_id() {
    return mac_id;
  }

  public void setMac_id(String mac_id) {
    this.mac_id = mac_id;
  }

  public String getLocation_name() {
    return location_name;
  }

  public void setLocation_name(String location_name) {
    this.location_name = location_name;
  }

  public String getRssi() {
    return rssi;
  }

  public void setRssi(String rssi) {
    this.rssi = rssi;
  }

  public String getMessage_broadcast() {
    return message_broadcast;
  }

  public void setMessage_broadcast(String message_broadcast) {
    this.message_broadcast = message_broadcast;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getStep() {
    return step;
  }

  public void setStep(String step) {
    this.step = step;
  }

  public String getHops() {
    return hops;
  }

  public void setHops(String hops) {
    this.hops = hops;
  }
}
