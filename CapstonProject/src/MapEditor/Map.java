package MapEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import viewControl.MainFrame;

import eventEditor.EventEditorSystem;


public class Map implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3882767401258211262L;
	private String m_MapName;
	private int m_Height;
	private int m_Width;
	private Tile[][] m_BackgroundTile;
	private Tile[][] m_ForegroundTile;
	private transient BufferedImage m_Background;
	private transient BufferedImage m_Foreground;
	private static final int offsetRow = 0;
	private static final int offsetCol = 0;
	
	private EventEditorSystem eventEditSys;

//	public Map() throws IOException {
////		//imagetmp = ImageIO.read(new File("src//icon//DefaultMapTile.png"));
////		// �̹��� ����
////		this.m_Background = new BufferedImage(DrawingTemplate.pixel * 1,
////				DrawingTemplate.pixel * 1, BufferedImage.TYPE_4BYTE_ABGR);
////		Graphics2D g = m_Background.createGraphics();
////		//g.drawImage(imagetmp, 0, 0, null);
////
////		g.setColor(Color.BLACK);
////		g.fillRect(0, 0, 10*DrawingTemplate.pixel, 10*DrawingTemplate.pixel);
////		g.dispose();
////
////		this.m_Foreground = new BufferedImage(DrawingTemplate.pixel * 1,
////				DrawingTemplate.pixel * 1, BufferedImage.TYPE_4BYTE_ABGR);
////		g = m_Foreground.createGraphics();
////		g.fillRect(0, 0, 10*DrawingTemplate.pixel, 10*DrawingTemplate.pixel);
////		g.dispose();
//	}

	public Map(String mapName, int height, int width) throws IOException {
		// ���� ũ��� �̸� ����
		this.setSize(height, width);
		this.setM_MapName(mapName);
		// �̹��� ����
		this.m_Background = new BufferedImage(DrawingTemplate.pixel * m_Width,
				DrawingTemplate.pixel * m_Height, BufferedImage.TYPE_4BYTE_ABGR);
		// System.out.println(m_Height+" and "+m_Width);
		// �⺻ �̹��� ����
		Graphics2D g = m_Background.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width*DrawingTemplate.pixel, height*DrawingTemplate.pixel);
		g.dispose();

		this.m_Foreground = new BufferedImage(DrawingTemplate.pixel * m_Width,
				DrawingTemplate.pixel * m_Height, BufferedImage.TYPE_4BYTE_ABGR);
		// �⺻ �̹��� ����
		g = m_Foreground.createGraphics();
		g.setColor(new Color(255,true));
		g.fillRect(0, 0, width*DrawingTemplate.pixel, height*DrawingTemplate.pixel);
		g.dispose();
		
		// Ÿ�ϻ���
		this.m_BackgroundTile = new Tile[height][width];
		this.m_ForegroundTile = new Tile[height][width];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.m_BackgroundTile[i][j] = new Tile();
				this.m_ForegroundTile[i][j] = new Tile();
			}
		}
		
		eventEditSys = new EventEditorSystem(MainFrame.OWNER.projectPath, m_MapName, this.m_Width, this.m_Height);
	}

	public String getM_MapName() {
		return m_MapName;
	}

	public void setM_MapName(String m_MapName) {
		this.m_MapName = m_MapName;
	}

	public int getM_Height() {
		return m_Height;
	}

	public int getM_Width() {
		return m_Width;
	}

	public void setSize(int height, int width) {
		this.m_Height = height;
		this.m_Width = width;
	}

	public Tile[][] getM_BackgroundTile() {
		return m_BackgroundTile;
	}

	// �ʿ� ������ Ÿ���� �׸��ϴ�.
	public void setM_BackgroundTile(int startCol, int startRow, Tile[][] t) {
		//��� : ������ Ÿ�� ����
		//������ Ÿ���� �̹����� ����̹����� �׸���
		// ��׶��� �̹����� �׸��� ���� �׷��Ƚ� ������Ʈ ����
		Graphics bg = m_Background.getGraphics();

		//�޾ƿ� Ÿ���� ũ�� ���
		int height = t.length;
		int width = t[0].length;
		
		//����ó�� ��ƾ
		if(height+startCol > this.m_Height)
			height = m_Height-startCol;
		if(width+startRow > this.m_Width)
			width = m_Width-startRow;

		//�޾ƿ� Ÿ�� ����
		for (int i = startCol; i < height+startCol; i++) {
			for (int j = startRow; j < width+startRow; j++) {
				m_BackgroundTile[i][j] = t[i-startCol][j-startRow];
			}
		}	
		//�̹��� ��ü
		for (int i = startCol; i < height+startCol; i++) {
			for (int j = startRow; j < width+startRow; j++) {
				bg.setColor(Color.WHITE);
				bg.fillRect(j* DrawingTemplate.pixel, i* DrawingTemplate.pixel,
						DrawingTemplate.pixel, DrawingTemplate.pixel);
				bg.drawImage(m_BackgroundTile[i][j].getM_TileIcon(), 
						 j* DrawingTemplate.pixel + offsetCol, 
						 i* DrawingTemplate.pixel + offsetRow, null);
			}
		}
		bg.dispose();
	}

	public Tile[][] getM_ForegroundTile() {
		return m_ForegroundTile;
	}

	//��׶��忡 �׸���.
	public void setM_ForegroundTile(int startCol, int startRow, Tile[][] t) {
		//��� : ������ Ÿ�� ����
		//������ Ÿ���� �̹����� ����̹����� �׸���
		// ����׶��� �̹����� �׸��� ���� �׷��Ƚ� ������Ʈ ����

		m_Foreground = new BufferedImage
		(m_Foreground.getWidth(), m_Foreground.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D bg = m_Foreground.createGraphics();
		
		//�޾ƿ� Ÿ���� ũ�� ���
		int height = t.length;
		int width = t[0].length;
		
		//����ó�� ��ƾ
		if(height+startCol > this.m_Height)
			height = m_Height-startCol;
		if(width+startRow > this.m_Width)
			width = m_Width-startRow;

		//�޾ƿ� Ÿ�� ����
		for (int i = startCol; i < height+startCol; i++) {
			for (int j = startRow; j < width+startRow; j++) {
				m_ForegroundTile[i][j] = t[i-startCol][j-startRow];
			}
		}	

		//�̹��� ��ü
//		for (int i = startCol; i < height+startCol; i++) {
//			for (int j = startRow; j < width+startRow; j++) {
//				bg.setColor(bg.getColor());
//				bg.fillRect(j* DrawingTemplate.pixel, i* DrawingTemplate.pixel,
//						DrawingTemplate.pixel, DrawingTemplate.pixel);
//				bg.drawImage(m_ForegroundTile[i][j].getM_TileIcon(), 
//						 j* DrawingTemplate.pixel + offsetCol, 
//						 i* DrawingTemplate.pixel + offsetRow, null);
//			}
//		}
		for(int i = 0 ; i < m_Foreground.getHeight()/DrawingTemplate.pixel; i++)
		{
			for(int j = 0 ; j < m_Foreground.getWidth()/DrawingTemplate.pixel; j++)
			{
				bg.drawImage(m_ForegroundTile[i][j].getM_TileIcon(), 
						 j* DrawingTemplate.pixel + offsetCol, 
						 i* DrawingTemplate.pixel + offsetRow, null);
			}
		}
		bg.dispose();
	}

	public BufferedImage getM_Background() {
		return m_Background;
	}

	public void setM_Background(BufferedImage m_Background) {
		this.m_Background = m_Background;
	}

	public BufferedImage getM_Foreground() {
		return m_Foreground;
	}

	public void setM_Foreground(BufferedImage m_Foreground) {
		this.m_Foreground = m_Foreground;
	}

	public EventEditorSystem getEventEditSys() {
		return eventEditSys;
	}

}
