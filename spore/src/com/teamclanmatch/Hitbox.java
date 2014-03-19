package com.teamclanmatch;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.math.Polygon;
import com.teamclanmatch.Enums.FACING;


public class Hitbox {
	public int           id;
	public float         x;
	public float         y;
	public Polygon       hitbox_polygon;
	public PolygonRegion polygon_region;
	public Entity        owner;
	public FACING        move_direction;
	public boolean       solid;
	
	public Hitbox(String name, float x, float y, Polygon polygon, PolygonRegion polygon_region, Entity owner, boolean solid, FACING move_direction){
		this.id             = owner.id;
		this.x              = x;
		this.y              = y;
		this.hitbox_polygon = polygon;
		this.polygon_region = polygon_region;
		this.owner          = owner;
		this.solid          = solid;
		this.move_direction = move_direction;
	}
	
	public void update_position(float x, float y){
		this.x = x;
		this.y = y;	
		this.hitbox_polygon.setPosition(x, y);
	}
		
}