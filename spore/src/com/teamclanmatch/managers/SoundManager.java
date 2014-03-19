package com.teamclanmatch.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private static Sound example;
	private static float example_count;
	
	public SoundManager(){
		example = Gdx.audio.newSound(Gdx.files.internal("sound/example.ogg"));
	}
		
	public static void play_example(boolean sound_on){
		example_count ++;
		if (example_count == 1){
			example.play(1f);
		} else if (example_count > 40) {
			example_count = 0;
		}
	}

}