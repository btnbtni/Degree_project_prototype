/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {

    final Prototype game;
    
	private Texture playerImage;

	private OrthographicCamera camera;

	private Rectangle player;

	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private TileMapHelper tileMapHelper;

	public GameScreen(final Prototype game) {
        this.game = game;

		playerImage = new Texture(Gdx.files.internal("player1.png"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		player = new Rectangle();
		player.x = (game.windowSizeX / 2) - (game.tileSize/2);
		player.y = game.windowSizeY/2;
		player.width = game.tileSize;
		player.height = game.tileSize;

		this.tileMapHelper = new TileMapHelper();
		this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
	 
	}
	

	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		orthogonalTiledMapRenderer.setView(camera);
		orthogonalTiledMapRenderer.render();

		game.batch.begin();
		game.batch.draw(playerImage, player.x, player.y);
		game.batch.end();

		float offsetX = 0;
		float offsetY = 0;

		int playerSpeed = 400;


		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			offsetX -= playerSpeed * Gdx.graphics.getDeltaTime();
		} 
   		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			offsetX += playerSpeed * Gdx.graphics.getDeltaTime();

			
		} 
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			offsetY -= playerSpeed * Gdx.graphics.getDeltaTime();
			
		}
   		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			offsetY += playerSpeed * Gdx.graphics.getDeltaTime();
		}

		player.x += offsetX;
		player.y += offsetY;

		int edgePanDistance = 100;

		if(camera.position.x + ((game.windowSizeX / 2) - edgePanDistance) - game.tileSize/2 < player.x || camera.position.x - ((game.windowSizeX / 2) - edgePanDistance) - game.tileSize/2 > player.x) 
			camera.translate(offsetX, 0);
		
		if(camera.position.y + (game.windowSizeY/2 - edgePanDistance) < player.y || camera.position.y - (game.windowSizeY/2 - edgePanDistance) - game.tileSize > player.y)
			camera.translate(0, offsetY);


		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			game.setScreen(new PauseScreen(game, this));
		}


	}
	
	@Override
	public void dispose () {
		
      	playerImage.dispose();
      	// game.batch.dispose();
	}

    @Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		//rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
