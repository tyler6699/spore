package com.teamclanmatch.map;

import java.util.ArrayList;
import com.badlogic.gdx.maps.MapObjects;
import com.teamclanmatch.Entity;
import com.teamclanmatch.screens.MapScreen;

public class Map {
	public String name;
	public float start_x;
	public float start_y;
	public float height;
	public float width;
	public int num_rows, num_cols;
	public float tile_height;
	public float tile_width;
	public ArrayList<MapObjects> mapObjects;
	public ArrayList<Entity> ObjectsArray;
	
	public Map(String name, MapScreen mapScreen){
		
		this.name = name;
		//TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
		this.tile_height = 32;
		this.tile_width = 64;
		
		// TODO Load this in from map Gen
		this.height = 4600;
		this.width = 4600;
		this.ObjectsArray  = new ArrayList<Entity>();
		
	}
	
		
}