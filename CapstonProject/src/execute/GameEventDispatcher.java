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
			JOptionPane.showMessageDialog(null, "�̺�Ʈ �δ��� ����");
			System.exit(0);
		}
		this.eventLoader = eventLoader;
	}

	// �� �̺�Ʈ ����
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
					//Ÿ�� ������ ���̺�Ʈ�̸�
					if(eventLoader.getEventTile(j,i).getObjectType() == EventEditorSystem.MAP_EVENT)
					{
						System.out.println("�� �ƴ�");
						mapEvents = new EventTile[i][j];
						mapEvents[i][j] = eventLoader.getEventTile(j, i);
					}
					else 
					{
						System.out.println("�� �̿�");
						mapEvents[i][j] = null;
					}
				}
			}
			
		}catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "���ӵ���ó, loadMapEvent() ����");
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
			JOptionPane.showMessageDialog(null, "���ӵ���ó, loadCharLoad() ����");
			System.exit(0);
		}
	}
	
	
	
	//�� �̺�Ʈ ��
	public void setMapEvents(EventTile[][] mapEvents) {
		this.mapEvents = mapEvents;
	}

	public EventTile[][] getMapEvents() {
		return mapEvents;
	}
	
}
