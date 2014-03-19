package com.teamclanmatch.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Controller extends InputAdapter implements InputProcessor {
	public OrthographicCamera camera;

	// POSITION
	public Vector3  curr  = new Vector3();
	public Vector3  last  = new Vector3(-1, -1, -1);
	public Vector3  delta = new Vector3();
	
	// MOUSE
	public int mouse_x;
	public int mouse_y;
	public int mouse_flip_y;
	public Vector2 mouse_map_vec;
	public Vector2 mouse_screen_vec;
	public Vector2 mouse_screen_click_at;
	public Vector2 mouse_map_click_at;
	public boolean processed_click;
	public Polygon click_screen_area;
	public Polygon click_map_area;
	public float   mouse_time;
	public boolean load_map;
	public boolean save_map;
	public boolean reset_map;
	
	// Distance from Centre
	Vector2      vec_center = new Vector2(0,0);
	public float off_centre_dst = 0;
	
	// MOVEMENT
	public Boolean move_right = false;
	public Boolean move_left  = false;
	public Boolean move_up    = false;
	public Boolean move_down  = false;
	public Boolean LMB        = false;
	public Boolean RMB        = false;
	public Boolean jump       = false;
	public Boolean rotate_ac  = false;
	public Boolean paused     = false;
	
	public Controller(OrthographicCamera camera){
		this.camera     = camera;
		this.mouse_screen_click_at = new Vector2();
		this.mouse_map_click_at    = new Vector2();
		this.mouse_time            = 0;
		this.mouse_map_vec         = new Vector2();
		this.mouse_screen_vec      = new Vector2();
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if( button == Buttons.LEFT){
			LMB = true;
		}
		if( button == Buttons.RIGHT){
			RMB = true;
		}
		
		// MOUSE CLICKED UPDATE VALUES
		float flip_y = Gdx.graphics.getHeight() - y;
		
		// SCREEN COORDS
		this.mouse_screen_click_at.set(x, flip_y);

		this.processed_click = false;
		this.mouse_time = 0;
		
		return false;
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {	
		if( button == Buttons.LEFT){
			LMB = false;
		}
		if( button == Buttons.RIGHT){
			RMB = false;
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		mouse_x      = x;
		mouse_y      = y;
		mouse_flip_y = Gdx.graphics.getHeight() - y;
		mouse_screen_vec.set(x, y);
		
		float mouse_offset_x = x - (Gdx.graphics.getWidth()/2);
		float mouse_offset_y = y - (Gdx.graphics.getHeight()/2);
		off_centre_dst = vec_center.dst(new Vector2(mouse_offset_x, mouse_offset_y));
		
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		check_controls(keycode);	
		return false;
	}
	
	private void check_controls(int keycode) {
		if (keycode == Keys.G)
			Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());

		switch (keycode) {
		case Keys.W:
			move_up = true;
			break;
		case Keys.S:
			move_down = true;
			break;
		case Keys.A:
			move_left = true;
			break;
		case Keys.D:
			move_right = true;
			break;
		case Keys.SPACE:
			LMB       = true;
			break;
		case Keys.Y:
			break;
		case Keys.P:
			paused = !paused;
			break;
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
		case Keys.BACKSPACE:
			Gdx.app.exit();
			break;
		}
    }

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W:
			move_up    = false;
			break;
		case Keys.S:
			move_down  = false;
			break;
		case Keys.A:
			move_left  = false;
			break;
		case Keys.D:
			move_right = false;
			break;
		case Keys.SPACE:
			LMB       = false;
			break;
		}
		return false;
	}
	
 }