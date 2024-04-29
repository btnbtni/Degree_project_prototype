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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Color;

public class GameScreen implements Screen {

    final Prototype game;
    
	private Texture playerImageDown;
	private Texture playerImageUp;
	private Texture playerImageLeft;
	private Texture playerImageRight;
	private int movingDirection;
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

	private BitmapFont font;


	public GameScreen(final Prototype game) {
        this.game = game;

		playerImageDown = new Texture(Gdx.files.internal("player1cropped.png"));
		playerImageUp = new Texture(Gdx.files.internal("player1upcropped.png"));
		playerImageLeft = new Texture(Gdx.files.internal("player1leftcropped.png"));
		playerImageRight = new Texture(Gdx.files.internal("player1rightcropped.png"));
		exclamationMarkImage = new Texture(Gdx.files.internal("map/exclamationmarklarge.png"));
		usbImage = new Texture(Gdx.files.internal("map/usb.png"));

		openDoorLeft = new Texture(Gdx.files.internal("map/openDoorLeft.png"));
		closedDoorLeft = new Texture(Gdx.files.internal("map/closedDoorLeft.png"));

		openDoorRight = new Texture(Gdx.files.internal("map/openDoorRight.png"));
		closedDoorRight = new Texture(Gdx.files.internal("map/closedDoorRight.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
		System.out.println("X: " + camera.position.x);
		System.out.println("Y: " + camera.position.y);

		player = new Rectangle();
		player.x = (game.windowSizeX / 2) - (game.tileSize/2);
		player.y = game.windowSizeY/2;
		player.width = 28;
		player.height = 20;
		movingDirection = 0;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("joystix monospace.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
		parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

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
		
		if(movingDirection == 0){
			game.batch.draw(playerImageDown, player.x, player.y);
		}else if(movingDirection == 1){
			game.batch.draw(playerImageUp, player.x, player.y);
		}else if(movingDirection == 2){
			game.batch.draw(playerImageLeft, player.x, player.y);
		}else if(movingDirection == 3){
			game.batch.draw(playerImageRight, player.x, player.y);
		}
		String progressString =  "Cases handled:\n" + "     " + game.numberOfAnsweredTests + " / " + game.numberOfTests;
		font.draw(game.batch, progressString, camera.position.x - 512 + (float)(game.windowSizeX*0.85),camera.position.y - 384 + (float)(game.windowSizeY*0.9));
		game.batch.end();
		readInput();

	}

	private void readInput(){

		float offsetX = 0;
		float offsetY = 0;

		int playerSpeed = 400;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			offsetX -= playerSpeed * Gdx.graphics.getDeltaTime();
			movingDirection = 2;
		} 
   		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			offsetX += playerSpeed * Gdx.graphics.getDeltaTime();
			movingDirection = 3;
		} 
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			offsetY -= playerSpeed * Gdx.graphics.getDeltaTime();
			movingDirection = 0;
		}
   		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			offsetY += playerSpeed * Gdx.graphics.getDeltaTime();
			movingDirection = 1;
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
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[0]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[1]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[2]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[3]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[4]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[5]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_6)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[6]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_7)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[7]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_8)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[8]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_9)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[9]);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
			game.pushPreviousScreen(this);
			game.setScreen(game.interactionScreens[0]);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
			game.pushPreviousScreen(this);
			game.setScreen(new EndScreen(game));
		}
	}
	
	@Override
	public void dispose () {
		
      	playerImageDown.dispose();
		playerImageUp.dispose();
		playerImageLeft.dispose();
		playerImageDown.dispose();
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
