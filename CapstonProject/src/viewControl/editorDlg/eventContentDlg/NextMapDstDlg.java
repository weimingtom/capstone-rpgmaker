package viewControl.editorDlg.eventContentDlg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import viewControl.MainFrame;

public class NextMapDstDlg extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;

	public NextMapDstDlg(Frame parent) {
		super(parent, true);
		setPreferredSize(new Dimension(800, 640));
		initComponents();
		setVisible(true);
	}

	private void initComponents() {

		topP = new JPanel();
		mapList = new JComboBox();
		btnBrower = new JButton("Browser");
		centerP = new JPanel();
		eastP = new JPanel();
		l_x = new JLabel("X :");
		l_y = new JLabel("Y :");
		l_xpoint = new JLabel("??");
		l_ypoint = new JLabel("??");
		btnOk = new JButton("O K");
		btnCancel = new JButton("Cancel");
		jScrollPane = new JScrollPane();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(400, 400));
		setName("Form"); // NOI18N

		topP.setName("topP"); // NOI18N
		topP.setLayout(new BorderLayout());

		topP.add(mapList, BorderLayout.CENTER);

		topP.add(btnBrower, BorderLayout.LINE_END);

		getContentPane().add(topP, BorderLayout.PAGE_START);

		centerP.setLayout(new BorderLayout());

		GroupLayout eastPLayout = new GroupLayout(eastP);
		eastP.setLayout(eastPLayout);
		eastPLayout.setHorizontalGroup(eastPLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				eastPLayout.createSequentialGroup().addContainerGap().addGroup(
						eastPLayout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								btnOk, GroupLayout.DEFAULT_SIZE, 77,
								Short.MAX_VALUE).addGroup(
								eastPLayout.createParallelGroup(
										GroupLayout.Alignment.LEADING, false)
										.addComponent(l_x,
												GroupLayout.DEFAULT_SIZE, 64,
												Short.MAX_VALUE).addComponent(
												l_xpoint,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).addComponent(
												l_y, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).addComponent(
												l_ypoint,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)).addComponent(
								btnCancel, GroupLayout.Alignment.TRAILING,
								GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
						.addContainerGap()));
		eastPLayout.setVerticalGroup(eastPLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
						eastPLayout.createSequentialGroup().addContainerGap()
								.addComponent(l_x).addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(l_xpoint).addGap(13, 13, 13)
								.addComponent(l_y).addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(l_ypoint).addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED,
										117, Short.MAX_VALUE).addComponent(
										btnOk).addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnCancel).addGap(13, 13, 13)));

		centerP.add(eastP, BorderLayout.LINE_END);

		centerP.add(jScrollPane, BorderLayout.CENTER);

		getContentPane().add(centerP, BorderLayout.CENTER);
		
		
		File mapf = new File(MainFrame.OWNER.ProjectFullPath+File.separator+"Map");
		File [] childf = mapf.listFiles();
		for(File f : childf){
			if(!f.getName().startsWith(".")){
				mapList.addItem(f.getName());
			}
		}

		pack();
		btnBrower.addActionListener(this);
		btnCancel.addActionListener(this);
		btnOk.addActionListener(this);
		mapList.addActionListener(this);
		
	}

	public JButton btnBrower;
	private JButton btnCancel;
	private JButton btnOk;
	private JPanel centerP;
	private JPanel eastP;
	private JComboBox mapList;
	private JScrollPane jScrollPane;
	private JLabel l_x;
	private JLabel l_xpoint;
	private JLabel l_y;
	private JLabel l_ypoint;
	private JPanel topP;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnBrower){
			btnBrower.setEnabled(false);
			new NextMapDstFileChooserDlg(this);
		}
	}

}
