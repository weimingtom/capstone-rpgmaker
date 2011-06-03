package execute;

import java.util.List;

import javax.swing.JOptionPane;

import eventEditor.EventEditorSystem;
import eventEditor.EventTile;

public class GameEventDispatcher {

	private EventEditorSystem eventLoader;
	private EventTile[][] mapEvents;
	private List<EventTile> charEvents;

	public GameEventDispatcher() {
		eventLoader = null;
	}

	public void setEventLoader(EventEditorSystem eventLoader) 
	{
		if(eventLoader == null)
		{
			JOptionPane.showMessageDialog(null, "이벤트 로더가 널임");
			System.exit(0);
		}
		this.eventLoader = eventLoader;
	}

	// 맵 이벤트 생성
	public void loadMapEvent(int width, int height) 
	{
		try 
		{
			this.mapEvents = new EventTile[height][];
			
			for(int i = 0; i < height; i++)
			{
				for(int j = 0 ; j < width; j++)
				{
					if(eventLoader.getEventTile(i, j)==null)
						continue;
					//타일 종류가 맵이벤트이면
					if(eventLoader.getEventTile(j,i).getObjectType() == EventEditorSystem.MAP_EVENT)
					{
						System.out.println("널 아님");
						mapEvents = new EventTile[i][j];
						mapEvents[i][j] = eventLoader.getEventTile(j, i);
					}
					else 
					{
						System.out.println("널 이여");
						mapEvents[i][j] = null;
					}
				}
			}
			
		}catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "게임디스패처, loadMapEvent() 오류");
			System.exit(0);
		}
	}

	public void loadChars()
	{
		try 
		{
			this.charEvents = eventLoader.getActorEventTiles();
			//charEvents.get(9).getObjectType()
			
		}catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "게임디스패처, loadCharLoad() 오류");
			System.exit(0);
		}
	}
	
	
	
	//맵 이벤트 셋
	public void setMapEvents(EventTile[][] mapEvents) {
		this.mapEvents = mapEvents;
	}

	public EventTile[][] getMapEvents() {
		return mapEvents;
	}
	
}
