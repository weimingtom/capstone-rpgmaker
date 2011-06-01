package viewControl.dialogSet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import explorer.tree.FileTree;



/**
 * 
 * ������ ���� �ͽ��÷η� ��� �ٸ� ������ ���Դϴ�. ������ = ��η� ���� �Ŀ� ��δ� ���� ������ְ� ������ ���̰� �ϵ�����.
 */

public class NewProjFolderChooserDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = -3371265423275031322L;

	public NewProjFolderChooserDlg(NewProjectDlg parant) {
		super(parant);
	
		owner = parant;
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		tree = new FileTree();
		tree.setFileSelectionMode(FileTree.DIRECTORIES_ONLY);
		
		JPanel btnPanel = new JPanel(new GridLayout(1, 2));
		btn_ok = new JButton("O K");
		btn_Cancel = new JButton("Cancel");
		btnPanel.add(btn_ok);
		btnPanel.add(btn_Cancel);
		cp.add(new JScrollPane(tree), BorderLayout.CENTER);
		cp.add(btnPanel, BorderLayout.SOUTH);

		setSize(500, 500);
		setVisible(true);

		btn_ok.addActionListener(this);
		btn_Cancel.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_ok) {
			if (tree.getSelectionPath()!=null) {
				owner.setWorkspace(tree.getSelectionPath()
						.getLastPathComponent().toString());
				owner.btn_browser.setEnabled(true);
				dispose();
			}
		} else if (e.getSource() == btn_Cancel) {
			owner.btn_browser.setEnabled(true);
			dispose();
		}
	}

	private FileTree tree;
	private JButton btn_ok;
	private JButton btn_Cancel;
	private NewProjectDlg owner;
}
