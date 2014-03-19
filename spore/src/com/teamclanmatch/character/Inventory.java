package com.teamclanmatch.character;

import java.util.ArrayList;
import java.util.Collections;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.teamclanmatch.Entity;
import com.teamclanmatch.Player;
import com.teamclanmatch.managers.Controller;
import com.teamclanmatch.map.Tile;
import com.teamclanmatch.screens.MapScreen;

public class Inventory {
	
	private Controller  gc;
	public MapScreen        ms;
	//public Crafting       crafting;
	private float           craft_y_offset = 97;
	
	private int             columns = 9;
	private int             action_bar_y_offset = 17;
	private float           o;
	public Polygon          tmp_polygon;
	private Vector2         tmp_prev;	
	public ArrayList<Item>  items;
	public Item             current_item, previous_item;
	public float            x, y, tick, slot_size;
	public int              number_slots;
	public boolean          accessed;
	
	// Offsets for Selector
	float                   frame_width = 7;
	float                   frame_height = 18;
	
	// GFX	
	private SpriteBatch     spriteBatch;
	private Texture         hud_display;
	
	// HAND
	public boolean          item_is_in_use;
	private int             item_in_use_slot;
	public Item             item_in_use;
	public float            place_dst = 200;
	
	public Inventory(Controller gameController, MapScreen mapScreen){
		this.gc             = gameController;
		this.ms             = mapScreen;
		
		this.number_slots   = 35;
		this.slot_size      = 48;
		this.items          = new ArrayList<Item>(number_slots);	
		this.y              = 0;
		this.tick           = 0;
		this.spriteBatch    = new SpriteBatch();
		//this.hud_display    = Art.inventory;
		this.accessed       = false;
		
		this.o = gc.o;
		float[]diamond   = {0*o,8*o,16*o,16*o,32*o,8*o,16*o,0*o};
		this.tmp_polygon = new Polygon(diamond);
		this.tmp_prev    = new Vector2(0,0);
		
		// HANDS
		this.item_is_in_use   = false;
		this.item_in_use_slot = -1;
		
		// CENTRE INVENTORY
		this.x = (Gdx.graphics.getWidth()/2)  - (this.hud_display.getWidth()/2);		
		this.y = (Gdx.graphics.getHeight()/2) - (this.hud_display.getHeight()/2);
		
		// POPULATE EMPTY ITEMS
		for (int i=0; i < this.number_slots+1; i++){
			add_empty_item(i);
		}
		
		// SORT ITEMS
		Collections.sort(items);
		
		// Current Item
		current_item = items.get(0);
		previous_item = current_item;
	}
	
	public void tick(float delta, SpriteBatch ratioBatch){
		this.tick += delta;
			
		empty_the_hand();
		Collections.sort(items);
		
//		if (!gc.show_crafting){
//			set_current_item();
//			update_item_in_use();
//			draw_inventory(delta);
//			draw_actionbar(ratioBatch);
//		}
		
		process_mouse_clicks();			
	}
	
	private void set_current_item(){
//		if (!gc.show_inventory){
//			current_item = items.get(gc.action);
//			
//			if (previous_item.slot_no != gc.action){
//				if (previous_item.item_entity != null){
//					previous_item.item_entity.inactive();
//				}
//				previous_item.moving = false;
//				this.item_is_in_use = false;
//				this.item_in_use = null;
//				previous_item = current_item;
//			}
//			
//			if (current_item.item_entity != null && (current_item.item_entity.placeable || current_item.item_entity.useable)){
//				this.item_is_in_use = true;
//				this.item_in_use_slot = current_item.slot_no;
//				this.item_in_use = current_item;	
//				current_item.moving = true;
//			}
//		}
	}
	
	private void process_mouse_clicks(){
//		boolean processed = false;
//		
//		if (gc.processed_click == false) {
//			// INVENTORY
//			if (gc.show_inventory){
//				for (Item item : items){
//					if(Intersector.overlapConvexPolygons(gc.click_screen_area, item.inventory_button)){	
//						if (process_click(item)){processed = true; break;}	
//					}	
//				}
//				processed = true;	
//				
//			// CRAFTING
//			} else if (gc.show_crafting){
//				for (Item item : items){
//					if(Intersector.overlapConvexPolygons(gc.click_screen_area, item.crafting_button)){	
//						if (process_click(item)){processed = true;break;}	
//					}
//					
//					if (!processed){
//						if(Intersector.overlapConvexPolygons(gc.click_screen_area, crafting.crafted_item.crafting_button)){	
//							if (crafting.crafted_item.item_entity != null){
//								if (process_click(crafting.crafted_item)){
//									processed = true;
//									crafting.empty_craft_items();
//									break;
//								}
//							}
//							processed = true;
//						}
//					}
//				}
//
//			// ACTIONBAR
//			} else {
//				for (Item item : items){
//					if(item.slot_no < 9 &&Intersector.overlapConvexPolygons(gc.click_screen_area, item.action_bar_button)){	
//						if (process_click(item)){processed = true;break;}	
//					}
//				}	
//			}
//			
//			gc.processed_click = processed;
//		}
	}
	
	private void update_item_in_use(){
//		tmp_prev.set(tmp_polygon.getX(), tmp_polygon.getY());
//		Vector2 map_coords = gc.get_map_coords(new Vector2(gc.mouse_x,gc.mouse_y));
//		
//		if (!can_move(item_in_use, ms.map.ObjectsArray, map_coords)){
//			tmp_polygon.setPosition(tmp_prev.x, tmp_prev.y);
//		}
	}
	
	private void draw_inventory(float delta){
//		if (gc.show_inventory && !gc.show_crafting){
//			accessed = true;
//			spriteBatch.begin();
//			spriteBatch.draw(hud_display, x,y);	
//			// DRAW ENTITIES
//			for (Item item : items){
//				if (item.item_type != Type.EMPTY){					
//					if(!item.moving){
//						spriteBatch.draw(item.item_entity.inv_texture, item.slot_vector.x,item.slot_vector.y);
//					} else if (item.item_type != Type.EMPTY && item.moving){
//						spriteBatch.draw(item.item_entity.inv_texture,gc.mouse_x - (slot_size/2),gc.mouse_flip_y-(slot_size/2));
//					}
//				}					
//			}
//			spriteBatch.end();
//			
//		}else{
//			if (!accessed){	}
//			tick = 0;
//		}
	}
	
	private void draw_actionbar(SpriteBatch sb){	
//		spriteBatch.begin();
//		spriteBatch.draw(Art.action_bar, x,0);	
//		// DRAW ENTITIES
//		for (int i=0; i < columns ; i++){
//			Item item = items.get(i);
//			
//			if (item.item_type != Type.EMPTY){
//				if ((item.slot_no == current_item.slot_no) || (item.slot_no != current_item.slot_no && !item.moving) ){
//					spriteBatch.draw(item.item_entity.inv_texture, item.slot_vector.x,24);	
//				}
//			}
//		}
//		
//		Vector2 pos = slot_location(gc.action);
//		spriteBatch.draw(Art.selector, pos.x - frame_width,frame_height);
//		spriteBatch.end();
	}
	
	public void draw_item_in_use(SpriteBatch sb){
//		if (!gc.show_inventory && !gc.show_crafting){
//			Entity e = null;
//			if (item_in_use != null && item_in_use.item_entity != null){
//				int mouse_button_down = (gc.LMB) ? 1 : 0;
//				e = item_in_use.item_entity;
//				
//				update_item_in_use();
//				e.x = tmp_polygon.getX();
//				e.y = tmp_polygon.getY();
//				
//				if (e != null){
//					sb.begin();
//
//					if(e.group_type == Type.TOOL) {
//						if (e.type == Type.HAMMER){
//							((iTool) e).update_and_draw(gc, sb, mouse_button_down, place_dst);				
//						}
//					// CURSOR
//					} else {
//						sb.draw(e.icon_texture,tmp_polygon.getX(), tmp_polygon.getY(),1,1);	
//					}
//					sb.end();
//				}	
//			}	
//		}
	}
	
	public Entity draw_item_in_use(SpriteBatch sb, Player hero, ArrayList<ArrayList<Tile>> mapArray){
		return hero;
//		Entity e = null;
//		
//		if (item_in_use != null && item_in_use.item_entity != null){
//			e = item_in_use.item_entity;
//		}
//		
//		if (e != null){
//			update_item_in_use();
//			e.x = tmp_polygon.getX();
//			e.y = tmp_polygon.getY();
//			
//			if (e != null){
//				if(e.group_type == Type.TOOL) {
//					if(e.type == Type.ROD ) {
//						((Rod) e).update_state(gc, hero, sb, tmp_polygon, gc.mouse_map_click_at, mapArray);
//						((Rod) e).draw(gc, hero, sb, tmp_polygon, gc.mouse_map_click_at);
//					}
//				}
//			}	
//		}
//		
//		return e;
	}
	
	private boolean can_move(Item item, ArrayList<Entity> objArray, Vector2 coords){
//		float half = (item != null && item.item_entity != null) ? item.item_entity.width/2 : .5f;
//		tmp_polygon.setPosition(coords.x - half,coords.y - half);
//		
//		if (item != null && item.item_entity != null && item.item_entity.useable){
//			return true;
//		} else {
//			for (Entity entity : objArray) {
//				if(entity.handitem == false && Intersector.overlapConvexPolygons(tmp_polygon, entity.collision_polygon.hitbox_polygon) && entity.solid){
//					return false;
//				}
//			}		
//		}
//		
		return true;
	}
	
	public Vector2 slot_location(int slot_no){
		Vector2 slot_vector = new Vector2(0,0);
		float start_x, start_y, between_x,between_y, row_x, row_y;
		int row, column;
		
		// PIXEL GAPS
		start_x = x + 24;
		start_y = y + 24;
		between_x = 54;	
		between_y = 54;
		
		// Find ROW & COLUMN
		row = slot_no / columns;
		column = slot_no % columns;
		
		row_x = start_x + (column * between_x);	
		row_y = start_y;
		
		// FIRST ROW ONLY HAS 24 PIXEL GAP
		if (row > 0){
			row_y = y + 36 + (row * between_y );	
		}
		
		// SET VISUAL POSITION OF SLOT
		slot_vector.set(row_x, row_y);
		return slot_vector;
	}
	
	private boolean process_click(Item item){
		if ( pickup(item) ) {
			set_pickup(true, item, item.slot_no);
			return true;
			
		} else if( replace(item) ){
			set_pickup(false, item, -1);
			return true;
			
		} else if( move(item) ){
			move_slot(item);
			clear_slot();				
			return true;
			
		} else if( stack_or_swap(item) ){
			System.out.println("STACK?: " + item.slot_no);	
			return true;	
	
		} else {
			return false;
		}	
	}
	
	public void clear_all_slots(){
//		for (Item item : items){
//			item.item_type = Type.EMPTY;
//			item.item_entity = null;
//			item.quantity = 0;
//			item.moving = false;
//		}
	}
	
	public void clear_slot(){
//		this.item_in_use.item_type = Type.EMPTY;
//		this.item_in_use.item_entity = null;
//		this.item_in_use.quantity = 0;
//		this.item_in_use.moving = false;
//		set_pickup(false, null, -1);
	}
	
	public void move_slot(Item item){
//		item.item_type = this.item_in_use.item_type;
//		item.quantity = this.item_in_use.quantity;
//		item.item_entity = this.item_in_use.item_entity;
//		item.moving = false;
	}
	
	public void set_pickup(boolean pick, Item item, int slot){
		if (item != null){
			item.moving = pick;
			this.item_in_use = item;
		}
		this.item_is_in_use = pick;
		this.item_in_use_slot = slot;
	}
	
	public boolean add_item(Entity entity){
//		// FIND SLOT_NO NOT IN USE
//		int id = this.find_empty_slot();
//		
//		if (id != -1){
//			// FIND ITEM TO UPDATE
//			Item inv_item 			= items.get(id);
//			inv_item.item_type 		= entity.type;
//			inv_item.item_entity 	= entity;
//			inv_item.quantity 		= 1;
//			
//			return true;
//		} else {
//			return false;
//		}		
	}
			
	public boolean add_empty_item(int i){
//		Item i_item = new Item(32, this.slot_size);
//		i_item.item_type 	= Type.EMPTY;
//		i_item.slot_no 		= i;
//		i_item.moving 		= false;
//		i_item.clicked 		= false;
//		i_item.slot_vector 	= slot_location(i_item.slot_no);
//		i_item.craft_slot_vector = new Vector2(i_item.slot_vector.x, i_item.slot_vector.y - craft_y_offset);
//		float bx = i_item.slot_vector.x + i_item.button_offset_x_y;
//		float by = i_item.slot_vector.y + i_item.button_offset_x_y;
//		i_item.inventory_button.setPosition(bx,by);
//		i_item.crafting_button.setPosition(bx,by - craft_y_offset);
//		
//		if (i < columns){
//			i_item.action_bar_button.setPosition(bx,action_bar_y_offset);
//		}
//		// ADD ITEM
//		this.items.add(i_item);
//		return true;
	}
	
	public int find_empty_slot(){	
//		Boolean used = false;
//		int free_slot = -1;
//		
//		// Loop all slots
//		for(int i=0; i<number_slots+1; i++){
//			
//			// LOOP ITEMS TO SEE IF i IS USED
//			for (Item item : items){
//				if (item.slot_no == i && item.item_type != Type.EMPTY){
//					used = true;
//					break;
//				}		
//			}
//			
//			// LOOPED ALL ITEMS AND DIDNT FIND i 
//			if (used==false){
//				free_slot = i;	
//				break;
//			}
//			
//			// RESET USED FOR NEXT CYCLE
//			used = false;
//		}
//		
//		// FREE SLOT NO OR -1 IF FULL INVENTORYs
//		return free_slot;
	}
	
	public boolean pickup(Item item){
		//return !this.item_is_in_use && item.item_type != Type.EMPTY;
	}
	
	public boolean replace(Item item){
		return this.item_is_in_use && this.item_in_use.slot_no == item.slot_no;
	}
	
	//public boolean move(Item item){
		//return item_is_in_use && this.item_in_use_slot != item.slot_no && item.item_type == Type.EMPTY;
	//}
	
	//public boolean craft_move(Item item){
		//return item_is_in_use && item.item_type == Type.EMPTY;
	//}
			
	public boolean stack_or_swap(Item item){
		//return this.item_is_in_use && this.item_in_use.slot_no != item.slot_no && item.item_type != Type.EMPTY;
	return true;
	}
	
	private void empty_the_hand(){
//		// Item is left in hand, Inventory closed
//		if (gc.empty_hand){
//			if (this.item_in_use != null){
//				this.item_in_use.moving = false;
//			}
//			this.item_is_in_use = false;
//			this.item_in_use_slot = -1;
//			gc.empty_hand = false;
//		}
	}
	
	public void empty_hand(){
		if (this.item_in_use != null){
			this.item_in_use.moving = false;
		}
		this.item_is_in_use = false;
		this.item_in_use_slot = -1;
	}

}