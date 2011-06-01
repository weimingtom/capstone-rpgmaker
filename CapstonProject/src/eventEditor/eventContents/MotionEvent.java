package eventEditor.eventContents;

import java.io.Serializable;

import eventEditor.exceptions.NotExistType;

public class MotionEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = 4145606328946221106L;
	
	
	// direction
	public final int EAST = 0;
	public final int WEST = 1;
	public final int SOUTH = 2;
	public final int NOUTH = 3;
	
	
	private int startCol;
	private int startRow;
	private int direction;
	private int countMove;
	private int speed;
	
	
	public MotionEvent() {
		this.startCol = 0;
		this.startRow = 0;
		this.direction = SOUTH;
		this.countMove = 0;
		this.speed = 1;
	}
	
	public MotionEvent(int startCol, int startRow, int direction, int countMove, int speed) {
		if(direction<0 || direction>3) {
			System.err.println("error: MotionEvent.MotionEvent() (direction: "+ direction +")");
			direction = SOUTH;
		}
		
		this.startCol = startCol;
		this.startRow = startRow;
		this.direction = direction;
		this.countMove = countMove;
		this.speed = speed;
	}
	
	
	public int getStartCol()				{	return startCol;			}
	public void setStartCol(int startCol)	{	this.startCol = startCol;	}
	public int getStartRow()				{	return startRow;			}
	public void setStartRow(int startRow)	{	this.startRow = startRow;	}
	public int getDirection()				{	return direction;			}
	public int getCountMove()				{	return countMove;			}
	public void setCountMove(int countMove)	{	this.countMove = countMove;	}
	public int getSpeed()					{	return speed;				}
	public void setSpeed(int speed)			{	this.speed = speed;			}
	
	/**
	 * @param direction : EAST / WEST / SOUTH / NOUTH
	 * @throws NotExistType : direction가 허용되지 않은 경우 발생
	 */
	public void setDirection(int direction) throws NotExistType {
		if(direction<EAST || direction>NOUTH) {
			throw new NotExistType("error: MotionEvent.setDirection() (direction: "+ direction +")");
		}
		
		this.direction = direction;
	}
}
