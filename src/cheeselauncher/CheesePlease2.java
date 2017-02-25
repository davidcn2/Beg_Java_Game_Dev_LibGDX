/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheeselauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author dcohn
 */
public class CheesePlease2 extends Game {
    
    private SpriteBatch batch;
    private Sprite mouseySprite;
    private Sprite cheeseSprite;
    private Sprite floorSprite;
    private Sprite winTextSprite;
    private boolean win;
    
    @Override
    public void create()
    {
        
        batch = new SpriteBatch();
        
        mouseySprite = new Sprite( new Texture(Gdx.files.internal("images/mouse.png")) );
        mouseySprite.setPosition( 20, 20 );
        
        cheeseSprite = new Sprite( new Texture(Gdx.files.internal("images/cheese.png")) );
        cheeseSprite.setPosition ( 400, 300 );
        
        floorSprite = new Sprite( new Texture(Gdx.files.internal("images/tiles.jpg")) );
        floorSprite.setPosition ( 0, 0 );
        
        winTextSprite = new Sprite( new Texture(Gdx.files.internal("images/you-win.png")) );
        winTextSprite.setPosition( 170, 60);
        
        win = false;
        
    }
    
    @Override
    public void render() {
        
        // process input
        // check user input
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            mouseySprite.translateX( -1 );
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            mouseySprite.translateX( 1 );
        if (Gdx.input.isKeyPressed(Keys.UP))
            mouseySprite.translateY( 1 );
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            mouseySprite.translateY( -1 );
        
        // check win condition
        Rectangle cheeseRectangle = cheeseSprite.getBoundingRectangle();
        Rectangle mouseyRectangle = mouseySprite.getBoundingRectangle();
        
        if ( cheeseRectangle.contains(mouseyRectangle) )
        {
            //System.out.println("Test");
            win = true;
        }
        
        // draw graphics
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        floorSprite.draw ( batch );
        cheeseSprite.draw ( batch );
        mouseySprite.draw ( batch );
        if (win)
            winTextSprite.draw( batch );
        batch.end();
        
    }
    
}
