package com.example.beaconapp.LocationSearch;

import java.util.ArrayList;

public class pathList {

  private ArrayList<Integer> pathList;

  public pathList(ArrayList<Integer> pathList) {
    this.pathList = pathList;
  }

  public ArrayList<Integer> getNode() {
    return pathList;
  }

  public void setNode(ArrayList<Integer> pathList) {
    this.pathList = pathList;
  }
}
