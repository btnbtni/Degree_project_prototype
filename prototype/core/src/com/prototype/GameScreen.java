/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
	private Texture exclamationMarkImage;
	private Texture usbImage;
	
	private Texture openDoorLeft;
	private Texture closedDoorLeft;
	private Texture openDoorRight;
	private Texture closedDoorRight;

	private OrthographicCamera camera;

	private Rectangle player;
	private Rectangle usb;

	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private TileMapHelper tileMapHelper;

	public GameScreen(final Prototype game) {
        this.game = game;

		playerImage = new Texture(Gdx.files.internal("player1cropped.png"));
		exclamationMarkImage = new Texture(Gdx.files.internal("map/exclamationmarklarge.png"));
		usbImage = new Texture(Gdx.files.internal("map/usb.png"));

		openDoorLeft = new Texture(Gdx.files.internal("map/openDoorLeft.png"));
		closedDoorLeft = new Texture(Gdx.files.internal("map/closedDoorLeft.png"));

		openDoorRight = new Texture(Gdx.files.internal("map/openDoorRight.png"));
		closedDoorRight = new Texture(Gdx.files.internal("map/closedDoorRight.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		player = new Rectangle();
		player.x = (game.windowSizeX / 2) - (game.tileSize/2);
		player.y = game.windowSizeY/2;
		player.width = 28;
		player.height = 20;

		

		this.tileMapHelper = new TileMapHelper();
		this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
	 
		usb = tileMapHelper.getUSB();
	}
	

	private void usbCollection(){
		Rectangle tempPlayer = new Rectangle(player.x, player.y, 64, 64);
	
		if(usb != null && tempPlayer.overlaps(usb)){
			usb = null;
			System.out.println("collected");
		}
	}

	private void drawExclamationMarks(){
		Rectangle exclamationRectangle;
		for(RectangleMapObject exclamationMark : tileMapHelper.getExclamationMarks()){
			if(exclamationMark.isVisible()){
				exclamationRectangle = exclamationMark.getRectangle();
				game.batch.draw(exclamationMarkImage, exclamationRectangle.getX(), exclamationRectangle.getY());
			}
		}
	}

	private void drawDoors(){
		Rectangle door;
		for(RectangleMapObject doorObject : tileMapHelper.getDoors()){
			MapProperties doorProperties = doorObject.getProperties();
			door = doorObject.getRectangle();
			if(doorProperties.get("left", boolean.class)){
				if(doorProperties.get("open", boolean.class)){
					game.batch.draw(openDoorLeft, door.getX(), door.getY());
				}
				else{
					game.batch.draw(closedDoorLeft, door.getX(), door.getY());
				}
			}
			else{
				if(doorProperties.get("open", boolean.class)){
					game.batch.draw(openDoorRight, door.getX(), door.getY());
				}
				else{
					game.batch.draw(closedDoorRight, door.getX(), door.getY());
				}
			}
		}
	}
	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		orthogonalTiledMapRenderer.setView(camera);
		orthogonalTiledMapRenderer.render();

		usbCollection();

		game.batch.begin();
		

		drawExclamationMarks();
		
		drawDoors();
		

		if(usb != null){
			game.batch.draw(usbImage, usb.x, usb.y);
		}
		
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

		if(tileMapHelper.detectCollision(player, "collisions")){
			player.x -= offsetX;
			player.y -= offsetY;
		}

		else if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){

			PolygonMapObject interactionObject = tileMapHelper.detectInteraction(player, "interaction");
			
			if(interactionObject != null){
				String objectType = interactionObject.getProperties().get("type", String.class);
				if(objectType.equals("desk")){
					tileMapHelper.toggleExclamationMark(interactionObject.getName());
				}
				else if(objectType.equals("door")){
					tileMapHelper.toggleDoor(interactionObject.getName());
				}
				
			}
		}
		

		int edgePanDistance = 100;

		if(camera.position.x + ((game.windowSizeX / 2) - edgePanDistance) - game.tileSize/2 < player.x || camera.position.x - ((game.windowSizeX / 2) - edgePanDistance) - game.tileSize/2 > player.x) 
			camera.translate(offsetX, 0);
		
		if(camera.position.y + (game.windowSizeY/2 - edgePanDistance) < player.y || camera.position.y - (game.windowSizeY/2 - edgePanDistance) - game.tileSize > player.y)
			camera.translate(0, offsetY);


		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			game.pushPreviousScreen(this);
			game.setScreen(new PauseScreen(game));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.P)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[0]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.K)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[1]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.L)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[2]);
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
