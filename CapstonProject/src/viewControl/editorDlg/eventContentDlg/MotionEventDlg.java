package viewControl.editorDlg.eventContentDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import viewControl.MainFrame;
import eventEditor.Event;

public class MotionEventDlg extends JDialog implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	
	// cb_actorType
	public static final int CHARACTER = 0;
	public static final int NPC = 1;
	public static final int MONSTER = 2;
	// cb_direction
	public static final int EAST = 0;
	public static final int WEST = 1;
	public static final int SOUTH = 2;
	public static final int NORTH = 3;
	
	// Variables declaration - do not modify
	private JButton btn_OK;
	private JButton btn_cancel;
	private JComboBox cb_actorIndex;
	private JComboBox cb_actorType;
	private JComboBox cb_direction;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JTextField tf_MotionDistance;
	private JTextField tf_speed;
	// End of variables declaration
	
	private MainFrame owner;
	private Event event;
	private boolean isNew;
	private int index;
	
	public MotionEventDlg(MainFrame parent, Event event, boolean isNew, int index) {
		super(parent, "Motion Event");
		
//		this.owner = parent;
		this.event = event;
		this.isNew = isNew;
		this.index = index;
		
		setResizable(false);
		setModal(true);
		initComponents();
		setVisible(true);
	}
	
	private void initComponents() {
		// 컨포넌트 정의 
		jLabel1 = new JLabel("Actor Type:");
		jLabel2 = new JLabel("Actor Index:");
		jLabel3 = new JLabel("Direction:");
		jLabel4 = new JLabel("Motion Distance:");
		jLabel5 = new JLabel("Speed:");
		cb_actorType = new JComboBox(new DefaultComboBoxModel(new String[] { "Character", "NPC", "Monster" }));
//		cb_actorIndex = new JComboBox();
		cb_direction = new JComboBox(new DefaultComboBoxModel(new String[] { "East", "West", "South", "North" }));
		tf_MotionDistance = new JTextField(3);
		tf_speed = new JTextField("1", 2);
		btn_OK = new JButton("O K");
		btn_cancel = new JButton("cancel");
		
		// 액션 이벤트
		btn_OK.addActionListener(this);
		btn_cancel.addActionListener(this);
		cb_actorType.addActionListener(this);
		
		// 마우스 이벤트
		cb_actorType.addMouseListener(this);
		
		// isNew가 false면 event의 index번 데이터로 초기화
//		if(!isNew) {
//			int selectedIndex = 0;
//			cb_actorType.setSelectedIndex(selectedIndex);
//			cb_actorIndex = new JComboBox(getActorNames(selectedIndex));
//		} else {
//			cb_actorType.setSelectedIndex(0);
//			cb_actorIndex = new JComboBox(getActorNames(CHARACTER));
//		}

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		cb_actorIndex.setModel(new DefaultComboBoxModel(new String[] { "Item 1" }));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(tf_MotionDistance, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
							.addComponent(tf_speed)
							.addComponent(cb_actorType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(cb_actorIndex, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(cb_direction, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(btn_OK, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_cancel)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel1)
					.addComponent(cb_actorType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel2)
					.addComponent(cb_actorIndex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel3)
					.addComponent(cb_direction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel4)
					.addComponent(tf_MotionDistance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel5)
					.addComponent(tf_speed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_cancel)
					.addComponent(btn_OK))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}
	
//	private String[] getActorNames(int actorType) {
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_OK) {
			this.dispose();
		} else if(e.getSource() == btn_cancel) {
			this.dispose();
		} else if(e.getSource() == cb_actorType) {
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
		if(e.getSource() == cb_actorType) {
		}
	}
}
