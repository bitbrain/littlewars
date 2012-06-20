/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Pair datatype
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components;

public class Pair <T, U>
{
   private final T first;
   private final U second;
   private transient final int hash;

   public Pair( T f, U s )
   {
    this.first = f;
    this.second = s;
    hash = (first == null? 0 : first.hashCode() * 31)
          +(second == null? 0 : second.hashCode());
   }

   public T getFirst()
   {
    return first;
   }
   public U getSecond()
   {
    return second;
   }

   @Override
   public int hashCode()
   {
    return hash;
   }

   @Override
   public boolean equals( Object oth )
   {
    if ( this == oth )
    {
      return true;
    }
    if ( oth == null || !(getClass().isInstance( oth )) )
    {
      return false;
    }
    @SuppressWarnings("unchecked")
	Pair<T, U> other = getClass().cast( oth );
    return (first == null? other.first == null : first.equals( other.first ))
     && (second == null? other.second == null : second.equals( other.second ));
   }

} 
