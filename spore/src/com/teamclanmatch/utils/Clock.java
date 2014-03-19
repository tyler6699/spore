package com.teamclanmatch.utils;

public class Clock {
	int hours, minutes, seconds, dusk, dawn, days;
	float               scale, bright, dark, one_hour, light_level;
	public time_cycle   time_of_day, sun_time;
	
	public enum time_cycle{
		EARLY_MORNING,
		MORNING,
		MIDDAY,
		AFTERNOON,
		DUSK,
		EVENING,
		MIDNIGHT,
		DAWN
	};
		
	public Clock(int hours, int minutes, int seconds, float scale){
		this.hours   = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.scale   = scale;
		one_hour     = 3600;
		dusk         = 18; // 6PM
		dawn         = 6; // AM
		bright       = 0.7f;
		dark         = 0.1f;
		days         = 0;
	}
	
	public String get_time(){
		return add_zero(hours) + ":" + add_zero(minutes) + ":" + add_zero(seconds);
	}
	
	String add_zero(int number){
		if(number < 10){
			return "0" + Integer.toString(number);
		} else {
			return Integer.toString(number);
		}
	}
	
	public void tick(float t){	
		seconds += t*scale;
		
		if (seconds >= 60){
			minutes += 1;
			seconds = 0;
		}
		if (minutes >= 60){
			hours += 1;
			minutes = 0;
		}
		if (hours >= 24){
			hours = 0;
			days += 1;
		}
		update_sun();
	}
	
	public float get_light_level(float current_level){
		float total_seconds = 0;
		
		if (time_of_day == time_cycle.EVENING){
			if(light_level >= dark){
				total_seconds = seconds + (minutes * 60) + ((hours-dusk) * one_hour);
				light_level = (2*one_hour - total_seconds) * 0.0001f;
			}
			
		} else if(time_of_day == time_cycle.EARLY_MORNING){
			// Starts getting light at 5am
			if(hours >= 5 && light_level <= bright){
				total_seconds = seconds + (minutes * 60) + ((hours-5) * one_hour);
				light_level = total_seconds * 0.0001f;	
			}	
		} else if(time_of_day == time_cycle.MORNING || time_of_day == time_cycle.AFTERNOON){
			light_level = 0.8f;
		}
		
		return light_level;
	}
	
	private void update_sun(){
		if( hours >= 0 && hours <= 6){
			time_of_day = time_cycle.EARLY_MORNING;
		} else if( hours >= 6 && hours <= 12){
			time_of_day = time_cycle.MORNING;
		} else if(hours > 12 && hours < 18 ){
			time_of_day = time_cycle.AFTERNOON;	
		} else if( hours >= 18 && hours <= 24){
			time_of_day = time_cycle.EVENING;
		} else if( hours >= 6 && hours <= 12){
			time_of_day = time_cycle.MORNING;
		}
		
		// Key sun positions
		if(hours == 6){
			sun_time = time_cycle.DAWN;
		} else if(hours == 12 ){
			sun_time = time_cycle.MIDDAY;
		} else if(hours == 18){
			sun_time = time_cycle.DUSK;
		} else if(hours == 24){
			sun_time = time_cycle.MIDNIGHT;	
		}
	}

	public String get_days() {
		return Integer.toString(days);
	}

}
