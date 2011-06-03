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
import viewControl.editorDlg.eventContentDlg.ChangeMapDlg;
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
import eventEditor.eventContents.EventContent;

public class EventEditPanel extends JPanel implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private MainFrame owner;
	private Event event;
	private int objectType;
	private DefaultListModel listModel; 
	
	// Variables declaration - do not modify
	private JButton btn_insertEventContest;
	private JButton btn_deleteEventContent;
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
		// 변수 초기화
		btn_insertEventContest = new JButton("Insert");
		btn_deleteEventContent = new JButton("Delete");
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
		ckb_ifDie = new JCheckBox("If Die");
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
		jLabel5 = new JLabel("= ");
		renewConditionComboBox();	// cb_condition#에 최신 Flag Name으로 갱신
		renewSelectedListComboBox();// cb_selectedEventContent에 최신 리스트 목록으로 갱신
		
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
		
		// 액션 이벤트
		ckb_condition1.addActionListener(this);
		ckb_condition2.addActionListener(this);
		ckb_condition3.addActionListener(this);
		ckb_ifDie.addActionListener(this);
		cb_actorIndex.addActionListener(this);
		btn_insertEventContest.addActionListener(this);
		btn_deleteEventContent.addActionListener(this);
		
		// 마우스 이벤트
		ckb_condition1.addMouseListener(this);
		ckb_condition2.addMouseListener(this);
		ckb_condition3.addMouseListener(this);
		ckb_ifDie.addActionListener(this);
		cb_actorIndex.addMouseListener(this);
		
		// JList의 마우스 이벤트 정의
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == lst_eventList) {
					// 편집할 이벤트의 index를 얻는다.
					int index = lst_eventList.getSelectedIndex();
					if(index == -1)	{
						// 없으면 마지막에 추가
						index = listModel.getSize();
						new EventContentDlg(owner, event, index);
						// JList 재출력
						setJListEventContents();
						renewSelectedListComboBox();
						
					} else {
						// EventContentDlg를 생성하여 이벤트 내용을 생성하도록 한다.
						createEditEventContentDlg(event, index);
						// JList 재출력
						setJListEventContents();
						renewSelectedListComboBox();
						
					}
				}
			}
		};
		lst_eventList.addMouseListener(mouseListener);
		
		// 패널 안의 각 데이터 설정
		setSelectedIndexCondition();
		int tmpObjectType = objectType;
		objectType = -1;
		renewActorMenu(tmpObjectType);
//		setSelectedIndexActorMenu();
		setEventStartType();
		
		if(event.getDieChangeConditionIndex() != -1)
			ckb_ifDie.setSelected(true);
		else
			ckb_ifDie.setSelected(false);
		renewDieCondition();
		
		if(event.getDieChangeConditionIndex() != -1) {
			cb_dieCondition.setSelectedIndex(event.getDieChangeConditionIndex());
			cb_dieConditionState.setSelectedIndex(event.getDieChangeComditionState()?0:1);
		}
		
		// Event Content의 JList 구성
		setJListEventContents();
		renewSelectedListComboBox();
		
		// 레이아웃 구성
		GroupLayout p_activeConditionLayout = new GroupLayout(p_activeCondition);
		p_activeCondition.setLayout(p_activeConditionLayout);
		p_activeConditionLayout.setHorizontalGroup(
			p_activeConditionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(p_activeConditionLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(p_activeConditionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(p_activeConditionLayout.createSequentialGroup()
						.addComponent(ckb_condition1)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_condition1, 0, 213, Short.MAX_VALUE))
					.addGroup(p_activeConditionLayout.createSequentialGroup()
						.addComponent(ckb_condition2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_condition2, 0, 213, Short.MAX_VALUE))
					.addGroup(p_activeConditionLayout.createSequentialGroup()
						.addComponent(ckb_condition3)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_condition3, 0, 213, Short.MAX_VALUE)))
				.addContainerGap())
		);
		p_activeConditionLayout.setVerticalGroup(
			p_activeConditionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(p_activeConditionLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(p_activeConditionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(ckb_condition1)
					.addComponent(cb_condition1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(p_activeConditionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(ckb_condition2)
					.addComponent(cb_condition2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(p_activeConditionLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(ckb_condition3)
					.addComponent(cb_condition3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		p_actor.setBorder(BorderFactory.createTitledBorder("Actor"));

		p_actorImg.setBackground(new java.awt.Color(255, 255, 255));
		p_actorImg.setBorder(BorderFactory.createEtchedBorder());

		GroupLayout p_actorImgLayout = new GroupLayout(p_actorImg);
		p_actorImg.setLayout(p_actorImgLayout);
		p_actorImgLayout.setHorizontalGroup(
			p_actorImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 131, Short.MAX_VALUE)
		);
		p_actorImgLayout.setVerticalGroup(
			p_actorImgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGap(0, 158, Short.MAX_VALUE)
		);

		GroupLayout p_actorLayout = new GroupLayout(p_actor);
		p_actor.setLayout(p_actorLayout);
		p_actorLayout.setHorizontalGroup(
			p_actorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(p_actorLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(p_actorImg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(p_actorLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addComponent(jLabel2, GroupLayout.Alignment.LEADING)
					.addComponent(cb_actorIndex, 0, 155, Short.MAX_VALUE)
					.addComponent(jLabel3, GroupLayout.Alignment.LEADING)
					.addComponent(cb_actorMotionType, 0, 155, Short.MAX_VALUE)
					.addGroup(GroupLayout.Alignment.LEADING, p_actorLayout.createSequentialGroup()
						.addComponent(ckb_ifDie)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_dieCondition, 0, 99, Short.MAX_VALUE))
					.addGroup(p_actorLayout.createSequentialGroup()
						.addComponent(jLabel5)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_dieConditionState, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap())
		);
		p_actorLayout.setVerticalGroup(
			p_actorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(p_actorLayout.createSequentialGroup()
				.addGroup(p_actorLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(p_actorLayout.createSequentialGroup()
						.addComponent(jLabel2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_actorIndex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel3)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_actorMotionType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(p_actorLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(ckb_ifDie)
							.addComponent(cb_dieCondition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(p_actorLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(cb_dieConditionState, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(jLabel5)))
					.addComponent(p_actorImg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap())
		);

		GroupLayout p_startTypeLayout = new GroupLayout(p_startType);
		p_startType.setLayout(p_startTypeLayout);
		p_startTypeLayout.setHorizontalGroup(
			p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(p_startTypeLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(rbtn_aboveTile)
					.addGroup(p_startTypeLayout.createSequentialGroup()
						.addGroup(p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(rbtn_pressButton)
							.addComponent(rbtn_contactPlayer))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
							.addComponent(rbtn_parallelStart, GroupLayout.Alignment.LEADING)
							.addComponent(rbtn_autoStart, GroupLayout.Alignment.LEADING))))
				.addContainerGap(81, GroupLayout.PREFERRED_SIZE))
		);
		p_startTypeLayout.setVerticalGroup(
			p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(p_startTypeLayout.createSequentialGroup()
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(rbtn_pressButton)
					.addComponent(rbtn_autoStart))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(p_startTypeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(rbtn_contactPlayer)
					.addComponent(rbtn_parallelStart))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(rbtn_aboveTile))
		);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addComponent(p_startType, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(p_actor, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(p_activeCondition, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
					.addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(jLabel4)
						.addComponent(sp_eventListPanel, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE))
					.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_selectedEventContent, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_insertEventContest)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_deleteEventContent)))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
					.addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
						.addComponent(jLabel4)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(sp_eventListPanel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(btn_deleteEventContent)
							.addComponent(cb_selectedEventContent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(jLabel1)
							.addComponent(btn_insertEventContest)))
					.addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
						.addComponent(p_activeCondition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(p_actor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(p_startType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
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
			// cb_condition1를 활성화한다.
			cb_condition1.setEnabled(true);
			
			// 미리 선택되었던 index를 임시 저장한다.
			int selectedIndex=0;
			if(cb_condition1 != null)
				selectedIndex = cb_condition1.getSelectedIndex();
			
			cb_condition1.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			
			// 선택된 index를 복원한다.
			cb_condition1.setSelectedIndex(selectedIndex);
		} else {
			cb_condition1.setEnabled(false);
		}
		
		// ckb_condition2
		if(ckb_condition2.isSelected()) {
			// cb_condition1를 활성화한다.
			cb_condition2.setEnabled(true);
			
			// 미리 선택되었던 index를 임시 저장한다.
			int selectedIndex=0;
			if(cb_condition2 != null)
				selectedIndex = cb_condition2.getSelectedIndex();
			
			cb_condition2.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			
			// 선택된 index를 복원한다.
			cb_condition2.setSelectedIndex(selectedIndex);
		} else {
			cb_condition2.setEnabled(false);
		}
		
		// ckb_condition3
		if(ckb_condition3.isSelected()) {
			// cb_condition1를 활성화한다.
			cb_condition3.setEnabled(true);
			
			// 미리 선택되었던 index를 임시 저장한다.
			int selectedIndex=0;
			if(cb_condition3 != null)
				selectedIndex = cb_condition3.getSelectedIndex();
			
			cb_condition3.setModel(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
			
			// 선택된 index를 복원한다.
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
	
	private void setJListEventContents() {
		listModel.removeAllElements();
		for (int i = 0; i < event.getEventContentList().size(); i++) {
			String strArray = "E" + (i+1) + ": ";
			if(getEventContent(i).getContentType() == EventContent.CHANGE_BGM_EVNET)
				strArray += "BGM Change Event";
			else if((getEventContent(i)).getContentType() == EventContent.CHANGE_MAP_EVNET)
				strArray += "Map Change Event";
			else if((getEventContent(i)).getContentType() == EventContent.CHANGE_FLAG_EVENT)
				strArray += "Flag Change Event";
			else if((getEventContent(i)).getContentType() == EventContent.DIALOG_EVNET)
				strArray += "Dialog Event";
			else if((getEventContent(i)).getContentType() == EventContent.GAMEOVER_EVNET)
				strArray += "Game Over Event";
			else if((getEventContent(i)).getContentType() == EventContent.MOTION_EVNET)
				strArray += "Motion Event";
			else if((getEventContent(i)).getContentType() == EventContent.SWITCH_DIALOG_EVNET)
				strArray += "Switch Dialog Event";
			
			listModel.addElement(strArray);
		}
	}
	
	private void renewSelectedListComboBox() {
		if(cb_selectedEventContent == null)
			cb_selectedEventContent = new JComboBox();
		
		int selectedIndex = 0;
		if(cb_selectedEventContent.getSelectedIndex() != -1)
			selectedIndex = cb_selectedEventContent.getSelectedIndex();
		
		int size = listModel.getSize()+1;
		
		String[] indexStr = new String[size];
		for (int i = 0; i < size; i++)
			indexStr[i] = "E"+(i+1);
		
		cb_selectedEventContent.setModel(new DefaultComboBoxModel(indexStr));
		
		cb_selectedEventContent.setSelectedIndex(selectedIndex);
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
	
	private void setSelectedIndexActorMenu() {
		if(objectType != EventEditorSystem.MAP_EVENT) {
			if(cb_actorIndex == null)		cb_actorIndex = new JComboBox();
			if(cb_actorMotionType == null)	cb_actorMotionType = new JComboBox();
			
			for (int i = 0; i < cb_actorIndex.getComponentCount(); i++) {
				if(event.getActorIndex() == (new Integer(((String)(cb_actorIndex.getItemAt(i))).substring(0, 3))).intValue())
					cb_actorIndex.setSelectedIndex(i);
			}
			
			if(cb_actorMotionType.getItemCount() > EventEditorSystem.ATTACK_ENEMY) {
				cb_actorMotionType.setSelectedIndex(event.getActionType());
			}
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
	
	// Actor 관련 메뉴의 활성화를 조작한다.
	public void renewActorMenu(int objectType) {
		if(this.objectType != objectType) {
			this.objectType = objectType;
			if(objectType == EventEditorSystem.MAP_EVENT) {
				p_actorImg.setEnabled(false);
				cb_actorIndex.setEnabled(false);
				cb_actorMotionType.setEnabled(false);
			} else {
				cb_actorIndex.setModel(new DefaultComboBoxModel(getComboBoxList(objectType)));
				if(cb_actorIndex.getItemCount()>0) {
					cb_actorIndex.setSelectedIndex(0);
					// 해당 actor를 불러와서 패널에 출력해준다.
					setAniImgPanel();
				}
	
				p_actorImg.setEnabled(true);
				cb_actorIndex.setEnabled(true);
				cb_actorMotionType.setEnabled(true);
			}
			
			p_actorImg.revalidate();
			cb_actorIndex.revalidate();
			cb_actorMotionType.revalidate();
		}
	}
	
	// Actor 의 인덱스를 ComboBox에 넣어준다.
	private String[] getComboBoxList(int objectType) {
		String[] returnStr = {"None"};
		File[] objectFiles = null;
		String extension = null;
		
		// 모든 object 파일을 배열에 저장한다.
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
	
	// AniImgPanel을 설정한 actor의 이미지로 출력해준다.
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
	
	private EventContent getEventContent(int index) {
		return event.getEventContent(index);
	}
	
	public Event getEvent()	{	return event;	}
	
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
		return cb_dieCondition.getSelectedIndex();
	}
	
	public boolean getDieConditionState() {
		return cb_dieConditionState.getSelectedIndex()==0?true:false;
	}
	
	// 설정된 objectType에 따라 actorIndex의 ComboBox를 정의한다.
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
		// 선택된 내용이 있으면 편집.
		if(event.getEventContent(insertIndex).getContentType() == EventContent.CHANGE_MAP_EVNET) {
			new NextMapDstDlg(owner, event, false, insertIndex);
//			new ChangeMapDlg(owner, event, false, insetIndex);
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
	
	private void renewDieCondition() {
		if(cb_dieCondition == null)
			cb_dieCondition = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		
		int selectedIndex = 0;
		if(cb_dieCondition.getSelectedIndex() != -1)
			selectedIndex = cb_dieCondition.getSelectedIndex();
		
		if(ckb_ifDie.isSelected()) {
			cb_dieCondition.setEnabled(true);
			cb_dieConditionState.setEnabled(true);
			cb_dieCondition.setSelectedIndex(selectedIndex);
			cb_dieConditionState.setSelectedIndex(event.getDieChangeComditionState()?0:1);
		} else {
			cb_dieCondition.setEnabled(false);
			cb_dieConditionState.setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ckb_condition1 || e.getSource() == ckb_condition2 || e.getSource() == ckb_condition3) {
			// Combo박스의 활성화 여부를 체크하여 설정한다.
			renewConditionComboBox();
		} else if(e.getSource() == cb_actorIndex) {
			if(cb_actorIndex.getSelectedIndex() != EventEditorSystem.MAP_EVENT) {
				// 해당 actor를 불러와서 패널에 출력해준다.
				setAniImgPanel();
			}
		} else if(e.getSource() == btn_insertEventContest) {
			// 새 이벤트를 삽입한다.
			int index = cb_selectedEventContent.getSelectedIndex();
			new EventContentDlg(owner, event, index);
			// JList 재출력
			setJListEventContents();
			renewSelectedListComboBox();
			
		} else if(e.getSource() == btn_deleteEventContent) {
			// 선택된 이벤트를 삭제한다.
			int index = cb_selectedEventContent.getSelectedIndex();
			if(index < cb_selectedEventContent.getItemCount()-1)
				event.getEventContentList().remove(index);
			// JList 재출력
			setJListEventContents();
			renewSelectedListComboBox();
		} else if(e.getSource() == ckb_ifDie) {
			renewDieCondition();
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
			// Combo박스의 활성화 여부를 체크하여 설정한다.
			renewConditionComboBox();
		} else if(e.getSource() == cb_actorIndex) {
			// 해당 actor를 불러와서 패널에 출력해준다.
			setAniImgPanel();
		} else if(e.getSource() == ckb_ifDie) {
			renewDieCondition();
		}
	}
}
