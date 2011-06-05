package MapEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import palette.PalettePanel;
import viewControl.MainFrame;
import viewControl.editorDlg.EventDlg;
import viewControl.editorDlg.SetCharStartPointDlg;
import viewControl.esComponent.EstyleCheckBoxItem;
import viewControl.esComponent.EstyleCheckBoxItemGroup;

public class MapIntegrateGUI extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4013785426996229097L;
	/*
	 * 1. �ȷ�Ʈ���� ���� ������ ���⿡�ٰ� ��� 2. ����� ��׶��带 ������ ��ƾ �ʿ�(ex ���� ȸ����� ���) 3. GUI�ʿ���
	 * ���ڰ��� �佺���ټ� �ֵ��� �ۼ�->�� �ý����� ���� �����ϰ� ����/�ε����ִ� ������ ���� 4. ���� �Ȱ��� �κ����� ä������
	 * ��ɵ� �߰�����
	 */
	// �ʿ����� ������ ��
	private MapEditorSystem mapSys;
	// �̹��� ����� ����
	private BufferedImage background;
	private BufferedImage foreground;
	// ���õ� �ȷ�Ʈ ������ ����
	PalettePanel paletteInfo;

	// ��¼����� ����/��¿ɼǼ���
	public static final int BACKGROUND_ONLY = 1;
	public static final int FOREGROUND_ONLY = 2;
	public static final int SEMITRANSPARENT = 3;
	public static final int SYNTHESYS_MODE = 4;
	public static final int GRID_MODE = 5;
	public static final int EVENTMODE_MODE = 6;
	public static final int CANVAS_MODE = 7;

	public static final int STAMP_TOOL = 8;
	public static final int PAINT_TOOL = 9;

	public static boolean GRIDMODE_FLAG = false;
	public static boolean EVENTMODE_FLAG = false;

	// ó���� ���� ���콺 ��ġ
	public static final int STARTING_POINT = 4;

	private int outputFlag = 4;
	private int drawTool = 0;

	// ���콺�� ��ġ Ȯ��
	private Point mousePoint;
	private Point dragPoint;
	private Point pressPoint;
	private Point startEventPoint;
	private Point endEventPoint;
	// ����� ���Ǹ� ���� ������ �ȷ�Ʈ ũ�� ������. ȭ�鿡 �Ѹ� �簢�� ������
	private int rectWidth = 0;
	private int rectHeight = 0;
	// �巡���ΰ�?
	private boolean isDrag = false;
	private boolean isDragEvent = false;
	// �гο� �׸��� ����� ��ġ
	private int xAxis = 0;
	private int yAxis = 0;

	// // �̺�Ʈ �ý���
	// private EventEditorSystem eventEditorSystem;

	// �˾� �޴�
	private JPopupMenu popupmenuCanvas;
	private JPopupMenu popupmenuEvent;
	private JMenu popupViewStyle;

	public EstyleCheckBoxItemGroup poppuEventBtnGroup;
	public EstyleCheckBoxItem popupEventGrid;
	public EstyleCheckBoxItem popupEventBgOnly;
	public EstyleCheckBoxItem popupEventFgOnly;
	public EstyleCheckBoxItem popupEventSemitransparent;

	public EstyleCheckBoxItemGroup poppuCanvasBtnGroup;
	public EstyleCheckBoxItem popupGrid;
	public EstyleCheckBoxItem popupBgOnly;
	public EstyleCheckBoxItem popupFgOnly;
	public EstyleCheckBoxItem popupSemitransparent;
	private ButtonGroup toolGroup;
	public JRadioButtonMenuItem popupPaintTool;
	public JRadioButtonMenuItem popupStampTool;

	private JMenuItem popupEventMode;
	private JMenuItem popupSetEvent;
	private JMenuItem popupCopyEvent;
	private JMenuItem popupPasteEvent;
	private JMenuItem popupDeleteEvent;
	private JMenuItem popupSetStartingPointEvent;

	private JMenuItem popupCanvasMode;

	public MapIntegrateGUI(MapEditorSystem mapsys) throws IOException {
		super();
		if (mapsys == null)
			mapSys = new MapEditorSystem();
		else {
			this.mapSys = mapsys;
		}
		drawTool = STAMP_TOOL;
		this.paletteInfo = null;
		mousePoint = new Point(0, 0);
		dragPoint = new Point(0, 0);
		pressPoint = new Point(0, 0);
		startEventPoint = new Point(STARTING_POINT, STARTING_POINT);
		endEventPoint = new Point(STARTING_POINT, STARTING_POINT);
		xAxis = 0;
		yAxis = 0;

		setMinimumSize(new Dimension(10, 10));
		// ���콺 �̺�Ʈ
		addMouseListener(this);
		addMouseMotionListener(this);

		// �˾�
		popupmenuCanvas = new JPopupMenu();
		popupmenuEvent = new JPopupMenu();
		popupViewStyle = new JMenu("Canvas view style");
		popupEventGrid = new EstyleCheckBoxItem("Grid");
		poppuEventBtnGroup = new EstyleCheckBoxItemGroup();
		popupEventBgOnly = new EstyleCheckBoxItem("Background only",
				poppuEventBtnGroup);
		popupEventFgOnly = new EstyleCheckBoxItem("ForeGround only",
				poppuEventBtnGroup);
		popupEventSemitransparent = new EstyleCheckBoxItem(
				"background translucence", poppuEventBtnGroup);
		popupEventMode = new JMenuItem("Set event mode");
		popupSetEvent = new JMenuItem("Set event");
		popupCopyEvent = new JMenuItem("Copy event");
		popupPasteEvent = new JMenuItem("Paste event");
		popupDeleteEvent = new JMenuItem("Delete event");
		popupSetStartingPointEvent = new JMenuItem(
				"Set user charactor starting point");
		popupCanvasMode = new JMenuItem("Set canvas mode");

		poppuCanvasBtnGroup = new EstyleCheckBoxItemGroup();
		popupGrid = new EstyleCheckBoxItem("Grid");
		popupBgOnly = new EstyleCheckBoxItem("Background only",
				poppuCanvasBtnGroup);
		popupFgOnly = new EstyleCheckBoxItem("Foreground only",
				poppuCanvasBtnGroup);
		popupSemitransparent = new EstyleCheckBoxItem(
				"background translucence", poppuCanvasBtnGroup);

		toolGroup = new ButtonGroup();
		popupPaintTool = new JRadioButtonMenuItem("Paint tool");
		popupStampTool = new JRadioButtonMenuItem("Stamp tool");
		toolGroup.add(popupStampTool);
		toolGroup.add(popupPaintTool);
		popupStampTool.setSelected(true);

		popupmenuCanvas.add(popupStampTool);
		popupmenuCanvas.add(popupPaintTool);
		popupmenuCanvas.addSeparator();
		popupmenuCanvas.add(popupBgOnly);
		popupmenuCanvas.add(popupFgOnly);
		popupmenuCanvas.add(popupSemitransparent);
		popupmenuCanvas.add(new JSeparator());
		popupmenuCanvas.add(popupGrid);
		popupmenuCanvas.addSeparator();
		popupmenuCanvas.add(popupEventMode);

		popupViewStyle.add(popupEventGrid);
		popupViewStyle.add(popupEventBgOnly);
		popupViewStyle.add(popupEventFgOnly);
		popupViewStyle.add(popupEventSemitransparent);

		popupmenuEvent.add(popupSetEvent);
		popupmenuEvent.add(popupCopyEvent);
		popupmenuEvent.add(popupPasteEvent);
		popupmenuEvent.add(popupDeleteEvent);
		popupmenuEvent.add(popupSetStartingPointEvent);
		popupmenuEvent.add(new JSeparator());
		popupmenuEvent.add(popupViewStyle);
		popupmenuEvent.add(popupCanvasMode);

		popupPasteEvent.setEnabled(false);

		popupStampTool.addActionListener(this);
		popupPaintTool.addActionListener(this);
		popupEventGrid.addActionListener(this);
		popupEventBgOnly.addActionListener(this);
		popupEventFgOnly.addActionListener(this);
		popupEventSemitransparent.addActionListener(this);
		popupSetEvent.addActionListener(this);
		popupCopyEvent.addActionListener(this);
		popupPasteEvent.addActionListener(this);
		popupDeleteEvent.addActionListener(this);
		popupSetStartingPointEvent.addActionListener(this);
		popupCanvasMode.addActionListener(this);

		popupGrid.addActionListener(this);
		popupBgOnly.addActionListener(this);
		popupFgOnly.addActionListener(this);
		popupSemitransparent.addActionListener(this);
		popupEventMode.addActionListener(this);
	}

	// ���� ������ �ʿ����� �ý����� ����
	public MapEditorSystem getMapSys() {
		return this.mapSys;
	}

	// ���� ���� ���鶧 ȣ���Ѵ�
	public void createMap(String mapName, int mapWidth, int mapHeight) {
		try {
			mapSys.newMap(mapName, mapWidth, mapHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
		background = mapSys.drawBackground(0, 0);
		foreground = mapSys.drawForeground(0, 0);
		setPreferredSize(new Dimension(background.getWidth(),
				background.getHeight()));

		repaint();
	}

	// �ȷ�Ʈ ���� �Է�
	public void setPaletteInfo(PalettePanel paletteInfo) {
		this.paletteInfo = paletteInfo;
	}

	// �ȷ�Ʈ ����/����/ȣ�� ��ƾ
	public void setBackgroundPalette(String fileName) {
		mapSys.makeBackTemplate(fileName);
	}

	public void setForegroundPalette(String fileName) {
		mapSys.makeForeTemplate(fileName);
	}

	public void deleteBackgroundPalette(int paletteIndex) {
		mapSys.deleteBackTemplate(paletteIndex);
	}

	public void deleteForegroundPalette(int paletteIndex) {
		mapSys.deleteForeTemplate(paletteIndex);
	}

	public BufferedImage getBackgroundInfo(int index) {
		return mapSys.getBackTemplate(index);
	}

	public BufferedImage getForeroundInfo(int index) {
		return mapSys.getForeTemplate(index);
	}

	// �ʵ����� ��ü�κ��� �ȷ�Ʈ ���� ����
	public void copyPaletteInfoFromMapData(MapEditorSystem mapsys) {
		this.mapSys.setBackPaletteVector(mapsys.getBackPalettes());
		this.mapSys.setForePaletteVector(mapsys.getForePalettes());
	}

	// ����� ���
	// 1. ������� ����� �������� ä���
	public void drawSelectedSpace() {
		// paletteInfo.
		// ���� ��ġ����
		int clickedCol = mousePoint.y / DrawingTemplate.pixel;
		int clickedRow = mousePoint.x / DrawingTemplate.pixel;
		// ����/�巡���� �ȷ�Ʈ�� ũ�⼳��
		int endCol = clickedCol + rectHeight / DrawingTemplate.pixel - 1;
		int endRow = clickedRow + rectWidth / DrawingTemplate.pixel - 1;
		// System.out.println("clickedCol : "+clickedCol);
		// System.out.println("clickedRow : "+clickedRow);
		// System.out.println("endCol : "+endCol);
		// System.out.println("endRow : "+endRow);

	}

	// �����մϴ�.
	public void save(String fileName) {
		try {
			mapSys.save(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveAs(String filePath, String fileName)
			throws ClassNotFoundException {
		try {
			mapSys.saveAs(filePath, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} // ��
	}

	// �ε��մϴ�.
	public void load(String fileName) {
		try {
			mapSys.load(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		background = mapSys.drawBackground(0, 0);
		foreground = mapSys.drawForeground(0, 0);
		repaint();
	}

	// ��¼����� ����/��¿ɼǼ���
	public void setOutputFlag(int outputFlag) {
		this.outputFlag = outputFlag;
		if (outputFlag == BACKGROUND_ONLY) {
			MainFrame.OWNER.setMainStateWest("View background");
		} else if (outputFlag == FOREGROUND_ONLY) {
			MainFrame.OWNER.setMainStateWest("View foreground");
		} else if (outputFlag == SEMITRANSPARENT) {
			MainFrame.OWNER.setMainStateWest("View semitransparent");
		} else {
			MainFrame.OWNER.setMainStateWest("View all");
		}
	}

	// �׸��� ���
	public void setGrid(boolean b) {
		MapIntegrateGUI.GRIDMODE_FLAG = b;
		repaint();
	}

	// �̺�Ʈ ���
	public void setEvent(boolean b) {
		MapIntegrateGUI.EVENTMODE_FLAG = b;
		repaint();
	}

	// ��׶��忡 �׸�
	private void insertTileToBack(int col, int row) {
		background = this.mapSys.drawBackground(col, row);
		repaint();
	}

	// ����׶��忡 �׸�
	private void insertTileToFore(int col, int row) {
		foreground = this.mapSys.drawForeground(col, row);
		repaint();
	}

	// ����ڿ��� ������ �簢�� ���� ( ������ ���)
	private void drawSelectedPaletteRect(Graphics2D g, Color color) {
		Color tmp = g.getColor();
		if (isDrag == false) {
			g.setColor(color);
			int x = mousePoint.x - mousePoint.x % DrawingTemplate.pixel;
			int y = mousePoint.y - mousePoint.y % DrawingTemplate.pixel;

			g.draw3DRect(x, y, rectWidth, rectHeight, false);
		} else {
			g.setColor(color);
			int x = dragPoint.x - dragPoint.x % DrawingTemplate.pixel;
			int y = dragPoint.y - dragPoint.y % DrawingTemplate.pixel;

			g.draw3DRect(x, y, rectWidth, rectHeight, false);
		}
		g.setColor(tmp);
	}

	private void drawDragedRect(Graphics2D g, Color color, Point startPoint,
			Point endPoint) {
		Color tmp = g.getColor();
		int sx = (startPoint.x/DrawingTemplate.pixel)*DrawingTemplate.pixel;
		int sy = (startPoint.y/DrawingTemplate.pixel)*DrawingTemplate.pixel;
		int ex = (endPoint.x/DrawingTemplate.pixel)*DrawingTemplate.pixel;
		int ey = (endPoint.y/DrawingTemplate.pixel)*DrawingTemplate.pixel;
		if (sx <= ex)
			ex += DrawingTemplate.pixel;
		else
			sx += DrawingTemplate.pixel;
		if (sy <= ey)
			ey += DrawingTemplate.pixel;
		else {
			sy += DrawingTemplate.pixel;
		}

		int w = Math.abs(ex - sx);
		int h = Math.abs(ey - sy);
		g.draw3DRect(sx, sy, w, h, false);
		g.setColor(tmp);
	}

	// �׸��带 ȭ�鿡 ���
	private void drawGridMap(Graphics2D g2d) {
		// �׸����������� ������
		Color tmp = g2d.getColor();
		g2d.setColor(new Color(0, 0, 0, 100));
		for (int i = 0; i < background.getHeight() / DrawingTemplate.pixel; i++) {
			g2d.drawLine(0, i * DrawingTemplate.pixel, background.getWidth(), i
					* DrawingTemplate.pixel);
		}
		for (int j = 0; j < background.getWidth() / DrawingTemplate.pixel; j++) {
			g2d.drawLine(j * DrawingTemplate.pixel, 0, j
					* DrawingTemplate.pixel, background.getHeight());
		}
		g2d.setColor(tmp);
	}

	// ȭ�鿡 ���
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		Color tmp = g2d.getColor();
		g.setColor(Color.white);
		g.fillRect(0, 0, background.getWidth(), background.getHeight());
		g2d.setColor(tmp);

		tmp = g2d.getColor();
		if (this.outputFlag == MapIntegrateGUI.BACKGROUND_ONLY) {
			if (background != null)
				g2d.drawImage(background, xAxis, yAxis, null);
		} else if (outputFlag == MapIntegrateGUI.FOREGROUND_ONLY) {
			if (foreground != null)
				g2d.drawImage(foreground, xAxis, yAxis, null);
		} else if (outputFlag == MapIntegrateGUI.SEMITRANSPARENT) {
			if (background != null)
				g2d.drawImage(background, xAxis, yAxis, null);
			g2d.setColor(new Color(10, 10, 10, 100));
			g2d.fillRect(0, 0, foreground.getWidth(), foreground.getHeight());
			g2d.setColor(tmp);
			if (foreground != null)
				g2d.drawImage(foreground, xAxis, yAxis, null);
		} else {
			if (background != null)
				g2d.drawImage(background, xAxis, yAxis, null);
			if (foreground != null)
				g2d.drawImage(foreground, xAxis, yAxis, null);
		}

		// �̺�Ʈ ���
		if (MapIntegrateGUI.EVENTMODE_FLAG) {
			tmp = g2d.getColor();

			// ������ ��� ����
			g2d.setColor(new Color(80, 150, 80, 100));
			g2d.fillRect(0, 0, foreground.getWidth(), foreground.getHeight());

			// �׸��� ��� ����
			g2d.setColor(new Color(255, 255, 255, 100));
			for (int i = 0; i < background.getHeight() / DrawingTemplate.pixel; i++) {
				g2d.drawLine(0, i * DrawingTemplate.pixel,
						background.getWidth(), i * DrawingTemplate.pixel);
			}
			for (int j = 0; j < background.getWidth() / DrawingTemplate.pixel; j++) {
				g2d.drawLine(j * DrawingTemplate.pixel, 0, j
						* DrawingTemplate.pixel, background.getHeight());
			}

			// ���� �̺�Ʈ ��ġ ����
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("����", Font.BOLD, 10));
			int reRatio = DrawingTemplate.pixel / 4;
			for (int i = 0; i < background.getWidth() / DrawingTemplate.pixel; i++) {
				for (int j = 0; j < background.getHeight()
						/ DrawingTemplate.pixel; j++) {
					if (mapSys.getEventEditSys().hasEventOnTile(j, i)) {
						g2d.drawString("E",
								i * DrawingTemplate.pixel + reRatio, j
										* DrawingTemplate.pixel
										+ DrawingTemplate.pixel - reRatio);
					}
				}
			}

			g2d.setColor(tmp);
		}
		// �׸����� ���
		if (MapIntegrateGUI.GRIDMODE_FLAG)
			drawGridMap(g2d);

		// ���콺 �̺�Ʈ�� ���� �簢�� ����
		if (EVENTMODE_FLAG) {
			drawDragedRect(g2d, g2d.getColor(), startEventPoint, endEventPoint);
		} else if (drawTool == STAMP_TOOL) {
			drawSelectedPaletteRect(g2d, Color.RED);
		} else if (drawTool == PAINT_TOOL) {
			drawDragedRect(g2d, Color.RED, pressPoint, dragPoint);
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!MouseDrawUtility.checkMouseBoundery(e.getPoint(), mapSys))
			return;
		MainFrame.OWNER.setCoordinate(e.getPoint());
		if (EVENTMODE_FLAG) {
			if (isDragEvent) {
				endEventPoint = e.getPoint();
			}
		} else { // �̺�Ʈ ��尡 �ƴϸ�
			if (drawTool == STAMP_TOOL) {
				dragPoint = e.getPoint();
				isDrag = true;
				if (this.paletteInfo == null)
					return;
				// ����������
				if (dragPoint.x - pressPoint.x >= rectWidth
						&& dragPoint.x <= this.background.getWidth()) {
					if (dragPoint.y >= background.getHeight())
						return;
					pressPoint.x = dragPoint.x;
					pressPoint.y = dragPoint.y;

					int mapCol = dragPoint.y / DrawingTemplate.pixel;
					int mapRow = dragPoint.x / DrawingTemplate.pixel;

					if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
						if (paletteInfo.isBackground()) {
							this.insertTileToBack(mapCol, mapRow);
						} else
							this.insertTileToFore(mapCol, mapRow);
					}
				}
				// ��������
				if (pressPoint.x - dragPoint.x >= rectWidth
						&& pressPoint.x <= this.background.getWidth()) {
					if (pressPoint.y >= background.getHeight())
						return;
					pressPoint.x = dragPoint.x;
					pressPoint.y = dragPoint.y;

					int mapCol = dragPoint.y / DrawingTemplate.pixel;
					int mapRow = dragPoint.x / DrawingTemplate.pixel;

					if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
						if (paletteInfo.isBackground()) {
							this.insertTileToBack(mapCol, mapRow);
						} else
							this.insertTileToFore(mapCol, mapRow);
					}
				}
				// �Ʒ���
				if (dragPoint.y - pressPoint.y >= rectHeight
						&& dragPoint.y <= this.background.getHeight()) {
					if (dragPoint.x >= background.getWidth())
						return;
					pressPoint.x = dragPoint.x;
					pressPoint.y = dragPoint.y;

					int mapCol = dragPoint.y / DrawingTemplate.pixel;
					int mapRow = dragPoint.x / DrawingTemplate.pixel;

					if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
						if (paletteInfo.isBackground()) {
							this.insertTileToBack(mapCol, mapRow);
						} else
							this.insertTileToFore(mapCol, mapRow);
					}
				}
				// ����
				if (pressPoint.y - dragPoint.y >= rectHeight
						&& pressPoint.y <= this.background.getHeight()) {
					if (pressPoint.x >= background.getWidth())
						return;
					pressPoint.x = dragPoint.x;
					pressPoint.y = dragPoint.y;

					int mapCol = dragPoint.y / DrawingTemplate.pixel;
					int mapRow = dragPoint.x / DrawingTemplate.pixel;
					if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
						if (paletteInfo.isBackground()) {
							this.insertTileToBack(mapCol, mapRow);
						} else
							this.insertTileToFore(mapCol, mapRow);
					}
				}
			} // ������ �� ��
			else if (drawTool == PAINT_TOOL) {
				isDrag = true;
				dragPoint = e.getPoint();
				
				int sx = (pressPoint.x/DrawingTemplate.pixel)*DrawingTemplate.pixel;
				int sy = (pressPoint.y/DrawingTemplate.pixel)*DrawingTemplate.pixel;
				int ex = (dragPoint.x/DrawingTemplate.pixel)*DrawingTemplate.pixel;
				int ey = (dragPoint.y/DrawingTemplate.pixel)*DrawingTemplate.pixel;
				if (sx <= ex)
					ex += DrawingTemplate.pixel;
				else
					sx += DrawingTemplate.pixel;
				if (sy <= ey)
					ey += DrawingTemplate.pixel;
				else {
					sy += DrawingTemplate.pixel;
				}
				int w = Math.abs(ex - sx)/DrawingTemplate.pixel;
				int h = Math.abs(ey - sy)/DrawingTemplate.pixel;
				MainFrame.OWNER.setMainStateCenter("Canvas " + w +" x " + h + " block");
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		if (!MouseDrawUtility.checkMouseBoundery(e.getPoint(), mapSys))
			return;
		MainFrame.OWNER.setCoordinate(e.getPoint());
		if (EVENTMODE_FLAG) {
			if (isDragEvent) {
				endEventPoint = e.getPoint();
			}
		} else {
			mousePoint = e.getPoint();
			// ȭ�鿡 �ѷ��� �簢�� ����
			if (paletteInfo == null)
				return;
			this.rectHeight = this.paletteInfo.getRectHeight();
			this.rectWidth = this.paletteInfo.getRectWidth();
			if (rectHeight == 0) {
				rectHeight = DrawingTemplate.pixel;
				rectWidth = DrawingTemplate.pixel;
			}
			repaint();
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!MouseDrawUtility.checkMouseBoundery(e.getPoint(), mapSys))
			return;

		if (EVENTMODE_FLAG) {
			if (e.getClickCount() >= 2) {

				int col = startEventPoint.y / DrawingTemplate.pixel;
				int row = startEventPoint.x / DrawingTemplate.pixel;
				String filePath = MainFrame.OWNER.ProjectFullPath
						+ File.separator + "Event" + File.separator
						+ mapSys.getMapInfo().getM_MapName() + ".event";
				new EventDlg(MainFrame.OWNER, new Point(row, col), new Point(
						row, col), mapSys.getEventEditSys());

			}
		} else {

		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!MouseDrawUtility.checkMouseBoundery(e.getPoint(), mapSys))
			return;

		if (EVENTMODE_FLAG) {
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {// ����
				isDragEvent = true;
				startEventPoint = e.getPoint();
				endEventPoint = e.getPoint();
			}
		} else { // ĵ���� ���
			if (drawTool == STAMP_TOOL) {
				pressPoint = e.getPoint();
				int mapCol = mousePoint.y / DrawingTemplate.pixel;
				int mapRow = mousePoint.x / DrawingTemplate.pixel;

				if (this.paletteInfo == null)
					return;
				if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
					if (paletteInfo.isBackground()) {
						this.insertTileToBack(mapCol, mapRow);
					} else
						this.insertTileToFore(mapCol, mapRow);
				}
			} else if (drawTool == PAINT_TOOL) {
				pressPoint = e.getPoint();
				dragPoint = e.getPoint();
				MainFrame.OWNER.setMainStateCenter("Canvas 1 x 1 block");
			}
		}

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (EVENTMODE_FLAG) {
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				isDragEvent = false;
			} else { // ������
				if (!MouseDrawUtility.checkMouseBoundery(e.getPoint(), mapSys))
					return;
				if (e.isPopupTrigger()) {
					popupmenuEvent.show(e.getComponent(), e.getX() + 10,
							e.getY() + 10);
				}
			}
		} else { // ĵ���� ���
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				if (drawTool == STAMP_TOOL) {
					if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
						isDrag = false;
					} else {
						if (e.isPopupTrigger()) {
							popupmenuCanvas.show(e.getComponent(),
									e.getX() + 10, e.getY() + 10);
						}
					}
				} else if (drawTool == PAINT_TOOL) {
					isDrag = false;
					if (this.paletteInfo == null)
						return;
					if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) { // ����
						int sr = pressPoint.x / DrawingTemplate.pixel;
						int sc = pressPoint.y / DrawingTemplate.pixel;
						int er = dragPoint.x / DrawingTemplate.pixel;
						int ec = dragPoint.y / DrawingTemplate.pixel;
						int temp;
						if (sr > er) {
							temp = sr;
							sr = er;
							er = temp;
						}
						if (sc > ec) {
							temp = sc;
							sc = ec;
							ec = temp;
						}
						int rowLength = er - sr + 1;
						int colLength = ec - sc + 1;
						int widthBlock = paletteInfo.getRectWidth()
								/ DrawingTemplate.pixel;
						int heightBlock = paletteInfo.getRectHeight()
								/ DrawingTemplate.pixel;
						for (int i = 0; i < rowLength / widthBlock; i++) {
							for (int j = 0; j < colLength / heightBlock; j++) {
								if (paletteInfo.isBackground()) {
									this.insertTileToBack(sc + j * heightBlock,
											sr + i * widthBlock);
								} else {
									this.insertTileToFore(sc + j * heightBlock,
											sr + i * widthBlock);
								}
							}
						}
					}
				}
			} else {
				if (!MouseDrawUtility.checkMouseBoundery(e.getPoint(), mapSys))
					return;
				if (e.isPopupTrigger()) {
					popupmenuCanvas.show(e.getComponent(), e.getX() + 10,
							e.getY() + 10);
				}
			}
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// canvas mode popup
		if (e.getSource() == popupGrid) {
			MainFrame.OWNER.setCanvasGridMode(popupGrid.isSelected());
		} else if (e.getSource() == popupBgOnly) {
			if (popupBgOnly.isSelect()) {
				popupBgOnly.unselect();
				MainFrame.OWNER.setCanvasBackgroundOnlyMode(false);
			} else
				MainFrame.OWNER.setCanvasBackgroundOnlyMode(true);
		} else if (e.getSource() == popupFgOnly) {
			if (popupFgOnly.isSelect()) {
				popupFgOnly.unselect();
				MainFrame.OWNER.setCanvasForegroundOnlyMode(false);
			} else
				MainFrame.OWNER.setCanvasForegroundOnlyMode(true);
		} else if (e.getSource() == popupSemitransparent) {
			if (popupSemitransparent.isSelect()) {
				popupSemitransparent.unselect();
				MainFrame.OWNER.setCanvasSemitransparentMode(false);
			} else
				MainFrame.OWNER.setCanvasSemitransparentMode(true);

		} else if (e.getSource() == popupEventMode) {
			MainFrame.OWNER.setCanvasEventMode(true);
		} else if (e.getSource() == popupEventMode) {
			MainFrame.OWNER
					.syncBetweenCanvasTabsMode(MapIntegrateGUI.PAINT_TOOL);
		} else if (e.getSource() == popupEventMode) {
			MainFrame.OWNER
					.syncBetweenCanvasTabsMode(MapIntegrateGUI.STAMP_TOOL);
		}

		// Event Mode popup
		else if (e.getSource() == popupEventGrid) {
			MainFrame.OWNER.setCanvasGridMode(popupEventGrid.isSelected());
		} else if (e.getSource() == popupEventBgOnly) {
			if (popupEventBgOnly.isSelect()) {
				popupEventBgOnly.unselect();
				MainFrame.OWNER.setCanvasBackgroundOnlyMode(false);
			} else
				MainFrame.OWNER.setCanvasBackgroundOnlyMode(true);
		} else if (e.getSource() == popupEventFgOnly) {
			if (popupEventFgOnly.isSelect()) {
				popupEventFgOnly.unselect();
				MainFrame.OWNER.setCanvasForegroundOnlyMode(false);
			} else
				MainFrame.OWNER.setCanvasForegroundOnlyMode(true);
		} else if (e.getSource() == popupEventSemitransparent) {
			if (popupEventSemitransparent.isSelect()) {
				popupEventSemitransparent.unselect();
				MainFrame.OWNER.setCanvasSemitransparentMode(false);
			} else
				MainFrame.OWNER.setCanvasSemitransparentMode(true);
		}

		else if (e.getSource() == popupSetEvent) {
			startEventDlg();
		} else if (e.getSource() == popupDeleteEvent) {
			deleteEvent();
		} else if (e.getSource() == popupCopyEvent) {
			copyEvent();
		} else if (e.getSource() == popupPasteEvent) {
			pasteEvent();
		} else if (e.getSource() == popupSetStartingPointEvent) {
			int mapCol = startEventPoint.y / DrawingTemplate.pixel;
			int mapRow = startEventPoint.x / DrawingTemplate.pixel;

			new SetCharStartPointDlg(MainFrame.OWNER, mapSys.getMapInfo()
					.getM_MapName(), new Point(mapRow, mapCol));
		} else if (e.getSource() == popupCanvasMode) {
			MainFrame.OWNER.setCanvasEventMode(false);
		} else if (e.getSource() == popupPaintTool) {
			MainFrame.OWNER.syncBetweenCanvasTabsMode(PAINT_TOOL);
		} else if (e.getSource() == popupStampTool) {
			MainFrame.OWNER.syncBetweenCanvasTabsMode(STAMP_TOOL);
		}
	}

	public Point getStartEventPoint() {
		return startEventPoint;
	}

	public Point getEndEventPoint() {
		return endEventPoint;
	}

	public void startEventDlg() {
		int startRow = startEventPoint.x / DrawingTemplate.pixel;
		int startCol = startEventPoint.y / DrawingTemplate.pixel;
		int endRow = endEventPoint.x / DrawingTemplate.pixel;
		int endCol = endEventPoint.y / DrawingTemplate.pixel;

		new EventDlg(MainFrame.OWNER, new Point(startRow, startCol), new Point(
				endRow, endCol), mapSys.getEventEditSys());
	}

	public void deleteEvent() {
		new JOptionPane();
		if (JOptionPane.showConfirmDialog(this, "Really delete event?",
				"CLOSE", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE) == 0) {
			int startRow = startEventPoint.x / DrawingTemplate.pixel;
			int startCol = startEventPoint.y / DrawingTemplate.pixel;
			int endRow = endEventPoint.x / DrawingTemplate.pixel;
			int endCol = endEventPoint.y / DrawingTemplate.pixel;

			mapSys.getEventEditSys().deleteEvents(
					new Point(startRow, startCol), new Point(endRow, endCol));
		}
	}

	public void setCharactorStartingPoint() {
	}

	public void copyEvent() {
		int startRow = startEventPoint.x / DrawingTemplate.pixel;
		int startCol = startEventPoint.y / DrawingTemplate.pixel;
		int endRow = endEventPoint.x / DrawingTemplate.pixel;
		int endCol = endEventPoint.y / DrawingTemplate.pixel;

		mapSys.getEventEditSys().copyEvents(new Point(startRow, startCol),
				new Point(endRow, endCol));
		MainFrame.OWNER.getEventItem_paste().setEnabled(syncEventPasteBtn());
	}

	public void pasteEvent() {
		int startRow = startEventPoint.x / DrawingTemplate.pixel;
		int startCol = startEventPoint.y / DrawingTemplate.pixel;

		mapSys.getEventEditSys().pasteEvents(new Point(startRow, startCol));
	}

	// �̺�Ʈ �ٿ� �ֱ� ��ư�� ���� �ְų� ����
	// �־����� Ʈ�� �ƴϸ� �޽��� ����
	public boolean syncEventPasteBtn() {
		boolean b = mapSys.getEventEditSys().canPaste();
		popupPasteEvent.setEnabled(b);
		return b;
	}

	public void setDrawTool(int drawTool) {
		this.drawTool = drawTool;
		pressPoint.x = STARTING_POINT;
		pressPoint.y = STARTING_POINT;
		dragPoint.x = STARTING_POINT;
		dragPoint.y = STARTING_POINT;
	}

}