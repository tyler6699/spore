package com.teamclanmatch.screens;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamclanmatch.Entity;
import com.teamclanmatch.game.MainGame;
import com.teamclanmatch.managers.Device;

public class GameScreen implements Screen {
	private MainGame game;
	public ArrayList<Entity> entities;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Device device;
	private BitmapFont font;
	
	public GameScreen(MainGame game) {
		this.game = game;
		device = new Device();
		
		camera = new OrthographicCamera();
		// 1 = 32 pixel
		camera.setToOrtho(false, (device.w / device.h) * 32, 32);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		// All Entities
		entities = new ArrayList<Entity>();
		
		// FONT
		font = new BitmapFont();
		font.scale(1);
		
		// Hide Cursor
		//Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
	}
	
	@Override
    public void render(float delta) {
		for (Entity e : entities){
			// e.tick(delta, batch, new Player(), entities);
			
			//if(e.type == TYPE.PLAYER){
			//	bot.tick(delta, batch, this, gc);
			//} else if (e.type == TYPE.SOLDIER){
			//	e.tick(delta, batch, bot, entities);
			//}
		}
		
		// Y Sorting
		Collections.sort(entities);
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
