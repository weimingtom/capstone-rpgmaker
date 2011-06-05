package MapEditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Vector;

import eventEditor.EventEditorSystem;

public class MapEditorSystem {

	private static final long serialVersionUID = 4612091568244227383L;
	/************* ��� ������ ***************/
	// ���� �׸��� ����� �׸����� �̸��� ���� ����
	// GUI���� ó����
	// private Vector<String> backgroundFile;
	// private Vector<String> foregroundFile;
	// ����ڰ� �巡�� �� Ŭ������ ������ ��Ÿ�ϵ��� �����ϴ� ����
	// private Vector m_template;
	private Tile[][] m_selectedTile;
	// ���ӿ� ���� ��Ŭ����
	private Map m_Map;
	// ��� ���ø�
	private Vector<BackgroundTemplate> m_backTemplate;
	// ���� ���ø�
	private Vector<ForegroundTemplate> m_foreTemplate;

	// ���


	/*************
	 * ��� �Լ���
	 * 
	 * @throws IOException
	 *****************/
	// ������
	public MapEditorSystem() throws IOException {
		this.m_backTemplate = null;
		this.m_foreTemplate = null;
		// this.m_Map = new Map();

		// this.m_template = null;
		this.m_selectedTile = null;
		m_backTemplate = new Vector<BackgroundTemplate>();
		m_foreTemplate = new Vector<ForegroundTemplate>();
	}

	// ���� ���� �ۼ��ϰ��� �Ҷ�
	// ���� �����Ҷ� �ʿ��� ������ : ���̸�, ���� ���̶� �ʺ�
	public void newMap(String mapName, int mapWidth, int mapHeight)
			throws IOException {

		// 1.���� �̸��� �����Ѵ�.
		// 2.���� ���� �����Ҷ� ���� ũ�⸦ �����Ѵ�.

		m_Map = new Map(mapName, mapHeight, mapWidth);
		m_backTemplate = new Vector<BackgroundTemplate>();
		m_foreTemplate = new Vector<ForegroundTemplate>();
		// this.backgroundFile = new Vector<String>();
		// this.foregroundFile = new Vector<String>();
	}

	// �̱��� : ���� �����Ҷ�
	public void save(String fileName) throws IOException {

		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutput oos = new ObjectOutputStream(fos);

		oos.writeObject(this.m_Map);
		oos.close();
		fos.close();

	}

	// �̱��� : ���� �ٸ��̸����� ����
	public void saveAs(String filePath, String mapName) throws IOException,
			ClassNotFoundException {
		this.m_Map.setM_MapName(mapName);
		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutput oos = new ObjectOutputStream(fos);

		oos.writeObject(this.m_Map);
		oos.close();
		fos.close();

	}

	// ���Ͽ� ����� ���� �ҷ��´�
	public void load(String fileName) throws IOException,
			ClassNotFoundException {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);

		this.m_Map = (Map) ois.readObject();

		ois.close();
		fis.close();

		Tile t[][] = null;
		t = m_Map.getM_BackgroundTile();

		BufferedImage tmp = new BufferedImage(m_Map.getM_Width()
				* DrawingTemplate.pixel, m_Map.getM_Height()
				* DrawingTemplate.pixel, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = tmp.createGraphics();

		for (int i = 0; i < m_Map.getM_Height(); i++) {
			for (int j = 0; j < m_Map.getM_Width(); j++) {
				g.drawImage(t[i][j].getM_TileIcon(), j * DrawingTemplate.pixel,
						i * DrawingTemplate.pixel, null);
			}
		}
		this.m_Map.setM_Background(tmp);

		t = m_Map.getM_ForegroundTile();

		tmp = new BufferedImage(m_Map.getM_Width() * DrawingTemplate.pixel,
				m_Map.getM_Height() * DrawingTemplate.pixel,
				BufferedImage.TYPE_4BYTE_ABGR);

		g = tmp.createGraphics();

		for (int i = 0; i < m_Map.getM_Height(); i++) {
			for (int j = 0; j < m_Map.getM_Width(); j++) {
				g.drawImage(t[i][j].getM_TileIcon(), j * DrawingTemplate.pixel,
						i * DrawingTemplate.pixel, null);
			}
		}
		this.m_Map.setM_Foreground(tmp);
		g.dispose();
	}

	// �̹����κ��� ��׶��� ���ø��� �����Ѵ�.
	public void makeBackTemplate(String imageFileName) {
		// this.backgroundFile.add(imageFileName);
		// ���Ϳ� ������� ��׶��� ���ø� ����
		BackgroundTemplate bg = new BackgroundTemplate();

		// ��׶���� �̹��� �佺
		bg.setImage(imageFileName);

		// �̹����� Ÿ��ȭ
		// bg.computeImageToTileSet();

		// ���� �̹����� �����ø����� �ۼ��Ǿ��� ���� ���Ϳ� ����ִ´�.
		m_backTemplate.add(bg);
	}

	// �̹����κ��� ����׶��� ���ø��� �����Ѵ�.
	public void makeForeTemplate(String imageFileName) {
		// this.foregroundFile.add(imageFileName);
		// ���Ϳ� ������� ����׶��� ���ø� ����
		ForegroundTemplate bg = new ForegroundTemplate();

		// ����׶���� �̹��� �佺
		bg.setImage(imageFileName);

		// �̹����� Ÿ��ȭ setImage���� ó��
		// bg.computeImageToTileSet();

		// ���� �̹����� �����ø����� �ۼ��Ǿ��� ���� ���Ϳ� ����ִ´�.
		m_foreTemplate.add(bg);
	}

	// ��׶� ���ø����� �̹��� ������ �����Ѵ�.
	public void deleteBackTemplate(int index) {
		this.m_backTemplate.removeElementAt(index);
		// this.backgroundFile.removeElementAt(index);
	}

	// ��׶� ���ø����� �̹��� ������ �����Ѵ�.
	public void deleteForeTemplate(int index) {
		this.m_foreTemplate.removeElementAt(index);
		// this.foregroundFile.removeElementAt(index);
	}

	// ����ڰ� �巡���� ��ġ ������ �� Ŭ������ �˷���
	// �巡�� ��ġ����. ���, �������ø��� ���Ϳ� ����� �ε�������
	// ���� Ŭ���� �κ��� ����ΰ�, �����ΰ� �ϴ� ����
	public void setTiles(int startCol, int startRow, int endCol, int endRow,
			int vectorIndex, boolean back) throws IllegalTileIndex {
		// ��׶��� ���ø��� Ŭ���ȰŸ�
		m_selectedTile = null;
		if (back == true) {
			// ��׶��� ���ø����� ������ Ÿ�ϼ��� �޾ƿ´�.
			if (m_backTemplate.size() < 0) {
				System.out.println("error in mapeditorSystem.java");
				// System.exit(-1);
			}
			m_selectedTile = m_backTemplate.elementAt(vectorIndex).getTileSet(
					startCol, startRow, endCol, endRow);
		}
		// ����׶��尡 Ŭ���ȰŸ�
		else {
			m_selectedTile = m_foreTemplate.elementAt(vectorIndex).getTileSet(
					startCol, startRow, endCol, endRow);

		}
	}

	// ȭ��� �ʿ� ������ ��׶��带 �׸��ϴ�.
	// ���Ⱑ ������;;
	public BufferedImage drawBackground(int startCol, int startRow) {
		if (this.m_selectedTile == null) {
			BufferedImage bg = m_Map.getM_Background();
			return bg;
		}
		// 1. �ʿ� �׸����� �ϴ� �κ��� �׸���.
		m_Map.setM_BackgroundTile(startCol, startRow, m_selectedTile);

		// 2. ��Ŭ�������� �� �׸������� ����� ����
		// ��Ŭ������ ��׶��带 �׸��ٰ� ����!!!!!!!!!!
		BufferedImage bg = m_Map.getM_Background();

		// return null;
		return bg;
	}

	// ȭ��� �ʿ� ������ fore�׶��带 �׸��ϴ�.
	// ���Ⱑ ������;;
	public BufferedImage drawForeground(int startCol, int startRow) {
		if (this.m_selectedTile == null) {
			BufferedImage fg = m_Map.getM_Foreground();
			return fg;
		}
		// 1. �ʿ� �׸����� �ϴ� �κ��� �׸���.
		m_Map.setM_ForegroundTile(startCol, startRow, m_selectedTile);

		// 2. ��Ŭ�������� �� �׸������� ����� ����
		// ��Ŭ������ ��׶��带 �׸��ٰ� ����!!!!!!!!!!
		BufferedImage fg = m_Map.getM_Foreground();

		// return null;
		return fg;
	}

	// ��׶��� �̹����� �����մϴ�.
	public BufferedImage getBackTemplate(int numberOfTab) {
		// TODO Auto-generated method stub
		return this.m_backTemplate.elementAt(numberOfTab).getM_Image();
	}

	// ����׶��� �̹����� �����մϴ�.
	public BufferedImage getForeTemplate(int numberOfTab) {
		// TODO Auto-generated method stub
		return this.m_foreTemplate.elementAt(numberOfTab).getM_Image();
	}

	// �� ������ Ŭ������ ������ �ִ� �������� ����
	public Map getMapInfo() {
		return m_Map;
	}

	public DrawingTemplate getPalette(int index, boolean back) {
		if (back == true)
			return this.m_backTemplate.elementAt(index);
		else
			return this.m_foreTemplate.elementAt(index);
	}

	public Vector<BackgroundTemplate> getBackPalettes() {
		return this.m_backTemplate;
	}

	public Vector<ForegroundTemplate> getForePalettes() {
		return this.m_foreTemplate;
	}

	public void setBackPaletteVector(Vector<BackgroundTemplate> v) {
		// this.m_backTemplate = new Vector<BackgroundTemplate>();
		// for(int i = 0 ; i < v.size(); i++)
		// {
		// m_backTemplate.add(v.elementAt(i));
		// }
		this.m_backTemplate = v;
	}

	public void setForePaletteVector(Vector<ForegroundTemplate> v) {
		// this.m_foreTemplate = new Vector<ForegroundTemplate>();
		// for(int i = 0 ; i < v.size(); i++)
		// {
		// m_foreTemplate.add(v.elementAt(i));
		// }
		this.m_foreTemplate = v;
	}


	public EventEditorSystem getEventEditSys() {
		return m_Map.getEventEditSys();
	}
}
