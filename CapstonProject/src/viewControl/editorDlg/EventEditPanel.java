package viewControl.editorDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import viewControl.MainFrame;
import characterEditor.Actors;
import characterEditor.MonsterEditorSystem;
import characterEditor.NPCEditorSystem;
import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.FlagList;
import eventEditor.eventContents.ChangeBGMEvent;
import eventEditor.eventContents.ChangeMapEvent;
import eventEditor.eventContents.DialogEvent;
import eventEditor.eventContents.EventContent;

public class EventEditPanel extends JPanel implements ListSelectionListener, ActionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	private Event event;
	private int objectType;
	
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
	private JComboBox cb_selectedEventContent;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
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
	
	public EventEditPanel(Event event, int objectType) {
		this.event = event;
		this.objectType = objectType;
		
		initComponents();
	}
	
	private void initComponents() {
		// 변수 초기화
		btn_insertEventContest = new JButton("Insert");
		btn_deleteEventContent = new JButton("Delete");
		btng_startType = new ButtonGroup();
		rbtn_pressButton = new JRadioButton();
		rbtn_contactPlayer = new JRadioButton();
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
		cb_actorIndex = new JComboBox(new DefaultComboBoxModel(new String[] { "None" }));
		cb_actorMotionType = new JComboBox(new DefaultComboBoxModel(new String[] { "Not Motion", "Random Motion", "Come to Player", "Attack Player", "Attack Enemy" }));
		sp_eventListPanel = new JScrollPane();
		lst_eventList = new JList();
		sp_eventListPanel.setViewportView(lst_eventList);
		jLabel2 = new JLabel("Actor Index");
		jLabel3 = new JLabel("Actor Motion Type");
		jLabel4 = new JLabel("Event Contents");
		renewConditionComboBox();	// cb_condition#에 최신 Flag Name으로 갱신
		renewSelectedListComboBox();// cb_selectedEventContent에 최신 리스트 목록으로 갱신
		
		// lst_eventList 이벤트 선언
		lst_eventList.addListSelectionListener(this);
		
		p_activeCondition.setBorder(BorderFactory.createTitledBorder("Active Conditions"));
		p_actor.setBorder(BorderFactory.createTitledBorder("Actor"));
		p_actorImg.setBackground(new java.awt.Color(255, 255, 255));
		p_actorImg.setBorder(BorderFactory.createEtchedBorder());
		p_startType.setBorder(BorderFactory.createTitledBorder("Event Start Type"));
		
		btng_startType.add(rbtn_pressButton);
		rbtn_pressButton.setSelected(true);
		rbtn_pressButton.setText("Press Button");
		btng_startType.add(rbtn_contactPlayer);
		rbtn_contactPlayer.setText("Contact with Player");
		rbtn_contactPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				rbtn_contactPlayerActionPerformed(evt);
			}
		});
		btng_startType.add(rbtn_aboveTile);
		btng_startType.add(rbtn_autoStart);
		btng_startType.add(rbtn_parallelStart);
		
		// 액션 이벤트
		ckb_condition1.addActionListener(this);
		ckb_condition2.addActionListener(this);
		ckb_condition3.addActionListener(this);
		cb_actorIndex.addActionListener(this);
		
		// 마우스 이벤트
		ckb_condition1.addMouseListener(this);
		ckb_condition2.addMouseListener(this);
		ckb_condition3.addMouseListener(this);
		cb_actorIndex.addMouseListener(this);
		
		// 패널 안의 각 데이터 설정
		setSelectedIndexCondition();
		int tmpObjectType = objectType;
		objectType = -1;
		renewActorMenu(tmpObjectType);
		
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
                    .addComponent(cb_actorMotionType, 0, 155, Short.MAX_VALUE))
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
                        .addGap(26, 26, 26)
                        .addComponent(jLabel3)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_actorMotionType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cb_selectedEventContent, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_insertEventContest, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_deleteEventContent, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)))
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
                            .addComponent(cb_selectedEventContent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_insertEventContest)
                            .addComponent(btn_deleteEventContent)))
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(p_activeCondition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_actor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_startType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
	}
	
	private void rbtn_contactPlayerActionPerformed(ActionEvent e) {
		// TODO add your handling code here:
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int selectedEntry = lst_eventList.getSelectedIndex();
		EventContent eventContent = event.getEventContent(selectedEntry);
		
//		if() {
//			
//		} else if() {
//			
//		}
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
			
			// 선택된 index를 복원한다.
			cb_condition3.setSelectedIndex(selectedIndex);
		} else {
			cb_condition3.setEnabled(false);
		}
	}
	
	private void renewEventJList() {
		String[] strArray = new String[event.getEventContentList().size()];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = "E" + (i+1) + ": ";
			if((event.getEventContent(i)).getContentType() == EventContent.CHANGE_BGM_EVNET) {
				strArray[i] += "BGM Change Event (File Name: " + ((ChangeBGMEvent)event.getEventContent(i)).getFileName() + ")";
			} else if((event.getEventContent(i)).getContentType() == EventContent.CHANGE_MAP_EVNET) {
				strArray[i] += "Map Change Event (Map Name: " + ((ChangeMapEvent)event.getEventContent(i)).getFileName() + ")";
			} else if((event.getEventContent(i)).getContentType() == EventContent.DIALOG_EVNET) {
				strArray[i] += "Dialog Event (Dialog Text: " + ((DialogEvent)event.getEventContent(i)).getText() + ")";
			} else if((event.getEventContent(i)).getContentType() == EventContent.GAMEOVER_EVNET) {
				strArray[i] += "Game Over Event";
			} else if((event.getEventContent(i)).getContentType() == EventContent.MOTION_EVNET) {
				strArray[i] += "Motion Event";
			} else if((event.getEventContent(i)).getContentType() == EventContent.SWITCH_DIALOG_EVNET) {
				strArray[i] += "";
			}
		}
	}
	
	private void renewSelectedListComboBox() {
		int selectedIndex = 0;
		if(cb_selectedEventContent != null) {
			selectedIndex = cb_selectedEventContent.getSelectedIndex();
		}
		
//		String[] eventContentList = lst_eventList.getModel().addListDataListener(l)
		String[] eventContentList = {"a", "b"};
		cb_selectedEventContent = new JComboBox(new DefaultComboBoxModel(eventContentList));
		
		cb_selectedEventContent.setSelectedIndex(selectedIndex);
	}
	
	private void setSelectedIndexCondition() {
		if(event.getPreconditionFlag(0) != -1) {
			ckb_condition1.setSelected(true);
			cb_condition1.setSelectedIndex(event.getPreconditionFlag(0));
		}
		if(event.getPreconditionFlag(1) != -1) {
			ckb_condition2.setSelected(true);
			cb_condition2.setSelectedIndex(event.getPreconditionFlag(1));
		}
		if(event.getPreconditionFlag(2) != -1) {
			ckb_condition3.setSelected(true);
			cb_condition3.setSelectedIndex(event.getPreconditionFlag(2));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ckb_condition1 || e.getSource() == ckb_condition2 || e.getSource() == ckb_condition3) {
			// Combo박스의 활성화 여부를 체크하여 설정한다.
			renewConditionComboBox();
		} else if(e.getSource() == cb_actorIndex) {
			// 해당 actor를 불러와서 패널에 출력해준다.
			setAniImgPanel();
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
		}
	}
	
	private int getActorIndex() {
		if(objectType != EventEditorSystem.MAP_EVENT) {
			return (new Integer(((String)(cb_actorIndex.getSelectedItem())).substring(0, 3))).intValue();
		}
		return -1;
	}
	
	// AniImgPanel을 설정한 actor의 이미지로 출력해준다.
	private void setAniImgPanel() {
		if(cb_actorIndex.getItemCount()>0 && cb_actorIndex.getSelectedIndex()!=-1) {
			Actors actor = null;
			
			if(objectType == EventEditorSystem.NPC_EVENT)
				actor = new NPCEditorSystem(MainFrame.OWNER.projectPath);
			else if(objectType == EventEditorSystem.MONSTER_EVENT)
				actor = new MonsterEditorSystem(MainFrame.OWNER.projectPath);
	
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
}
