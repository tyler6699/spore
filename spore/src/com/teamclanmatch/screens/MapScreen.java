package com.teamclanmatch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamclanmatch.game.MainGame;
import com.teamclanmatch.managers.Controller;
import com.teamclanmatch.renderers.MapRenderer;

public class MapScreen implements Screen {
	MainGame game;
	//public Map map;
	public OrthographicCamera camera;
	public BitmapFont font;
	public SpriteBatch batch;
	public Controller gameController;
	public MapRenderer mapRenderer;
	public int id_count = 0;
	
	public MapScreen(MainGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//mapRenderer.render(delta);
	}

	@Override
	public void show() {
		// PARSE SUPPORT
		//Parse parse = new Parse();
		//Parse parse = new Parse();
		//parse.add_net_score();
		//parse.get_net_score();
		//parse.add_score();
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.zoom = 1.5873016f;
		camera.update();
		
		font = new BitmapFont();
		batch = new SpriteBatch();	
		
		//map = new Map("Island_01", this);
		//Art.load_textures();
		//Controller = new Controller(camera, map);
		//mapRenderer = new MapRenderer(game, gameController, this);
		//Gdx.input.setInputProcessor(gameController);
	}
	
	@Override
	public void resize(int width, int height) {
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