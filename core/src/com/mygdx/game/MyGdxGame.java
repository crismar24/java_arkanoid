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
	Texture platform;
	Texture youwin;
	Vector2 speed;
	Vector2 pos;

	int brksCount = 7;
	Brick[] brks = new Brick[brksCount];

	Vector2 maxPos = new Vector2(0,0);
	Vector2 oldPos = new Vector2(0,0);

	boolean bGameOver = false;
	boolean bWin = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		ball = new Ball("ball.png", 200, 200, 1, 1);
		gameover = new Texture("gameover.jpg");
		platform = new Texture("platform.png");
		youwin = new Texture("youwin.png");
		speed = new Vector2(0,0);
		pos = new Vector2(0,0);
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

		if (!bGameOver) {

			// control
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				speed.x -= 0.3;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				speed.x += 0.3;
			}

			// move ball
			ball.move(maxPos);

			if (speed.x != 0) {
				pos.x = pos.x + 2 * speed.x;
			} // Рассчитаем следующую позицию платформы
			if (speed.x > 7) {
				speed.x = 7;
			} // Негуманно двигать платформу быстрее
			if (pos.x < 0) {
				speed.x = 0;
				pos.x = 0;
			} // Тормозим платформу об левую стену
			if (pos.x > Gdx.graphics.getWidth() - platform.getWidth()) { // Тормозим платформу об правую стену
				speed.x = 0;
				pos.x = Gdx.graphics.getWidth() - platform.getWidth();
			}

			// Проверяем все кирпичи на предмет столкновения с шариком
			for (Brick brk : brks) {
				if (brk.isCollision(ball.pos, ball.img)) {
					oldPos.set(ball.pos.x - 2 * ball.speed.x, ball.pos.y - 2 * ball.speed.y);
					if (brk.isCollisionHorizontal(oldPos, ball.img)) ball.speed.y = -ball.speed.y;
					else if (brk.isCollisionVertical(oldPos, ball.img)) ball.speed.x = -ball.speed.x;
					else {
						ball.speed.x = -ball.speed.x;
						ball.speed.y = -ball.speed.y;
					}
					// out of the box
					ball.pos.x = oldPos.x;
					ball.pos.y = oldPos.y;
					// Так как мы не можем простым способом удалить кирпич из массива -
					// меняем ему координату высоты на отрицательную
					brk.pos.y = -brk.pos.y;
				}
			}

			// Условие завершения с выигрышем - нет кирпичей с положительной высотой
			bWin = true;
			for (Brick brk : brks) {
				if (brk.pos.y > 0) {
					bWin = false;
					break;
				}
			}

			// Если игрок выиграл, то игра определенно закончилась
			if (bWin) {
				bGameOver = true;
			}

			// Работа с отбиванием шарика
			if (ball.pos.y < platform.getHeight()) {
				// Шарик ниже платформы - это наш случай
				if ((ball.pos.x > pos.x) && (ball.pos.x + ball.img.getWidth() < pos.x + platform.getWidth())) {
					// Игрок успел подвести платформу. Начинаем расчет угла отражения шарика.

					// Обратим вектор вертикальной скорости
					ball.speed.y = -ball.speed.y;

					// Вычислим центр шарика
					double ballCenter = ball.pos.x + ball.img.getWidth();
					// Вычислим центр платформы
					double platformCenter = pos.x + platform.getWidth();
					// Расстояние между центром шара центром платформы
					double ro = ballCenter - platformCenter;

					// Если значение шара плюс-минус небольшая величина, то отражаем с тем же углом, с каким упал
					if (Math.abs(ro) < 20) {
						// т.е. не изменяем горизонтальный вектор скорости
//					} else if (Math.abs(ro) > 18) {
//						// Если слишком большая - инвертируем горизонтальную скорость
//						ball.speed.x *= -1;
					} else {
						// В ином случае

//						double cx = ball.speed.x;
//						double cy = ball.speed.y;
//						// Вычисляем угол падения
//						double alfa = Math.PI * 0.2 * (ro) / 18;
//						// Вычисляем угол отражения
//						ball.speed.x = (float) (cx * Math.cos(alfa) - cy * Math.sin(alfa));
//						ball.speed.y = (float) (cy * Math.cos(alfa) - cx * Math.sin(alfa));
					}

					// Скорректируем положение шарика относительно платформы
					ball.pos.y = platform.getHeight();
				} else {
					// Игрок промахнулся - игре конец
					bGameOver = true;
				}
			}
		}

		// render
		batch.begin();
		if (!bGameOver) {
			batch.draw(ball.img, ball.pos.x, ball.pos.y);
			for (Brick brk : brks) {
				brk.render(batch);
			}
			batch.draw(platform, pos.x, pos.y);
		} else {
			// game over
			if (bWin) {
				// win
				batch.draw(youwin,
						Gdx.graphics.getWidth() / 2 - youwin.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 - youwin.getHeight() / 2);
			} else {
				// lose
				batch.draw(gameover,
						Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
			}
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
		youwin.dispose();
	}
}
