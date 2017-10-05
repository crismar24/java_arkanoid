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
	Ball ball;
	Texture gameover;

	int brksCount = 7;
	Brick[] brks = new Brick[brksCount];

	Vector2 maxPos = new Vector2(0,0);
	Vector2 oldPos = new Vector2(0,0);

	boolean bGameOver = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		ball = new Ball("ball.png", 0, 0);
		gameover = new Texture("gameover.jpg");
		// make wall
		for (int i=0; i<brksCount; i++) {
			int y = 300;
			if ((i & 1) == 0) y = 420;
			brks[i] = new Brick("brick.png", i * 80 + 30, y);
		}
		// max coords
		maxPos.y = Gdx.graphics.getHeight() - ball.img.getHeight();
		maxPos.x = Gdx.graphics.getWidth() - ball.img.getWidth();
	}

	@Override
	public void render () {
		// clear
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// control
		if (Gdx.input.isKeyPressed(Input.Keys.UP))    { ball.speed.y += 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  { ball.speed.y -= 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  { ball.speed.x -= 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { ball.speed.x += 1; }

		// move ball
		ball.move(maxPos);

		// collision detection and reaction
		for (Brick brk : brks) {
			if (brk.isCollision(ball.pos, ball.img)) {
				oldPos.set(ball.pos.x - 2 * ball.speed.x, ball.pos.y - 2 * ball.speed.y);
				if      (brk.isCollisionHorizontal(oldPos, ball.img))   ball.speed.y = - ball.speed.y;
				else if (brk.isCollisionVertical  (oldPos, ball.img))   ball.speed.x = - ball.speed.x;
				else {                                                  ball.speed.x = - ball.speed.x; ball.speed.y = - ball.speed.y; }
				// out of the box
				ball.pos.x = oldPos.x;
				ball.pos.y = oldPos.y;
				// delete brick
				brk.pos.y = - brk.pos.y;
			}
		}

		// condition of end game
		bGameOver = true;
		for (Brick brk : brks) {
			if (brk.pos.y > 0) {
				bGameOver = false;
				break;
			}
		}

		// render
		batch.begin();
		if (!bGameOver) {
			batch.draw(ball.img, ball.pos.x, ball.pos.y);
			for (Brick brk : brks) {
				brk.render(batch);
			}
		} else {
			// game over
			batch.draw(gameover,
					maxPos.x/2 - gameover.getWidth()/2,
					maxPos.y/2 - gameover.getHeight()/2);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		ball.dispose();
		for (Brick brk : brks) {
			brk.dispose();
		}
		gameover.dispose();
	}
}
