package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Brick {

    public Texture img;
    public Vector2 pos;

    public Brick (String imgFileName, int x, int y) {
        img = new Texture(Gdx.files.internal(imgFileName));
        pos = new Vector2(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(img, pos.x, pos.y);
    }

    public boolean isCollision(Vector2 ballPosition, Texture ballImg) {
        if (       (ballPosition.x                       < pos.x + img.getWidth()   )  // левая сторона img должна быть слева от правой стороны brk
                && (ballPosition.x + ballImg.getWidth()  > pos.x                    )  // правая сторона img должна быть справа от левой стороны brk
                && (ballPosition.y                       < pos.y + img.getHeight()  )  // верхняя сторона img должна быть сверху от нижней стороны brk
                && (ballPosition.y + ballImg.getHeight() > pos.y                    )) // нижняя сторона img должна быть снизу от верхней стороны brk
            return true;
        else return false;
    }

    public boolean isCollisionHorizontal(Vector2 ballOldPosition, Texture ballImg) {
        if (       (ballOldPosition.x                  < pos.x + img.getWidth())
                && (ballOldPosition.x + ballImg.getWidth() > pos.x))
            return true;
        else return false;
    }

    public boolean isCollisionVertical (Vector2 ballOldPosition, Texture ballImg) {
        if (       (ballOldPosition.y                   < pos.y + img.getHeight())
                && (ballOldPosition.y + ballImg.getHeight() > pos.y))
            return true;
        else return false;
    }

    public void dispose () {
        img.dispose();
    }
}
