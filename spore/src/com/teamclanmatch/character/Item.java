package com.teamclanmatch.character;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.teamclanmatch.Entity;

public class Item implements Comparable<Item>{
	//public Type     item_type;
	public Entity   item_entity;
	public String   menu;
	public int      quantity, slot_no, row_no;
	public boolean  moving, clicked;
	public Vector2  slot_vector, craft_slot_vector;
	public Polygon  inventory_button;
	public Polygon  crafting_button;
	public Polygon  action_bar_button;
	public float    button_size, button_offset_x_y;
	
	public Item(float button_size, float slot_size){
		float s                 = button_size;
		float[]vertice          = {0,0,  0,s,  s,s,  s,0};
		//this.item_type          = Type.EMPTY;
		this.button_size        = button_size;	
		this.button_offset_x_y  = (slot_size - button_size)/2;
		this.inventory_button   = new Polygon(vertice);
		this.crafting_button    = new Polygon(vertice);
		this.action_bar_button  = new Polygon(vertice);
		this.menu               = "INV";
	}
	
	public int compareTo(Item item) {
		int item_slot = item.slot_no;
		int this_slot = this.slot_no;
		// 0 to 999999
		return (item_slot > this_slot ) ? -1: (item_slot < this_slot) ? 1:0 ;
	}

}
