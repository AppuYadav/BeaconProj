package com.example.beaconapp.LocationSearch;

import androidx.annotation.NonNull;

public class Edge {
  private final String id;
  private final Vertex source;
  private final Vertex dest;
  private final int weight;

  public Edge(String id, Vertex source, Vertex dest, int weight) {
    this.id = id;
    this.source = source;
    this.dest = dest;
    this.weight = weight;
  }

  public String getId() {
    return id;
  }

  public Vertex getSource() {
    return source;
  }

  public Vertex getDest() {
    return dest;
  }

  public int getWeight() {
    return weight;
  }

  @NonNull
  @Override
  public String toString() {
    return source + " " + dest;
  }
}
