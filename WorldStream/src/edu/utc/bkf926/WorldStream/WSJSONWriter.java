package edu.utc.bkf926.WorldStream;

import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.Bukkit;

public class WSJSONWriter {
	
	private String world;
	private FileOutputStream stream;
	
	public WSJSONWriter(String worldName){
		//Constructor stub - be sure to initialize stream
		//Will need to edit filepath for each individual computer this runs on
		try {
			stream = new FileOutputStream(worldName+".json");
		} catch (IOException e){
			Bukkit.getLogger().severe("ERROR: Failed to access file for world "+worldName+"! Streaming will be disabled! Please correct this issue and restart your server.");
		}
		
	}
	
	public void writeBlock(WSBlock block) throws IOException{
		//Write a single block
		//Creates a string to match the JSON format that Unity is expecting, then writes it to the file
		String blockText = "{ \"blocks\" : [ { \"type\": " + block.getBlockID() + 
				", \"position\": { \"x\"" + block.x + ", \"y\"" + block.y + ", \"z\"" + block.z
				+ "}, ] }";
		stream.write(blockText.getBytes()); //TODO Test this!
		
	}
	
	public void writeChunk(WSChunk chunk) throws IOException{
		//Write a chunk (16x16x256)
		while (chunk.hasNextBlock())
		{
			writeBlock(chunk.nextBlock());
		}
	}
	
	public void close(){
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the world associated with this JSON Writer.
	 * @return
	 */
	public World getWorld(){
		return Bukkit.getServer().getWorld(world);
	}
	
}