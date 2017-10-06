package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {
    // Минимальная скорость шарика
    private static float minBallSpeedY = 2;
    private static float minBallSpeedX = 2;
    // Все необходимые вещи
	private SpriteBatch batch;
	private Ball ball;
	private Texture gameover;
	private Texture youwin;
    private Platform platform;
    // Кирпичи
	private int brksCount = 7;
	private Brick[] brks = new Brick[brksCount];
    // Граница области рисования
	private Vector2 maxPos = new Vector2(0,0);
	// Переменная для хранения предыдущей позиции шарика
	private Vector2 oldPos = new Vector2(0,0);
    // Состояние игры
	private boolean bGameOver = false;
	private boolean bWin = false;

	@Override
	public void create () {
	    // Создаем SpriteBatch
		batch    = new SpriteBatch();
		// Создаем игровые компоненты
		ball     = new Ball    ("ball.png",     200, 200, minBallSpeedX, minBallSpeedY);
		platform = new Platform("platform.png", 400,   0,   0, 0);
		// Создаем картинки, показываемые при завершении игры
		gameover = new Texture(Gdx.files.internal("gameover.jpg"));
		youwin   = new Texture(Gdx.files.internal("youwin.png"));
		// Создаем два ряда кирпичей
		for (int i=0; i<brksCount; i++) {
			int y = 300;
			if ((i & 1) == 0) y = 420;
			brks[i] = new Brick("brick.png", i * 80 + 30, y);
		}
		// Определяем максимальные координаты окна
		maxPos.y = Gdx.graphics.getHeight();
		maxPos.x = Gdx.graphics.getWidth();
	}

	@Override
	public void render () {
		// Очищаем пространство вывода
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!bGameOver) { // Если игра не закончена

			// CONTROL: Управляем скоростью платформы клавишами ВЛЕВО и ВПРАВО
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  { platform.speed.x -= 0.3; }
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { platform.speed.x += 0.3; }

			// MOVE: Осуществляем вычисление новых координат платформы и шарика
			ball.move(maxPos);
			platform.move(maxPos);

			// COLLISIONS: Проверяем все кирпичи на предмет столкновения с шариком
			for (Brick brk : brks) {
			    // Для каждого выбранного кирпича brk
				if (brk.isCollision(ball.pos, ball.img)) {
				    // Если есть коллизия - вычисляем предыдующую позицию шарика
					oldPos.set(ball.pos.x - ball.speedMult.x * ball.speed.x, ball.pos.y - ball.speedMult.y * ball.speed.y);
					if (brk.isCollisionHorizontal(oldPos, ball.img)) ball.speed.y = -ball.speed.y;
					else if (brk.isCollisionVertical(oldPos, ball.img)) ball.speed.x = -ball.speed.x;
					else {
						ball.speed.x = -ball.speed.x;
						ball.speed.y = -ball.speed.y;
					}
					// Перемещаем шарик за пределы кирпича
					ball.pos.x = oldPos.x;
					ball.pos.y = oldPos.y;
					// Так как мы не можем простым способом удалить кирпич из массива -
					// меняем ему координату высоты на отрицательную
					brk.pos.y *= -1;
				}
			}

			// WIN/LOSE: Условие завершения с выигрышем - нет кирпичей с положительной высотой
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

			// REBOND: Работа с отбиванием шарика
			if (ball.pos.y < platform.img.getHeight()) { // Шарик ниже платформы - это наш случай
				if (       (ball.pos.x > platform.pos.x) // Игрок успел подвести платформу. Начинаем расчет угла отражения шарика.
						&& (ball.pos.x + ball.img.getWidth() < platform.pos.x + platform.img.getWidth())) {

					// Вычислим центр шарика
					float ballCenter = ball.pos.x + ball.img.getWidth() / 2;
					// Вычислим центр платформы
					float platformCenter = platform.pos.x + platform.img.getWidth() / 2;
					// Вычислим расстояние между центром шара центром платформы
					float distance = ballCenter - platformCenter;
					// Отладочный вывод для тюнинга
					// System.out.println("=ro:"+ro);

					// Обратим вектор вертикальной скорости
					ball.speed.y = - ball.speed.y;
                    // Горизонтальная скорость зависит только от расстояния от центра платформы
					ball.speed.x = distance / 20; // Magic Number - подбирается экспериментально
					// Чтобы не было слишком медленно - установим минимальную вертикальную скорость скорость
                    if (ball.speed.y < minBallSpeedY) ball.speed.y = minBallSpeedY;

					// Выведем шарика за платформу
					ball.pos.y = platform.img.getHeight();
				} else {
					// Игрок промахнулся - игре конец
					bGameOver = true;
				}
			}
		}

		// RENDER
		batch.begin();
		if (!bGameOver) {
			batch.draw(ball.img, ball.pos.x, ball.pos.y);             // ball
			for (Brick brk : brks) brk.render(batch);                 // bricks
			batch.draw(platform.img, platform.pos.x, platform.pos.y); // platform
		} else {
			// game over
            if (bWin) batch.draw(youwin,   maxPos.x / 2 - youwin.getWidth()   / 2, maxPos.y / 2 - youwin.getHeight() / 2);
            else      batch.draw(gameover, maxPos.x / 2 - gameover.getWidth() / 2, maxPos.y / 2 - gameover.getHeight() / 2);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		ball.dispose();
		for (Brick brk : brks) brk.dispose();
		gameover.dispose();
		youwin.dispose();
	}
}
