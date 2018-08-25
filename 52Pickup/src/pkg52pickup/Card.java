package pkg52pickup;

import core.BaseActor;

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

public class Card extends BaseActor { // Extends the BaseActor class.
    
    /*
    The main purpose of the Card class involves handling functionality related to cards.
    
    Methods include:
    
    getRank:  Returns rank of the card.
    getRankIndex:  Returns the rank index of the card.
    getSuit:  Returns suit of the card.
    */
    
    private String rank; // Card rank.
    private String suit; // Card suit.
    public float offsetX; // X-coordinate of the point where the player first touched the card.
    public float offsetY; // Y-coordinate of the point where the player first touched the card.
    public float originalX; // Original position (x-coordinate) of the card on the stage before it is dragged.
    public float originalY; // Original position (y-coordinate) of the card on the stage before it is dragged.
    public boolean dragable; // Indicates whether the card can be dragged by the player.
    
    // r = Rank to which to set card.
    // s = Suit to which to set card.
    public Card(String r, String s)
    {
        
        // The constructor of the class calls the constructor of the parent (BaseActor), sets the rank and suit
        // based on the passed values, and defaults dragable to true.
        
        // Call the constructor for the BaseActor (parent / super) class.
        super();
    
        // Set rank and suit based on passed values.
        rank = r;
        suit = s;
    
        // Default dragable flag to true.
        dragable = true;
        
    }
    
    public String getRank()
    {
        // The function returns the rank of the card.
        return rank;
    }
    
    public String getSuit()
    {
        // The function returns the suit of the card.
        return suit;
    }
    
    public int getRankIndex()
    {
        
        // The function returns the rank index of the card.
        // Rank converts to a number essentially based on numeric order (A = 1, 2 = 2, 3 = 3, ..., J = 11, 
        // Q = 12, K = 13, ...).
        
        String[] rankNames; // Array of card ranks used to get index.
        
        // Store card rank array.
        rankNames = new String[] {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    
        // Loop through card rank array.
        for (int i = 0; i < rankNames.length; i++)
            {
            // If card rank equals current value in array, then...
            if ( rank.equals( rankNames[i] ) )
                // Card rank equals current value in array.
                // Return index.
                return i;
            }
        
        // Card rank not found in array.
        // Return -1.
        return -1;
        
    }
    
}
