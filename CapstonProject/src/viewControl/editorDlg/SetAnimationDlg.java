package viewControl.editorDlg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import viewControl.MainFrame;
import animationEditor.Animations;
import animationEditor.eceptions.IllegalImageIndex;
import characterEditor.exceptions.NullBufferedImage;

public class SetAnimationDlg extends EditorDlg implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	// Variables declaration - do not modify
	private JButton btn_OK;
	private JButton btn_cancel;
	private JButton btn_loadBaseImg;
	private JButton btn_loadTmpImg;
	private JButton btn_insertImg;
	private JButton btn_deleteImg;
	private JButton btn_setPoint;
	private JButton btn_resetPoint;
	private JButton btn_playAnimation;
	private JButton btn_stopAnimation;
	private JComboBox cb_selectedImg;
	private JTextField tf_name;
	private JLabel l_name;
	private JLabel l_baseImg;
	private JLabel l_aniImgList;
	private JLabel l_listSetter;
	private JLabel l_selectIndex;
	private JLabel l_paintPointSetter;
	private JLabel l_animationPlayer;
	private AniImgPanel p_baseImg;
	private AniImgPanel p_playAnimation;
	private AniImgPanel p_pointSetImg;
	private AniImgPanel p_tmpImg;
	private JScrollPane sp_imgList;
	private JSeparator jSeparator1;
	// End of variables declaration

	
	private Animations animation; 			// ���޹��� Animations ������ �����ϱ� ���� �ʿ�
	private List<AniImgPanel> imgPanelList; // ������ �̹������� �ӽ÷� �����ϴ� ����Ʈ
	private String str_fileName; 			// �߰��ϰ��� �ϴ� �̹����� ���� ��θ� �ӽ� ����
	private int indexSelectedPanel;
	
	
	private Timer timer;			// �ִϸ��̼� ����� ���� Ÿ�̸�
	ActionListener actionListener;	// �ִϸ��̼� ����� ���� �׼�
	private int animationIndex;		// �ִϸ��̼� ����� ��µǴ� �̹����� �ε����� �����Ѵ�.
	private int countBaseImg;		// �ִϸ��̼� ����� 1ȸ ��� �� ���̽� �̹����� 3ȸ ��µǵ��� �Ѵ�.

 	public SetAnimationDlg(MainFrame parent, Animations data) {
		super(parent, "Set Animation");
		projectPath = MainFrame.OWNER.ProjectFullPath;
		animation = data;
		str_fileName = null;
		indexSelectedPanel = 0;
		animationIndex = -1;
		countBaseImg = 0;
		
		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animationIndex++;
				
				if (animationIndex >= imgPanelList.size()) {
					loadPanel(p_playAnimation, p_baseImg);
					p_playAnimation.revalidate();
					repaint();
					
					// 1ȸ ��� �� ���̽� �̹��� 3ȸ ���
					countBaseImg++;
					if(countBaseImg < 3)
						animationIndex--;
					else {
						countBaseImg = 0;
						animationIndex %= imgPanelList.size();
						animationIndex--;
					}
					
				} else {
					loadPanel(p_playAnimation, imgPanelList.get(animationIndex));
					p_playAnimation.revalidate();
					repaint();
				}
			}
		};
		timer = new Timer(300, actionListener);

		Dimension d = new Dimension(416, 323);
		setMaximumSize(d);
		setMinimumSize(d);
		setSize(d);
		initComponents();
		setResizable(false);
		setVisible(true);
		setModal(true);
	}

	private void initComponents() {
		// Component ����
		btn_OK = new JButton("O K");
		btn_cancel = new JButton("Cancel");
		btn_loadBaseImg = new JButton("Load Image");
		btn_loadTmpImg = new JButton("Load Image");
		btn_insertImg = new JButton("Insert Image");
		btn_deleteImg = new JButton("Delete Image");
		btn_setPoint = new JButton("Set Point");
		btn_resetPoint = new JButton("Reset Point");
		btn_playAnimation = new JButton("Play Animation");
		btn_stopAnimation = new JButton("Stop Animation");
		tf_name = new JTextField(animation.getName());
		l_name = new JLabel("Animation Name:");
		l_baseImg = new JLabel("Base Image");
		l_aniImgList = new JLabel("Animation Image List");
		l_listSetter = new JLabel("List Setter");
		l_selectIndex = new JLabel("Index");
		l_paintPointSetter = new JLabel("Print Point Setter");
		l_animationPlayer = new JLabel("Animation Player");
		p_tmpImg = new AniImgPanel(new BufferedImage(24, 32, BufferedImage.TYPE_3BYTE_BGR), AniImgPanel.TEMP_IMG_PANEL, false);
		try {
			p_baseImg = new AniImgPanel(animation.getBaseImage(), AniImgPanel.BASE_IMG_PANEL, false);
			p_baseImg.setPaintPointX(animation.getPointXBaseImg());
			p_baseImg.setPaintPointY(animation.getPointYBaseImg());
			p_pointSetImg = new AniImgPanel(animation.getImage(0), AniImgPanel.POINT_SET_PANEL, false);
			p_playAnimation = new AniImgPanel(animation.getImage(0), AniImgPanel.PLAY_ANIMATION_PANEL, false);
		} catch (IllegalImageIndex e2) {
			e2.printStackTrace();
		}
		sp_imgList = new JScrollPane();
		jSeparator1 = new JSeparator();

		// ��ư�� �̺�Ʈ�� ����
		btn_OK.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_loadBaseImg.addActionListener(this);
		btn_loadTmpImg.addActionListener(this);
		btn_insertImg.addActionListener(this);
		btn_deleteImg.addActionListener(this);
		btn_setPoint.addActionListener(this);
		btn_resetPoint.addActionListener(this);
		btn_playAnimation.addActionListener(this);
		btn_stopAnimation.addActionListener(this);

		// �̹��� ����Ʈ�� animation�� �̹����� �ʱ�ȭ
		try {
			imgPanelList = new LinkedList<AniImgPanel>();
			// List�� �г��� �����Ѵ�. Add ��ư�� ������ �̹����� ���� �г��� �߰��ȴ�.
			for (int i = 0; i < animation.getCountImage(); i++) {
				AniImgPanel insertPanel = new AniImgPanel(animation.getImage(i), AniImgPanel.LIST_IMG_PANEL, true);
				insertPanel.setPaintPointX(animation.getPointX(i));
				insertPanel.setPaintPointY(animation.getPointY(i));
				imgPanelList.add(insertPanel);
			}
		} catch (IllegalImageIndex e) {
			e.printStackTrace();
		}
		cb_selectedImg = new JComboBox();
		setComboBoxImgPanelIndex();

		// �ݺ� ���� �ҽ��̹Ƿ� ���� ����
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		sp_imgList.createHorizontalScrollBar();
		sp_imgList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// AniImgPanel�� ���� ����
		p_baseImg.setBackground(new Color(255, 255, 255));
		p_baseImg.setBorder(BorderFactory.createEtchedBorder());
		p_baseImg.setAutoscrolls(true);
		
		p_tmpImg.setBackground(new Color(255, 255, 255));
		p_tmpImg.setBorder(BorderFactory.createEtchedBorder());
		p_tmpImg.setAutoscrolls(true);
		
		p_pointSetImg.setBackground(new java.awt.Color(255, 255, 255));
		p_pointSetImg.setBorder(BorderFactory.createEtchedBorder());
		p_pointSetImg.setAutoscrolls(true);

		p_playAnimation.setBackground(new java.awt.Color(255, 255, 255));
		p_playAnimation.setBorder(BorderFactory.createEtchedBorder());
		p_pointSetImg.setAutoscrolls(true);
		
		// AniImgPanel�� Layout ����
		GroupLayout p_baseImgLayout = new GroupLayout(p_baseImg);
		p_baseImg.setLayout(p_baseImgLayout);
		p_baseImgLayout.setHorizontalGroup(
			p_baseImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 106, Short.MAX_VALUE)
		);
		p_baseImgLayout.setVerticalGroup(
			p_baseImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 100, Short.MAX_VALUE)
		);
		
		GroupLayout p_tmpImgLayout = new GroupLayout(p_tmpImg);
		p_tmpImg.setLayout(p_tmpImgLayout);
		p_tmpImgLayout.setHorizontalGroup(
			p_tmpImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 100, Short.MAX_VALUE)
		);
		p_tmpImgLayout.setVerticalGroup(
			p_tmpImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 108, Short.MAX_VALUE)
		);
		
		GroupLayout p_pointSetImgLayout = new GroupLayout(p_pointSetImg);
		p_pointSetImg.setLayout(p_pointSetImgLayout);
		p_pointSetImgLayout.setHorizontalGroup(
			p_pointSetImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 100, Short.MAX_VALUE)
		);
		p_pointSetImgLayout.setVerticalGroup(
			p_pointSetImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 108, Short.MAX_VALUE)
		);
		
		GroupLayout p_playAnimationLayout = new GroupLayout(p_playAnimation);
		p_playAnimation.setLayout(p_playAnimationLayout);
		p_playAnimationLayout.setHorizontalGroup(
			p_playAnimationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 100, Short.MAX_VALUE)
		);
		p_playAnimationLayout.setVerticalGroup(
			p_playAnimationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 108, Short.MAX_VALUE)
		);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jSeparator1, GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
					.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
							.addComponent(btn_loadBaseImg, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(p_baseImg, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(l_baseImg, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(l_name, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(sp_imgList, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
							.addComponent(l_aniImgList, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
							.addComponent(tf_name, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)))
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(l_listSetter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(p_tmpImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(btn_insertImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup()
								.addComponent(l_selectIndex)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(cb_selectedImg, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addComponent(btn_loadTmpImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btn_deleteImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(l_paintPointSetter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(p_pointSetImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(btn_setPoint, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btn_resetPoint, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(l_animationPlayer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btn_OK, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(p_playAnimation, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(btn_cancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btn_playAnimation, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btn_stopAnimation, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(l_name)
					.addComponent(tf_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(l_baseImg)
					.addComponent(l_aniImgList))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
					.addComponent(sp_imgList)
					.addComponent(p_baseImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(btn_loadBaseImg)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(l_listSetter)
					.addComponent(l_paintPointSetter)
					.addComponent(l_animationPlayer))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
					.addComponent(p_pointSetImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(l_selectIndex)
							.addComponent(cb_selectedImg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_loadTmpImg)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_insertImg)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_deleteImg))
					.addComponent(p_tmpImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(layout.createSequentialGroup()
						.addComponent(btn_setPoint)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_resetPoint))
					.addGroup(layout.createSequentialGroup()
						.addComponent(btn_playAnimation)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_stopAnimation))
					.addComponent(p_playAnimation, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_OK)
					.addComponent(btn_cancel))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
		// ���̾ƿ� ���� ��

		// sp_imgList�� �̹��� �г� ���� �� ���
		printPanelList(sp_imgList, imgPanelList);
		
		// ���콺 �̺�Ʈ �߰�
		p_baseImg.addMouseListener(this);
		p_pointSetImg.addMouseListener(this);
		for (int i = 0; i < imgPanelList.size(); i++) {
			imgPanelList.get(i).addMouseListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_OK) {
			// �ִϸ��̼��� �̸��� ��ĭ�̸� ������ ����Ѵ�. â ���� ����.
			if (tf_name.getText().compareTo("") != 0) {
				// �̸� ����
				animation.setName(tf_name.getText());

				// ���̽� �̹��� �гΰ� ����Ʈ ���� �гο� �ִ� �̹����� animation�� �����Ѵ�.
				// �����ϱ� ���� animation�� �̹��� ����Ʈ�� �ʱ�ȭ�Ѵ�.
				animation.clearAllList();
				animation.setBaseImage(p_baseImg.getPrintImg());
				animation.setPointBaseImg(p_baseImg.getPaintPointX(), p_baseImg.getPaintPointY());
				for (int i = 0; i < imgPanelList.size(); i++) {
					try {
						animation.setAnimationImage(imgPanelList.get(i).getPrintImg(), imgPanelList.get(i).getPaintPointX(), imgPanelList.get(i).getPaintPointY(), i);
					} catch (NullBufferedImage e1) {
						e1.printStackTrace();
					}
				}

				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Not Exist Object Name!", "Waring", JOptionPane.WARNING_MESSAGE);
			}

		} else if (e.getSource() == btn_cancel) {
			dispose();

		} else if (e.getSource() == btn_loadBaseImg) {
			// ������ �̹����� ��θ� ������ �ӽ� �����Ѵ�.
			// ���� ��θ� �ޱ� ���� Chooser ����
			JFileChooser fileChooser = new JFileChooser(MainFrame.OWNER.ProjectFullPath);

			// Chooser�� ���� ����
			fileChooser.setDialogTitle("Image File Chooser");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);

			// Chooser�� ���� ����
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Image File", "png");
//																						   "PNG",
//																						   "Png",
//																						   "pNg",
//																						   "pnG",
//																						   "PNg",
//																						   "PnG",
//																						   "pNG");
			fileChooser.setFileFilter(filter);

			// Chooser�� ����Ѵ�. ���� ���ΰ� returnName�� ����
			int returnName = fileChooser.showOpenDialog(owner);
			if (returnName == JFileChooser.APPROVE_OPTION) {
				// ������ ���õǾ����� �ӽ� �̹��� �гο� ����� �����Ѵ�.
				try {
					String tmpBaseImg = fileChooser.getSelectedFile().getCanonicalPath();
					p_baseImg.setPrintImg(tmpBaseImg);
					p_baseImg.repaint();
					this.repaint();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} else if (e.getSource() == btn_loadTmpImg) {
			// ������ �̹����� ��θ� ������ �ӽ� �����Ѵ�.
			// ���� ��θ� �ޱ� ���� Chooser ����
			JFileChooser fileChooser = new JFileChooser(MainFrame.OWNER.ProjectFullPath);

			// Chooser�� ���� ����
			fileChooser.setDialogTitle("Image File Chooser");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);

			// Chooser�� ���� ����
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Image File", "png");
			fileChooser.setFileFilter(filter);

			// Chooser�� ����Ѵ�. ���� ���ΰ� returnName�� ����
			int returnName = fileChooser.showOpenDialog(owner);
			if (returnName == JFileChooser.APPROVE_OPTION) {
				// ������ ���õǾ����� �ӽ� �̹��� �гο� ����� �����Ѵ�.
				try {
					str_fileName = fileChooser.getSelectedFile().getCanonicalPath();
					p_tmpImg.setPrintImg(str_fileName);
					p_tmpImg.repaint();
					this.repaint();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} else if (e.getSource() == btn_insertImg) {
			if (tf_name.getText() != null) {
				AniImgPanel insertPanel = new AniImgPanel(str_fileName, AniImgPanel.LIST_IMG_PANEL, true);
				imgPanelList.add(cb_selectedImg.getSelectedIndex(), insertPanel);
				
				// cb_selectedImg �缳��
				setComboBoxImgPanelIndex();
				cb_selectedImg.revalidate();
				
				// JScrollPane �����
				printPanelList(sp_imgList, imgPanelList);
				
				// ���� ������ �г��� �̺�Ʈ ����
				insertPanel.addMouseListener(this);
				
				// �ش� �ε����� �г��� ����Ǿ����Ƿ� p_pointSetImg�� �̹����� �����Ѵ�.
				loadPanel(p_pointSetImg, insertPanel);
				p_pointSetImg.revalidate();
			}
		}  else if (e.getSource() == btn_deleteImg) {
			if(cb_selectedImg.getSelectedIndex() < imgPanelList.size()) {
				// �ش� �г��� �̺�Ʈ ����
				imgPanelList.get(cb_selectedImg.getSelectedIndex()).removeMouseListener(this);
				
				// �ش� �г� ����
				imgPanelList.remove(cb_selectedImg.getSelectedIndex());
	
				// cb_selectedImg �缳��
				setComboBoxImgPanelIndex();
				cb_selectedImg.revalidate();
				
				// JScrollPane �����
				printPanelList(sp_imgList, imgPanelList);
				
				// �ش� �ε����� �г��� ����Ǿ����Ƿ� p_pointSetImg�� �̹����� �����Ѵ�.
				if(cb_selectedImg.getSelectedIndex() != imgPanelList.size())
					loadPanel(p_pointSetImg, imgPanelList.get(cb_selectedImg.getSelectedIndex()));
				else
					loadPanel(p_pointSetImg, imgPanelList.get(cb_selectedImg.getSelectedIndex()-1));
				p_pointSetImg.revalidate();
			}
		}  else if (e.getSource() == btn_setPoint) {
			if(indexSelectedPanel !=-1)
				imgPanelList.get(indexSelectedPanel).getMousePoint().setLocation(p_pointSetImg.getPaintPointX(), p_pointSetImg.getPaintPointY());
			else
				p_baseImg.getMousePoint().setLocation(p_pointSetImg.getPaintPointX(), p_pointSetImg.getPaintPointY());
			
		}  else if (e.getSource() == btn_resetPoint) {
			if(indexSelectedPanel !=-1) {
				p_pointSetImg.setPaintPointX(imgPanelList.get(indexSelectedPanel).getPrintImg().getWidth()/2);
				p_pointSetImg.setPaintPointY(imgPanelList.get(indexSelectedPanel).getPrintImg().getHeight()/2);
				imgPanelList.get(indexSelectedPanel).getMousePoint().setLocation(p_pointSetImg.getPaintPointX(), p_pointSetImg.getPaintPointY());
				
				imgPanelList.get(indexSelectedPanel).revalidate();
				imgPanelList.get(indexSelectedPanel).repaint();
				sp_imgList.revalidate();
			} else {
				p_pointSetImg.setPaintPointX(p_baseImg.getPrintImg().getWidth()/2);
				p_pointSetImg.setPaintPointY(p_baseImg.getPrintImg().getHeight()/2);
				p_baseImg.getMousePoint().setLocation(p_pointSetImg.getPaintPointX(), p_pointSetImg.getPaintPointY());
				
				p_baseImg.revalidate();
			}
			this.repaint();
			
		} else if (e.getSource() == btn_playAnimation) {
			timer.stop();
			animationIndex = 0;
			timer.start();
			
		} else if (e.getSource() == btn_stopAnimation) {
			timer.stop();
			
			// ù ���������� ���ư���.
			animationIndex = 0;
			loadPanel(p_playAnimation, imgPanelList.get(0));
			p_playAnimation.revalidate();
		}

	}

	// scrollPane�� panelList ���� �г��� �����Ͽ� ����Ѵ�.
	private void printPanelList(JScrollPane scrollPane, List<AniImgPanel> panelList) {
		// �г� �ϳ��� �����ϰ� �� �гο� �̹����� �����Ѵ�.
		// �г��� ũ��� ���� ū �̹����� �ʺ� * ����
		int width = panelList.size() * 5;
		int maxHeight = 0;
		for (int i = 0; i < panelList.size(); i++) {
			width += panelList.get(i).getWidth();
			if (maxHeight < panelList.get(i).getHeight())
				maxHeight = panelList.get(i).getHeight() + 10;
		}

		Dimension d = new Dimension(width, maxHeight);
		JPanel p_imgList = new JPanel();
		p_imgList.setMaximumSize(d);
		p_imgList.setMinimumSize(d);
		p_imgList.setSize(d);

		for (int i = 0; i < panelList.size(); i++)
			addItem(p_imgList, panelList.get(i), i, 0, 1, 1, GridBagConstraints.CENTER);

		// ��ũ�ѹ��� ����Ʈ�� ����
		scrollPane.getViewport().setView(p_imgList);

		// ������ scrollPane�� ���� ���
		p_imgList.revalidate();
		scrollPane.revalidate();
		this.repaint();
	}
	
	// �̹����� �����ϱ� ���� �޺��ڽ��� ���Ҹ� �־� ���� �� ��ȯ�Ѵ�.
	private void setComboBoxImgPanelIndex() {
		int selectedImdex = cb_selectedImg.getSelectedIndex();
		cb_selectedImg.removeAllItems();
		for (int i = 0; i < imgPanelList.size()+1; i++) {
			String index = "" + (i+1);
			cb_selectedImg.addItem(index);
		}
		if(selectedImdex != -1)
			cb_selectedImg.setSelectedIndex(selectedImdex);
		else
			cb_selectedImg.setSelectedIndex(0);
	}
	
	// p_pointSetImg�� imgPanel�� ������ ��ü
	private void loadPanel(AniImgPanel objPanel, AniImgPanel imgPanel) {
		objPanel.setPrintImg(imgPanel.getPrintImg());
		objPanel.setPaintPointX(imgPanel.getPaintPointX());
		objPanel.setPaintPointY(imgPanel.getPaintPointY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == p_pointSetImg) {
			// Ŭ���� ��ǥ�� p_pointSetImg�� �Ѱ��ָ� �гο� Ŭ���� ��ǥ�� ǥ���ϰ� ��ǥ�� �����Ѵ�.
			p_pointSetImg.mouseClicked(e.getPoint());
			
			p_pointSetImg.revalidate();
			p_pointSetImg.repaint();
			
		} else if(e.getSource() == p_baseImg) {
			if(indexSelectedPanel!=-1)
				imgPanelList.get(indexSelectedPanel).setBackground(new Color(255, 255, 255));
			loadPanel(p_pointSetImg, p_baseImg);
			indexSelectedPanel = -1;
		} else {
			for (int i = 0; i < imgPanelList.size(); i++) {
				// Ŭ���� �г��̸� ���õ� �гη� �����ϰ�
				// �׷��� ���� �г��̸� ���õ��� ���� �гη� �����Ѵ�.
				if(e.getSource() == imgPanelList.get(i)) {
					// p_pointSetImg�� �̹����� ���� ��ǥ�� �����Ѵ�.
					p_pointSetImg.setPrintImg(imgPanelList.get(i).getPrintImg());
					p_pointSetImg.setPaintPointX(imgPanelList.get(i).getMousePoint().x);
					p_pointSetImg.setPaintPointY(imgPanelList.get(i).getMousePoint().y);
					
					// imgPanelList�� ���õ� ���� ǥ���Ѵ�.
					imgPanelList.get(i).setBackground(new Color(200, 200, 200));
					indexSelectedPanel = i;
				} else
					imgPanelList.get(i).setBackground(new Color(255, 255, 255));
				
				// �гθ� �ٽ� ����Ѵ�.
				imgPanelList.get(i).revalidate();
			}
			
			sp_imgList.revalidate();
		}
		
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
