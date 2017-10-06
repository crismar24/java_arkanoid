package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    
    public Texture img;
    public Vector2 pos;
    public Vector2 speed;
    public Vector2 speedMult;
    public Vector2 maxSpeed;
    
    public Ball (String imgFileName, int x, int y, float speed_x, float speed_y) {
        img = new Texture(Gdx.files.internal(imgFileName));
        pos = new Vector2(x, y);
        speed = new Vector2(speed_x,speed_y);
        speedMult = new Vector2(2,2);
        maxSpeed = new Vector2(7,7);
    }
    
    public void move (Vector2 maxpos) {
        // Вычисляем следующую позицию на основании скорости и множителя
        if (speed.x != 0) { pos.x = pos.x + speedMult.x * speed.x; }
        if (speed.y != 0) { pos.y = pos.y + speedMult.y * speed.y; }
        // Если скорость выше предельной - уменьшаем до предельной
        if (speed.x > maxSpeed.x) speed.x = maxSpeed.x;
        if (speed.y > maxSpeed.y) speed.y = maxSpeed.y;
        // Обработка отскоков от стен
        if (pos.x < 0) { speed.x *= -1; pos.x = 0; } // Отскок от левой стены
        // if (pos.y < 0) { speed.y = -speed.y; pos.y = 0; } // Нижняя граница
        // Отскок от правой стены
        if (pos.x > maxpos.x - img.getWidth())  { speed.x = -speed.x; pos.x = maxpos.x - img.getWidth();  }
        // Отскок от потолка
        if (pos.y > maxpos.y - img.getHeight()) { speed.y = -speed.y; pos.y = maxpos.y - img.getHeight(); }
        // Притяжение (отключено)
        // speed.y -= 0.01;
    }

    public void dispose () {
        img.dispose();
    }
}
