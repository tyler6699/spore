package com.teamclanmatch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teamclanmatch.game.MainGame;
import com.teamclanmatch.managers.Device;

public class MainMenu implements Screen  {
	private MainGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private Device device;
	
	public MainMenu(MainGame game) {
		this.game = game;
		device = new Device();
		
		camera = new OrthographicCamera();
		// 1 = 32 pixel
		camera.setToOrtho(false, (device.w / device.h) * 10, 10);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		// Test Image
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		sprite = new Sprite(region);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
	}

	@Override
    public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(sprite, 0,0,10,10);
		batch.end();
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
		batch.dispose();
		texture.dispose();
    }

}
