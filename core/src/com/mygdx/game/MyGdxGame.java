package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	int brksCount = 7;
	Brick[] brks = new Brick[brksCount];

	Vector2 position = new Vector2(0, 0);
	Vector2 speed = new Vector2(0, 0);
	Vector2 maxPosition = new Vector2(0,0);
	Vector2 oldPos = new Vector2(0,0);

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("ball.png");
		// make wall
		for (int i=0; i<brksCount; i++) {
			int y = 300;
			if ((i & 1) == 0) y = 420;
			brks[i] = new Brick("brick.png", i * 80 + 30, y);
		}
		// max coords
		maxPosition.y = Gdx.graphics.getHeight() - img.getHeight();
		maxPosition.x = Gdx.graphics.getWidth() - img.getWidth();
	}

	@Override
	public void render () {
		// clear
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// control
		if (Gdx.input.isKeyPressed(Input.Keys.UP))    { speed.y += 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  { speed.y -= 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  { speed.x -= 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { speed.x += 1; }

		// physics
		if (speed.x != 0) { position.x = position.x + 2 * speed.x; }
		if (speed.y != 0) { position.y = position.y + 2 * speed.y; }
		if (speed.x > 7) speed.x = 7;
		if (speed.y > 7) speed.y = 7;
		if (position.x < 0) { speed.x = -speed.x; position.x = 0; }
		if (position.y < 0) { speed.y = -speed.y; position.y = 0; }
		if (position.x > maxPosition.x - img.getWidth())  { speed.x = -speed.x; position.x = maxPosition.x - img.getWidth();  }
		if (position.y > maxPosition.y - img.getHeight()) { speed.y = -speed.y; position.y = maxPosition.y - img.getHeight(); }

		// collision detection and reaction
		for (Brick brk : brks) {
			if (brk.isCollision(position, img)) {
				oldPos.set(position.x - 2 * speed.x, position.y - 2 * speed.y);
				if      (brk.isCollisionHorizontal(oldPos, img))   speed.y = -speed.y;
				else if (brk.isCollisionVertical(oldPos, img))     speed.x = -speed.x;
				else {                                             speed.x = -speed.x; speed.y = -speed.y; }
				// out of the box
				position.x = oldPos.x;
				position.y = oldPos.y;
			}
		}

		// render
		batch.begin();
		batch.draw(img, position.x, position.y);
		for (Brick brk : brks) {
			brk.render(batch);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
