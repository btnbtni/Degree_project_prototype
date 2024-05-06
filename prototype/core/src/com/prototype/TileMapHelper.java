package com.prototype;

import java.util.Random;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Follows the tutorial on https://www.youtube.com/watch?v=PMiodP2q8-4
 */

public class TileMapHelper {
    
    private TiledMap tilemap;

    public TileMapHelper(){

    }

    public OrthogonalTiledMapRenderer setupMap(){
        // tilemap = new TmxMapLoader().load("map/deskmap2.tmx");
        tilemap = new TmxMapLoader().load("tileMap/office.tmx");
        return new OrthogonalTiledMapRenderer(tilemap);
    }

    public MapObjects getMapObjects(String tileLayerName){
        return tilemap.getLayers().get(tileLayerName).getObjects();
    }

    /**
     * Code inspired by: https://stackoverflow.com/questions/20063281/libgdx-collision-detection-with-tiledmap
     */
    public boolean detectCollision(Rectangle player, String layer){

        MapObjects collisionObjects = getMapObjects(layer);

        for (RectangleMapObject rectangleObject : collisionObjects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, player)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Code inspired by: https://stackoverflow.com/questions/28522313/java-libgdx-how-to-check-polygon-collision-with-rectangle-or-circle
     */
    public PolygonMapObject detectInteraction(Rectangle player, String layer){

        MapObjects interactionObjects = getMapObjects(layer);

        Polygon playerPolygon = new Polygon(new float[] { 0, 0, player.width, 0, player.width,
            player.height, 0, player.height });
        playerPolygon.setPosition(player.x, player.y);

        for (PolygonMapObject polygonObject : interactionObjects.getByType(PolygonMapObject.class)) {
            Polygon polygon = polygonObject.getPolygon();
            if (Intersector.overlapConvexPolygons(polygon, playerPolygon)) {
                return polygonObject;
            }
        }

        return null;
    }

    public void toggleExclamationMark(String name){
        MapObject exlamationMapObject = getMapObjects("exclamationmark").get(name);

        exlamationMapObject.setVisible(false);
    }

    public Array<RectangleMapObject> getExclamationMarks(){
        return getMapObjects("exclamationmark").getByType(RectangleMapObject.class);
    }

    public Rectangle getUSB(){
        MapObjects usbObjects = getMapObjects("usb");

        Random rnd = new Random();

        int selectedUSBIndex = rnd.nextInt(usbObjects.getCount());

        Rectangle usb = usbObjects.getByType(RectangleMapObject.class).get(selectedUSBIndex).getRectangle();

        usbObjects.remove(selectedUSBIndex);
        return usb;
    }

    public Array<RectangleMapObject> getDoors(){
        return getMapObjects("doors").getByType(RectangleMapObject.class);
    }

    public void toggleDoor(String name){
        MapObject doorMapObject = getMapObjects("doors").get(name);
        boolean open = doorMapObject.getProperties().get("open", boolean.class);
        doorMapObject.getProperties().put("open", !open);
    }

    public void setExclamationMarks(Integer[] testArray){
        Array<RectangleMapObject> exclamationMarksArray = getExclamationMarks();

        // for(int i = 0; i < exclamationMarksArray.size; i++){
            
        //     if(exclamationMarksArray.get(i).getProperties.get("desk", boolean.class) && testArray[i] == null){
        //         exclamationMarksArray.get(i).setVisible(false);
        //     }
        // }
    }
}
