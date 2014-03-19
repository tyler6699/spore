package com.teamclanmatch.utils;

import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Maths {
	public static float getAngle(Vector2 origin, Vector2 target) {
	    float angle = (float) Math.toDegrees(Math.atan2(target.x - origin.x, target.y - origin.y));
	    angle = angle < 0 ? angle += 360: angle;
	    return angle;
	}
	
	public static double GetAngleOfLine(Vector2 p1, Vector2 p2) { 
	    double xDiff = p2.x - p1.x; 
	    double yDiff = p2.y - p1.y; 
	    
	    double angle = Math.toDegrees(Math.atan2(yDiff, xDiff));
	    angle = angle < 0 ? angle += 360: angle;
	    return angle;
	}
	
	public static double get_angle(Vector2 target, Vector2 origin)
	{
	    double dx = target.x - origin.x;
	    double dy = -(target.y - origin.y);
	    double inRads = Math.atan2(dy,dx);

	    if (inRads < 0)
	        inRads = Math.abs(inRads);
	    else
	        inRads = 2*Math.PI - inRads;

	    return Math.toDegrees(inRads);
	}
	
	public static float GetDistanceToPoint(Vector2 p1, Vector2 p2){
	    float distance = (float) Math.sqrt(Math.pow(p2.y - p1.y, 2) + Math.pow((p2.x - p1.x), 2));
	    return distance;
	}
	
	public static boolean chance(int percentage){
		Random r = new Random(); 
		int chance = r.nextInt(100);
		    
		if(percentage > chance){
			return true;
		}else{
			return false;
		}
	}
	
	void drawLine(SpriteBatch sb, Texture texture, float x0, float y0, float x1, float y1, float o) {
		int n = 32;
		float dx = x1 - x0;
		float dy = y1 - y0;
		float sx = dx > 0 ? 1 : -1;
		float sy = dy > 0 ? 1 : -1;

		dx = (dx * sx) * n;
		dy = (dy * sy) * n;
				
		float dxdy = (dx / dy) / n;
		float dydx = (dy / dx) / n;

		dxdy *= sx;
		dydx *= sy;

		float py = y0;
		float px = x0;
		
		sy = sy/n;
		sx = sx/n;
		
		if (dx > dy) {
			for (int x = 0; x < dx; x++) {
				sb.draw(texture,px, py,o*1,o*1);	
				px += sx;
				py += dydx;
			}// End for loop

		} else {
			for (int y = 0; y < dy; y++) {
				sb.draw(texture,px, py,o*1,o*1);
				py += sy;
				px += dxdy;
			}// End for loop
		}
	}
}
