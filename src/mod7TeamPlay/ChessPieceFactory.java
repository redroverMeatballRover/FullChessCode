package mod7TeamPlay;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ChessPieceFactory {

	public ChessPiece create(String pieceName, String pieceRepresentation) {
	      ChessPiece piece = null;
	      try {
	        
	    	
	    	//System.out.println(pieceName);
	    	@SuppressWarnings("rawtypes")
			Class pieceClass = Class.forName("mod7TeamPlay." + pieceName);
	    	
	        
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			Constructor ctor = pieceClass.getDeclaredConstructor(String.class);
	        
	        ctor.setAccessible(true);
	        piece = (ChessPiece)ctor.newInstance(pieceRepresentation);
	     // production code should handle these exceptions more gracefully
	     } 
	      catch (ClassNotFoundException x) {
	        x.printStackTrace();
	     } catch (InstantiationException x) {
	        x.printStackTrace();
	     } catch (IllegalAccessException x) {
	        x.printStackTrace();
	     } catch (InvocationTargetException x) {
	        x.printStackTrace();
	     } catch (NoSuchMethodException x) {
	        x.printStackTrace();
	     }

	     return piece;
	}
}