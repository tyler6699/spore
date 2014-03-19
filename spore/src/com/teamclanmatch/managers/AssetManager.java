package com.teamclanmatch.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
	public static Texture example;
	
	public AssetManager(){
		example = new Texture(Gdx.files.internal("data/libgdx.png"));
	}
}
