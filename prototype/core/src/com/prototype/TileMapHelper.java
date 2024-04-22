package com.prototype;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Follows the tutorial on https://www.youtube.com/watch?v=PMiodP2q8-4
 */

public class TileMapHelper {
    
    private TiledMap tilemap;

    public TileMapHelper(){

    }

    public OrthogonalTiledMapRenderer setupMap(){
        tilemap = new TmxMapLoader().load("map/deskmap.tmx");
        return new OrthogonalTiledMapRenderer(tilemap);
    }
}
