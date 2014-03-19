package com.teamclanmatch.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GLTexture;

import com.teamclanmatch.screens.GameScreen;
import com.teamclanmatch.screens.MainMenu;

public class MainGame extends Game {
	private MainMenu main_menu;
	private GameScreen game_screen;
	
	@Override
	public void create() {			
		// Allow odd sized images
		GLTexture.setEnforcePotImages(false);
		
		// Main Menu
		main_menu = new MainMenu(this);
		
		// Game Screen
		game_screen = new GameScreen(this);
		
		setScreen(main_menu);
	}


}
