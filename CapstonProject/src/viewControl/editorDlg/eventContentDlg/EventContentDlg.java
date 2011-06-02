package viewControl.editorDlg.eventContentDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import viewControl.MainFrame;
import eventEditor.Event;
import eventEditor.eventContents.EventContent;
import eventEditor.eventContents.GameOverEvent;

public class EventContentDlg extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// Variables declaration - do not modify
	private JButton btn_changeMap;
	private JButton btn_changeBGM;
	private JButton btn_changeFlag;
	private JButton btn_motionEvent;
	private JButton btn_switchDialog;
	private JButton btn_gameOver;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	// End of variables declaration
	
	private MainFrame owner;
	private Event event;
	private int insetIndex;
	
	public EventContentDlg(MainFrame parent, Event event, int insetIndex) {
		super(parent, "Select Event Content");
		
		this.owner = parent;
		this.event = event;
		this.insetIndex = insetIndex;
		
		setResizable(false);
		setModal(true);
		initComponents();
		setVisible(true);
	}
	
	private void initComponents() {
		// 컨포넌트 정의
		btn_changeMap = new JButton("Change Map");
		btn_changeBGM = new JButton("Change BGM");
		btn_changeFlag = new JButton("Change Flag");
		btn_motionEvent = new JButton("Motion Event");
		btn_switchDialog = new JButton("Switch Dialog");
		btn_gameOver = new JButton("Game Over");
		jLabel1 = new JLabel("현재 맵의 특정 장소에 도달하면 맵 이동");
		jLabel2 = new JLabel("BGM 변경");
		jLabel3 = new JLabel("원하는 Flag 활성화/비활성화");
		jLabel4 = new JLabel("Actor의 강제 이동 이벤트");
		jLabel5 = new JLabel("조건을 통한 분기 이벤트");
		jLabel6 = new JLabel("Game Over 이벤트");
		
		// 액션 이벤트
		btn_changeMap.addActionListener(this);
		btn_changeBGM.addActionListener(this);
		btn_changeFlag.addActionListener(this);
		btn_motionEvent.addActionListener(this);
		btn_switchDialog.addActionListener(this);
		btn_gameOver.addActionListener(this);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		// 레이아웃 구성
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
					.addComponent(btn_switchDialog, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btn_motionEvent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btn_changeFlag, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btn_changeBGM, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btn_changeMap, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btn_gameOver, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jLabel1)
					.addComponent(jLabel2)
					.addComponent(jLabel3)
					.addComponent(jLabel4)
					.addComponent(jLabel5)
					.addComponent(jLabel6))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_changeMap)
					.addComponent(jLabel1))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_changeBGM)
					.addComponent(jLabel2))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_changeFlag)
					.addComponent(jLabel3))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_motionEvent)
					.addComponent(jLabel4))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_switchDialog)
					.addComponent(jLabel5))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_gameOver)
					.addComponent(jLabel6))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}// </editor-fold>
	
	private List<EventContent> getEventContentList() {
		return event.getEventContentList();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_changeMap) {
			new ChangeMapDlg(owner, event, insetIndex);
		} else if(e.getSource() == btn_changeBGM) {
			new ChangeBGMDlg(owner, event, insetIndex);
		} else if(e.getSource() == btn_changeFlag) {
			new ChangeFlagDlg(owner, event, insetIndex);
		} else if(e.getSource() == btn_motionEvent) {
			new MotionEventDlg(owner, event, insetIndex);
		} else if(e.getSource() == btn_switchDialog) {
			new SwitchDialogDlg(owner, event, insetIndex);
		} else if(e.getSource() == btn_gameOver) {
			getEventContentList().add(insetIndex, new GameOverEvent());
		}
		this.dispose();
	}
}
