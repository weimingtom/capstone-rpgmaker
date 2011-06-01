package viewControl.editorDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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

import eventEditor.Event;
import eventEditor.FlagList;
import eventEditor.eventContents.ChangeBGMEvent;
import eventEditor.eventContents.ChangeMapEvent;
import eventEditor.eventContents.DialogEvent;
import eventEditor.eventContents.EventContent;

public class EventEditPanel extends JPanel implements ListSelectionListener {
	
	private static final long serialVersionUID = 1L;
	
	private Event event;
	
	// Variables declaration - do not modify
	private JButton btn_insertEventContest;
	private JButton btn_deleteEventContent;
	private ButtonGroup btng_startType;
	private JComboBox cb_actorIndex;
	private JComboBox cb_actorMotionType;
//	private JComboBox cb_actorType;
	private JComboBox cb_condition1;
	private JComboBox cb_condition2;
	private JComboBox cb_condition3;
	private JCheckBox ckb_condition1;
	private JCheckBox ckb_condition2;
	private JCheckBox ckb_condition3;
	private JComboBox cb_selectedEventContent;
	private JLabel jLabel1;
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
	
	public EventEditPanel(Event event) {
		this.event = event;
		
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
		renewConditionComboBox();	// cb_condition#에 최신 Flag Name으로 갱신
		renewSelectedListComboBox();// cb_selectedEventContent에 최신 리스트 목록으로 갱신
//		cb_actorType = new JComboBox(new DefaultComboBoxModel(new String[] { "None", "NPC", "Monster" }));
		cb_actorIndex = new JComboBox(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cb_actorMotionType = new JComboBox(new DefaultComboBoxModel(new String[] { "Not Motion", "Random Motion", "Come to Player", "Attack Player", "Attack Enemy" }));
		sp_eventListPanel = new JScrollPane();
		lst_eventList = new JList();
		sp_eventListPanel.setViewportView(lst_eventList);
		jLabel1 = new JLabel("Actor Type");
		jLabel2 = new JLabel("Actor Index");
		jLabel3 = new JLabel("Actor Motion Type");
		jLabel4 = new JLabel("Event Contents");
		
		// lst_eventList 이벤트 선언
		lst_eventList.addListSelectionListener(this);

		// 초기 Event 저장 내용을 패널의 Component에 설정
		setSelectedIndexCondition();
		
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
	
	// 액터의 인덱스와 이름을 String 배열로 반환한다.
	private String[] getActorIndex(int type) {
		return null;
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
		int selectedIndex1=0;
		int selectedIndex2=0;
		int selectedIndex3=0;
		if(cb_condition1 != null) {
			selectedIndex1 = cb_condition1.getSelectedIndex();
			selectedIndex2 = cb_condition2.getSelectedIndex();
			selectedIndex3 = cb_condition3.getSelectedIndex();
		}
		
		cb_condition1 = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		cb_condition2 = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		cb_condition3 = new JComboBox(new DefaultComboBoxModel(FlagList.getIndexedFlagNames()));
		
		cb_condition1.setSelectedIndex(selectedIndex1);
		cb_condition2.setSelectedIndex(selectedIndex2);
		cb_condition3.setSelectedIndex(selectedIndex3);
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
}
