package pkg52pickup;

import core.BaseActor;
import java.util.ArrayList;

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

public class Pile extends BaseActor { // Extends the BaseActor class.
    
    /*
    The main purpose of the Pile class involves handling functionality related to a list of cards.
    Pile extends the BaseActor class due to acting as a visible object in the game and serving as a 
    drop target for Card objects.
    
    Methods include:
    
    addCard:  Adds the passed card to the list / pile / array.
    getRank:  Returns the rank of the last card in the list (top in the pile).
    getRankIndex:  Returns the rank index of the last card in the list (top in the pile).
    getSuit:  Returns the suit of the last card in the list (top in the pile).
    getTopCard:  Returns the last card in the list (top in the pile) -- or null (when empty).
    isEmpty:  Returns whether the array of cards is empty.
    */
    
    private ArrayList<Card> list; // Array of cards.
    
    public Pile()
    {
        
        // The constructor of the class calls the constructor of the parent (BaseActor) and instantiates
        // the array of cards.
        
        // Call the constructor for the BaseActor (parent / super) class.
        super();
    
        // Instantiate array of cards.
        // list = new ArrayList<Card>();
        list = new ArrayList<>();
    
    }
    
    // c = Card to add to pile / array.
    public void addCard(Card c)
    {
        // The function adds the passed card to the list / pile / array.
        list.add(c);
    }
    
    public String getRank()
    {
        // The function returns the rank of the last card in the list (top in the pile).
        return getTopCard().getRank();
    }
    
    public int getRankIndex()
    {
        // The function returns the rank index of the last card in the list (top in the pile).
        return getTopCard().getRankIndex();
    }
    
    public String getSuit()
    {
        // The function returns the suit of the last card in the list (top in the pile).
        return getTopCard().getSuit();
    }
    
    public Card getTopCard()
    {
        
        // The function returns the last card in the list (top in the pile) -- or null (when empty).
        
        // If the list is empty, then...
        if ( list.isEmpty() )
            // List is empty.  Return nothing (null).
            return null;
        
        // Otherwise -- list has one or more cards...
        else
            // Return the last card in the list (top in the pile).
            return list.get( list.size() - 1 );
        
    }
    
    public boolean isEmpty()
    {
        // The function returns whether the array of cards is empty.
        return list.isEmpty(); 
    }
    
}
