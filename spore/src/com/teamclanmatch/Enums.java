package com.teamclanmatch;

public class Enums {
		
	public enum STATE {
		IDLE,
		ATTACK,
		ROAM, 
		HIDE,
		ALIVE,
		DYING, 
		FALLING;
	}
	
	public enum E_TYPE {
		FLOOR,
		BLOCKER,
		GOAL,
		SOLID,
		MOVABLE, 
		HERO;
	}
	
	public enum ENEMY_TYPE {
		BOT,
		NULL;
	}
	
	public enum FACING{
		UP,
		DOWN,
		LEFT,
		RIGHT,
		UP_LEFT,
		UP_RIGHT,
		DOWN_LEFT,
		DOWN_RIGHT,
		NONE;
	}
	
	public enum ASTAR {
		OPEN, 
		CLOSED,
		NULL
	}
	
	public enum TILE_TYPE {
		EMPTY,
		FLOOR,
		BLOCKER,
		GOAL,
		CRATE,
		MANA,
		HERO,
		FENCE,
		WOOD,
		HAMMER,
		TOOL,
		BLOCK,
		HAND_ITEM,
		TREE,
		// BLOCKS
		GRASS,
		SAND,
		WATER,
		SHORE, 
		ROWBOAT,
		BOAT, 
		ROD, WEAPON, FISH
	}
	
}