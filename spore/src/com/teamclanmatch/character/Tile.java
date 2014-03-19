package com.teamclanmatch.character;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamclanmatch.Enums.TILE_TYPE;

public class Tile {
	public float x, y;
	public int row, column, number;
	public float width,height;
	public String name, code, sand_code, water_code, texture_name;
	public TILE_TYPE tile_type;
	public TextureRegion texture_region;
	boolean marker;
	
	public Tile(float x, float y, float w, float h, int row, int column, String name, TILE_TYPE tile_type, TextureRegion texture_region, int number){
		this.name           = name;
		this.texture_name   = name;
		this.tile_type      = tile_type;
		this.x              = x;
		this.y              = y;
		this.height         = h;
		this.width          = w;
		this.row            = row;
		this.number         = number;
		this.column         = column;
		this.texture_region = texture_region;
		this.marker         = false;
		this.code           = "99999999";
		this.water_code     = "99999999";
	}

	public int compareTo(Tile tile) {
		float temp_y = tile.number;
		float compare_y = this.number;
		
		return (temp_y > compare_y ) ? -1: (temp_y < compare_y) ? 1:0 ;
	}
	
}