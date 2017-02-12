package edu.utc.bkf926.WorldStream;

import java.util.HashMap;

public class WSBlock {

	int x, y, z;
	//Positions of the block relative to the world origin, NOT the chunk.
	
	public WSBlock(int[] xyz, String type){
		setCoordinates(xyz);
		blockType = type;
	}
	
	private String blockType;
	private HashMap<String, Integer> metadata;
	
	public void setCoordinates(int[] xyz){
		x = xyz[0];
		y = xyz[1];
		z = xyz[2];
	}
	
	public String getBlockID(){
		return blockType;
	}
	
}