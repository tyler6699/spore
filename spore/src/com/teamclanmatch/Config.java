package com.teamclanmatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Config {

	Preferences prefs;
	boolean sound_on;
	
	public Config(){
		prefs = Gdx.app.getPreferences("app_name");
	}
	
	public boolean load(){
		// SOUND ON/OFF
		if (!prefs.contains("sound")){
			prefs.putBoolean("sound", true);
			sound_on = true;
		} else {
			sound_on = prefs.getBoolean("sound");
		}
		return true;
	}
	
	public boolean save(){
		prefs.putBoolean("sound", sound_on);
		prefs.flush();
		return true;
	}
}
