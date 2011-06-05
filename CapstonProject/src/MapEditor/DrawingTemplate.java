package MapEditor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DrawingTemplate  {


	public static final int pixel = 16;
	protected BufferedImage m_Image; // Ÿ��ȭ�� �̹����� ������.
	protected Tile[][] m_Tile; // m_Image�� Ÿ��ȭ �̹����� �����Ѵ�.
	protected final int m_TileHeight; // Ÿ���� ���� ũ��
	protected final int m_TileWidth; // Ÿ���� ���� ũ��
	protected int numberCol; // Ÿ��ȭ �� ���� ����
	protected int numberRow; // Ÿ��ȭ �� ���� ����

	public DrawingTemplate() {
		m_TileHeight = pixel;
		m_TileWidth = pixel;
	}

	// fileName�� �̹����� ���� �� ��� �������� �����ϰ� Tile �迭�� �̹����� �̵����� ���θ� �����Ѵ�.
	public void setImage(String fileName) {
		try {
			m_Image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		computeImageToTileSet();
	}

	// setImage�� ���� ȣ��Ǵ� �Լ�. Tile �迭�� ���������� �����ϴ� �κ�
	private void computeImageToTileSet() { // 4�ȼ��� �ڸ���.
		numberCol = m_Image.getHeight() / m_TileHeight;
		numberRow = m_Image.getWidth() / m_TileWidth;
		m_Tile = new Tile[numberCol][numberRow];

		for (int i = 0; i < numberCol; i++) {
			for (int j = 0; j < numberRow; j++) {
				BufferedImage CuttedBufImg = m_Image.getSubimage(j
						* m_TileWidth, i * m_TileHeight, m_TileWidth,
						m_TileHeight);
				m_Tile[i][j] = new Tile();
				m_Tile[i][j].setM_TileIcon(CuttedBufImg);
			}
		}
	}

	// Ÿ��ȭ�� �̹��� ������ �迭�� (x1, y1)���� (x2, y2)���� ��ȯ
	// x��ǥ�� x2���� �����ϸ� y��ǥ�� 1 ���ϴ� ������ Tile�迭�� ����
	public Tile[][] getTileSet(int startCol, int startRow, int endCol,
			int endRow) throws IllegalTileIndex {// �Ķ���� ����, ��ȯŸ�� ����
		// �ε����� �߸� ���޵� ��� ���� �߻����ش�.
		if (startRow < 0 || startRow > endRow)
			throw new IllegalTileIndex("error: DrawingTemplate.getTileSet (startRow:"
					+ startRow + " endRow:" + endRow + " numberRow:" + numberRow + ")");
		if (startCol < 0 || startCol > endCol)
			throw new IllegalTileIndex("error: DrawingTemplate.getTileSet (startCol:"
					+ startCol + " endCol:" + endCol + " numberCol:" + numberCol + ")");
		
		if (endRow >= numberRow) {
			System.out.println("error : in DrawingTemplate endRow : "+endRow);
			System.out.println("error : numberRow : "+numberRow);
			endRow = numberRow-1;
			System.err.println("error : DrawingTemplate.getTileSet (endRow >= numberRow)");
		}
		if (endCol >= numberCol) {
			System.out.println(m_Image.getHeight());
			System.out.println("error : in DrawingTemplate endCol : "+endCol);
			System.out.println("error : numberCol : "+numberCol);
			endCol = numberCol-1;
			System.err.println("error : DrawingTemplate.getTileSet (endCol >= numberCol)");
		}

		// Ÿ�� ���� : ����Ʈ �̹����� ä���ش�.
		Tile[][] returnTileSet = new Tile[endCol - startCol + 1][endRow
				- startRow + 1];
		
//		System.out.println("startCol is : "+startCol + " StartRow is : "+startRow);
//		System.out.println("endCol is : "+endCol + " endRow is : " + endRow);
		for (int i = 0; i < endCol - startCol + 1; i++) {
			for (int j = 0; j < endRow - startRow + 1; j++) {
				returnTileSet[i][j] = new Tile();
				returnTileSet[i][j] = m_Tile[i + startCol][j + startRow];
//				if(returnTileSet[i][j].equals(m_Tile[i + startCol][j + startRow])==true)
//					System.out.println("����");
			}
		}

		return returnTileSet;
	}

	// Image ��ü�� BufferedImage �� ��ȯ
	public BufferedImage toBufferedImage(Image im) { // �߰��� �޼ҵ�
		BufferedImage bi = new BufferedImage(im.getWidth(null),
				im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}

	// BufferedImage ��ü�� Image �� ��ȯ
	public Image toImage(BufferedImage bufferedImage) { // �߰��� �޼ҵ�
		return Toolkit.getDefaultToolkit().createImage(
				bufferedImage.getSource());
	}

	// m_Image �� ��ȯ
	public BufferedImage getM_Image() {
		return m_Image;
	}

	// (width, height) �� Ÿ���� ��ȯ
	public Tile getM_Tile(int height, int width) throws IllegalTileIndex { // �Ķ����
																			// ����
		if (height < 0 || height >= numberCol)
			throw new IllegalTileIndex("error: DrawingTemplate.getM_Tile");
		if (width < 0 || width >= numberRow)
			throw new IllegalTileIndex("error: DrawingTemplate.getM_Tile");

		return m_Tile[height][width];
	}

	// (width, height) �� Ÿ���� �Ķ���� tile �� �����Ѵ�.
	public void setM_Tile(int height, int width, Tile tile)
			throws IllegalTileIndex { // ����
		if (height < 0 || height >= numberCol)
			throw new IllegalTileIndex("error: DrawingTemplate.getM_Tile");
		if (width < 0 || width >= numberRow)
			throw new IllegalTileIndex("error: DrawingTemplate.getM_Tile");

		this.m_Tile[height][width] = tile;
	}

	public int getM_TileHeight() {
		return m_TileHeight;
	}

	public int getM_TileWidth() {
		return m_TileWidth;
	}

	public int getNumberCol() {
		return numberCol;
	}

	public int getNumberRow() {
		return numberRow;
	}
	
	//Ÿ���� �̵�����
	public void setTileMoveFlag(int x, int y)
	{
		this.m_Tile[y][x].setIsMove();
	}
	
	//Ÿ���� ��¼��� ����
	public void setTileUpperFlag(int x, int y)
	{
		this.m_Tile[y][x].setIsUpper();
	}

}
