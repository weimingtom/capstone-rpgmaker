package viewControl.editorDlg.eventContentDlg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import viewControl.MainFrame;
import eventEditor.Event;
import eventEditor.eventContents.ChangeBGMEvent;

public class ChangeBGMDlg extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// Variables declaration - do not modify
	private JButton btn_OK;
	private JButton btn_bgmSelect;
	private JButton btn_cancel;
	private JLabel jLabel1;
	private JTextField tf_filePath;
	private JTextField tf_volumn;
	// End of variables declaration
	
	private MainFrame owner;
	private Event event;
	private boolean isNew;
	private int index;
	
	public ChangeBGMDlg(MainFrame parent, Event event, boolean isNew, int index) {
		super(parent, "Change BGM Event");
		
		this.owner = parent;
		this.event = event;
		this.isNew = isNew;
		this.index = index;
		
		setResizable(false);
		setModal(true);
		initComponents();
		setVisible(true);
	}
	
	private void initComponents() {
		//컨포넌트 정의
		btn_OK = new JButton("OK");
		btn_cancel = new JButton("Cancel");
		btn_bgmSelect = new JButton("BGM Select");
		tf_filePath = new JTextField();
		tf_volumn = new JTextField("70", 3);
		jLabel1 = new JLabel("Volumn:");
		
		// 액션 이벤트
		btn_OK.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_bgmSelect.addActionListener(this);
		
		// isNew가 false면 event의 index번 데이터로 초기화
		if(!isNew) {
			tf_filePath.setText(((ChangeBGMEvent)(event.getEventContent(index))).getSrcFileName());
			tf_volumn.setText(new Integer(((ChangeBGMEvent)(event.getEventContent(index))).getVolumn()).toString());
		}

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		// 레이아웃 구성
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addGroup(layout.createSequentialGroup()
						.addComponent(btn_OK, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_cancel))
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(tf_filePath, GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
							.addGroup(layout.createSequentialGroup()
								.addComponent(jLabel1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(tf_volumn, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_bgmSelect)))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(tf_filePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(btn_bgmSelect))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel1)
					.addComponent(tf_volumn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
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
			// EventContent를 event에 삽입한다.
			if(!isNew)
				event.getEventContentList().remove(index);
			
			event.getEventContentList().add(index, new ChangeBGMEvent(tf_filePath.getText(),
																	  ".DefaultData" + File.separator + "Musics" + File.separator + tf_filePath.getText().substring(tf_filePath.getText().lastIndexOf(File.separator)+1),
																	  (new Integer(tf_volumn.getText())).intValue()));
			fileCopy(tf_filePath.getText(), MainFrame.OWNER.ProjectFullPath + File.separator + ".DefaultData" + File.separator + "Musics" + File.separator + tf_filePath.getText().substring(tf_filePath.getText().lastIndexOf(File.separator)+1));
			
			this.dispose();
		} else if(e.getSource() == btn_cancel) {
			this.dispose();
		} else if(e.getSource() == btn_bgmSelect) {
			// 파일의 경로를 가져와 임시 저장한다.
			// 파일 경로를 받기 위한 Chooser 생성
			JFileChooser fileChooser = new JFileChooser(MainFrame.OWNER.ProjectFullPath);

			// Chooser의 세부 설정
			fileChooser.setDialogTitle("Image File Chooser");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);

			// Chooser의 필터 설정
			FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
			fileChooser.setFileFilter(filter);

			// Chooser를 출력한다. 선택 여부가 returnName에 저장
			int returnName = fileChooser.showOpenDialog(owner);
			if (returnName == JFileChooser.APPROVE_OPTION) {
				// 파일이 선택되었으면 임시 이미지 패널에 출력을 수정한다.
				try {
					tf_filePath.setText(fileChooser.getSelectedFile().getCanonicalPath());
					tf_filePath.revalidate();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private boolean fileCopy(String src, String dst) {
		OutputStream out = null;
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(src)));
			out = new FileOutputStream(dst);

			int data = -1;
			while ((data = in.read()) != -1) {
				out.write(data);
			}
			out.close();
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (Exception e2) {
			e2.printStackTrace();
			return false;
		}
		
		return true;
	}
}
