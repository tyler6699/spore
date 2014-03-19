package com.teamclanmatch;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.teamclanmatch.Enums.*;

public class Entity implements Comparable<Entity>{
	public int id;
	public String name;
	int HP;
	
	// SIZE
	public float w;
	public float h;
	public Rectangle hitbox;
	
	// Position
	public Vector2 current_position;
	public Vector2 previous_position;
	public Vector2 dest_position;
	
	// ENUMS
	public ENEMY_TYPE enemy_type;
	public STATE currnet_state;
	public E_TYPE e_type;
	
	// MOVE TO ARRAY OF TEXTURES, ANIMS
	public Texture texture;
	public Texture alt_texture;
	public float alt_w;
	public float alt_h;
	public float tick;
	
	
	public Entity (){
		current_position = new Vector2();
		previous_position = new Vector2();
		dest_position = new Vector2();
	}

	public int compareTo(Entity entity) {
		float temp_y =  entity.current_position.y;
		float compare_y = this.current_position.y;
				
		// FOR entities where Y is incorrect due to space at bottom of image
		//if(this.type == TYPE.NULL){
		//	compare_y +=0.25;
		//} else if (entity.type == TYPE.NULL){
		//	temp_y +=0.25;
		//}
		
		return (temp_y < compare_y ) ? -1: (temp_y > compare_y) ? 1:0 ;
	}

	// Draw - player class available
	public void tick(float delta, SpriteBatch batch, Player bot) {}
	
	// Draw - all entities available
	public void tick(float delta, SpriteBatch batch, Player bot, ArrayList<Entity> entities) {}

}