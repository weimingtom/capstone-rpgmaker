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

import userData.DeepCopier;
import viewControl.MainFrame;
import editor.ObjectEditorSystem;

public class EventEditorSystem extends ObjectEditorSystem implements Serializable {

	private static final long serialVersionUID = -7162021909241756496L;
	
	// startType
	public final static int PRESS_BUTTON = 0;			// ������ư�� ������ �̺�Ʈ ����
	public final static int CONTACT_WITH_PLAYER = 1;	// �÷��̾�� �����ϸ� �̺�Ʈ ����
	public final static int ABOVE_EVENT_TILE = 2;		// �÷��̾ �̺�Ʈ�� Ÿ�Ͽ� ��ġ�ϸ� ����
	public final static int AUTO_START = 3;				// �ڵ� ����
	public final static int PARALLEL_START = 4;			// ���������� ����
	// actionType
	public final static int NOT_MOTION = 0;				// �̺�Ʈ�� �̵����� �ʴ´�.
	public final static int RANDOM_MOTION = 1;			// �̺�Ʈ�� �������� �̵��Ѵ�.
	public final static int COME_CLOSER_TO_PLAYER = 2;	// �̺�Ʈ�� �÷��̾�� �����Ѵ�.
	public final static int ATTACK_PLAYER = 3;			// �̺�Ʈ�� �÷��̾ �����Ѵ�.
	public final static int ATTACK_ENEMY = 4;			// �̺�Ʈ�� ���� �����Ѵ�.
	// objectType
	public final static int NONE = -1;
	public final static int MAP_EVENT = 0;
	public final static int NPC_EVENT = 1;				// �̺�Ʈ�� NPC�� �����ȴ�.
	public final static int MONSTER_EVENT = 2;			// �̺�Ʈ�� Monster�� �����ȴ�.
	
	private String projectPath;
	private List<EventTile> eventTileList;
	
	private Point startPointToPaste;
	private Point endPointToPaste;
	private List<EventTile> eventTilesToPaste;
	private int width;
	private int height;
	
	public EventEditorSystem(String projectPath, String mapName, int width, int height) {
		super(projectPath, "Event");
		extension = "event";
		
		this.width = width;
		this.height = height;
		
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
			// ois�� ���޵� ��ü�� �����Ѵ�.
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
	
	// null�� ��ȯ�ϸ� ���̺� ����
	public String save() {
		String resultStr = null;
		
		// �ε����� �ߺ��� ������ ���� ����
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
		
		// ���� ����
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
	
	/** �迭�� �������� EventTile�� ��ȯ�Ѵ�.
	 *  ���ϴ� �迭�� �ε����� �Է��Ͽ� �ش� �̺�Ʈ�� ��� ���� ����Ѵ�.
	 *  ex) getEventTile(3,4) -> ���� 4��°, ���� 5��° �̺�Ʈ ��ȯ
	 *  
	 *  ���� : col�� ����, row�� �������� �ε���(0���� ����)
	 *  */
	public EventTile getEventTile(int y, int x) {
		EventTile returnTile = null;
		
		for (int i = 0; i < eventTileList.size(); i++) {
			returnTile = eventTileList.get(i);
			if(returnTile.getInitColLocation()==y && returnTile.getInitRowLocation()==x)
				return returnTile;
		}
		
		return null;
	}
	
	/** �迭�� �������� EventTile�� ��ȯ�Ѵ�.
	 *  ���ϴ� �ε����� �̺�Ʈ�� ��� ���� ����Ѵ�.
	 *  
	 *  getEventTile(int col, int row)�� ��������� �ε����� ����Ʈ ������� ����Ѵ�.
	 **/
	public EventTile getEventTile(int index) {
		return eventTileList.get(index);
	}
	
	public boolean hasEventOnTile(int col, int row) {
		return getEventTile(col, row) == null ? false : true;
	}
	
	/** 
	 *  Actor �̺�Ʈ�� ��Ƽ� ����Ʈ�� ��ȯ�Ѵ�.
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
	 *  Map �̺�Ʈ�� ��Ƽ� ��ȯ�Ѵ�.
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
		if(newTile.getInitRowLocation() >= 0 &&
				newTile.getInitRowLocation() < width &&
				newTile.getInitColLocation() >= 0 &&
				newTile.getInitColLocation() < height) {
			EventTile existedTile;
			
			for (int i = 0; i < eventTileList.size(); i++) {
				existedTile = eventTileList.get(i);
				if(existedTile.getInitColLocation() == newTile.getInitColLocation() && existedTile.getInitRowLocation() == newTile.getInitRowLocation())
					eventTileList.remove(i);
			}
			
			eventTileList.add(newTile);
			
			return true;
		} else {
			return false;
		}
	}
	
	public void copyEvents(Point startPointCopy, Point endPointCopy) {
		startPointToPaste = startPointCopy;
		endPointToPaste = endPointCopy;
		eventTilesToPaste = new LinkedList<EventTile>();
		
		for(int i = 0; i < eventTileList.size(); i++) {
			// �ش��ϴ� ��ǥ�� EventTile ��ü�� �ӽ� ����Ʈ�� �ִ´�.
			if(eventTileList.get(i).getInitRowLocation() >= startPointCopy.x &&
					eventTileList.get(i).getInitRowLocation() <= endPointCopy.x &&
					eventTileList.get(i).getInitColLocation() >= startPointCopy.y &&
					eventTileList.get(i).getInitColLocation() <= endPointCopy.y) {
				try {
					eventTilesToPaste.add((EventTile) DeepCopier.deepCopy(eventTileList.get(i)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void cutEvents(Point startPointCopy, Point endPointCopy) {
		startPointToPaste = startPointCopy;
		endPointToPaste = endPointCopy;
		eventTilesToPaste = new LinkedList<EventTile>();
		
		for(int i = 0; i < eventTileList.size(); i++) {
			// �ش��ϴ� ��ǥ�� EventTile ��ü�� �ӽ� ����Ʈ�� �ִ´�.
			if(eventTileList.get(i).getInitRowLocation() >= startPointCopy.x &&
					eventTileList.get(i).getInitRowLocation() <= endPointCopy.x &&
					eventTileList.get(i).getInitColLocation() >= startPointCopy.y &&
					eventTileList.get(i).getInitColLocation() <= endPointCopy.y) {
				eventTilesToPaste.add(eventTileList.get(i));
			}
		}
	}
	
	public void pasteEvents(Point startPointTarget) {
		if(startPointToPaste.x != startPointTarget.x || startPointToPaste.y != startPointTarget.y) {
			// ��ǥ�� �����Ѵ�.
			int diffX = startPointTarget.x - startPointToPaste.x;
			int diffY = startPointTarget.y - startPointToPaste.y;
			for (int i = 0; i < eventTilesToPaste.size(); i++) {
				eventTilesToPaste.get(i).setInitRowLocation(eventTilesToPaste.get(i).getInitRowLocation() + diffX);
				eventTilesToPaste.get(i).setInitColLocation(eventTilesToPaste.get(i).getInitColLocation() + diffY);
			}
			
			// �ӽ� ����Ʈ�� �ִ� EventTile ��ü�� ��ǥ�� �����Ͽ� �ϳ��� �����Ѵ�.
			for (int i = 0; i < eventTilesToPaste.size(); i++) {
				this.addEventTile(eventTilesToPaste.get(i));
			}
		}
	}
	
	public boolean canPaste() {
		if(startPointToPaste != null && endPointToPaste != null) {
			return true;
		} else {
			startPointToPaste = null;
			endPointToPaste = null;
			eventTilesToPaste = null;
			return false;
		}
	}
	
	public void deleteEvents(Point startPoint, Point endPoint) {
		for (int i = 0; i < eventTileList.size(); i++) {
			int x = eventTileList.get(i).getInitRowLocation();
			int y = eventTileList.get(i).getInitColLocation();
			if(x >= startPoint.x && x <= endPoint.x && y >= startPoint.y && y <= endPoint.y)
				eventTileList.remove(i--);
		}
	}
	
	public List<Event> getEventList(int col, int row) {
		return getEventTile(col, row)!=null ? getEventTile(col, row).getEventList() : null;
	}

	public void setWidth(int width)		{
		this.width = width;		}
	public void setHeight(int height)	{
		this.height = height;	}
}
