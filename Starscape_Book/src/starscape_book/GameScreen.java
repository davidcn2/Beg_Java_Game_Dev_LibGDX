package starscape_book;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;

public class GameScreen extends BaseScreen
{
    private PhysicsActor spaceship;
    private ParticleActor thruster;
    private ParticleActor baseExplosion;
    
    // game world dimensions
    final int mapWidth = 800;
    final int mapHeight = 600;

    public GameScreen(BaseGame g)
    {  super(g);  }

    public void create() 
    {        
        BaseActor background = new BaseActor();
        background.setTexture( new Texture(Gdx.files.internal("assets/space.png")) );
        background.setPosition(0, 0);
        mainStage.addActor(background);

        spaceship = new PhysicsActor();
        Texture shipTex = new Texture(Gdx.files.internal("assets/spaceship.png"));
        shipTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        spaceship.storeAnimation( "default", shipTex );
        spaceship.setPosition(400, 300);
        spaceship.setOriginCenter();
        spaceship.setMaxSpeed(200);
        spaceship.setDeceleration(20);
        mainStage.addActor(spaceship);

        thruster = new ParticleActor();
        thruster.load("assets/thruster.pfx", "assets/");
        BaseActor thrusterAdjuster = new BaseActor();
        thrusterAdjuster.setTexture(new Texture(Gdx.files.internal("assets/blank.png")));
        thrusterAdjuster.addActor(thruster);
        thrusterAdjuster.setPosition(0,32);
        thrusterAdjuster.setRotation(90);
        thrusterAdjuster.setScale(0.25f);
        thruster.start();
        spaceship.addActor(thrusterAdjuster);
        
        baseExplosion = new ParticleActor();
        baseExplosion.load("assets/explosion.pfx", "assets/");
    }

    public void update(float dt) 
    {
        spaceship.setAccelerationXY(0,0);

        if (Gdx.input.isKeyPressed(Keys.LEFT)) 
            spaceship.rotateBy(180 * dt);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            spaceship.rotateBy(-180 * dt);
            
        if (Gdx.input.isKeyPressed(Keys.UP))
        {
            spaceship.addAccelerationAS(spaceship.getRotation(), 100);
            thruster.start();
        }   
        else
        {
            thruster.stop();
        }
    }

    public boolean keyDown(int keycode)
    {
        if (keycode == Keys.P)    
            togglePaused();

        if (keycode == Keys.R)    
            game.setScreen( new GameScreen(game) );
            
        if (keycode == Keys.SPACE)
        {
            ParticleActor explosion = baseExplosion.clone();
            explosion.setPosition( MathUtils.random(800), MathUtils.random(600) );
            explosion.start();
            mainStage.addActor(explosion);
        } 
        
        return false;
    }
}