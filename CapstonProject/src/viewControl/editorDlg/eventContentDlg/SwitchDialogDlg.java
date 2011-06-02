package viewControl.editorDlg.eventContentDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class SwitchDialogDlg extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// Variables declaration - do not modify
	private JButton btn_OK;
	private JButton btn_cancel;
	private JTextField tf_answer1;
	private JTextField tf_answer2;
	private JTextField tf_answer3;
	private JTextField tf_answer4;
	private JTextField tf_question;
	private JComboBox cb_flagName1;
	private JComboBox cb_flagName2;
	private JComboBox cb_flagName3;
	private JComboBox cb_flagName4;
	private JComboBox cb_value1;
	private JComboBox cb_value2;
	private JComboBox cb_value3;
	private JComboBox cb_value4;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel7;
	private JLabel jLabel8;
	private JLabel jLabel9;
	private JLabel jLabel10;
	private JLabel jLabel11;
	private JLabel jLabel12;
	private JLabel jLabel13;
	private JLabel jLabel14;
	private JLabel jLabel15;
	private JLabel jLabel16;
	private JLabel jLabel17;
	// End of variables declaration
	
	private MainFrame owner;
	private Event event;
	private int insetIndex;
	
	public SwitchDialogDlg(MainFrame parent, Event event, int insetIndex) {
		super(parent, "Switch Dialog Event");
		
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
		btn_OK = new JButton("O K");
		btn_cancel = new JButton("Cancel");
		tf_question = new JTextField();
		tf_answer1 = new JTextField();
		tf_answer2 = new JTextField();
		tf_answer3 = new JTextField();
		tf_answer4 = new JTextField();
		cb_flagName1 = new JComboBox();
		cb_flagName2 = new JComboBox();
		cb_flagName3 = new JComboBox();
		cb_flagName4 = new JComboBox();
		cb_value1 = new JComboBox(new DefaultComboBoxModel(new String[] { "True", "False" }));
		cb_value2 = new JComboBox(new DefaultComboBoxModel(new String[] { "True", "False" }));
		cb_value3 = new JComboBox(new DefaultComboBoxModel(new String[] { "True", "False" }));
		cb_value4 = new JComboBox(new DefaultComboBoxModel(new String[] { "True", "False" }));
		jLabel1 = new JLabel("Question");
		jLabel2 = new JLabel("Answer1");
		jLabel3 = new JLabel("Answer2");
		jLabel4 = new JLabel("Answer3");
		jLabel5 = new JLabel("Answer4");
		jLabel6 = new JLabel("Flag Name");
		jLabel7 = new JLabel(" = ");
		jLabel8 = new JLabel(" = ");
		jLabel9 = new JLabel(" = ");
		jLabel10 = new JLabel(" = ");
		jLabel11 = new JLabel("Value");
		jLabel12 = new JLabel("Flag Name");
		jLabel13 = new JLabel("Flag Name");
		jLabel14 = new JLabel("Flag Name");
		jLabel15 = new JLabel("Value");
		jLabel16 = new JLabel("Value");
		jLabel17 = new JLabel("Value");

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		cb_flagName1.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cb_flagName2.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cb_flagName3.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		cb_flagName4.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		
		// 레이아웃 구성
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jLabel1)
					.addComponent(jLabel3)
					.addComponent(jLabel4)
					.addComponent(jLabel5)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
							.addComponent(tf_question, GroupLayout.Alignment.LEADING)
							.addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(tf_answer4, GroupLayout.Alignment.LEADING)
										.addComponent(tf_answer3, GroupLayout.Alignment.LEADING)
										.addComponent(tf_answer2, GroupLayout.Alignment.LEADING)
										.addComponent(tf_answer1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))
									.addComponent(jLabel2))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(jLabel6)
									.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
											.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addComponent(cb_flagName1, 0, 113, Short.MAX_VALUE)
												.addComponent(cb_flagName2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(cb_flagName3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(cb_flagName4, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
												.addGroup(layout.createSequentialGroup()
													.addComponent(jLabel12)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
												.addGroup(layout.createSequentialGroup()
													.addComponent(jLabel13)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
												.addGroup(layout.createSequentialGroup()
													.addComponent(jLabel14)
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
											.addComponent(btn_OK, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addGroup(layout.createSequentialGroup()
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
													.addGroup(layout.createSequentialGroup()
														.addComponent(jLabel9)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(cb_value3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
													.addGroup(layout.createSequentialGroup()
														.addComponent(jLabel7)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
															.addComponent(jLabel11)
															.addComponent(cb_value1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
													.addGroup(layout.createSequentialGroup()
														.addComponent(jLabel8)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
															.addComponent(jLabel15)
															.addComponent(cb_value2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
															.addComponent(jLabel16)))
													.addGroup(layout.createSequentialGroup()
														.addComponent(jLabel10)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
															.addComponent(jLabel17)
															.addComponent(cb_value4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
											.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addGap(4, 4, 4)
												.addComponent(btn_cancel)))))))
						.addGap(7, 7, 7)))
				.addContainerGap(15, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jLabel1)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(tf_question, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(37, 37, 37)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel2)
					.addComponent(jLabel6)
					.addComponent(jLabel11))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(tf_answer1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(cb_flagName1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel7)
					.addComponent(cb_value1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel3)
					.addComponent(jLabel12)
					.addComponent(jLabel15))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(tf_answer2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(cb_flagName2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel8)
					.addComponent(cb_value2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel4)
					.addComponent(jLabel13)
					.addComponent(jLabel16))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(tf_answer3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(cb_flagName3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel9)
					.addComponent(cb_value3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel5)
					.addComponent(jLabel14)
					.addComponent(jLabel17))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(tf_answer4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(cb_flagName4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel10)
					.addComponent(cb_value4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_cancel)
					.addComponent(btn_OK))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_OK) {
			this.dispose();
		} else if(e.getSource() == btn_OK) {
			this.dispose();
		}
	}
	
}
