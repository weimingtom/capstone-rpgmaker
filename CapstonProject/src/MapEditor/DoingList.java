package MapEditor;

import java.io.Serializable;

import userData.DeepCopier;

class DoingList implements Serializable {

	private static final long serialVersionUID = 1750396870091427205L;
	private int listSize;
	private int lastIndex;
	private RapperTileset[] tileSetList;
	private int currIndex;
	private Map map;

	public DoingList(Map map) {
		listSize = 100;
		tileSetList = new RapperTileset[listSize];
		currIndex = -1;
		lastIndex = listSize - 1;
		this.map = map;
		addMapToDoingList(map);
	}

	public int addMapToDoingList(Map m) {
		RapperTileset r;

		try {
			r=(RapperTileset)DeepCopier.deepCopy(new RapperTileset(m));
			tileSetList[++currIndex % (listSize)] = r;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return currIndex;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getCurrIndex() {
		return currIndex;
	}

	public void setCurrIndex(int currIndex) {
		this.currIndex = currIndex;
	}

	public Map undo() {
		if (currIndex == 0 && tileSetList[lastIndex] != null) {
			currIndex = lastIndex;
		}if (currIndex == 0 && tileSetList[lastIndex] == null) {
			return map;
		}  else {
			currIndex--;
		}
		map.setM_BackgroundTile(tileSetList[currIndex]
				.getM_BackgroundTile());
		map.setM_ForegroundTile(tileSetList[currIndex]
				.getM_ForegroundTile());
		return map;
	}

	public Map redo() {
		if (currIndex == lastIndex) {
			currIndex = 0;
		} else if(tileSetList[currIndex+1]!=null){
			currIndex++;
		} else {
			return map;
		}
		map.setM_BackgroundTile(tileSetList[currIndex]
				.getM_BackgroundTile());
		map.setM_ForegroundTile(tileSetList[currIndex]
				.getM_ForegroundTile());
		return map;
	}
}