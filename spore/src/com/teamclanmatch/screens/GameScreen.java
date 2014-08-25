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
import com.teamclanmatch.utils.Maths;

public class GameScreen implements Screen {
	private MainGame game;
	
	public ArrayList<Entity> entities;
	public ArrayList<Tile> tiles;
	public ArrayList<Tile> open_tiles;

	public int[] find_tiles;
	public ArrayList<Tile> path;
	public Tile lowest_tile;
	public Tile current_tile;
	public Tile start_tile;
	public int tile_size;
	public boolean search_over, reset_states;
	int column, row;
	
	// TEST
	boolean step = false;
	boolean breaks = false;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Device device;
	private BitmapFont font;
	private Texture texture, rock, hero, goal, no_path, current;
	private Controller controls;
	Entity e_hero;
	
	// Entity ID
	int id 	= 0;
	int min_distance = 3;
	int SQUARE = 20;
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
		tile_size = 32;
		
		// All Entities
		entities = new ArrayList<Entity>();
		tiles = new ArrayList<Tile>();
		open_tiles = new ArrayList<Tile>();
		path = new ArrayList<Tile>();
		find_tiles = new int[] {1, -1, -SQUARE, SQUARE, SQUARE-1, SQUARE+1, -(SQUARE-1), -(SQUARE+1)};
		//find_tiles = new int[] {1, -1, -SQUARE, SQUARE};
		// FONT
		font = new BitmapFont();
		font.scale(.1F);
		
		// Hide Cursor
		//Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
		
		texture  = new Texture(Gdx.files.internal("data/32x32.png"));
		rock     = new Texture(Gdx.files.internal("data/rock.png"));
		goal     = new Texture(Gdx.files.internal("data/goal.png"));
		hero     = new Texture(Gdx.files.internal("data/blue.png"));
		no_path  = new Texture(Gdx.files.internal("data/path-no.png"));
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
				t.w = tile_size;
				t.h = tile_size;
				t.row = y;
				t.column = x;
				t.current_position.x = x * tile_size;
				t.current_position.y = y * tile_size;
				t.hitbox = new Rectangle(t.current_position.x, t.current_position.y, t.w, t.h);
				t.texture = texture;
				if(t.column == 0 || t.column == SQUARE -1 || t.row == 0 || t.row == SQUARE-1){
					t.e_type = E_TYPE.BLOCKER;
					t.texture = rock ;
				}
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
			RESET_SEARCH();
			set_current_hero_tile();
			search_over = false;
			controls.spacebar = false;
		}
					
		A_STAR_SEARCH();
	
		camera.update();
		// System.out.println(e_hero.row + " " + e_hero.column + " y:" + e_hero.current_position.y);
    }
	
	private void A_STAR_SEARCH() {
		int search_time = 0;
		if (!search_over && has_target){
			current_tile.state = ASTAR.CLOSED;
			while (!search_over){
				search_time ++;
				int move_cost;
				
				// SCORE ADJACENT TILES
				for(int i = 0; i < find_tiles.length; i++){
					move_cost = i < 4? 10 : 14;
					mark_tile(current_tile.id, current_tile.id + find_tiles[i] , move_cost);
				}

				// FIND MIN F VALUE
				float min = Float.MAX_VALUE;
				for (Tile t : open_tiles){	
					if (lowest_tile == null || (t.path_f < min)) {
						if (t.state != ASTAR.CLOSED){
							lowest_tile = t;
							min = t.path_f;
						}
					}
				}	
								
				if (lowest_tile == null){
					System.out.println("NO PATH");
					search_over = true;
				} else {
					current_tile = lowest_tile;
					current_tile.state = ASTAR.CLOSED;
					
					if (current_tile.path_h <= min_distance){
						// Get Path
						path.clear();
						path.add(current_tile);

						while (current_tile.id != start_tile.id ){
							current_tile = tiles.get(current_tile.parent_id);
							if (current_tile.state == ASTAR.CLOSED){
								path.add(current_tile);	
							}
						}
						search_over = true;
						reset_states = true;
					}	
					
				}
				if(search_time > 500){
					System.out.println("CRASH");
					search_over = true;
					reset_states = true;
					path.clear();
				}
			}
		}
    }

	private void mark_tile(int parent_id, int tile_id, int move_cost){		
		Tile parent = tiles.get(parent_id);
		float tile_x, tile_y, p_x, p_y;
		
		if (tile_id < tiles.size() && tile_id >= 0){
			Tile t = tiles.get(tile_id);
			
			//DONT SKIP FROM RIGHT TO LEFT (MIRROR)
			if (!(parent.column == SQUARE-1 && t.column == 0 || parent.column == 0 && t.column == SQUARE-1)){
				tile_x = t.current_position.x + (tile_size/2);
				tile_y = t.current_position.y + (tile_size/2);
				p_x = parent.current_position.x + (tile_size/2);
				p_y = parent.current_position.y + (tile_size/2);
				
				if(t.e_type == E_TYPE.FLOOR && t.state != ASTAR.CLOSED){
					if (t.state == ASTAR.OPEN){
						// ALREADY ON OPEN LIST - SHOULD WE REPLACE THE PARENT?					
						int test = parent.id - tile_id;
						int move_value = t.current_position.dst(parent.current_position) == SQUARE ? 10 : 14;
						
						if ((t.path_h + move_value + parent.path_g) < t.path_f){
							t.path_f = t.path_h + move_value + parent.path_g;
							t.parent_id = parent.parent_id;
						}
					} else { 
						// NEW TILE ADD TO OPEN
						t.parent_id = parent_id;
						t.state = ASTAR.OPEN;
						t.path_h = (calculate_dist(tile_x, tile_y, target_x, target_y, true));
						t.path_g = parent.path_g + move_cost;	
						t.path_f = t.path_g + t.path_h;
										
						open_tiles.add(t);
					}
				}
			}
		}	
	}
	
	private void DRAW() {
		batch.begin();
		for (Entity e: entities){
			batch.draw(e.texture, e.current_position.x,e.current_position.y, e.w, e.h);
		}
		
		//for (Entity e: path){
			//batch.draw(no_path, e.current_position.x,e.current_position.y, e.w, e.h);
		//}
		
		Entity prev = new Entity();
		for (int i = 0;i < path.size();i++){
			Entity p = path.get(i);
			if (i == 0){
				Maths.drawLine(batch, rock, p.current_position.x + (tile_size/2), p.current_position.y + (tile_size/2), target_x, target_y,2);
			} else {
				Maths.drawLine(batch, rock, prev.current_position.x + (tile_size/2), prev.current_position.y + (tile_size/2), p.current_position.x + (tile_size/2), p.current_position.y + (tile_size/2),2);
			}
				prev = p;
		}

		batch.end(); 
    }
	

	private void RESET_SEARCH() {
		if (search_over && reset_states){
			for (Tile t: tiles){
				t.state = ASTAR.NULL;
				t.parent_id = 0;
				t.path_f = 0;
				t.path_g = 0;
				t.path_h = 0;
			}
			open_tiles.clear();
			set_current_hero_tile();
			reset_states = false;
		}
    }
	
	private int calculate_dist(float orig_x,float orig_y,float targ_x,float targ_y, boolean diagonal){
		// USED TO CALCULATE H & G VALUES
		float h, dist_x, dist_y ;
		
		if (diagonal){
			dist_x = Math.abs(orig_x - targ_x);
			dist_y = Math.abs(orig_y - targ_y);
			
			if (dist_x > dist_y){
				h = 14*dist_y + 10*(dist_x-dist_y);
			} else {
				h = 14*dist_x + 10*(dist_y-dist_x);
			}
			return (int) (h/100);
		} else {
			h = (Math.abs(orig_x-targ_x) + Math.abs(orig_y-targ_y));
			return (int) h/10;
		}
		
	}
	
	private void update_hero_values() {
		// UPDATE HERO
		e_hero.current_position.x = camera.position.x - 8;
		e_hero.current_position.y = camera.position.y - 8;
		e_hero.hitbox.set(e_hero.current_position.x, e_hero.current_position.y, e_hero.w, e_hero.h);
		
		e_hero.column = Math.round((e_hero.current_position.x-8)/tile_size);
		e_hero.row = Math.round((e_hero.current_position.y-8)/tile_size);	
		
		// CENTRE OF HEROS TILE
		origin_x = (e_hero.column * tile_size) + 16;
		origin_y = (e_hero.row * tile_size) + 16;
	}
	

	private void process_tile_changes() {
		// LOGIC FOR CHANGING TILES
		if(!controls.processed_click){		
			// May need to remove tiles
			//Iterator<Entity> tiles = entities.iterator();
			controls.processed_click = true;
			
			// find tile position
			column = Math.round((controls.mouse_map_click_at.x-(tile_size/2))/tile_size);
			row = Math.round((controls.mouse_map_click_at.y-(tile_size/2))/tile_size);	
			
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
							target_x = t.current_position.x + (tile_size/2);
							target_y = t.current_position.y + (tile_size/2);
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