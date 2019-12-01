package com.example.beaconapp.Model;

public class GraphData {
  Integer source, dest, distance, hops;
  String direction;

  public GraphData(Integer source, Integer dest, Integer distance, Integer hops, String direction) {
    this.source = source;
    this.dest = dest;
    this.distance = distance;
    this.direction = direction;
    this.hops = hops;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Integer getDest() {
    return dest;
  }

  public void setDest(Integer dest) {
    this.dest = dest;
  }

  public Integer getDistance() {
    return distance;
  }

  public Integer getHops() {
    return hops;
  }

  public void setHops(Integer hops) {
    this.hops = hops;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }
}
