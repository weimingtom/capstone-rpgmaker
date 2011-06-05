package animationEditor;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import animationEditor.eceptions.IllegalImageIndex;
import characterEditor.exceptions.NullBufferedImage;
import editor.ObjectEditorSystem;

public class Animations extends ObjectEditorSystem implements Serializable {

	private static final long serialVersionUID = -2391408276822866276L;

	private final int START_INDEX = 0;
	private transient List<BufferedImage> bufImgList;
	private transient BufferedImage baseImg;
	private Point pointBaseImg;
	private List<Point> pointList;
	private List<ImageIcon> imgIconList;
	private ImageIcon baseImgIcon;
	private int callIndex;
	
	public Animations(String projectFullPath) {
		super(projectFullPath, "Animation");
		extension = "ani";
		
		this.name = "New_Animation";

		bufImgList = new ArrayList<BufferedImage>();
		pointBaseImg = new Point(0,0);
		pointList = new ArrayList<Point>();
		imgIconList = new ArrayList<ImageIcon>();
		
		callIndex = START_INDEX - 1;
	}
	
	public Animations(String name, String projectPath) {
		super(projectPath, "Animation");
		
		this.name = name;
		extension = "armor";

		bufImgList = new ArrayList<BufferedImage>();
		pointBaseImg = new Point(0,0);
		pointList = new ArrayList<Point>();
		imgIconList = new ArrayList<ImageIcon>();
		
		callIndex = START_INDEX - 1;
	}
	
	// fileName �� ������ �о�ͼ� �ش� index�� BufferedImage��  �����Ѵ�.
	public void setAnimationImage(String fileName, int index) throws NullBufferedImage {
		try {
			BufferedImage img = (BufferedImage) ImageIO.read(new File(fileName));
			bufImgList.add(index, img);
			pointList.add(new Point(img.getWidth()/2, img.getHeight()/2));
			imgIconList.add(index, toImageIcon(bufImgList.get(index)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// fileName �� ������ �о�ͼ� �ش� index�� BufferedImage��  �����Ѵ�.
	public void setAnimationImage(String fileName, int width, int height, int index) throws NullBufferedImage {
		try {
			BufferedImage img = (BufferedImage) ImageIO.read(new File(fileName));
			bufImgList.add(index, img);
			pointList.add(new Point(width, height));
			imgIconList.add(index, toImageIcon(bufImgList.get(index)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// img�� �ش� index�� BufferedImage��  �����Ѵ�.
	public void setAnimationImage(BufferedImage img, int index) throws NullBufferedImage {
		if (img == null) throw new NullBufferedImage("error: Animations.setImageList() (img=null)");

		bufImgList.add(index, img);
		pointList.add(new Point(img.getWidth()/2, img.getHeight()/2));
		imgIconList.add(index, toImageIcon(img));
	}
	
	// img�� �ش� index�� BufferedImage��  �����Ѵ�.
	public void setAnimationImage(BufferedImage img, int width, int height, int index) throws NullBufferedImage {
		if (img == null) throw new NullBufferedImage("error: Animations.setImageList() (img=null)");

		bufImgList.add(index, img);
		pointList.add(new Point(width, height));
		imgIconList.add(index, toImageIcon(img));
	}
	
	// �ش� �ε����� BufferedImage�� ImageIcon�� ����
	public void deleteAnimationImage(int index) throws IllegalImageIndex {
		if (index < 0 || index >= bufImgList.size()) throw new IllegalImageIndex("error: Animations.getImage() (index:" + index + ", countImage:" + bufImgList.size() + ")");
		
		bufImgList.remove(index);
		pointList.remove(index);
		imgIconList.remove(index);
	}
	
	// load �� �����͸� �ҷ����� ImageIcon���� ������ �Ǵµ�
	// ���� ���ӿ� ���Ǵ� BufferedImage���� �����ϱ� ���� ���ȴ�. 
	// BufferedImage�� ���� �� ImageIcon�� �ٽ� �ʱ�ȭ
	public void recoveryBufImg() {
		bufImgList = new ArrayList<BufferedImage>();
		for (int i = 0; i < imgIconList.size(); i++)
			bufImgList.add(i, toBufferedImage(imgIconList.get(i)));
		baseImg = toBufferedImage(baseImgIcon);
		
		imgIconList.clear();
	}
	
	// ���� �̹����� ��ȯ�Ѵ�. 0�� �̹����� ���̽��̹����̹Ƿ� ��ȯ���� �ʴ´�.
	public BufferedImage getNextImage() {
		callIndex = (callIndex+1) % (bufImgList.size() - START_INDEX);
		return bufImgList.get(callIndex);
	}
	
	
	// ���� callIndex�� ����Ű�� �̹����� ��ȯ�Ѵ�.
	public BufferedImage getCurrentImage()	{	return bufImgList.get(callIndex);	}
	
	// index��° �̹����� ��ȯ�Ѵ�.
	public BufferedImage getImage(int index) throws IllegalImageIndex {
		if (index < 0 || index >= bufImgList.size()) throw new IllegalImageIndex("error: Animations.getImage() (index:" + index + ", countImage:" + bufImgList.size() + ")");
		return bufImgList.get(index);
	}
	
	// �ִϸ��̼��� �ε����� �����Ѵ�. ���Ϸ� ����� �� �� ������ ����
	public void setIndexAnimation(int index) {
		if (index < 0) {
			System.err.println("error: Animations.setIndexAnimation() (indexAnimation<0)");
			index = 0;
		} else if (index > 999) {
			System.err.println("error: Animations.setIndexAnimation() (indexAnimation>999)");
			index = 999;
		}
		this.index = index;
	}

	public String getName()					{	return name;					}
	public void setName(String name)		{	this.name = name;				}
	public void resetCallIndex()			{	callIndex = START_INDEX - 1;	}
	public int getCountImage()				{	return bufImgList.size();		}
	public int getCallIndex()				{	return callIndex;				}
	public void setCallIndex(int callIndex)	{	this.callIndex = callIndex;		}
	public List<Point> getPointList()		{	return pointList;				}
	public int getPointX(int index)			{	return pointList.get(index).x;	}
	public int getPointY(int index)			{	return pointList.get(index).y;	}
	public void setPointBaseImg(int x, int y){	this.pointBaseImg.setLocation(x, y);	}
	public int getPointXBaseImg()			{	return pointBaseImg.x;			}
	public int getPointYBaseImg()			{	return pointBaseImg.y;			}
	public BufferedImage getBaseImage()			{	return baseImg;				}
	public List<BufferedImage> getBufImgList() 	{	return bufImgList;			}
	public int getCountImg()				{	return bufImgList.size();		}
	public void setBaseImage(BufferedImage img) {
		baseImg = img;
		baseImgIcon = toImageIcon(baseImg);
		pointBaseImg.setLocation(baseImg.getWidth()/2, baseImg.getHeight()/2);
	}
	public void setBaseImage(String fileName) {
		try {
			baseImg = (BufferedImage) ImageIO.read(new File(fileName));
			baseImgIcon = toImageIcon(baseImg);
			pointBaseImg.setLocation(baseImg.getWidth()/2, baseImg.getHeight()/2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ĳ������ ���� ��µǱ� ���ϴ� x��ǥ�� width�� �����ָ� ���� ��� ��ǥ�� ��ȯ�ȴ�. 
	public int convertPaintPointX(int width, int imgIndex)	{	return width - pointList.get(imgIndex).x;	}
	// ĳ������ ���� ��µǱ� ���ϴ� y��ǥ�� width�� �����ָ� ���� ��� ��ǥ�� ��ȯ�ȴ�. 
	public int convertPaintPointY(int height, int imgIndex)	{	return height - pointList.get(imgIndex).y;	}

	// ImageIcon ��ü�� BufferedImage �� ��ȯ�Ѵ�. load �� ���
	private BufferedImage toBufferedImage(ImageIcon im) { // �߰��� �޼ҵ�
		BufferedImage bi = new BufferedImage(im.getImage().getWidth(null),
				im.getImage().getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im.getImage(), 0, 0, null);
		bg.dispose();
		return bi;
	}

	// BufferedImage ��ü�� ImageIcon �� ��ȯ�Ѵ�. save �� ���
	private ImageIcon toImageIcon(BufferedImage bufferedImage) { // �߰��� �޼ҵ�
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource()));
	}
	
	public void clearAllList() {
		bufImgList.clear();
		pointList.clear();
		imgIconList.clear();
	}
	
	@Override
	protected void copyObject(ObjectEditorSystem data) {
		Animations tmpData = (Animations)data;
		
		this.bufImgList = tmpData.bufImgList;
		this.baseImg = tmpData.baseImg;
		this.pointList = tmpData.pointList;
		this.pointBaseImg = tmpData.pointBaseImg;
		this.imgIconList = tmpData.imgIconList;
		this.baseImgIcon = tmpData.baseImgIcon;
		this.callIndex = tmpData.callIndex;
		
		bufImgList = new ArrayList<BufferedImage>();
		this.recoveryBufImg();
	}
}
