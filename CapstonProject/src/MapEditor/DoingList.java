package MapEditor;

import java.io.Serializable;

import userData.DeepCopier;

// 언두와 리두를 위한 자료구조
class DoingList implements Serializable {

	private static final long serialVersionUID = 1750396870091427205L;
	private int listSize;
	private int lastIndex;
	private RapperTileset[] tileSetList;
	private int currIndex;
	private Map map;
	private int top;

	public DoingList(Map map) {
		listSize = 1;
		tileSetList = new RapperTileset[listSize];
		currIndex = -1;
		lastIndex = listSize - 1;
		this.map = map;
		top = 0;
		addMapToDoingList(map);
	}



	public int addMapToDoingList(Map m) {
		RapperTileset r;
		try {
			r = (RapperTileset) DeepCopier.deepCopy(new RapperTileset(m));
			if (tileSetList[++currIndex % (listSize)] != null) {
				tileSetList[currIndex % (listSize)] = null;
				tileSetList[currIndex % (listSize)] = r;
			} else
				tileSetList[currIndex % (listSize)] = r;

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (currIndex == top)
			top = (top + 1) % listSize;
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
		if (currIndex-1 != top) {
			if (currIndex - 1 < 0)
				currIndex = lastIndex;
			else
				currIndex--;
			if(tileSetList[currIndex]!=null){
			map.setM_BackgroundTile(tileSetList[currIndex]
					.getM_BackgroundTile());
			map.setM_ForegroundTile(tileSetList[currIndex]
					.getM_ForegroundTile());
			} else {
				currIndex = (currIndex+1)%listSize;
			}
		}
		return map;
	}

	public Map redo() {
		if (currIndex != top) {
			map.setM_BackgroundTile(tileSetList[++currIndex]
					.getM_BackgroundTile());
			map.setM_ForegroundTile(tileSetList[currIndex]
					.getM_ForegroundTile());
		}
		return map;
	}
}