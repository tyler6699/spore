package com.teamclanmatch.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamclanmatch.Entity;
import com.teamclanmatch.Enums.TILE_TYPE;

public class Tile extends Entity{
	public float path_h, path_g, path_f;
	public int number;
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
	}
	
	public Tile(){
		super();
	}

	public int compareTo(Tile tile) {
		float temp_y = tile.number;
		float compare_y = this.number;
		
		return (temp_y > compare_y ) ? -1: (temp_y < compare_y) ? 1:0 ;
	}
	
}