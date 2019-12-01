package com.example.beaconapp.Model;

public class MappingBeacon {

  private String id, direction, created_date, updated_date, location_name, mapping_message;
  private Integer b1, b2, distance, numberofhops;

  public String getId() {
    return id;
  }

  public MappingBeacon(String direction, String mapping_message, Integer b1, Integer b2, Integer distance,
      Integer numberofhops) {
    this.direction = direction;
    this.b1 = b1;
    this.b2 = b2;
    this.distance = distance;
    this.numberofhops = numberofhops;
    this.mapping_message = mapping_message;
  }

  public MappingBeacon(String direction, Integer b1, Integer b2, Integer distance,
      Integer numberofhops, String id, String mapping_message) {
    this.direction = direction;
    this.b1 = b1;
    this.b2 = b2;
    this.distance = distance;
    this.numberofhops = numberofhops;
    this.id = id;
    this.mapping_message = mapping_message;
  }

  public String getLocation_name() {
    return location_name;
  }

  public void setLocation_name(String location_name) {
    this.location_name = location_name;
  }

  public MappingBeacon(String direction, String location_name, Integer distance,
      Integer numberofhops, String created_date, String updated_date, String id, String mapping_message) {
    this.direction = direction;
    this.location_name = location_name;
    this.distance = distance;
    this.numberofhops = numberofhops;
    this.created_date = created_date;
    this.updated_date = updated_date;
    this.id = id;
    this.mapping_message = mapping_message;
  }




  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public Integer getB1() {
    return b1;
  }

  public void setB1(Integer b1) {
    this.b1 = b1;
  }

  public Integer getB2() {
    return b2;
  }

  public void setB2(Integer b2) {
    this.b2 = b2;
  }

  public Integer getDistance() {
    return distance;
  }

  public String getMapping_message() {
    return mapping_message;
  }

  public void setMapping_message(String mapping_message) {
    this.mapping_message = mapping_message;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public Integer getNumberofhops() {
    return numberofhops;
  }

  public void setNumberofhops(Integer numberofhops) {
    this.numberofhops = numberofhops;
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
}
