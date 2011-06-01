package eventEditor;

import java.awt.Point;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import viewControl.MainFrame;
import editor.ObjectEditorSystem;

public class EventEditorSystem extends ObjectEditorSystem implements Serializable {

	private static final long serialVersionUID = -7162021909241756496L;
	
	// startType
	public final static int PRESS_BUTTON = 0;			// 결정버튼이 눌리면 이벤트 시작
	public final static int CONTACT_WITH_PLAYER = 1;	// 플레이어와 접촉하면 이벤트 시작
	public final static int ABOVE_EVENT_TILE = 2;		// 플레이어가 이벤트의 타일에 위치하면 시작
	public final static int AUTO_START = 3;				// 자동 시작
	public final static int PARALLEL_START = 4;			// 병렬적으로 시작
	// actionType
	public final static int NOT_MOTION = 0;				// 이벤트가 이동하지 않는다.
	public final static int RANDOM_MOTION = 1;			// 이벤트가 랜덤으로 이동한다.
	public final static int COME_CLOSER_TO_PLAYER = 2;	// 이벤트가 플레이어에게 접근한다.
	public final static int ATTACK_PLAYER = 3;			// 이벤트가 플레이어를 공격한다.
	public final static int ATTACK_ENEMY = 4;			// 이벤트가 적을 공격한다.
	// objectType
	public final static int MAP_EVENT = 0;
	public final static int NPC_EVENT = 1;				// 이벤트가 NPC로 설정된다.
	public final static int MONSTER_EVENT = 2;			// 이벤트가 Monster로 설정된다.
	
	private String projectPath;
	private List<EventTile> eventTileList;
	
	public EventEditorSystem(String projectPath, String mapName) {
		super(projectPath, "Event");
		extension = "event";
		
		if(mapName.lastIndexOf(".") == -1)
			this.name = mapName;
		else
			this.name = mapName.substring(0, mapName.lastIndexOf("."));
		
		eventTileList = new LinkedList<EventTile>();
	}
	
	public boolean load(String fileName) throws FileNotFoundException {
		FileInputStream fis = null;
		fis = new FileInputStream(baseFolderPath+File.separator+name+".event");
		
		ObjectInput ois = null;
		try {
			ois = new ObjectInputStream(fis);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		try {
			// ois에 전달된 객체를 저장한다.
			EventEditorSystem data = (EventEditorSystem)ois.readObject();
			this.projectPath = data.projectPath;
			this.name = data.name;
			this.eventTileList = data.eventTileList;
			
			ois.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// null을 반환하면 세이브 실패
	public String save() {
		String resultStr = null;
		
		// 인덱스가 중복된 파일을 먼저 삭제
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathName) {
				if(pathName.getName().endsWith("."+extension)) {
					return true;
				} else
					return false;
			}
		};
		File[] files = (new File(baseFolderPath)).listFiles(filter);
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				String nameFile = files[i].getName();
				if (nameFile.charAt(0) == '.') continue;
				if (nameFile.equals(name + "." + extension)) {
					MainFrame.OWNER.getProjTree().removeObject(files[i]);
					files[i].delete();
				}
			}
		
		FileOutputStream fos = null;
		try {
			resultStr = baseFolderPath + File.separator + name + "." + extension;
			fos = new FileOutputStream(resultStr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// 파일 저장
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			
			oos.reset();
			oos.writeObject(this);
			
			oos.flush();
			
			oos.close();
			fos.close();
			
			MainFrame.OWNER.getProjTree().addObject(new File(resultStr));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultStr;
	}
	
	/** 배열의 관점에서 EventTile을 반환한다.
	 *  원하는 배열의 인덱스를 입력하여 해당 이벤트를 얻기 위해 사용한다.
	 *  ex) getEventTile(3,4) -> 세로 4번째, 가로 5번째 이벤트 반환
	 *  
	 *  주의 : col은 세로, row는 가로줄의 인덱스(0부터 시작)
	 *  */
	public EventTile getEventTile(int col, int row) {
		EventTile returnTile = null;
		
		for (int i = 0; i < eventTileList.size(); i++) {
			returnTile = eventTileList.get(i);
			if(returnTile.getInitColLocation()==col && returnTile.getInitRowLocation()==row)
				return returnTile;
		}
		
		return null;
	}
	
	/** 배열의 관점에서 EventTile을 반환한다.
	 *  원하는 인덱스의 이벤트를 얻기 위해 사용한다.
	 *  
	 *  getEventTile(int col, int row)와 비슷하지만 인덱스를 리스트 기반으로 사용한다.
	 **/
	public EventTile getEventTile(int index) {
		return eventTileList.get(index);
	}
	
	public boolean hasEventOnTile(int col, int row) {
		return getEventTile(col, row) == null ? false : true;
	}
	
	/** 
	 *  Actor 이벤트만 모아서 리스트로 반환한다.
	 **/
	public List<EventTile> getActorEventTiles() {
		List<EventTile> returnList = new LinkedList<EventTile>();
		for (int indexEventTile = 0; indexEventTile < eventTileList.size(); indexEventTile++)
			if(   eventTileList.get(indexEventTile).getObjectType() >= NPC_EVENT
			   && eventTileList.get(indexEventTile).getObjectType() <= MONSTER_EVENT)
				returnList.add(eventTileList.get(indexEventTile));
		return returnList;
	}
	
	/** 
	 *  Map 이벤트만 모아서 반환한다.
	 **/
	public List<EventTile> getMapEventTiles() {
		List<EventTile> returnList = new LinkedList<EventTile>();
		for (int indexEventTile = 0; indexEventTile < eventTileList.size(); indexEventTile++)
			if(eventTileList.get(indexEventTile).getObjectType() == MAP_EVENT)
				returnList.add(eventTileList.get(indexEventTile));
		return returnList;
	}
	
	public List<EventTile> getEventTileList()	 {	return eventTileList;	}

	@Override
	protected void copyObject(ObjectEditorSystem data) {
		EventEditorSystem tmpData = (EventEditorSystem)data;
		
		this.eventTileList = tmpData.eventTileList;
	}
	
	public boolean addEventTile(EventTile newTile) {
		EventTile existedTile;
		
		for (int i = 0; i < eventTileList.size(); i++) {
			existedTile = eventTileList.get(i);
			if(existedTile.getInitColLocation() == newTile.getInitColLocation() && existedTile.getInitRowLocation() == newTile.getInitRowLocation())
				eventTileList.remove(i);
		}
		
		eventTileList.add(newTile);
		
		return true;
	}
	
	public void copyEvents(Point startPointCopy, Point endPointCopy, Point startPointTarget, Point endPointTarget) {
		
	}
	
	public void deleteEvents(Point startPoint, Point endPoint) {
		
	}
	
	public List<Event> getEventList(int col, int row) {
		return getEventTile(col, row)!=null ? getEventTile(col, row).getEventList() : null;
	}
}
