package com.teamclanmatch.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.teamclanmatch.Entity;
import com.teamclanmatch.Enums.ASTAR;
import com.teamclanmatch.Enums.E_TYPE;
import com.teamclanmatch.game.MainGame;
import com.teamclanmatch.managers.Controller;
import com.teamclanmatch.managers.Device;
import com.teamclanmatch.map.Tile;

public class GameScreen implements Screen {
	private MainGame game;
	
	public ArrayList<Entity> entities;
	public ArrayList<Tile> tiles;
	public ArrayList<Tile> open_tiles;
	public ArrayList<Tile> path;
	public Tile lowest_tile;
	public Tile current_tile;
	public Tile start_tile;
	public boolean search_over, reset_states;
	int column, row;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Device device;
	private BitmapFont font;
	private Texture texture, rock, hero, npc, goal, no_path, yes_path, current;
	private Controller controls;
	Entity e_hero;
	
	// Entity ID
	int id 	= 0;
	int SQUARE = 19;
	float speed = 3;
	boolean has_target = false;
	boolean found_open = false;
	float target_x, target_y, origin_x, origin_y;
	
	public GameScreen(MainGame game) {
		this.game = game;
		device = new Device();
		camera = new OrthographicCamera();
		// 1 = 10 pixel
		camera.setToOrtho(false, (device.w / device.h) * 10, 10);
		camera.zoom = 60f;
		camera.position.x = device.w/2;
		camera.position.y = device.h/2;
		camera.update();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		controls = new Controller(camera);
		
		// All Entities
		entities = new ArrayList<Entity>();
		tiles = new ArrayList<Tile>();
		open_tiles = new ArrayList<Tile>();
		path = new ArrayList<Tile>();
		
		// FONT
		font = new BitmapFont();
		font.scale(.2F);
		
		// Hide Cursor
		//Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
		
		texture  = new Texture(Gdx.files.internal("data/32x32.png"));
		rock     = new Texture(Gdx.files.internal("data/rock.png"));
		goal     = new Texture(Gdx.files.internal("data/goal.png"));
		npc      = new Texture(Gdx.files.internal("data/green.png"));
		hero     = new Texture(Gdx.files.internal("data/blue.png"));
		no_path  = new Texture(Gdx.files.internal("data/path-no.png"));
		yes_path = new Texture(Gdx.files.internal("data/path-yes.png"));
		current  = new Texture(Gdx.files.internal("data/current.png"));
		
		Gdx.input.setInputProcessor(controls);
		
		create_map();
	}
	
	private void create_map(){
		for (int y = 0; y < SQUARE; y++) {
			for (int x = 0; x < SQUARE; x++) {					
				Tile t = new Tile();
				t.id = id;
				t.name = "MAP";
				t.e_type = E_TYPE.FLOOR;
				t.w = 32;
				t.h = 32;
				t.row = y;
				t.column = x;
				t.current_position.x = x << 5;
				t.current_position.y = y << 5;
				t.hitbox = new Rectangle(t.current_position.x, t.current_position.y, t.w, t.h);
				t.texture = texture;
				
				tiles.add(t);
				entities.add(t);				
				id += 1;
			}
		}	
		
		e_hero = new Entity();
		e_hero.id = id;
		e_hero.w = 16;
		e_hero.h = 16;
		e_hero.name = "HERO";
		e_hero.e_type = E_TYPE.HERO;
		e_hero.hitbox = new Rectangle(0,0,16,16);
		e_hero.current_position.x = ((camera.position.x + camera.viewportWidth/2) - e_hero.w/2) - 8;
		e_hero.current_position.y = ((camera.position.y + camera.viewportHeight/2) - e_hero.h/2) - 8;
		e_hero.texture = hero;
		
		entities.add(e_hero);
		search_over = true;
		reset_states = false;
	}
	
	@Override
    public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		//Collections.sort(entities);
		
		// DRAW
		DRAW();
		
		// PROCESSES
		process_movements();
		process_tile_changes();
		update_hero_values();			
		
		// RUN PATH FINDING IF SPACE IS PRESSED
		if (controls.spacebar == true){
			set_current_hero_tile();
			search_over = false;
			controls.spacebar = false;
		}
			
		RESET_SEARCH();
		A_STAR_SEARCH();
	
		camera.update();
		// System.out.println(e_hero.row + " " + e_hero.column + " y:" + e_hero.current_position.y);
    }
	
	private void A_STAR_SEARCH() {
		if (!search_over && has_target){
			while (!search_over){
				//System.out.println("Open Tiles: " + open_tiles.size());
				int prev = current_tile.id;
				mark_tile(current_tile.id, current_tile.id -1);
				mark_tile(current_tile.id, current_tile.id +1);
				//mark_tile(current_tile.id, current_tile.id +(SQUARE-1));
				mark_tile(current_tile.id, current_tile.id +SQUARE);
				//mark_tile(current_tile.id, current_tile.id +(SQUARE+1));
				//mark_tile(current_tile.id, current_tile.id -(SQUARE-1));
				mark_tile(current_tile.id, current_tile.id -SQUARE);
				//mark_tile(current_tile.id, current_tile.id -(SQUARE+1));
				lowest_tile = null;
				
				for (Tile t : open_tiles){
					if (lowest_tile == null || (t.path_f <= current_tile.path_f && t.path_h <= current_tile.path_h && t.path_g <= current_tile.path_g)) {
						if (t.state != ASTAR.CLOSED){
							lowest_tile = t;	
						}
					}
				}
				
				if (lowest_tile == null){
					System.out.println("NO PATH");
					search_over = true;
				} else {
					//System.out.println("Open Tiles: " + open_tiles.size() + " lowest is:" + lowest_tile.id + "(" +lowest_tile.path_f + ")");
					if (prev == lowest_tile.id){
	
					} else {
						current_tile = lowest_tile;
						current_tile.state = ASTAR.CLOSED;
						
						if (current_tile.path_h <= 32){
							// Get Path
							path.clear();
							int step_count = 0;
							path.add(current_tile);
							step_count ++;
							while (current_tile.id != start_tile.id ){
								current_tile = tiles.get(current_tile.parent_id);
								if (current_tile.state == ASTAR.CLOSED){
									path.add(current_tile);	
									//System.out.println(step_count + " ID: " + current_tile.id + " F: " + current_tile.path_f + " H: " + current_tile.path_h);
									step_count ++;
								} else {
									System.out.println(current_tile.id + " " + current_tile.path_f);
								}
							}
							
							System.out.println("STEPS: " + step_count);
							search_over = true;
							reset_states = true;
						}	
					}
				}
			}
		}
    }

	private void mark_tile(int parent_id, int tile_id){		
		if (tile_id < tiles.size() && tile_id >= 0){
			Tile t = tiles.get(tile_id);
			
			if(t.e_type == E_TYPE.FLOOR && t.state != ASTAR.CLOSED){	
				t.path_h = calculate_dist((t.current_position.x + 16), (t.current_position.y + 16), target_x, target_y);
				t.path_g =  calculate_dist(origin_x, origin_y, (t.current_position.x + 16), (t.current_position.y + 16));
				t.path_f = t.path_g + t.path_h;
				t.state = ASTAR.OPEN;
				t.parent_id = parent_id;
				
				open_tiles.add(t);
			}
		}	
	}
	
	private void DRAW() {
		batch.begin();
		for (Entity e: entities){
			batch.draw(e.texture, e.current_position.x,e.current_position.y, e.w, e.h);
		}
		
		int i = 0;
		for (Entity e: path){
			batch.draw(no_path, e.current_position.x,e.current_position.y, e.w, e.h);
			font.draw(batch, i + "", e.current_position.x+8, e.current_position.y+22);
			i++;
		}
		batch.end(); 
    }

	private void RESET_SEARCH() {
		if (search_over && reset_states){
			for (Tile t: tiles){
				t.state = ASTAR.NULL;
				t.parent_id = 0;
			}
			open_tiles.clear();
			set_current_hero_tile();
			reset_states = false;
		}
    }
	
	private float calculate_dist(float orig_x,float orig_y,float targ_x,float targ_y){
		// USED TO CALCULATE H & G VALUES
		float h, dist_x, dist_y ;
		
		dist_x = Math.abs(orig_x - targ_x);
		dist_y = Math.abs(orig_y - targ_y);
		
		if (dist_x > dist_y){
			h = 14*dist_y + 10*(dist_x-dist_y);
		} else {
			h = 14*dist_x + 10*(dist_y-dist_x);
		}
		return h/10;
	}
	
	private void update_hero_values() {
		// UPDATE HERO
		e_hero.current_position.x = camera.position.x - 8;
		e_hero.current_position.y = camera.position.y - 8;
		e_hero.hitbox.set(e_hero.current_position.x, e_hero.current_position.y, e_hero.w, e_hero.h);
		
		e_hero.column = Math.round((e_hero.current_position.x-8)/32);
		e_hero.row = Math.round((e_hero.current_position.y-8)/32);	
		
		// CENTRE OF HEROS TILE
		origin_x = (e_hero.column * 32) + 16;
		origin_y = (e_hero.row * 32) + 16;
	}

	private void process_tile_changes() {
		// LOGIC FOR CHANGING TILES
		if(!controls.processed_click){		
			// May need to remove tiles
			//Iterator<Entity> tiles = entities.iterator();
			controls.processed_click = true;
			
			// find tile position
			column = Math.round((controls.mouse_map_click_at.x-16)/32);
			row = Math.round((controls.mouse_map_click_at.y-16)/32);	
			
			if (controls.LMB){		
				for(Tile t : tiles){
					if (t.row == row && t.column == column){
						if (t.e_type == E_TYPE.BLOCKER){
							t.e_type = E_TYPE.FLOOR;
							t.texture = texture;
							break;
						} else {
							t.e_type = E_TYPE.BLOCKER;
							t.texture = rock;
							break;
						}
					}
				}
		    // RMB
			} else {
				for(Tile t : tiles){
					if (t.row == row && t.column == column){
						if (t.e_type != E_TYPE.GOAL){
							has_target = true;
							target_x = t.current_position.x + 16;
							target_y = t.current_position.y + 16;
							t.e_type = E_TYPE.GOAL;
							t.texture = goal;
						} else {	
							has_target = false;
							t.e_type = E_TYPE.FLOOR;
							t.texture = texture;
						}
					} else if (t.e_type == E_TYPE.GOAL){ 
						t.e_type = E_TYPE.FLOOR;
						t.texture = texture;
					}
				}
			}
		}
	}

	private void set_current_hero_tile() {
		// SHOW CURRENT TILE HERO IS ON
		for(Tile t : tiles){
			if (t.row == e_hero.row && t.column == e_hero.column){
				t.texture = current;
				current_tile = t;
				start_tile = t;
			} else if(t.texture == current) {
				t.texture = texture;
			}
		}
	}

	private void process_movements() {
		// MOVEMENT
		if (controls.move_left){
			if (can_move(-speed, 0)){
				camera.position.x -= speed;
			}	
		}
		if (controls.move_right){
			if (can_move(speed, 0)){
				camera.position.x += speed;
			}	
		}
		if (controls.move_up){
			if (can_move(0, speed)){
				camera.position.y += speed;
			}	
		}
		if (controls.move_down){
			if (can_move(0, -speed)){
				camera.position.y -= speed;
			}
		}
	}
	
	private boolean can_move(float xx, float yy){
		boolean q = true;
		Vector2 tmp = new Vector2(e_hero.current_position);
		tmp.x += xx;
		tmp.y += yy;
		Rectangle rec = new Rectangle(tmp.x, tmp.y, e_hero.w, e_hero.h);
		
		for (Entity e: entities){
			if(e.e_type == E_TYPE.BLOCKER){
				if (e.hitbox.overlaps(rec)){
					q = false;
					break;
				}
			}
		}
		
		return q; 
	}

	@Override
    public void resize(int width, int height) {}

	@Override
    public void show() {}

	@Override
    public void hide() {}

	@Override
    public void pause() {}

	@Override
    public void resume() {}

	@Override
    public void dispose() {}
}