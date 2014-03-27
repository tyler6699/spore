package com.teamclanmatch.screens;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamclanmatch.Entity;
import com.teamclanmatch.game.MainGame;
import com.teamclanmatch.managers.Controller;
import com.teamclanmatch.managers.Device;

public class GameScreen implements Screen {
	private MainGame game;
	public ArrayList<Entity> entities;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Device device;
	private BitmapFont font;
	private Texture texture, rock;
	private Controller controls;
	
	// Entity ID
	int id 	= 0;
	
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
		
		// FONT
		font = new BitmapFont();
		font.scale(1);
		
		// Hide Cursor
		//Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
		
		texture = new Texture(Gdx.files.internal("data/32x32.png"));
		rock  = new Texture(Gdx.files.internal("data/rock.png"));
		Gdx.input.setInputProcessor(controls);
		
		create_map();
	}
	
	private void create_map(){
		int SQUARE = 18;
		for (int y = 0; y < SQUARE; y++) {
			for (int x = 0; x < SQUARE; x++) {	
				Entity e = new Entity();
				e.id = id;
				e.current_position.x = x << 5;
				e.current_position.y = y << 5;
				e.texture = texture;
				id += 1;
				entities.add(e);
			}
		}		
		
	}
	
	@Override
    public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(texture, 0,0,64,64);
		for (Entity e: entities){
			batch.draw(e.texture, e.current_position.x,e.current_position.y,32,32);
		}
		
		batch.end();
		
		//Collections.sort(entities);
		
		if (controls.move_left){
			camera.position.x -= 1;
		}
		
		if (controls.move_right){
			camera.position.x += 1;
		}
		
		if (controls.move_up){
			camera.position.y += 1;
		}
		
		if (controls.move_down){
			camera.position.y -= 1;
		}
		
		if(!controls.processed_click){
			System.out.println(controls.mouse_map_click_at.x);
			controls.processed_click = true;
			Entity e = new Entity();
			e.id = id;
			e.current_position.x = controls.mouse_map_click_at.x - 16;
			e.current_position.y = controls.mouse_map_click_at.y - 16;
			e.texture = rock;
			id += 1;
			entities.add(e);
		}
		
		camera.update();
    }

	@Override
    public void resize(int width, int height) {   
    }

	@Override
    public void show() {   
    }

	@Override
    public void hide() {  
    }

	@Override
    public void pause() {    
    }

	@Override
    public void resume() {   
    }

	@Override
    public void dispose() { 
    }

}
