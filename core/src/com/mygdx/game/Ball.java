package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    
    public Texture img;
    public Vector2 pos;
    public Vector2 speed;
    
    public Ball (String imgFileName, int x, int y, int speed_x, int speed_y) {
        img = new Texture(imgFileName);
        pos = new Vector2(x, y);
        speed = new Vector2(speed_x,speed_y);
    }
    
    public void move (Vector2 maxpos) {
        // physics
        if (speed.x != 0) { pos.x = pos.x + 2 * speed.x; }
        if (speed.y != 0) { pos.y = pos.y + 2 * speed.y; }
        if (speed.x > 7) speed.x = 7;
        if (speed.y > 7) speed.y = 7;
        if (pos.x < 0) { speed.x = -speed.x; pos.x = 0; }
        //if (pos.y < 0) { speed.y = -speed.y; pos.y = 0; }
        if (pos.x > maxpos.x - img.getWidth())  { speed.x = -speed.x; pos.x = maxpos.x - img.getWidth();  }
        if (pos.y > maxpos.y - img.getHeight()) { speed.y = -speed.y; pos.y = maxpos.y - img.getHeight(); }
    }

    public void dispose () {
        img.dispose();
    }
}
