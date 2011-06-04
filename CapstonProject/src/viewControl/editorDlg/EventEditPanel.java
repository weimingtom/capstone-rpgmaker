package viewControl.editorDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

import viewControl.MainFrame;
import viewControl.editorDlg.eventContentDlg.ChangeBGMDlg;
import viewControl.editorDlg.eventContentDlg.ChangeFlagDlg;
import viewControl.editorDlg.eventContentDlg.DialogEventDlg;
import viewControl.editorDlg.eventContentDlg.EventContentDlg;
import viewControl.editorDlg.eventContentDlg.MotionEventDlg;
import viewControl.editorDlg.eventContentDlg.NextMapDstDlg;
import viewControl.editorDlg.eventContentDlg.SwitchDialogDlg;
import characterEditor.Actors;
import characterEditor.MonsterEditorSystem;
import characterEditor.NPCEditorSystem;
import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.FlagList;
import eventEditor.eventContents.ChangeBGMEvent;
import eventEditor.eventContents.ChangeFlagEvent;
import eventEditor.eventContents.ChangeMapEvent;
import eventEditor.eventContents.DialogEvent;
import eventEditor.eventContents.EventContent;
import eventEditor.eventContents.MotionEvent;
import eventEditor.eventContents.SwitchDialogEvent;

public class EventEditPanel extends JPanel implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private MainFrame owner;
	private Event event;
	private int objectType;
	private DefaultListModel listModel; 
	
	// Variables declaration - do not modify
	private JButton btn_insertEventContest;
	private JButton btn_deleteEventContent;
	private JButton btn_moveUpEventContent;
	private JButton btn_moveDownEventContent;
	private ButtonGroup btng_startType;
	private JComboBox cb_actorIndex;
	private JComboBox cb_actorMotionType;
	private JComboBox cb_condition1;
	private JComboBox cb_condition2;
	private JComboBox cb_condition3;
	private JCheckBox ckb_condition1;
	private JCheckBox ckb_condition2;
	private JCheckBox ckb_condition3;
	private JCheckBox ckb_ifDie;
	private JComboBox cb_dieCondition;
	private JComboBox cb_dieConditionState;
	private JComboBox cb_selectedEventContent;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JScrollPane sp_eventListPanel;
	private JList lst_eventList;
	private JPanel p_activeCondition;
	private JPanel p_actor;
	private AniImgPanel p_actorImg;
	private JPanel p_startType;
	private JRadioButton rbtn_aboveTile;
	private JRadioButton rbtn_autoStart;
	private JRadioButton rbtn_contactPlayer;
	private JRadioButton rbtn_parallelStart;
	private JRadioButton rbtn_pressButton;
	// End of variables declaration
	
	public EventEditPanel(MainFrame parent, Event event, int objectType) {
		this.owner = parent;
		this.event = event;
		this.objectType = objectType;
		this.listModel = new DefaultListModel();
		
		initComponents();
	}
	
	private void initComponents() {
		// ���� �ʱ�ȭ
		btn_insertEventContest = new JButton("Insert");
		btn_deleteEventContent = new JButton("Delete");
		btn_moveUpEventContent = new JButton("��");
		btn_moveDownEventContent = new JButton("��");
		btng_startType = new ButtonGroup();
		rbtn_pressButton = new JRadioButton("Press Button");
		rbtn_contactPlayer = new JRadioButton("Contact with Player");
		rbtn_aboveTile = new JRadioButton("Above Event Tile");
		rbtn_autoStart = new JRadioButton("Auto Start");
		rbtn_parallelStart = new JRadioButton("Parallel Start");
		p_activeCondition = new JPanel();
		p_actor = new JPanel();
		p_actorImg = new AniImgPanel(new BufferedImage(24, 32, BufferedImage.TYPE_3BYTE_BGR), AniImgPanel.LIST_IMG_PANEL, false);
		p_startType = new JPanel();
		ckb_condition1 = new JCheckBox("Condition1");
		ckb_condition2 = new JCheckBox("Condition2");
		ckb_condition3 = new JCheckBox("Condition3");
		ckb_ifDie = new JCheckBox("If this actor die");
		cb_dieCondition = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		cb_dieConditionState = new JComboBox(new DefaultComboBoxModel(new String[] { "True", "False" }));
		cb_actorIndex = new JComboBox(new DefaultComboBoxModel(new String[] { "None" }));
		cb_actorMotionType = new JComboBox(new DefaultComboBoxModel(new String[] { "Not Motion", "Random Motion", "Come to Player", "Attack Player", "Attack Enemy" }));
		sp_eventListPanel = new JScrollPane();
		lst_eventList = new JList();
		lst_eventList.setModel(listModel);
		sp_eventListPanel.setViewportView(lst_eventList);
		jLabel1 = new JLabel("Content Index");
		jLabel2 = new JLabel("Actor Index");
		jLabel3 = new JLabel("Actor Motion Type");
		jLabel4 = new JLabel("Event Contents");
		jLabel5 = new JLabel("=");
		renewConditionComboBox();	// cb_condition#�� �ֽ� Flag Name���� ����
		renewSelectedListComboBox();// cb_selectedEventContent�� �ֽ� ����Ʈ ������� ����
		
		rbtn_parallelStart.setEnabled(false);
		
		p_activeCondition.setBorder(BorderFactory.createTitledBorder("Active Conditions"));
		p_actor.setBorder(BorderFactory.createTitledBorder("Actor"));
		p_actorImg.setBackground(new java.awt.Color(255, 255, 255));
		p_actorImg.setBorder(BorderFactory.createEtchedBorder());
		p_startType.setBorder(BorderFactory.createTitledBorder("Event Start Type"));
		
		btng_startType.add(rbtn_pressButton);
		btng_startType.add(rbtn_contactPlayer);
		btng_startType.add(rbtn_aboveTile);
		btng_startType.add(rbtn_autoStart);
		btng_startType.add(rbtn_parallelStart);
		
		// �׼� �̺�Ʈ
		btn_moveUpEventContent.addActionListener(this);
		btn_moveDownEventContent.addActionListener(this);
		ckb_condition1.addActionListener(this);
		ckb_condition2.addActionListener(this);
		ckb_condition3.addActionListener(this);
		ckb_ifDie.addActionListener(this);
		cb_actorIndex.addActionListener(this);
		btn_insertEventContest.addActionListener(this);
		btn_deleteEventContent.addActionListener(this);
		cb_selectedEventContent.addActionListener(this);
		
		// ���콺 �̺�Ʈ
		ckb_condition1.addMouseListener(this);
		ckb_condition2.addMouseListener(this);
		ckb_condition3.addMouseListener(this);
		ckb_ifDie.addActionListener(this);
		cb_actorIndex.addMouseListener(this);
		
		// JList�� ���콺 �̺�Ʈ ����
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == lst_eventList) {
					if (e.getClickCount() == 1) {
						// Ŭ���� content�� ���õǵ��� �Ѵ�.
						int index = lst_eventList.getSelectedIndex();
						setSelectedIndexJList(index);
						
					} else if (e.getClickCount() >= 2) {
						// ������ �̺�Ʈ�� index�� ��´�.
						int index = lst_eventList.getSelectedIndex();
						if(index == -1)	{
							// ������ �������� �߰�
							index = listModel.getSize();
							int size = lst_eventList.getComponentCount();
							new EventContentDlg(owner, event, index);
							// JList �����
							setJListEventContents();
							renewSelectedListComboBox();
							
							if(size < lst_eventList.getComponentCount())
								setSelectedIndexJList(index+1);
							else
								setSelectedIndexJList(index);
						} else {
							// ���õ� �����͸� �о ���̾�α׿��� �����ϵ��� �Ѵ�.
							int size = lst_eventList.getComponentCount();
							createEditEventContentDlg(event, index);
							// JList �����
							setJListEventContents();
							renewSelectedListComboBox();
							
							if(size < lst_eventList.getComponentCount())
								setSelectedIndexJList(index+1);
							else
								setSelectedIndexJList(index);
						}
					}
				}
			}
		};
		lst_eventList.addMouseListener(mouseListener);
		
		// �г� ���� �� ������ ����
		setSelectedIndexCondition();
		int tmpObjectType = objectType;
		objectType = -1;
		renewActorMenu(tmpObjectType);
//		setSelectedIndexActorMenu();
		setEventStartType();
		lst_eventList.setSelectedIndex(0);
		
		if(event.getDieChangeConditionIndex() != -1)
			ckb_ifDie.setSelected(true);
		else
			ckb_ifDie.setSelected(false);
		renewDieCondition();
		
		if(event.getDieChangeConditionIndex() != -1) {
			cb_dieCondition.setSelectedIndex(event.getDieChangeConditionIndex());
			cb_dieConditionState.setSelectedIndex(event.getDieChangeComditionState()?0:1);
		}
		
		// Event Content�� JList ����
		setJListEventContents();
		renewSelectedListComboBox();
		
		lst_eventList.setSelectedIndex(0);
		
		// ���̾ƿ� ����
		javax.swing.GroupLayout p_activeConditionLayout = new javax.swing.GroupLayout(p_activeCondition);
        p_activeCondition.setLayout(p_activeConditionLayout);
        p_activeConditionLayout.setHorizontalGroup(
            p_activeConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_activeConditionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_activeConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_activeConditionLayout.createSequentialGroup()
                        .addComponent(ckb_condition1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_condition1, 0, 219, Short.MAX_VALUE))
                    .addGroup(p_activeConditionLayout.createSequentialGroup()
                        .addComponent(ckb_condition2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_condition2, 0, 219, Short.MAX_VALUE))
                    .addGroup(p_activeConditionLayout.createSequentialGroup()
                        .addComponent(ckb_condition3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_condition3, 0, 219, Short.MAX_VALUE)))
                .addContainerGap())
        );
        p_activeConditionLayout.setVerticalGroup(
            p_activeConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_activeConditionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_activeConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckb_condition1)
                    .addComponent(cb_condition1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(p_activeConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckb_condition2)
                    .addComponent(cb_condition2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(p_activeConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckb_condition3)
                    .addComponent(cb_condition3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout p_actorImgLayout = new javax.swing.GroupLayout(p_actorImg);
        p_actorImg.setLayout(p_actorImgLayout);
        p_actorImgLayout.setHorizontalGroup(
            p_actorImgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );
        p_actorImgLayout.setVerticalGroup(
            p_actorImgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout p_actorLayout = new javax.swing.GroupLayout(p_actor);
        p_actor.setLayout(p_actorLayout);
        p_actorLayout.setHorizontalGroup(
            p_actorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_actorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_actorImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_actorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_actorLayout.createSequentialGroup()
                        .addComponent(cb_dieCondition, 0, 95, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_dieConditionState, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ckb_ifDie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(cb_actorMotionType, javax.swing.GroupLayout.Alignment.TRAILING, 0, 177, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(cb_actorIndex, javax.swing.GroupLayout.Alignment.TRAILING, 0, 177, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                .addContainerGap())
        );
        p_actorLayout.setVerticalGroup(
            p_actorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_actorLayout.createSequentialGroup()
                .addGroup(p_actorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p_actorImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(p_actorLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_actorIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_actorMotionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ckb_ifDie)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(p_actorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_dieConditionState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cb_dieCondition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout p_startTypeLayout = new javax.swing.GroupLayout(p_startType);
        p_startType.setLayout(p_startTypeLayout);
        p_startTypeLayout.setHorizontalGroup(
            p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_startTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtn_aboveTile)
                    .addGroup(p_startTypeLayout.createSequentialGroup()
                        .addGroup(p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtn_pressButton)
                            .addComponent(rbtn_contactPlayer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(rbtn_parallelStart, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtn_autoStart, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(87, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        p_startTypeLayout.setVerticalGroup(
            p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_startTypeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtn_pressButton)
                    .addComponent(rbtn_autoStart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_startTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtn_contactPlayer)
                    .addComponent(rbtn_parallelStart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtn_aboveTile))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(p_startType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(p_actor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(p_activeCondition, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(228, 228, 228))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sp_eventListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(1, 1, 1)
                                .addComponent(cb_selectedEventContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_insertEventContest)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_deleteEventContent)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_moveDownEventContent)
                            .addComponent(btn_moveUpEventContent))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(p_activeCondition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_actor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_startType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_moveUpEventContent)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_moveDownEventContent))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(sp_eventListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(cb_selectedEventContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_insertEventContest)
                                    .addComponent(btn_deleteEventContent))))))
                .addContainerGap())
        );
	}
	
	public void renewConditionComboBox() {
		if(cb_condition1 == null) {
			cb_condition1 = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			cb_condition2 = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			cb_condition3 = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		}
		
		// ckb_condition1
		if(ckb_condition1.isSelected()) {
			// cb_condition1�� Ȱ��ȭ�Ѵ�.
			cb_condition1.setEnabled(true);
			
			// �̸� ���õǾ��� index�� �ӽ� �����Ѵ�.
			int selectedIndex=0;
			if(cb_condition1 != null)
				selectedIndex = cb_condition1.getSelectedIndex();
			
			cb_condition1.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			
			// ���õ� index�� �����Ѵ�.
			cb_condition1.setSelectedIndex(selectedIndex);
		} else {
			cb_condition1.setEnabled(false);
		}
		
		// ckb_condition2
		if(ckb_condition2.isSelected()) {
			// cb_condition1�� Ȱ��ȭ�Ѵ�.
			cb_condition2.setEnabled(true);
			
			// �̸� ���õǾ��� index�� �ӽ� �����Ѵ�.
			int selectedIndex=0;
			if(cb_condition2 != null)
				selectedIndex = cb_condition2.getSelectedIndex();
			
			cb_condition2.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			
			// ���õ� index�� �����Ѵ�.
			cb_condition2.setSelectedIndex(selectedIndex);
		} else {
			cb_condition2.setEnabled(false);
		}
		
		// ckb_condition3
		if(ckb_condition3.isSelected()) {
			// cb_condition1�� Ȱ��ȭ�Ѵ�.
			cb_condition3.setEnabled(true);
			
			// �̸� ���õǾ��� index�� �ӽ� �����Ѵ�.
			int selectedIndex=0;
			if(cb_condition3 != null)
				selectedIndex = cb_condition3.getSelectedIndex();
			
			cb_condition3.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			
			// ���õ� index�� �����Ѵ�.
			cb_condition3.setSelectedIndex(selectedIndex);
		} else {
			cb_condition3.setEnabled(false);
		}
		
		cb_condition1.revalidate();
		cb_condition2.revalidate();
		cb_condition3.revalidate();
		p_activeCondition.revalidate();
		this.repaint();
	}

	private void renewSelectedListComboBox() {
		if(cb_selectedEventContent == null)
			cb_selectedEventContent = new JComboBox();
		
//		int selectedIndex = 0;
//		if(cb_selectedEventContent.getSelectedIndex() != -1)
//			selectedIndex = cb_selectedEventContent.getSelectedIndex();
		
		int size = listModel.getSize()+1;
		
		String[] indexStr = new String[size];
		for (int i = 0; i < size; i++)
			indexStr[i] = "E"+(i+1);
		
		cb_selectedEventContent.setModel(new DefaultComboBoxModel(indexStr));
		
//		if(selectedIndex+1 < cb_selectedEventContent.getItemCount())
//			cb_selectedEventContent.setSelectedIndex(selectedIndex+1);
//		else
//			cb_selectedEventContent.setSelectedIndex(selectedIndex);
	}

	// Actor ���� �޴��� Ȱ��ȭ�� �����Ѵ�.
	public void renewActorMenu(int objectType) {
		if(this.objectType != objectType) {
			this.objectType = objectType;
			if(objectType == EventEditorSystem.MAP_EVENT) {
				p_actorImg.setEnabled(false);
				cb_actorIndex.setEnabled(false);
				cb_actorMotionType.setEnabled(false);
				ckb_ifDie.setEnabled(false);
				cb_dieCondition.setEnabled(false);
				cb_dieConditionState.setEnabled(false);
			} else {
				cb_actorIndex.setModel(new DefaultComboBoxModel(getComboBoxList(objectType)));
				if(cb_actorIndex.getItemCount()>0) {
					cb_actorIndex.setSelectedIndex(0);
					// �ش� actor�� �ҷ��ͼ� �гο� ������ش�.
					setAniImgPanel();
				}
	
				p_actorImg.setEnabled(true);
				cb_actorIndex.setEnabled(true);
				cb_actorMotionType.setEnabled(true);
				ckb_ifDie.setEnabled(true);
				cb_dieCondition.setEnabled(true);
				cb_dieConditionState.setEnabled(true);
			}
			
//			p_actorImg.revalidate();
//			cb_actorIndex.revalidate();
//			cb_actorMotionType.revalidate();
//			ckb_ifDie.revalidate();
//			cb_dieCondition.setEnabled(true);
//			cb_dieConditionState.setEnabled(true);
		}
		
		renewDieCondition();
	}

	private void renewDieCondition() {
		if(cb_dieCondition == null)
			cb_dieCondition = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		
		int selectedIndex = 0;
		if(cb_dieCondition.getSelectedIndex() != -1)
			selectedIndex = cb_dieCondition.getSelectedIndex();
		
		if(objectType != EventEditorSystem.MAP_EVENT && ckb_ifDie.isSelected()) {
			cb_dieCondition.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			cb_dieCondition.setEnabled(true);
			cb_dieConditionState.setEnabled(true);
			cb_dieCondition.setSelectedIndex(selectedIndex);
			cb_dieConditionState.setSelectedIndex(event.getDieChangeComditionState()?0:1);
		} else {
			cb_dieCondition.setEnabled(false);
			cb_dieConditionState.setEnabled(false);
		}
	}
	
	private void setSelectedIndexJList(int index) {
		cb_selectedEventContent.setSelectedIndex(index);
		if(index != cb_selectedEventContent.getItemCount())
			lst_eventList.setSelectedIndex(index);
		else
			setJListEventContents();
	}
	
	private void setJListEventContents() {
		listModel.removeAllElements();
		for (int i = 0; i < event.getEventContentList().size(); i++) {
			String strArray = "E" + (i+1) + ": ";
			if(getEventContent(i).getContentType() == EventContent.CHANGE_BGM_EVNET) {
				String bgmName = "(BGM Name: "+((ChangeBGMEvent)getEventContent(i)).getFileName()+")";
				strArray += "BGM Change Event "+bgmName;
				
			} else if((getEventContent(i)).getContentType() == EventContent.CHANGE_MAP_EVNET) {
				String mapName = "(Map Name:"+((ChangeMapEvent)getEventContent(i)).getMapName()+")";
				strArray += "Map Change Event "+mapName;
				
			} else if((getEventContent(i)).getContentType() == EventContent.CHANGE_FLAG_EVENT) {
				String flagInfo = "(Flag Index:"+((ChangeFlagEvent)getEventContent(i)).getIndexFlag()+"  State:"+((ChangeFlagEvent)getEventContent(i)).isState()+")";
				strArray += "Flag Change Event "+flagInfo;
				
			} else if((getEventContent(i)).getContentType() == EventContent.DIALOG_EVNET) {
				String dialogContent = "(Dialog Content:"+((DialogEvent)getEventContent(i)).getText()+")";
				strArray += "Dialog Event "+dialogContent;
				
			} else if((getEventContent(i)).getContentType() == EventContent.GAMEOVER_EVNET) {
				strArray += "Game Over Event";
				
			} else if((getEventContent(i)).getContentType() == EventContent.MOTION_EVNET) {
				String motionInfo = "(Actor:"+(((MotionEvent)getEventContent(i)).getActorType()==MotionEvent.PLAYER?"PLAYER":"SELF")+")";
				strArray += "Motion Event "+motionInfo;
				
			} else if((getEventContent(i)).getContentType() == EventContent.SWITCH_DIALOG_EVNET) {
				String switchQuestion = "(Question:"+((SwitchDialogEvent)getEventContent(i)).getQuestion()+")";
				strArray += "Switch Dialog Event "+switchQuestion;
				
			}
			
			listModel.addElement(strArray);
		}
	}
	
	private void setSelectedIndexCondition() {
		if(event.getPreconditionFlag(0) != -1) {
			ckb_condition1.setSelected(true);
			cb_condition1.setEnabled(true);
			cb_condition1.setSelectedIndex(event.getPreconditionFlag(0));
		}
		if(event.getPreconditionFlag(1) != -1) {
			ckb_condition2.setSelected(true);
			cb_condition2.setEnabled(true);
			cb_condition2.setSelectedIndex(event.getPreconditionFlag(1));
		}
		if(event.getPreconditionFlag(2) != -1) {
			ckb_condition3.setSelected(true);
			cb_condition3.setEnabled(true);
			cb_condition3.setSelectedIndex(event.getPreconditionFlag(2));
		}
	}
	
	private void setEventStartType() {
		if(btng_startType == null)	btng_startType = new ButtonGroup();
		
		if(btng_startType.getButtonCount() > EventEditorSystem.PARALLEL_START) {
			if(event.getStartType() == EventEditorSystem.PRESS_BUTTON) {
				rbtn_pressButton.setSelected(true);
			} else if(event.getStartType() == EventEditorSystem.CONTACT_WITH_PLAYER) {
				rbtn_contactPlayer.setSelected(true);
			} else if(event.getStartType() == EventEditorSystem.ABOVE_EVENT_TILE) {
				rbtn_aboveTile.setSelected(true);
			} else if(event.getStartType() == EventEditorSystem.AUTO_START) {
				rbtn_autoStart.setSelected(true);
			} else if(event.getStartType() == EventEditorSystem.PARALLEL_START) {
				rbtn_parallelStart.setSelected(true);
			}
		}
	}
	
	// AniImgPanel�� ������ actor�� �̹����� ������ش�.
	private void setAniImgPanel() {
		if(objectType != EventEditorSystem.MAP_EVENT && cb_actorIndex.getItemCount()>0 && cb_actorIndex.getSelectedIndex()!=-1) {
			Actors actor = null;
			
			if(objectType == EventEditorSystem.NPC_EVENT)
				actor = new NPCEditorSystem(MainFrame.OWNER.ProjectFullPath);
			else if(objectType == EventEditorSystem.MONSTER_EVENT)
				actor = new MonsterEditorSystem(MainFrame.OWNER.ProjectFullPath);
	
			try {
				actor.load(getActorIndex());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			p_actorImg.setPrintImg(actor.getMoveDownAnimation().getBaseImage());
			p_actorImg.revalidate();
			p_actor.revalidate();
			this.repaint();
		}
	}
	
	private EventContent getEventContent(int index)	{	return event.getEventContent(index);	}
	public Event getEvent()							{	return event;							}

	// Actor �� �ε����� ComboBox�� �־��ش�.
	private String[] getComboBoxList(int objectType) {
		String[] returnStr = {"None"};
		File[] objectFiles = null;
		String extension = null;
		
		// ��� object ������ �迭�� �����Ѵ�.
		if(objectType == EventEditorSystem.NPC_EVENT) {
			objectFiles = (new File(MainFrame.OWNER.ProjectFullPath+File.separator+"NPC")).listFiles();
			extension = "npc";
		} else if(objectType == EventEditorSystem.MONSTER_EVENT) {
			objectFiles = (new File(MainFrame.OWNER.ProjectFullPath+File.separator+"monster")).listFiles();
			extension = "monster";
		} else {
			System.err.println("error: EventEditPanel.getComboBoxList() (objectType: " + objectType + ")");
			return null;
		}
		
		returnStr = new String[objectFiles.length-1];
		
		int index = 0;
		for (int i = 0; i < objectFiles.length; i++) {
			String fileName = objectFiles[i].getName();
			if(fileName.charAt(0) == '.') continue;
			if(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).equals(extension)) {
				returnStr[index++] = fileName.substring(0, fileName.lastIndexOf("."));
			}
		}
		
		return returnStr;
	}
	
	public int getCondition1() {
		if(ckb_condition1.isSelected())
			return cb_condition1.getSelectedIndex();
		return -1;
	}
	public int getCondition2() {
		if(ckb_condition2.isSelected())
			return cb_condition2.getSelectedIndex();
		return -1;
	}
	public int getCondition3() {
		if(ckb_condition3.isSelected())
			return cb_condition3.getSelectedIndex();
		return -1;
	}
	
	public int getActionType() {
		return cb_actorMotionType.getSelectedIndex();
	}
	
	public int getDieConditionIndex() {
		if(objectType != EventEditorSystem.MAP_EVENT && ckb_ifDie.isSelected())
			return cb_dieCondition.getSelectedIndex();
		return -1;
	}
	
	public boolean getDieConditionState() {
		if(objectType != EventEditorSystem.MAP_EVENT && ckb_ifDie.isSelected())
			return cb_dieConditionState.getSelectedIndex()==0?true:false;
		return false;
	}
	
	// ������ objectType�� ���� actorIndex�� ComboBox�� �����Ѵ�.
	public int getActorIndex() {
		if(objectType != EventEditorSystem.MAP_EVENT) {
			return (new Integer(((String)(cb_actorIndex.getSelectedItem())).substring(0, 3))).intValue();
		}
		return -1;
	}
	
	public int getStartType() {
		if(rbtn_aboveTile.isSelected()) {
			return EventEditorSystem.ABOVE_EVENT_TILE;
		} else if(rbtn_autoStart.isSelected()) {
			return EventEditorSystem.AUTO_START;
		} else if(rbtn_contactPlayer.isSelected()) {
			return EventEditorSystem.CONTACT_WITH_PLAYER;
		} else if(rbtn_parallelStart.isSelected()) {
			return EventEditorSystem.PARALLEL_START;
		} else if(rbtn_pressButton.isSelected()) {
			return EventEditorSystem.PRESS_BUTTON;
		}
		
		System.err.println("error: EventEditPanel.getSelectedIndexRadioButton()");
		return 0;
	}
	
	private void createEditEventContentDlg(Event event, int insertIndex) {
		// ���õ� ������ ������ ����.
		if(event.getEventContent(insertIndex).getContentType() == EventContent.CHANGE_MAP_EVNET) {
			new NextMapDstDlg(owner, event, false, insertIndex);
		} else if(event.getEventContent(insertIndex).getContentType() == EventContent.CHANGE_FLAG_EVENT) {
			new ChangeFlagDlg(owner, event, false, insertIndex);
		} else if(event.getEventContent(insertIndex).getContentType() == EventContent.CHANGE_BGM_EVNET) {
			new ChangeBGMDlg(owner, event, false, insertIndex);
		} else if(event.getEventContent(insertIndex).getContentType() == EventContent.DIALOG_EVNET) {
			new DialogEventDlg(owner, event, false, insertIndex);
		} else if(event.getEventContent(insertIndex).getContentType() == EventContent.GAMEOVER_EVNET) {
			JOptionPane.showMessageDialog(null, "You can't modify Gameover Event!");
		} else if(event.getEventContent(insertIndex).getContentType() == EventContent.MOTION_EVNET) {
			new MotionEventDlg(owner, event, false, insertIndex);
		} else if(event.getEventContent(insertIndex).getContentType() == EventContent.SWITCH_DIALOG_EVNET) {
			new SwitchDialogDlg(owner, event, false, insertIndex);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_moveUpEventContent) {
			int index = lst_eventList.getSelectedIndex();
			if(index>=1 && index<cb_selectedEventContent.getItemCount()) {
				event.getEventContentList().add(index-1, event.getEventContentList().remove(index));
				setJListEventContents();
				setSelectedIndexJList(index-1);
			}
		} else if(e.getSource() == btn_moveDownEventContent) {
			int index = lst_eventList.getSelectedIndex();
			if(index>=0 && index<cb_selectedEventContent.getItemCount()-1) {
				event.getEventContentList().add(index+1, event.getEventContentList().remove(index));
				setJListEventContents();
				setSelectedIndexJList(index+1);
			}
		} else if(e.getSource() == btn_insertEventContest) {
			// �� �̺�Ʈ�� �����Ѵ�.
			int index = cb_selectedEventContent.getSelectedIndex();
			int size = lst_eventList.getComponentCount();
			new EventContentDlg(owner, event, index);
			// JList �����
			setJListEventContents();
			renewSelectedListComboBox();

			if(size < lst_eventList.getComponentCount())
				setSelectedIndexJList(index+1);
			else
				setSelectedIndexJList(index);
			
		} else if(e.getSource() == btn_deleteEventContent) {
			// ���õ� �̺�Ʈ�� �����Ѵ�.
			int index = cb_selectedEventContent.getSelectedIndex();
			if(index < cb_selectedEventContent.getItemCount()-1)
				event.getEventContentList().remove(index);
			// JList �����
			setJListEventContents();
			renewSelectedListComboBox();
			if(index-1>=0)
				setSelectedIndexJList(index-1);
			else
				setSelectedIndexJList(index);
		} else if(e.getSource() == ckb_condition1 || e.getSource() == ckb_condition2 || e.getSource() == ckb_condition3) {
			// Combo�ڽ��� Ȱ��ȭ ���θ� üũ�Ͽ� �����Ѵ�.
			renewConditionComboBox();
		} else if(e.getSource() == cb_actorIndex) {
			if(cb_actorIndex.getSelectedIndex() != EventEditorSystem.MAP_EVENT) {
				// �ش� actor�� �ҷ��ͼ� �гο� ������ش�.
				setAniImgPanel();
			}
		} else if(e.getSource() == ckb_ifDie) {
			renewDieCondition();
		} else if(e.getSource() == cb_selectedEventContent) {
			int index = cb_selectedEventContent.getSelectedIndex();
			setSelectedIndexJList(index);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == ckb_condition1 || e.getSource() == ckb_condition2 || e.getSource() == ckb_condition3) {
			// Combo�ڽ��� Ȱ��ȭ ���θ� üũ�Ͽ� �����Ѵ�.
			renewConditionComboBox();
		} else if(e.getSource() == cb_actorIndex) {
			// �ش� actor�� �ҷ��ͼ� �гο� ������ش�.
			setAniImgPanel();
		} else if(e.getSource() == ckb_ifDie) {
			renewDieCondition();
		}
	}
}
