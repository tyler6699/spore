package com.teamclanmatch.renderers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.teamclanmatch.Player;
import com.teamclanmatch.character.Inventory;
import com.teamclanmatch.game.MainGame;
import com.teamclanmatch.screens.MapScreen;
import com.teamclanmatch.utils.Clock;

import carelesslabs.Particles;
import carelesslabs.Zipper;
import carelesslabs.character.*;
import carelesslabs.controls.*;
import carelesslabs.entities.*;
import carelesslabs.entities.Enums.*;
import carelesslabs.maps.*;
import carelesslabs.screens.*;

public class MapRenderer {
	public Controller       gameController;
	//public Clicker              click_processor;
	public Player                 hero;
	public Clock                clock;
	//public Builder              builder;
	//public Crafting             craft;
	public MainGame             game;
	public MapScreen            mapScreen;
	public Inventory            inventory;
	public boolean              paused;
	public SpriteBatch          spriteBatch;
	public SpriteBatch          light_batch;
	public World                world;
	//public Lights               lights;
	private float               light_level;
	private Mapgenerator        MapGenerator;	
	private Tile                b_tile, t_tile;
	public ArrayList<Entity>    RemoveObjectsArray;
	
	// DEBUG TESTING
	MyTextInputListener         text_input;
	public PolygonRegion        region_32, region_black, region_yellow, map_region_blue;
	public ShapeRenderer        renderer;
	public PolygonSpriteBatch   batch;
	public PolygonSpriteBatch   mouse_batch;
    ArrayList<Vector2>          DotArray = new ArrayList<Vector2>();
	private boolean             debug = false;
			
	public MapRenderer(Particles game, GameController gameController, MapScreen mapScreen){
		this.game               = game;
		this.paused             = false;
		this.gameController     = gameController;
		this.mapScreen          = mapScreen;
		this.spriteBatch        = new SpriteBatch();
		this.clock              = new Clock(8, 0, 0, 360);
		this.inventory          = new Inventory(this.gameController, this.mapScreen);
		this.builder            = new Builder(this.gameController, this.inventory, this.mapScreen);
		this.craft              = new Crafting(this.gameController, this.inventory);
		this.world              = new World(new Vector2(), true); 
		this.inventory.crafting = this.craft;
		int hero_y              = 80 * 32;
		int hero_x              = -90* 32;
		this.hero               = new Hero(gameController.camera, mapScreen.map, mapScreen.id_count, "HERO", Type.HERO, 1, hero_x/32, hero_y/32, 32/32, 48/32);
		this.lights             = new Lights(hero, gameController, world);
		this.mapScreen.id_count ++;
		RemoveObjectsArray      = new ArrayList<Entity>();
		
		// Random Map
		MapGenerator = new Mapgenerator(mapScreen, hero);
		mapScreen.map.num_rows = MapGenerator.mapArray.size()+1;
		mapScreen.map.num_cols = MapGenerator.mapArray.get(0).size();
		
		// ADD HERO TO OBJECTS
		mapScreen.map.ObjectsArray.add(hero);
		
		gameController.camera.position.x = (hero_x/32) + .5f;
		gameController.camera.position.y = (hero_y/32) + .5f;
		// Enable ENTER to input text
		gameController.allow_text = true;
		
		// BASIC CONSOLE
		text_input = new MyTextInputListener(mapScreen, hero);
		
		// TEST POLYS, SPRITES			
		renderer = new ShapeRenderer();
		batch = new PolygonSpriteBatch();
		mouse_batch = new PolygonSpriteBatch();
		light_batch = new SpriteBatch();
		
		float o = gameController.o;
		float[]square = {0,0,  0,16,  16,16,  16,0};
		float[]diamond = {0,8,16,16,32,8,16,0};
		float[]diamond_32 = {0,8*o,16*o,16*o,32*o,8*o,16*o,0};

		region_32       = new PolygonRegion(new TextureRegion(Art.box),diamond);
		region_black    = new PolygonRegion(new TextureRegion(Art.texture_blk),square);
		region_yellow   = new PolygonRegion(new TextureRegion(Art.texture_yellow),square);
		map_region_blue = new PolygonRegion(new TextureRegion(Art.texture_blue),diamond_32);
		
		// Process all the mouse clicks in here
		click_processor = new Clicker(this,mapScreen.map.ObjectsArray, hero);
	}
	
	public void render(float delta) {	
		if (gameController.show_menu){
			gameController.show_menu = false;
			game.load_menu();
		}
		
		// MUSIC
		Art.main_loop.play((float) 0.05);
		
		if (gameController.input_text){
			Gdx.input.getTextInput(text_input, "Console", "");
			gameController.input_text = false;
		}
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gameController.number_blocks_in_view();
		gameController.update_mouse_map_v();
		if( !gameController.paused ) { perform_movement(delta);}
		
		if (gameController.rumble.time  > 0){
			gameController.rumble.tick(delta, gameController, hero);
		}
		
        gameController.camera.update();	
        light_batch.setProjectionMatrix(gameController.camera.combined);
        
        if (gameController.reset_map){
        	mapScreen.mapRenderer.inventory.clear_all_slots();
        	MapGenerator.reset(mapScreen, hero);
        	gameController.reset_map = false;
        }
        
        if (gameController.load_map){
        	gameController.load_map = false;
        	Gdx.app.postRunnable(new Runnable() {
        	     @Override
        	     public void run() {
        	    	 load_map();
        	     }
        	  });
    	
        }
                
        if (gameController.save_map){
        	gameController.save_map = false;
        	
        	new Thread(new Runnable() {
        		   @Override
        		   public void run() {
        		      // do something important here, asynchronously to the rendering thread
        			   save_map();
        		      // post a Runnable to the rendering thread that processes the result
        		      Gdx.app.postRunnable(new Runnable() {
        		         @Override
        		         public void run() {
        		            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
        		            System.out.println("Done");
        		         }
        		      });
        		   }
        		}).start();
        }
       		
        clock.tick(delta);
        
        // SET TOP, BOTTOM, CURRENT TILES
        b_tile = get_tile_from_screen_coords(hero.x, hero.y - gameController.view_height * 1.5f); 
        t_tile = get_tile_from_screen_coords(hero.x, hero.y+gameController.view_height * 1.5f); 
        hero.current_tile = get_tile_from_screen_coords(hero.x-.5f, hero.y);       
        
 	    // BACKGROUND
 	    light_batch.begin();
 	    MapGenerator.draw(gameController.camera, light_batch, hero.current_tile, b_tile, t_tile, hero.current_tile.number);
 	    light_batch.end();   
		
		// HERO		
		hero.setAimByMouse(this.gameController.mouse_x - (Gdx.graphics.getWidth() / 2), this.gameController.mouse_y - (Gdx.graphics.getHeight() / 2));
		
		// Order by Y
		Collections.sort(mapScreen.map.ObjectsArray);
			
		draw_and_tick_entities(delta,light_batch);
		hero.current_weapon.tick(delta, light_batch);
		
		click_processor.tick(delta);
	
		light_level = clock.get_light_level(light_level);
		lights.tick(light_level, hero, gameController);
		inventory.tick(delta, spriteBatch);
		inventory.draw_item_in_use(light_batch);
		craft.tick(delta, spriteBatch);	
						
		// SET CLICK PROCESS COMPLETE
		gameController.processed_click = true;
		screen_values();
		if (debug){	debug_info(delta); }
	}
	
	public void save_map(){
		FileHandle file = Gdx.files.local("map.txt");
		String str = "";
		ArrayList<String> tmp = new ArrayList<String>() ;

		// Build Up JSON
		for(ArrayList<Tile> tile_array : MapGenerator.mapArray){
			for(Tile tile: tile_array){
				if (tile.number != 20592){
					str = "{\"class\":\"tile\",\"w\":2,\"h\":1.0625,\"sc\":" + tile.sand_code + ",\"no\":" + tile.number + ",\"n\":\"" + tile.name + "\",\"c\":\"" + tile.code + "\",\"wc\":\"" + tile.water_code + "\",\"col\":" + tile.column + ",\"x\":" + tile.x + ",\"y\":" + tile.y + ",\"ro\":" + tile.row + ",\"ty\":\"" + tile.tile_type + "\",\"t\":\"" + tile.texture_name + "\"}, \n";
				}else{
					str = "{\"class\":\"tile\",\"w\":2,\"h\":1.0625,\"sc\":" + tile.sand_code + ",\"no\":" + tile.number + ",\"n\":\"" + tile.name + "\",\"c\":\"" + tile.code + "\",\"wc\":\"" + tile.water_code + "\",\"col\":" + tile.column + ",\"x\":" + tile.x + ",\"y\":" + tile.y + ",\"ro\":" + tile.row + ",\"ty\":\"" + tile.tile_type + "\",\"t\":\"" + tile.texture_name + "\"}";
				}
				tmp.add(str);
			}
		}
		
		// SAVE JSON TO FILE
		file.writeString("[", false);
		for(String s: tmp){
			file.writeString(s, true);
		}
		file.writeString("]", true);
		System.out.println("Saved");
		
		// COMPRESS VERSION
		Zipper zip = new Zipper();
		String text = file.readString();
		String zipped = "";
		
		try {
			zipped = zip.compressString(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileHandle com_file = Gdx.files.local("map-zip.txt");
		com_file.writeString(zipped, false);
		System.out.println("Compressed");
		
		// CLEAR TEMP
		file.writeString("", false);
	}
	
	public void load_map(){								
		Zipper zip = new Zipper();
		String unzipped = "";
		FileHandle com_file = Gdx.files.local("map-zip.txt");
		String text = com_file.readString();
		try {
			unzipped = zip.uncompressString(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int the_row = 0;
		JsonValue root = new JsonReader().parse(unzipped);
		JsonValue j_tile;
		ArrayList<Tile> temp_array = new ArrayList<Tile>();
		int row, column, number; 
		float x,y,w, h;
		String name, t_name, code, water_code, sand_code; 
		Type tile_type; 
		TextureRegion texture_region = Art.grass;
		MapGenerator.mapArray.clear();
		
		for (int i = 0; i < root.size; i++){
			j_tile = root.get(i);
						
			number         = j_tile.getInt("no");
			name           = j_tile.getString("n");
			t_name         = j_tile.getString("t");
			code           = j_tile.getString("c");
			water_code     = j_tile.getString("wc");
			sand_code      = j_tile.getString("sc");
			x              = j_tile.getFloat("x");
			y              = j_tile.getFloat("y");
			row            = j_tile.getInt("ro");
			column         = j_tile.getInt("col");
			w              = j_tile.getFloat("w");
			h              = j_tile.getFloat("h");
			tile_type      = Type.valueOf(j_tile.getString("ty").toUpperCase());
						
			if(t_name.equals("WATER")){
				texture_region = Art.water;
			} else if(t_name.equals("GRASS")){
				texture_region = Art.grass;
			} else if(t_name.equals("SAND")){
				texture_region = Art.sand;
			} else if(t_name.equals("w1")){
				texture_region = Art.water_r_long;
			} else if(t_name.equals("w2")){
				texture_region = Art.water_r_short;
			} else if(t_name.equals("w3")){
				texture_region = Art.water_l_long;
			} else if(t_name.equals("w4")){
				texture_region = Art.water_l_short;
			} else if(t_name.equals("w5")){
				texture_region = Art.water_top_full;
			} else if(t_name.equals("w6")){
				texture_region = Art.water_top_dot;
			} else if(t_name.equals("s1")){
				texture_region = Art.sand_r_long;
			} else if(t_name.equals("s2")){
				texture_region = Art.sand_r_short;
			} else if(t_name.equals("s3")){
				texture_region = Art.sand_l_long;
			} else if(t_name.equals("s4")){
				texture_region = Art.sand_l_short;
			} else if(t_name.equals("s5")){
				texture_region = Art.sand_top_full;
			} else if(t_name.equals("s6")){
				texture_region = Art.sand_top_dot;
			}
			
			Tile tile = new Tile(x,y,w,h,row,column,name, tile_type, texture_region, number);
			tile.water_code   = water_code;
			tile.sand_code    = sand_code;
			tile.code         = code;
			tile.texture_name = t_name;
															
			if (row != the_row){
				MapGenerator.mapArray.add(temp_array);
				temp_array =  new ArrayList<Tile>();
				temp_array.add(tile);
				the_row ++;
			} else {
				temp_array.add(tile);
			}
		}
	
		System.out.println("-- LOADED --");
	}
	
    private Tile get_tile_from_screen_coords(float x, float y){
    	int cx, cy, max_x, max_y;
    	ArrayList<Tile> tile_array;
    	
    	max_x = MapGenerator.mapArray.get(0).size();
    	max_y = MapGenerator.mapArray.size();
    	
    	// COLUMN
    	cx = (int) (x + (y / .5f)) /2;
        cx = cx >= max_x-1 ? max_x-2 : cx;
        cx = cx < 0 ? 0 : cx;
        
        // ROW
        cy = (int)((y / .5f) - x) /2;
        cy = cy >= max_y ? max_y-1 : cy;
        cy = cy < 0 ? 0 : cy;
        
        tile_array = MapGenerator.mapArray.get(cy);
    	return tile_array.get(cx);
    }
	
	void screen_values(){
		// SCREEN
		mapScreen.batch.begin();
		mapScreen.font.draw(mapScreen.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		mapScreen.font.draw(mapScreen.batch, " Tile: " + hero.current_tile.number + " row: " + hero.current_tile.row , 10, 40);
		mapScreen.font.draw(mapScreen.batch, " Code: " + hero.current_tile.code, 10, 60);
		mapScreen.font.draw(mapScreen.batch, " Type: " + hero.current_tile.tile_type, 10, 80);
		mapScreen.font.draw(mapScreen.batch, " X&Y: " + (int) hero.x + " , " + (int) hero.y, 10, 100);
		mapScreen.font.draw(mapScreen.batch, " TIME: " + clock.get_time(), 10, 120);
		mapScreen.font.draw(mapScreen.batch, " Days: " + clock.get_days(), 10, 140);
		mapScreen.batch.end();	
	}
	
	void projectile(double velocity, double angle, Direction dir, float delta, float y_offset) {	
		DotArray.clear();
		double vx, vy, ux, uy, time_in_air;
		double gravity = 9.8;

		ux = velocity * Math.cos(angle * Math.PI / 180);
		uy = velocity * Math.sin(angle * Math.PI / 180);

		time_in_air = (uy + (Math.sqrt(Math.pow(uy, 2)-4*(gravity/2)*(-y_offset))))/gravity;
		
		for (float i = 0; i < time_in_air; i += 0.03 ){
			if (dir == Direction.LEFT){
				vx = -ux * i;
			} else {
				vx = ux * i;
			}
			vy = uy * i - 0.5 * - -gravity * Math.pow(i, 2);
			Vector2 t = new Vector2((float) vx + hero.x ,(float) vy + hero.y + y_offset);
			DotArray.add(t);
		}
	 }
		    			
	public void perform_movement(float delta){
		float speed = hero.speed;
		float temp_x = hero.x;
		float temp_y = hero.y;
		float o = 0.03125f;
		float no_checks = speed*32;
		
		if (!hero.jumping && gameController.jump){
			hero.jumping = true;
			gameController.jump = false;
		} else if (hero.jumping && gameController.jump){
			gameController.jump = false;
		}
		
		if (hero.current_tile != null && hero.current_tile.tile_type == Type.WATER){
			o = o/10;
		} else if (hero.current_tile != null && hero.current_tile.tile_type == Type.SHORE){
			o = o/1.8f;
		} else if(hero.moving_up && (hero.moving_right || hero.moving_left)){
			o = o/1.2f;
		} else if(hero.moving_down && (hero.moving_right || hero.moving_left)){
			o = o/1.2f;
		}
				
		if (!hero.mounted){
			// DOWN
			if (gameController.move_down) {
				for (int i = 1; i<=no_checks ;i++ ){
					temp_x = hero.x; temp_y = hero.y;
					
					if (hero.can_move(mapScreen.map.ObjectsArray, temp_x,temp_y -= (i*o), inventory)){
						gameController.camera.translate(0 , -o , 0);
						hero.y -= o;
						hero.moving_down = true;
					} else {
						hero.moving_down = false;
						i = (int) (speed*33);				
					}
				}
			} else {
				hero.moving_down = false;
			}
	
			// UP
			if (gameController.move_up) {
				for (int i = 1; i<=no_checks ;i++ ){
					temp_x = hero.x; temp_y = hero.y;
					if (hero.can_move(mapScreen.map.ObjectsArray, temp_x,temp_y += (i*o), inventory)){
						gameController.camera.translate(0 , o , 0);
						hero.y += o;
						hero.moving_up = true;
					} else {
						hero.moving_up = false;
						i = (int) (speed*33);
					}
				}
			} else {
				hero.moving_up = false;
			}
			
			// RIGHT
			if (gameController.move_right) {
				for (int i = 1; i<=no_checks ;i++ ){
					temp_x = hero.x; temp_y = hero.y;
					if (hero.can_move(mapScreen.map.ObjectsArray, temp_x += (i*o),temp_y, inventory)){
						gameController.camera.translate(o ,0, 0);
						hero.x += o;
						hero.moving_right = true;
					} else {
						hero.moving_right = false;
						i = (int) (speed*33);
					}
				}
			} else {
				hero.moving_right = false;
			}
	
			// LEFT		
			if (gameController.move_left) {
				for (int i = 1; i<=no_checks ;i++ ){
					temp_x = hero.x; temp_y = hero.y;
					if (hero.can_move(mapScreen.map.ObjectsArray, temp_x -= (i*o),temp_y, inventory)){
						gameController.camera.translate(-o ,0, 0);
						hero.x -= o;
						hero.moving_left = true;
					} else {
						hero.moving_left = false;
						i = (int) (speed*33);
					}
				}
			} else {
				hero.moving_left = false;
			}
		} else if (hero.mounted) {
			//System.out.println(hero.mounted_entity.speed);
			
			if(gameController.move_up){
				hero.mounted_entity.apply_force();	
			}
			
			if (hero.mounted_entity.down_time > 0 && is_water()){
				hero.mounted_entity.y -= hero.mounted_entity.speed*o;
				hero.mounted_entity.down_time -= delta;
			} else {
				hero.mounted_entity.down_time = 0;
			}
			
			if (hero.mounted_entity.up_time > 0 && is_water()){
				hero.mounted_entity.y += hero.mounted_entity.speed*o;
				hero.mounted_entity.up_time -= delta;
			} else {
				hero.mounted_entity.up_time = 0;
			}
			
			if (hero.mounted_entity.left_time > 0 &&  is_water()){
				hero.mounted_entity.x -= hero.mounted_entity.speed*o;
				hero.mounted_entity.left_time -= delta;
			} else {
				hero.mounted_entity.left_time = 0;
			}
			
			if (hero.mounted_entity.right_time > 0 &&  is_water()){
				hero.mounted_entity.x += hero.mounted_entity.speed*o;
				hero.mounted_entity.right_time -= delta;
			} else {
				hero.mounted_entity.right_time = 0;
			}
			
			hero.x = hero.mounted_entity.x;
			hero.y = hero.mounted_entity.y;
			gameController.camera.position.set(hero.x + .8f, hero.y, 0);
		}
		//update_velocity(delta);
	}		
	
	private boolean is_water(){
		if (hero.current_tile.tile_type == Type.WATER || hero.current_tile.tile_type == Type.SHORE){
			return true;
		} else {
			return false;
		}
	}
	
	private void draw_and_tick_entities(float delta, SpriteBatch spriteBatch){	
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(gameController.camera.combined);
		for (Entity entity : mapScreen.map.ObjectsArray) {
			if (entity.removed == true){
				RemoveObjectsArray.add(entity);
			}
			
			if (entity.handitem){
				Entity e = inventory.draw_item_in_use(spriteBatch, hero, MapGenerator.mapArray);
				entity.y = e == null? 0 : e.y;
				entity.type = e == null? null : e.type;
			} else {
				if (entity.group_type == Type.FISH){
					// SERIOUS SORT OUT HERO STATES!
					if (inventory.item_in_use != null && inventory.item_in_use.item_type == Type.ROD && inventory.item_in_use.item_entity.state == STATE.CAST){
						entity.tick(delta, MapGenerator.mapArray, hero, true, (Rod) inventory.item_in_use.item_entity);
					} else {
						entity.tick(delta, MapGenerator.mapArray, hero, false, null);
					}	
				} else {
					entity.tick(delta);	
				}
				
				//if (tick_counter > .3f){ }
				entity.update_dst_from_hero(hero.entity_vec);
				entity.setEntity_vec();
				// ONLY WHEN BROKEN
				entity.set_scale();
				if (entity.dst_cam_centre <= gameController.cull_size){
					entity.tick(delta, spriteBatch);
				}	
			}	
		}
		spriteBatch.end();
		
		// ITEMS FOR REMOVAL
		for (Entity entity : RemoveObjectsArray) {
			mapScreen.map.ObjectsArray.remove(entity);
		}
		RemoveObjectsArray.clear();
	}
	
	void debug_info(float delta){
		// RENDER 4 COLOURED HITBOXES
		batch.begin();
		batch.setProjectionMatrix(gameController.camera.combined);
		for (Entity entity : mapScreen.map.ObjectsArray) {
			entity.tick(delta, batch);	
		}
		batch.end();	
		
		// BOX AROUND HERO
		//batch.begin();
		//batch.setProjectionMatrix(gameController.camera.combined);
		//batch.draw(region_32,hero.x - hero.x_poly_offset, hero.y - hero.y_poly_offset, 1,1);
		//batch.end();
		
		//CURSOR
		//mouse_batch.setProjectionMatrix(gameController.camera.combined);
				
		if (gameController.processed_click == false) {
			mouse_batch.begin();
			float x = gameController.click_screen_area.getX();
			float y = gameController.click_screen_area.getY();
			mouse_batch.draw(region_black, x, y, 32,32);
			mouse_batch.end();	
		}else{
			if (gameController.mouse_time < .2f){
				mouse_batch.begin();
				// SCREEN
				float x = gameController.click_screen_area.getX();
				float y = gameController.click_screen_area.getY();				
				mouse_batch.draw(region_yellow, x, y, 32,32);
				mouse_batch.end();	
				
				if (!gameController.show_inventory){
					// MAP
					float map_x = gameController.click_map_area.getX();
					float map_y = gameController.click_map_area.getY();
					
					batch.begin();
					batch.setProjectionMatrix(gameController.camera.combined);
					batch.draw(map_region_blue, map_x, map_y, 32,32);
					batch.end();
				}
				gameController.mouse_time += delta;				
			}
			
		}
		
	}
	
	public void p(String str){
		System.out.println(str);
	}
}