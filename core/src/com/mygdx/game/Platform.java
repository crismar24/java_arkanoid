package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Platform {

    public Texture img;
    public Vector2 pos;
    public Vector2 speed;
    public Vector2 speedMult;
    public Vector2 maxSpeed;
    public Vector2 friction;

    public Platform(String imgFileName, int x, int y, float speed_x, float speed_y) {
        img = new Texture(Gdx.files.internal(imgFileName));
        pos = new Vector2(x, y);
        speed = new Vector2(speed_x, speed_y);
        speedMult = new Vector2(2,0);
        maxSpeed = new Vector2(7,0);
        friction = new Vector2(1f / 10f * 9.5f, 0);
    }

    public void move (Vector2 maxpos) {
        // Рассчитаем следующую позицию платформы
        pos.x = pos.x + speedMult.x * speed.x;
        // Негуманно двигать платформу быстрее
        if (speed.x > maxSpeed.x) speed.x = maxSpeed.x;
        // Тормозим платформу об левую стену
        if (pos.x < 0) { speed.x = 0; pos.x = 0; }
        // Тормозим платформу об правую стену
        if (pos.x > maxpos.x - img.getWidth()) { speed.x = 0; pos.x = maxpos.x - img.getWidth(); }
        // Притормаживаем платформу трением
        speed.x *= friction.x;
    }

    public void dispose () {
        img.dispose();
    }
}
