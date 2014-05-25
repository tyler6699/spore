package com.teamclanmatch.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamclanmatch.Entity;
import com.teamclanmatch.Enums.ASTAR;
import com.teamclanmatch.Enums.TILE_TYPE;

public class Tile extends Entity{
	public float path_h, path_g, path_f;
	public int number, parent_id;
	public ASTAR state;
	public String name, code, sand_code, water_code, texture_name;
	public TILE_TYPE tile_type;
	public TextureRegion texture_region;
	boolean marker;
	
	public Tile(float w, float h, int row, int column, String name, TILE_TYPE tile_type, TextureRegion texture_region, int number){
		super();
		this.name           = name;
		this.texture_name   = name;
		this.tile_type      = tile_type;
		this.h              = h;
		this.w              = w;
		this.row            = row;
		this.number         = number;
		this.column         = column;
		this.texture_region = texture_region;
		this.marker         = false;
		this.code           = "99999999";
		this.water_code     = "99999999";
		state = ASTAR.NULL;
	}
	
	public Tile(){
		super();
		parent_id = -1;
		state = ASTAR.NULL;
	}

	public int compareTo(Tile tile) {
		float temp_f = tile.path_f;
		float compare_f = this.path_f;
		
		return (temp_f > compare_f ) ? -1: (temp_f < compare_f) ? 1:0 ;
	}
		
}