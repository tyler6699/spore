package com.teamclanmatch.utils;

import java.util.Random;

import com.teamclanmatch.Player;
import com.teamclanmatch.managers.Controller;


public class Rumble {
	Controller gc;
	public float time;
	Random random;
	float x, y;
	float current_time;
	float power;
	float current_power;

	public Rumble(){
		time          = 0;
		current_time  = 0;
		power         = 0;
		current_power = 0;
	}
	   
	public void rumble(float power, float time) {
		random            = new Random();
		this.power        = power;
		this.time         = time;
		this.current_time = 0;
	}
	
	public void tick(float delta, Controller gc, Player player){
		if(current_time <= time) {
			current_power = power * ((time - current_time) / time);
			
	        x = (random.nextFloat() - 0.5f) * 2 * current_power;
	        y = (random.nextFloat() - 0.5f) * 2 * current_power;
	        	                
	        gc.camera.translate(-x, -y);
	        current_time += delta;
		} else {
			gc.camera.position.x = player.current_position.x;
			gc.camera.position.y = player.current_position.y;
	    }
	}
	   
}