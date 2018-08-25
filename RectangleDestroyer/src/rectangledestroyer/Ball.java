package rectangledestroyer;

import core.PhysicsActor;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your
class, and extends means that you are creating a subclass of the class you are extending. You can
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Abstract:  Abstract classes are similar to interfaces.  You cannot instantiate them, and they may
contain a mix of methods declared with or without an implementation. However, with abstract classes,
you can declare fields that are not static and final, and define public, protected, and private
concrete methods.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class Ball extends PhysicsActor { // Extends the PhysicsActor class.
    
    /*
    The class handles functionality related to the Ball Actor.
    
    Methods include:
    
    act:  Store current and previous boundary circles of Ball and perform time based positional update.
    getBottom:  Returns bottom of bounding circle.
    getBottomLeft:  Returns a vector with the bottom left position of the passed rectangle.
    getBottomRight:  Returns a vector with the bottom right position of the passed rectangle.
    getCircle:  Returns the bounding circle for the Ball Actor.
    getLeft:  Returns leftmost edge of bounding circle.
    getRight:  Returns rightmost edge of bounding circle.
    getTop:  Returns top of bounding circle.
    getTopLeft:  Returns a vector with the top left position of the passed rectangle.
    getTopRight:  Function returns a vector with the top right position of the passed rectangle.
    multVelocityX:  Multiplies the x component of the velocity vector by the passed parameter.
    multVelocityY:  Multiplies the y component of the velocity vector by the passed parameter.
    overlaps:  Handles the interesection of the Paddle and the Brick or Paddle, depending on function.
    */
    
    private Circle prevCircle; // Previous boundary circle of Ball.
    private Circle currCircle; // Current boundary circle of Ball.
    
    public Ball()
    {

        // The constructor of the class calls the constructor of the parent (PhysicsActor).

        // Call the constructor for the PhysicsActor (parent / super) class.
        super();

    }
    
    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {
        
        // The function stores the current (after updating) and previous (before updating) boundary 
        // circles of the Ball.
        // The function calls the parent act function in order to perform a time based positional update.
        
        // Store boundary circle of the Ball before updating.
        prevCircle = getCircle();
        
        // Call parent act function in order to perform time based positional update.
        super.act(dt);
        
        // Store boundary circle of the Ball after updating.
        currCircle = getCircle();
    
    }
    
    // c = Bounding circle from which to get bottom.
    public Vector2 getBottom(Circle c)
    {
        // The function returns a vector with the bottom of the passed bounding circle.
        return new Vector2(c.x, c.y - c.radius);
    }
    
    // r = Rectangle from which to get bottom left position.
    public Vector2 getBottomLeft(Rectangle r)
    {
        // The function returns a vector with the bottom left position of the passed rectangle.
        return new Vector2( r.getX(), r.getY() ); 
    }
    
    // r = Rectangle from which to get bottom right position.
    public Vector2 getBottomRight(Rectangle r)
    {
        // The function returns a vector with the bottom right position of the passed rectangle.
        return new Vector2( r.getX() + r.getWidth(), r.getY() );
    }
    
    public Circle getCircle()
    {
        
        // The function returns the bounding circle for the Ball Actor.
        
        // Return the bounding circle for the Ball Actor.
        return new Circle( getX() + getWidth()/2, getY() + getHeight()/2, getWidth()/2 );
    
    }
    
    // c = Bounding circle from which to get leftmost edge.
    public Vector2 getLeft(Circle c)
    {
        // The function returns a vector with the leftmost edge of the passed bounding circle.
        return new Vector2(c.x - c.radius, c.y);
    }
    
    // c = Bounding circle from which to get rightmost edge.
    public Vector2 getRight(Circle c)
    {
        // The function returns a vector with the rightmost edge of the passed bounding circle.
        return new Vector2(c.x + c.radius, c.y);
    }
    
    // c = Bounding circle from which to get top.
    public Vector2 getTop(Circle c)
    {
        // The function returns a vector with the top of the passed bounding circle.
        return new Vector2(c.x, c.y + c.radius);
    }
    
    // r = Rectangle from which to get top left position.
    public Vector2 getTopLeft(Rectangle r)
    {
        // The function returns a vector with the top left position of the passed rectangle.
        return new Vector2( r.getX(), r.getY() + r.getHeight() );
    }
    
    // r = Rectangle from which to get top right position.
    public Vector2 getTopRight(Rectangle r)
    {
        // The function returns a vector with the top right position of the passed rectangle.
        return new Vector2( r.getX() + r.getWidth(), r.getY() + r.getHeight() );
    }
    
    // m = Factor by which to multiple x component of velocity.
    public void multVelocityX(float m)
    {
        
        // The function multiplies the x component of the velocity vector by the passed parameter.
        // The function gets used to change velocity when the ball hits a brick -- reversing direction.
        // Multiplying by -1 reverses direction.
        
        // Multiply the x component of the velocity vector by the passed parameter.
        velocity.x *= m;
        
    }

    // m = Factor by which to multiple y component of velocity.
    public void multVelocityY(float m)
    {
        
        // The function multiplies the y component of the velocity vector by the passed parameter.
        // The function gets used to change velocity when the ball hits a brick -- reversing direction.
        // Multiplying by -1 reverses direction.
        
        // Multiply the y component of the velocity vector by the passed parameter.
        velocity.y *= m;
        
    }
    
    // brick = Reference to the Brick actor to check for overlap.
    // bounceOff = Determines whether the velocity of the ball should be adjusted to simulate bouncing 
    // off the brick.
    public boolean overlaps(Brick brick, boolean bounceOff)
    {
    
        boolean sideHit; // Whether a collision occurred to the side of the brick.  Any side (top, left, right, bottom).
        Rectangle rect; // Bounding rectangle for passed Brick.
        
        // If Ball DOES NOT overlap Brick, then...
        if ( !Intersector.overlaps( this.getCircle(), brick.getRectangle() ) )
            
            // Ball DOES NOT overlap Paddle.
            return false;
        
        // Ball DOES overlap Brick...
        
        // If flagged for Ball to bounce off Brick, then...
        if ( bounceOff )
            
            {
            // Flagged for Ball to bounce off Paddle.
            
            // Store bounding rectangle for passed brick.    
            rect = brick.getRectangle();
            
            // Default as collision NOT occurring to side of brick.
            // Side means any (top, left, right, bottom).
            sideHit = false;
            
            // If ball moving and right edge of circle intersects left edge of brick, then...
            // ... Checking whether line segment connecting rightmost points of current and previous circles
            // ... intersects left edge of rectangle.
            if (velocity.x > 0 && Intersector.intersectSegments(
            getRight(prevCircle), getRight(currCircle),
            getTopLeft(rect), getBottomLeft(rect), null) )
            
                {
                // Ball moving and right edge of circle intersects left edge of brick.
                
                // Multiple x component of velocity vector to reverse associated direction.
                multVelocityX(-1);
                
                // Flag a side of the brick as being hit.
                sideHit = true;
                }
            
            // Otherwise, if ball moving and left edge of circle intersects right edge of brick, then...
            // ... Checking whether line segment connecting leftmost points of current and previous circles
            // ... intersects right edge of rectangle.
            else if (velocity.x < 0 && Intersector.intersectSegments(
            getLeft(prevCircle), getLeft(currCircle),
            getTopRight(rect), getBottomRight(rect), null) )
            
                {
                // Ball moving and left edge of circle intersects right edge of brick.
                
                // Multiple x component of velocity vector to reverse associated direction.
                multVelocityX(-1);
                
                // Flag a side of the brick as being hit.
                sideHit = true;
                }
            
            // If ball moving and top edge of circle intersects bottom edge of brick, then...
            // ... Checking whether line segment connecting topmost points of current and previous circles 
            // ... intersects bottom edge of rectangle.
            if (velocity.y > 0 && Intersector.intersectSegments(
            getTop(prevCircle), getTop(currCircle),
            getBottomLeft(rect), getBottomRight(rect), null) )
            
                {
                // Ball moving and top edge of circle intersects bottom edge of brick.
                
                // Multiple y component of velocity vector to reverse associated direction.
                multVelocityY(-1);
                
                // Flag a side of the brick as being hit.
                sideHit = true;
                }
            
            // Otherwise, if ball moving and bottom edge of circle intersects top edge of brick, then...
            // ... Checking whether line segment connecting bottommost points of current and previous circles 
            // ... intersects top edge of rectangle.
            else if (velocity.y < 0 && Intersector.intersectSegments(
            getBottom(prevCircle), getBottom(currCircle),
            getTopLeft(rect), getTopRight(rect), null) )
            
                {
                // Ball moving and bottom edge of circle intersects top edge of brick.
                
                // Multiple y component of velocity vector to reverse associated direction.
                multVelocityY(-1);
                
                // Flag a side of the brick as being hit.
                sideHit = true;
                }
            
            // If no side of brick hit, then...
            if (!sideHit)
                
                {
                // No side of the brick was hit.
                // By process of elimination, ball hit corner of brick first.
                    
                // Reverse the x and y components of the velocity vector to completely reverse direction.
                multVelocityX(-1);
                multVelocityY(-1);
                }
            
            }
        
        // Ball overlaps brick.
        return true;
        
    }
    
    // paddle = Reference to the Paddle actor to check for overlap.
    // bounceOff = Determines whether the velocity of the ball should be adjusted to simulate bouncing 
    // off the paddle.
    public boolean overlaps(Paddle paddle, boolean bounceOff)
    {
        
        // The function handles the interesection of the Paddle and the Ball.
        // When the Paddle intersects the Ball (and bounceOff set to true),
        // adjusts angle of ball.
        // Returns true when collision occurs, false when ball not intersecting paddle.
        
        float ballCenterX; // Center position of ball, horizontally.
        float bounceAngle; // Angle to which to set ball, based on horizontal position within paddle. 
        float percent; // Position of ball along paddle (left to right), expressed as a percentage.
        
        // If Ball DOES NOT overlap Paddle, then...
        if ( !Intersector.overlaps( this.getCircle(), paddle.getRectangle() ) )
            
            // Ball DOES NOT overlap Paddle.
            return false;
        
        // Ball DOES overlap Paddle.
        
        // If flagged for Ball to bounce off Paddle, then...
        if ( bounceOff )
        
            {
            // Flagged for Ball to bounce off Paddle.
                
            // Store current center of ball (horizontally).
            ballCenterX = this.getX() + this.getWidth() / 2;
            
            // Store current ball position relative to paddle, looking left to right, expressed as a percentage. 
            percent = (ballCenterX - paddle.getX()) / paddle.getWidth();
            
            // Interpolate value between 150 and 30.
            bounceAngle = 150 - percent * 120;
            
            // Set velocity vector using calculated angle (based on position within paddle) and current speed.
            this.setVelocityAS( bounceAngle, this.getSpeed() );
            
            }
        
        // Ball overlaps paddle.
        return true;
        
    }
    
}
