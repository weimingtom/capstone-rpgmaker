package execute;


import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import MapEditor.Map;

import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.EventTile;

public class GameEventDispatcher {

	private EventEditorSystem eventLoader;
	private boolean[][] mapEventChecker;
	private List<Event> autoEvents;
	private List<EventTile> actors;
	
	private GameData gameData;
	
	public GameEventDispatcher() {
		eventLoader = null;
		//�ڵ� ���� �̺�Ʈ ����
		setAutoEvents(new LinkedList<Event>());
	}

	public void setEventLoader(EventEditorSystem eventLoader, GameData gameData) 
	{
		this.gameData = gameData;
		if(eventLoader == null)
		{
			JOptionPane.showMessageDialog(null, "�̺�Ʈ �δ��� ����");
			System.out.println("�̺�Ʈ �δ��� ����");
			System.exit(0);
		}
		this.eventLoader = eventLoader;
	}

	public void makeMapEvent(int width, int height)
	{
		//���̺�Ʈ üĿ ����
		mapEventChecker = new boolean[height][];
		for(int i = 0 ; i < height; i++)
			mapEventChecker[i] = new boolean[width];
		
		for(int y = 0 ; y < height; y++)
		{
			for(int x = 0 ; x < width; x++)
			{
				if(eventLoader.hasEventOnTile(y, x) == true)
				{
					mapEventChecker[y][x] = true;
				}
				else
				{
					mapEventChecker[y][x] = false;
				}
			}
		}
		
		//�ڵ� ���� �̺�Ʈ ����
		makeAutoEvent();
	}

	public boolean hasMapEvent(int width, int height)
	{
		return this.mapEventChecker[height][width];
	}

	//�̺�Ʈ Ÿ�� ��
	public EventTile getEventTile(int width, int height)
	{
		return eventLoader.getEventTile(height, width);
	}
	
	//�ڵ� ���� �̺�Ʈ ����
	public void makeAutoEvent()
	{
		//�ڵ� ������ �ִ��� Ȯ��
		EventTile mapEventTile = null;
		List<Event> eventList = null;
		Event eventContentsList = null;

		//�� ������ �޾Ƽ�
		Map gameMap = gameData.getGameMap();
		
		for(int height = 0 ; height < gameMap.getM_Height(); height++)
		{
			for(int width = 0 ; width < gameMap.getM_Width(); width++)
			{
				//�ʿ� �̺�Ʈ�� �ִٸ�
				if(this.hasMapEvent(width, height))
				{
					mapEventTile = this.getEventTile(width, height);
					eventList = mapEventTile.getEventList();
					//�ڵ��̺�Ʈ�� �ִ��� Ȯ��
					for(int length = 0 ; length < eventList.size(); length++)
					{
						eventContentsList = eventList.get(length);
						//�ڵ� �̺�Ʈ�� �ִٸ�, �׸��� �������� Ȯ��
						if(eventContentsList.getStartType() == EventEditorSystem.AUTO_START)
						{
							autoEvents.add(eventContentsList);
						}				
					}
				}
			}
		}
	}
	
	//npc�� ����
	public void makeAlliances()
	{
		setActors(eventLoader.getActorEventTiles());
	}

	public void setAutoEvents(List<Event> autoEvents) {
		this.autoEvents = autoEvents;
	}

	public List<Event> getAutoEvents() {
		return autoEvents;
	}

	public void refrash()
	{
		eventLoader = null;
		//�ڵ� ���� �̺�Ʈ ����
		setAutoEvents(new LinkedList<Event>());
		mapEventChecker = null;
	}


	public void setActors(List<EventTile> actors) {
		this.actors = actors;
	}

	public List<EventTile> getActors() {
		return actors;
	}


}
