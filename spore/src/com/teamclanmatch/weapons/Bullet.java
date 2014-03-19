package com.teamclanmatch.weapons;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.teamclanmatch.Entity;

public class Bullet extends Entity {
	public boolean	  friendly;
	private float     startX = 0;
	private float     startY = 0;
	public float      destX = 0;
	public float      destY = 0;
	public Vector2    location = new Vector2();
	private float     speed = 20f; // how fast this moves.
	private float     dx, dy;
	public float      duration = 0;
	public float      max_duration = 30;
	public float      ammo_width = 8;
	public float      ammo_height = 15;
	public int        damage = 1;
	
	public Bullet(float startX, float startY, float destX, float destY, float ammo_width, float ammo_height, int damage, float speed, float range) {
		friendly 			= false;
		this.speed 			= speed;
		this.damage 		= damage;
		this.startX         = startX;
		this.startY         = startY;
		this.ammo_width     = ammo_width;
		this.ammo_height    = ammo_height;
		this.destX          = destX;
		this.destY          = destY;
		this.duration       = 0;
		this.max_duration   = range;
		this.location.set(startX, startY);
		recalculateVector(destX, destY);
		this.hitbox = new Rectangle(startX, startY,ammo_width,ammo_height);
	}

	// Calculates a new vector based on the input destination X and Y
	public void recalculateVector(float destX, float destY) {
		double rad = Math.atan2(destX - startX, startY - destY);
		this.dx = (float) Math.sin(rad) * speed;
		this.dy = -(float) Math.cos(rad) * speed;
	}

	// Recalculates the vector for this bullet based on the current destination.
	public void recalculateVector() {
		recalculateVector(destX, destY);
	}

	public void move(float delta) {
		location.x += dx;
		location.y += dy;
		duration += 1;
		hitbox.set(location.x, location.y,ammo_width,ammo_height);
	}
	
}