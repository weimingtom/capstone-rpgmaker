package bootstrap;

import java.awt.Point;

public class BootstrapInfo {
	private String mapName;
	private Point startPoint;
	
	public BootstrapInfo(String mapName, Point startPoint) {
		this.mapName = mapName;
		this.startPoint = startPoint;
	}
	
	public String getMapName()					{	return mapName;					}
	public void setMapName(String mapName)		{	this.mapName = mapName;			}
	public Point getStartPoint()				{	return startPoint;				}
	public void setStartPoint(Point startPoint)	{	this.startPoint = startPoint;	}
}
