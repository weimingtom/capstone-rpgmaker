package eventEditor.eventContents;

import java.io.Serializable;

public class ChangeMapEvent extends EventContent implements Serializable {

	private static final long serialVersionUID = -828828425991104090L;
	
	
	private String fileName;
	private int startColLocation;
	private int startRowLocation;
	
	
	public ChangeMapEvent() {
		this.fileName = "";
		this.startColLocation = 0;
		this.startRowLocation = 0;
	}

	public String getFileName()						{	return fileName;							}
	public void setFileName(String fileName)		{	this.fileName = fileName;					}
	public int getStartCol()						{	return startColLocation;					}
	public void setStartCol(int startColLocation)	{	this.startColLocation = startColLocation;	}
	public int getStartRow()						{	return startRowLocation;					}
	public void setStartRow(int startRowLocation)	{	this.startRowLocation = startRowLocation;	}
}