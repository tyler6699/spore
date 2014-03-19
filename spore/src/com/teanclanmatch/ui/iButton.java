package com.teanclanmatch.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.teamclanmatch.Entity;

public class iButton extends Entity {
	String target;
	String menu;
	Boolean active;
	float fx, fy;
	
	public iButton(String menu, float x, float y, String target, Texture texture, float w_scale, float h_scale, boolean active){
		super();
		
		this.menu = menu;
		this.target = target;
		this.texture = texture;
		this.current_position.x = x;
		this.current_position.y = y;
		this.w = w_scale * texture.getWidth();
		this.h = h_scale * texture.getHeight();
		this.active = active;
	}
	
	public void set_hitbox(){
		this.hitbox = new Rectangle(current_position.x,current_position.y,w,h);
	}
}
