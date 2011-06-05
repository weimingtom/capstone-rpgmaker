package palette;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import viewControl.MainFrame;
import viewControl.esComponent.EstyleCheckBoxItem;
import viewControl.esComponent.EstyleCheckBoxItemGroup;
import MapEditor.DrawingTemplate;
import MapEditor.IllegalTileIndex;
import MapEditor.MapEditorSystem;
import MapEditor.MapIntegrateGUI;

public class PalettePanel extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener {
	/**
	 * 
	 */
	public static final int PALETTEMODE = 1;
	public static final int MOVEEMODE = 2;
	public static final int UPPERMODE = 3;
	public static final int GRIDMODE = 4;
	public static final int THIS_IS_BACKPALLETE = 5;
	public static final int THIS_IS_FOREPALLETE = 6;

	private static final long serialVersionUID = 2032530768724582092L;
	// �� ȭ�鿡 ����� �̹���
	private BufferedImage imageGround = null;
	// �׸��� �����϶�
	private boolean isGrid = false;

	// �ʿ����� �ý��ۿ� �׸��� �׷����ϹǷ� ������ ������ �ʿ�
	private MapEditorSystem mapSys = null;
	// ���� �� �ȷ�Ʈ�� ��׶������� ����׶�������?
	private boolean back = true;
	// ���� �� �ȷ�Ʈ�� ���° �ȷ�Ʈ�ΰ�?
	private int paletteIndex = 0;
	// ���콺�� ����Ʈ
	private Point mousePoint;
	// ���콺 Ŭ�� ����Ʈ
	private Point clickPoint;
	// ���콺�� ����Ű�� ����Ʈ��ġ
	private Point dragPoint;
	// Ŭ�� �����ΰ�?
	private boolean click = false;
	// �巡�� �����ΰ�?
	private boolean isDrag = false;
	// ȭ�鿡 �Ѹ� �簢�� ������
	private int rectWidth = 0;
	private int rectHeight = 0;
	// ��ο� ��� : �Ϲ����� �ȷ�Ʈ���/ĳ���Ϳ����Ӹ��/ĳ������¼������
	private boolean paletteMode = true;
	private boolean moveMode = false;
	private boolean upperMode = false;

	// �˾�
	private JPopupMenu popup;
	public ButtonGroup popupGroundGroup;
	public JRadioButtonMenuItem popupBackground;
	public JRadioButtonMenuItem popupForeground;
	public EstyleCheckBoxItemGroup popupSettingModeGroup;
	public EstyleCheckBoxItem popupMovable;
	public EstyleCheckBoxItem popupUpperTile;
	public EstyleCheckBoxItem popupGrid;

	// �� gui���� ������ ����

	// ������
	// �ʿ����ͽý�������, ��׶������� ��������, �ȷ�Ʈ �ε���
	public PalettePanel(MapEditorSystem paraSys, boolean backGround,
			int paletteIndex) {
		super();
		// �ʱ� �Ķ���� ����
		this.mousePoint = new Point(0, 0);
		clickPoint = new Point(0, 0);
		dragPoint = new Point(0, 0);
		this.mapSys = paraSys;
		back = backGround;
		this.paletteIndex = paletteIndex;
		this.rectHeight = this.rectWidth = 0;

		// ���콺 �̺�Ʈ
		addMouseListener(this);
		addMouseMotionListener(this);
		initPopup();
	}

	public PalettePanel(MapIntegrateGUI tot, boolean backGround,
			int paletteIndex) {
		this(tot.getMapSys(), backGround, paletteIndex);
		tot.setPaletteInfo(this);
		initPopup();
	}

	private void initPopup() {
		popup = new JPopupMenu();
		popupGroundGroup = new ButtonGroup();
		popupSettingModeGroup = new EstyleCheckBoxItemGroup();
		popupBackground = new JRadioButtonMenuItem("Show background pallete",
				true);
		popupForeground = new JRadioButtonMenuItem("Show foreground pallete",
				false);
		popupMovable = new EstyleCheckBoxItem("Movable tiles setting",
				popupSettingModeGroup);
		popupUpperTile = new EstyleCheckBoxItem(
				"Upper tiles then characters setting", popupSettingModeGroup);
		popupGrid = new EstyleCheckBoxItem("Grid style");

		popupGroundGroup.add(popupBackground);
		popupGroundGroup.add(popupForeground);

		popup.add(popupBackground);
		popup.add(popupForeground);
		popup.addSeparator();
		popup.add(popupMovable);

		if (!isBackground())
			popup.add(popupUpperTile);

		popup.addSeparator();
		popup.add(popupGrid);

		popupBackground.addActionListener(this);
		popupForeground.addActionListener(this);
		popupMovable.addActionListener(this);
		popupUpperTile.addActionListener(this);
		popupGrid.addActionListener(this);
	}

	// �ȷ�Ʈ�� ������ map���� ��ü
	public void setMapDataForPaletteDraw(MapEditorSystem mapsys) {
		this.mapSys = mapsys;
	}

	// ���� �ȷ�Ʈ�� ������ ���������� ���� ����Ʈ���� ��׶���
	public void setGround(boolean tmp) {
		back = tmp;
	}

	public MapEditorSystem getMapData() {
		return this.mapSys;
	}

	// ���� �ȷ�Ʈ�� ��������� ��������
	public boolean isBackground() {
		if (back == true)
			return true;
		else
			return false;
	}

	// ���� �� �ȷ�Ʈ�� ���° �ȷ�Ʈ�ΰ��� ����
	public void setPaletteIndex(int index) {
		paletteIndex = index;
	}

	public int getPaletteIndex() {
		return this.paletteIndex;
	}

	// �׸���ȭ �Ұ��ΰ�? �ٽ� ���׸���ȭ�Ұ��ΰ�?
	public void setGrid(boolean b) {
		isGrid = b;
		repaint();
	}

	// ȭ�鿡 ����� �̹��� ����
	public void setImage(BufferedImage thisImage) {
		// �̹����� �׸��� ũ�⿡ �°� �ڸ���.
		BufferedImage buffimage = new BufferedImage(thisImage.getWidth()
				- thisImage.getWidth() % DrawingTemplate.pixel,
				thisImage.getHeight() - thisImage.getHeight()
						% DrawingTemplate.pixel, BufferedImage.TYPE_4BYTE_ABGR);
		// ���� �߸� �̹����� �׷��� ũ�⸦ �����.
		Graphics2D g = buffimage.createGraphics();
		g.drawImage(thisImage, 0, 0, null);
		g.dispose();
		imageGround = buffimage;
		this.setPreferredSize(new Dimension(imageGround.getWidth(), imageGround
				.getHeight()));
		repaint();
	}

	// ��弳��
	public void setMode(int mode) {
		paletteMode = false;
		moveMode = false;
		upperMode = false;

		if (mode == PalettePanel.PALETTEMODE)
			paletteMode = true;
		else if (mode == PalettePanel.MOVEEMODE)
			moveMode = true;
		else if (mode == PalettePanel.UPPERMODE)
			if (!isBackground())
				upperMode = true;
			else
				paletteMode = true;

		repaint();
	}

	// �簢�� ���� ����
	public int getRectWidth() {
		return this.rectWidth;
	}

	public int getRectHeight() {
		return this.rectHeight;
	}

	// ���콺 �̺�Ʈ�� ���� ȭ�� ���
	private void drawMouseEvent(Graphics2D g2d, Color rectColor) {
		Color tmp = g2d.getColor();
		// 1.���콺 �̺�Ʈ�� ó�� �̹��� ���� �������� �׷���
		// ���콺�� Ŭ��������
		if (click == true) {
			if (mousePoint.x >= 0 && mousePoint.x <= imageGround.getWidth()
					&& mousePoint.y >= 0
					&& mousePoint.y <= imageGround.getHeight()) {

				g2d.setColor(rectColor);
				int x = (clickPoint.x) - (clickPoint.x % DrawingTemplate.pixel);
				int y = (clickPoint.y) - (clickPoint.y % DrawingTemplate.pixel);
				g2d.draw3DRect(x, y, DrawingTemplate.pixel,
						DrawingTemplate.pixel, false);
			}
			// /////////////////////////////
		} else if (isDrag == true || click == false) {
			if (mousePoint.x >= 0 && mousePoint.x <= imageGround.getWidth()
					&& mousePoint.y >= 0
					&& mousePoint.y <= imageGround.getHeight()
					&& dragPoint.x >= 0
					&& dragPoint.x <= imageGround.getWidth()
					&& dragPoint.y >= 0
					&& dragPoint.y <= imageGround.getHeight()) {

				g2d.setColor(rectColor);
				int cx = (clickPoint.x)
						- (clickPoint.x % DrawingTemplate.pixel);
				int cy = (clickPoint.y)
						- (clickPoint.y % DrawingTemplate.pixel);
				int dx = (dragPoint.x) - (dragPoint.x % DrawingTemplate.pixel);
				int dy = (dragPoint.y) - (dragPoint.y % DrawingTemplate.pixel);

				if (cx <= dx) {
					if (cy <= dy)
						g2d.draw3DRect(cx, cy, this.rectWidth, this.rectHeight,
								false);
					else
						g2d.draw3DRect(cx, dy, this.rectWidth, this.rectHeight,
								false);
				} else {
					if (cy <= dy)
						g2d.draw3DRect(dx, cy, this.rectWidth, this.rectHeight,
								false);
					else
						g2d.draw3DRect(dx, dy, this.rectWidth, this.rectHeight,
								false);
				}
			}
		}
		g2d.setColor(tmp);
	}

	// �Ϲ� �ȷ�Ʈ ��ο� ���
	public void drawPalette(Graphics2D g2d) {
		g2d.drawImage(imageGround, 0, 0, this);
		if (isGrid == true) {
			g2d.setColor(new Color(0, 0, 0, 100));
			for (int i = 0; i < imageGround.getHeight() / DrawingTemplate.pixel; i++) {
				g2d.drawLine(0, i * DrawingTemplate.pixel,
						imageGround.getWidth(), i * DrawingTemplate.pixel);

			}
			for (int j = 0; j < imageGround.getWidth() / DrawingTemplate.pixel; j++) {
				g2d.drawLine(j * DrawingTemplate.pixel, 0, j
						* DrawingTemplate.pixel, imageGround.getHeight());

			}
		}
		// 1.���콺 �̺�Ʈ�� ó�� �̹��� ���� �������� �׷���
		// ���콺�� Ŭ��������
		this.drawMouseEvent(g2d, Color.GREEN);
	}

	// ĳ���� ���� ���
	public void drawMoveMode(Graphics2D g2d) {
		// �׸����� �׸��� �� ����
		g2d.drawImage(imageGround, 0, 0, this);

		// �׸����������� ������
		g2d.setColor(new Color(0, 0, 0, 100));
		for (int i = 0; i < imageGround.getHeight() / DrawingTemplate.pixel; i++) {
			g2d.drawLine(0, i * DrawingTemplate.pixel, imageGround.getWidth(),
					i * DrawingTemplate.pixel);
		}
		for (int j = 0; j < imageGround.getWidth() / DrawingTemplate.pixel; j++) {
			g2d.drawLine(j * DrawingTemplate.pixel, 0, j
					* DrawingTemplate.pixel, imageGround.getHeight());
		}
		// ���Ŀ� Ÿ�ϼ��� ���ư��鼭 Ȯ��
		for (int col = 0; col < imageGround.getHeight() / DrawingTemplate.pixel; col++) {
			for (int row = 0; row < imageGround.getWidth()
					/ DrawingTemplate.pixel; row++) {
				try {
					if (mapSys.getPalette(paletteIndex, back)
							.getM_Tile(col, row).getIsMove())
						drawO(g2d, row * DrawingTemplate.pixel, col
								* DrawingTemplate.pixel);
					// g2d.drawString("O", row*DrawingTemplate.pixel,
					// col*DrawingTemplate.pixel);
					else
						drawX(g2d, row * DrawingTemplate.pixel, col
								* DrawingTemplate.pixel);
				} catch (IllegalTileIndex e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// 1.���콺 �̺�Ʈ�� ó�� �̹��� ���� �������� �׷���
		// ���콺�� Ŭ��������
		this.drawMouseEvent(g2d, Color.red);
	}

	// ĳ���� ȭ�� ��� ���
	public void drawUpperMode(Graphics2D g2d) {
		// �׸����� �׸��� �� ����
		g2d.drawImage(imageGround, 0, 0, this);

		// �׸����������� ������
		g2d.setColor(new Color(0, 0, 0, 100));
		for (int i = 0; i < imageGround.getHeight() / DrawingTemplate.pixel; i++) {
			g2d.drawLine(0, i * DrawingTemplate.pixel, imageGround.getWidth(),
					i * DrawingTemplate.pixel);
		}
		for (int j = 0; j < imageGround.getWidth() / DrawingTemplate.pixel; j++) {
			g2d.drawLine(j * DrawingTemplate.pixel, 0, j
					* DrawingTemplate.pixel, imageGround.getHeight());
		}
		// ���Ŀ� Ÿ�ϼ��� ���ư��鼭 Ȯ��
		for (int col = 0; col < imageGround.getHeight() / DrawingTemplate.pixel; col++) {
			for (int row = 0; row < imageGround.getWidth()
					/ DrawingTemplate.pixel; row++) {
				try {
					if (mapSys.getPalette(paletteIndex, back)
							.getM_Tile(col, row).getIsUpper())
						drawUp(g2d, row * DrawingTemplate.pixel, col
								* DrawingTemplate.pixel);
					// g2d.drawString("O", row*DrawingTemplate.pixel,
					// col*DrawingTemplate.pixel);
					else
						drawDown(g2d, row * DrawingTemplate.pixel, col
								* DrawingTemplate.pixel);
				} catch (IllegalTileIndex e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// ���콺 �̺�Ʈ ����
		this.drawMouseEvent(g2d, Color.BLUE);
	}

	// ���� ��ο� x o
	public void drawX(Graphics2D g2d, int x, int y) {
		int xPoints[] = new int[12];
		int yPoints[] = new int[12];

		xPoints[0] = x + 1 * 2;
		yPoints[0] = y + 2 * 2;

		xPoints[1] = x + 2 * 2;
		yPoints[1] = y + 1 * 2;

		xPoints[2] = x + 3 * 2;
		yPoints[2] = y + 3 * 2;

		xPoints[3] = x + 4 * 2;
		yPoints[3] = y + 1 * 2;

		xPoints[4] = x + 5 * 2;
		yPoints[4] = y + 2 * 2;

		xPoints[5] = x + 4 * 2;
		yPoints[5] = y + 3 * 2;

		xPoints[6] = x + 5 * 2;
		yPoints[6] = y + 4 * 2;

		xPoints[7] = x + 4 * 2;
		yPoints[7] = y + 5 * 2;

		xPoints[8] = x + 3 * 2;
		yPoints[8] = y + 4 * 2;

		xPoints[9] = x + 2 * 2;
		yPoints[9] = y + 5 * 2;

		xPoints[10] = x + 1 * 2;
		yPoints[10] = y + 5 * 2;

		xPoints[11] = x + 2 * 2;
		yPoints[11] = y + 3 * 2;

		Color tmp = g2d.getColor();
		g2d.setColor(new Color(255, 0, 0, 100));
		g2d.fillPolygon(xPoints, yPoints, 12);
		g2d.setColor(new Color(0, 0, 0, 100));
		g2d.drawPolygon(xPoints, yPoints, 12);
		g2d.setColor(tmp);
	}

	public void drawO(Graphics2D g2d, int x, int y) {
		Color tmp = g2d.getColor();

		g2d.setColor(new Color(0, 255, 0, 100));
		g2d.fillOval(x, y, DrawingTemplate.pixel / 3, DrawingTemplate.pixel / 3);
		g2d.setColor(new Color(0, 0, 0, 100));
		g2d.drawOval(x, y, DrawingTemplate.pixel / 3, DrawingTemplate.pixel / 3);
		g2d.setColor(tmp);
	}

	public void drawUp(Graphics2D g2d, int x, int y) {
		int xPoints[] = new int[7];
		int yPoints[] = new int[7];

		xPoints[0] = x + 4;
		yPoints[0] = y;

		xPoints[1] = x + 7;
		yPoints[1] = y + 5;

		xPoints[2] = x + 5;
		yPoints[2] = y + 5;

		xPoints[3] = x + 5;
		yPoints[3] = y + 8;

		xPoints[4] = x + 3;
		yPoints[4] = y + 8;

		xPoints[5] = x + 3;
		yPoints[5] = y + 4;

		xPoints[6] = x + 2;
		yPoints[6] = y + 5;

		Color tmp = g2d.getColor();
		g2d.setColor(new Color(0, 255, 0, 100));
		g2d.fillPolygon(xPoints, yPoints, 7);
		g2d.setColor(new Color(0, 0, 0, 200));
		g2d.drawPolygon(xPoints, yPoints, 7);
		g2d.setColor(tmp);

	}

	public void drawDown(Graphics2D g2d, int x, int y) {
		int xPoints[] = new int[7];
		int yPoints[] = new int[7];

		xPoints[0] = x + 4;
		yPoints[0] = y + 8;

		xPoints[1] = x + 7;
		yPoints[1] = y + 4;

		xPoints[2] = x + 5;
		yPoints[2] = y + 4;

		xPoints[3] = x + 5;
		yPoints[3] = y + 1;

		xPoints[4] = x + 3;
		yPoints[4] = y + 1;

		xPoints[5] = x + 3;
		yPoints[5] = y + 4;

		xPoints[6] = x + 2;
		yPoints[6] = y + 4;

		Color tmp = g2d.getColor();
		g2d.setColor(new Color(255, 0, 0, 100));
		g2d.fillPolygon(xPoints, yPoints, 7);
		g2d.setColor(new Color(0, 0, 0, 200));
		g2d.drawPolygon(xPoints, yPoints, 7);
		g2d.setColor(tmp);
	}

	// ȭ�鿡 �׷��ش�.
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		// �̹����� �޾ƿ� ���
		if (imageGround != null) {
			// ����弳��
			if (this.paletteMode == true)
				drawPalette(g2d);
			else if (this.moveMode == true)
				drawMoveMode(g2d);
			else if (this.upperMode == true)
				drawUpperMode(g2d);
		} else {
			g2d.setColor(new Color(255, true));
			g2d.fillRect(0, 0, 100, 100);
		}
	}

	// ���콺 �������� ����
	@Override
	public void mouseDragged(MouseEvent me) {
		MainFrame.OWNER.setCoordinate(me.getPoint());
		// �巡�� �Ǹ鼭 ������ ��ǥ ����
		if ((me.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
			dragPoint = me.getPoint();
			isDrag = true;
			this.click = false;
			if (imageGround == null)
				return;
			// �� ��ǥ�� ���ؾ���
			int startCol = 0, startRow = 0, endCol = 0, endRow = 0;
			// �ε��� ���
			startCol = clickPoint.y / DrawingTemplate.pixel;
			startRow = clickPoint.x / DrawingTemplate.pixel;
			endCol = dragPoint.y / DrawingTemplate.pixel;
			endRow = dragPoint.x / DrawingTemplate.pixel;
			this.rectHeight = (Math.abs(startCol - endCol) + 1)
					* DrawingTemplate.pixel;
			this.rectWidth = (Math.abs(endRow - startRow) + 1)
					* DrawingTemplate.pixel;

			MainFrame.OWNER.setMainStateEast("Palette " + rectWidth
					/ DrawingTemplate.pixel + " x " + rectHeight
					/ DrawingTemplate.pixel + " block");
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		MainFrame.OWNER.setCoordinate(me.getPoint());
		this.mousePoint = me.getPoint();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if ((me.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
			if (imageGround == null)
				return;
			this.isDrag = false;
			this.click = true;
			// TODO Auto-generated method stub
			if (mousePoint.x >= 0 && mousePoint.x <= imageGround.getWidth()
					&& mousePoint.y >= 0
					&& mousePoint.y <= imageGround.getHeight()) {
				this.clickPoint.x = mousePoint.x;
				this.clickPoint.y = mousePoint.y;
				int startRow = mousePoint.x / DrawingTemplate.pixel;
				int startCol = mousePoint.y / DrawingTemplate.pixel;
				this.rectHeight = DrawingTemplate.pixel;
				this.rectWidth = DrawingTemplate.pixel;
				// ��ο� ����϶�
				if (this.paletteMode == true) {
					try {
						mapSys.setTiles(startCol, startRow, startCol, startRow,
								paletteIndex, back);
					} catch (IllegalTileIndex e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (this.moveMode == true) {
					try {
						mapSys.getPalette(paletteIndex, back)
								.getM_Tile(startCol, startRow).setIsMove();
					} catch (IllegalTileIndex e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (this.upperMode == true) {
					try {
						mapSys.getPalette(paletteIndex, back)
								.getM_Tile(startCol, startRow).setIsUpper();
					} catch (IllegalTileIndex e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if ((me.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
			clickPoint = me.getPoint();
			MainFrame.OWNER.setMainStateEast("Palette 1 x 1 block");
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if ((me.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
			if (imageGround == null)
				return;
			// �� ��ǥ�� ���ؾ���
			int startCol = 0, startRow = 0, endCol = 0, endRow = 0;
			// �ε��� ���
			startCol = clickPoint.y / DrawingTemplate.pixel;
			startRow = clickPoint.x / DrawingTemplate.pixel;
			endCol = dragPoint.y / DrawingTemplate.pixel;
			endRow = dragPoint.x / DrawingTemplate.pixel;

			this.rectHeight = (Math.abs(startCol - endCol) + 1)
					* DrawingTemplate.pixel;
			this.rectWidth = (Math.abs(endRow - startRow) + 1)
					* DrawingTemplate.pixel;
			if (startRow < 0)
				startRow = 0;
			if (startCol < 0)
				startCol = 0;
			if (endRow < 0)
				endRow = 0;
			if (endCol < 0)
				endCol = 0;
			// �巡�� �� ������
			if (clickPoint.x <= dragPoint.x
					&& dragPoint.x <= imageGround.getWidth()
					&& clickPoint.x >= 0) {
				// �巡�� �� �Ʒ���
				if (clickPoint.y <= dragPoint.y
						&& dragPoint.y <= imageGround.getHeight()) {
					try {
						if (this.paletteMode == true)
							this.mapSys.setTiles(startCol, startRow, endCol,
									endRow, paletteIndex, back);
						else if (this.moveMode == true && this.isDrag == true
								&& this.click == false) {
							for (int i = startCol; i <= endCol; i++) {
								for (int j = startRow; j <= endRow; j++) {
									mapSys.getPalette(paletteIndex, back)
											.getM_Tile(i, j).setIsMove();
								}
							}
						} else if (this.upperMode == true
								&& this.isDrag == true && this.click == false) {
							for (int i = startCol; i <= endCol; i++) {
								for (int j = startRow; j <= endRow; j++) {
									mapSys.getPalette(paletteIndex, back)
											.getM_Tile(i, j).setIsUpper();
								}
							}
						}
					} catch (IllegalTileIndex e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// �巡�� �� ����
				else if (clickPoint.y > dragPoint.y
						&& clickPoint.y <= imageGround.getHeight()) {
					try {
						if (this.paletteMode == true)
							this.mapSys.setTiles(endCol, startRow, startCol,
									endRow, paletteIndex, back);
						else if (this.moveMode == true && this.isDrag == true
								&& this.click == false) {

							for (int i = endCol; i <= startCol; i++) {
								for (int j = startRow; j <= endRow; j++) {
									mapSys.getPalette(paletteIndex, back)
											.getM_Tile(i, j).setIsMove();
								}
							}
						} else if (this.upperMode == true
								&& this.isDrag == true && this.click == false) {
							for (int i = endCol; i <= startCol; i++) {
								for (int j = startRow; j <= endRow; j++) {
									mapSys.getPalette(paletteIndex, back)
											.getM_Tile(i, j).setIsUpper();
								}
							}
						}
					} catch (IllegalTileIndex e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// �巡�� �� ����
			else if (clickPoint.x > dragPoint.x && dragPoint.x >= 0
					&& clickPoint.x <= imageGround.getWidth()) {
				// �巡�� �� �Ʒ���
				if (clickPoint.y <= dragPoint.y
						&& dragPoint.y <= imageGround.getHeight()) {
					try {
						if (this.paletteMode == true)
							this.mapSys.setTiles(startCol, endRow, endCol,
									startRow, paletteIndex, back);
						else if (this.moveMode == true && this.isDrag == true
								&& this.click == false) {

							for (int i = startCol; i <= endCol; i++) {
								for (int j = endRow; j <= startRow; j++) {
									mapSys.getPalette(paletteIndex, back)
											.getM_Tile(i, j).setIsMove();
								}
							}
						} else if (this.upperMode == true
								&& this.isDrag == true && this.click == false) {
							for (int i = startCol; i <= endCol; i++) {
								for (int j = endRow; j <= startRow; j++) {
									mapSys.getPalette(paletteIndex, back)
											.getM_Tile(i, j).setIsUpper();
								}
							}
						}
					} catch (IllegalTileIndex e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// �巡�� �� ����
				else if (clickPoint.y > dragPoint.y
						&& clickPoint.y <= imageGround.getHeight()) {
					{
						try {
							if (this.paletteMode == true)
								this.mapSys.setTiles(endCol, endRow, startCol,
										startRow, paletteIndex, back);
							else if (this.moveMode == true
									&& this.isDrag == true
									&& this.click == false) {

								for (int i = endCol; i <= startCol; i++) {

									for (int j = endRow; j <= startRow; j++) {
										mapSys.getPalette(paletteIndex, back)
												.getM_Tile(i, j).setIsMove();
									}
								}
							} else if (this.upperMode == true
									&& this.isDrag == true
									&& this.click == false) {
								for (int i = endCol; i <= startCol; i++) {

									for (int j = endRow; j <= startRow; j++) {
										mapSys.getPalette(paletteIndex, back)
												.getM_Tile(i, j).setIsUpper();
									}
								}
							}
						} catch (IllegalTileIndex e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			isDrag = false;
		} else {
			if (me.isPopupTrigger()) {
				popup.show(me.getComponent(), me.getX() + 10, me.getY() + 10);
			}
		}
		repaint();

	}

	// // getter

	public boolean isGrid() {
		return isGrid;
	}

	public boolean isPaletteMode() {
		return paletteMode;
	}

	public boolean isMoveMode() {
		return moveMode;
	}

	public boolean isUpperMode() {
		return upperMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == popupGrid) {
			MainFrame.OWNER.setPalletSetGridMode(popupGrid.isSelected());
		} else if (e.getSource() == popupBackground) {
			MainFrame.OWNER.setPalletSetBackgroundMode();

		} else if (e.getSource() == popupForeground) {
			MainFrame.OWNER.setPalletSetForegroundMode();
		} else if (e.getSource() == popupMovable) {
			if (popupMovable.isSelect()) {
				popupMovable.unselect();
				MainFrame.OWNER
						.syncBetweenPalettesMode(PalettePanel.PALETTEMODE);
			} else
				MainFrame.OWNER.syncBetweenPalettesMode(PalettePanel.MOVEEMODE);
		} else if (e.getSource() == popupUpperTile) {
			if (popupUpperTile.isSelect()) {
				popupUpperTile.unselect();
				MainFrame.OWNER
						.syncBetweenPalettesMode(PalettePanel.PALETTEMODE);
			} else
				MainFrame.OWNER.syncBetweenPalettesMode(PalettePanel.UPPERMODE);
		}
	}
}
