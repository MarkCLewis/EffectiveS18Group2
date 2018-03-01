package entity;

import virtualworld.terrain.Pair;

public interface Entity {
	
	//functions to implement
	
	public Pair<Double,Double> getCenter();
	
	public int getSize();
	
}
