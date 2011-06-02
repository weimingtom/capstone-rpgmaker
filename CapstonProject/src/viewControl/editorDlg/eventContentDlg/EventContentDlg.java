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
		// ������Ʈ ����
		btn_changeMap = new JButton("Change Map");
		btn_changeBGM = new JButton("Change BGM");
		btn_changeFlag = new JButton("Change Flag");
		btn_motionEvent = new JButton("Motion Event");
		btn_switchDialog = new JButton("Switch Dialog");
		btn_gameOver = new JButton("Game Over");
		jLabel1 = new JLabel("���� ���� Ư�� ��ҿ� �����ϸ� �� �̵�");
		jLabel2 = new JLabel("BGM ����");
		jLabel3 = new JLabel("���ϴ� Flag Ȱ��ȭ/��Ȱ��ȭ");
		jLabel4 = new JLabel("Actor�� ���� �̵� �̺�Ʈ");
		jLabel5 = new JLabel("������ ���� �б� �̺�Ʈ");
		jLabel6 = new JLabel("Game Over �̺�Ʈ");
		
		// �׼� �̺�Ʈ
		btn_changeMap.addActionListener(this);
		btn_changeBGM.addActionListener(this);
		btn_changeFlag.addActionListener(this);
		btn_motionEvent.addActionListener(this);
		btn_switchDialog.addActionListener(this);
		btn_gameOver.addActionListener(this);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		// ���̾ƿ� ����
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
