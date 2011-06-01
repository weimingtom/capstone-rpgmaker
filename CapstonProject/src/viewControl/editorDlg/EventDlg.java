package viewControl.editorDlg;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import userData.DeepCopier;
import viewControl.MainFrame;
import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.EventTile;


// 필요한 자동화 함수
// 1. 액터 인덱스 받아오기
// 2. 플래그 목록 받아오기
// 3. 이벤트 목록 받아오기
// 4. 패널 이미지 받아오기
// 5. 탭 자동 생성
public class EventDlg extends EditorDlg implements ActionListener {

	private static final long serialVersionUID = 6090426854673494361L;
	
	private EventEditorSystem eventEditsSys;	// EventEditorSystem 이 Map에 저장된다면 이 변수를 이용해 메인프레임과 연결된다. 
//	private EventEditorSystem tmpEventEditsSys;
	private List<EventEditPanel> eventTabPanelList;
	private String mapName;
	private Point startPoint;
	private Point endPoint;
	
	// Variables declaration - do not modify
	private JButton btn_OK;
	private JButton btn_addEvent;
	private JButton btn_apply;
	private JButton btn_cancel;
	private JButton btn_clearEvent;
	private JButton btn_deleteEvent;
	private JButton btn_editFlagList;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel l_mapName;
	private JComboBox cb_objectType;
	private JTabbedPane tp_eventTab;
	// End of variables declaration

	// EventEditorSystem 이 Map 파일에 포함된다면 생성자도 수정되어야 한다.
	public EventDlg(MainFrame parent, Point startPoint, Point endPoint, EventEditorSystem eventEditsSys) {
		super(parent, "Event Editor");
		
		this.eventEditsSys = eventEditsSys;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		EventEditorSystem tmpEventEditsSys = null;
		try {
			tmpEventEditsSys = (EventEditorSystem)(DeepCopier.deepCopy(eventEditsSys));
			if(isNew(tmpEventEditsSys, startPoint, endPoint)) {
//				addNewEventTile();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// map 이름을 저장한다. 단, 확장자는 제거한다.
		this.mapName = eventEditsSys.getName();
		
		// 패널을 생성한다.
		createEventEditPanelList(tmpEventEditsSys.getEventTile(startPoint.y, startPoint.x));
		
		setSize(new Dimension(600, 700));
		setResizable(false);
		initComponents();
		setVisible(true);
		setModal(true);
	}
	
	private void initComponents() {
		// 변수 초기화
		btn_OK = new JButton("O K");
		btn_cancel = new JButton("Cancel");
		btn_apply = new JButton("Apply");
		btn_addEvent = new JButton("Add Event");
		btn_deleteEvent = new JButton("Delete Event");
		btn_clearEvent = new JButton("Clear Event");
		btn_editFlagList = new JButton("Edit Flag List");
		tp_eventTab = new JTabbedPane();
		jLabel1 = new JLabel("Map Name : ");
		jLabel2 = new JLabel("Event Tile Type: ");
		l_mapName = new JLabel(mapName);
		cb_objectType = new JComboBox(new DefaultComboBoxModel(new String[] { "None", "NPC", "Monster" }));
		
		// 버튼의 액션이벤트 정의
		btn_OK.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_apply.addActionListener(this);
		btn_addEvent.addActionListener(this);
		btn_deleteEvent.addActionListener(this);
		btn_clearEvent.addActionListener(this);
		btn_editFlagList.addActionListener(this);
		
		// tp_eventTap을 정의한다.
		renewTabPanels(0);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp_eventTab.setPreferredSize(new java.awt.Dimension(682, 460));
		
		// 레이아웃 구성
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addComponent(tp_eventTab, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
					.addGroup(layout.createSequentialGroup()
						.addComponent(btn_editFlagList)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 307, Short.MAX_VALUE)
						.addComponent(btn_OK, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_cancel)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_apply, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
						.addGap(37, 37, 37))
					.addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
						.addComponent(jLabel1)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(l_mapName, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel2)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_objectType, 0, 92, Short.MAX_VALUE)
						.addGap(20, 20, 20)
						.addComponent(btn_addEvent)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_deleteEvent, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_clearEvent, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel1)
					.addComponent(l_mapName)
					.addComponent(btn_clearEvent)
					.addComponent(btn_deleteEvent)
					.addComponent(btn_addEvent)
					.addComponent(jLabel2)
					.addComponent(cb_objectType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(tp_eventTab, GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btn_apply)
					.addComponent(btn_cancel)
					.addComponent(btn_OK)
					.addComponent(btn_editFlagList))
				.addContainerGap())
		);

		pack();
	}
	
 	private boolean isNew(EventEditorSystem eventEditsSys, Point startPoint, Point endPoint) {
		if(startPoint.x == endPoint.x && startPoint.y == endPoint.y){
			if(eventEditsSys.getEventList(endPoint.y, startPoint.y) != null)
				return false;
			else
				return true;
		} else {
			return true;
		}
	}
	
//	private void addNewEventTile() {
//		// EventTile 하나를 생성하고 새로 만든 Event를 추가한다.
//		EventTile addEventTile = new EventTile(startPoint.y, startPoint.x);
//		
//		// eventEditsSys를 정의하고 EventTile 하나를 삽입한다.
//		tmpEventEditsSys.addEventTile(addEventTile);
//	}
	
	private void createEventEditPanelList(EventTile events) {
		// 새로운 리스트를 생성한다. 데이터가 있었다면 모두 버린다.
		eventTabPanelList = new LinkedList<EventEditPanel>();
		
		// EventEditPanel을 하나씩 생성하여 eventTabPanelList에 넣는다.
		List<Event> tmpEvents = events.getEventList();
		for (int i = 0; i < tmpEvents.size(); i++) {
			EventEditPanel addPanel = new EventEditPanel(tmpEvents.get(i));
			eventTabPanelList.add(addPanel);
		}
	}
	
	private void addNewEvent() {
		// 이벤트 하나를 생성하여 EventEditorSystem에 삽입
		Event addEvent = new Event();
		// 새 이벤트를 패널에 넣어준다.
		addTabPanel(addEvent);
	}
	
	private void addEvent(Event addEvent) {
		addTabPanel(addEvent);
	}
	
	private void deleteEvent(int index) {
		// 해당 index의 패널을 삭제한다.
		eventTabPanelList.remove(index);
		
		// 삭제한 패널부터 tp_eventTap의 패널을 다시 삽입한다.
		renewTabPanels(index);
	}
	
	private void clearEvents() {
		
	}

	private void addTabPanel(Event event) {
		// 전달받은 event를 패널에 넣고 eventTabPanelList에 삽입한다.
		EventEditPanel addPanel = new EventEditPanel(event);
		eventTabPanelList.add(addPanel);
		
		// 새로 생성한 패널을 tp_eventTap에 넣는다.
		tp_eventTab.addTab(new String("Event"+tp_eventTab.getTabCount()), addPanel);
	}
	
	private void renewTabPanels(int beginIndex) {
		int selectedIndex = 0;
		if(tp_eventTab!=null)
			selectedIndex = tp_eventTab.getSelectedIndex();
		
		// Panel들을 삭제한다.
		while(tp_eventTab.getTabCount() != beginIndex)
			tp_eventTab.remove(beginIndex);
		
		// 다시 Panel들을 삽입한다.
		for (int i = beginIndex; i < eventTabPanelList.size(); i++) {
			tp_eventTab.addTab(new String("Event"+i), eventTabPanelList.get(i));
		}
		
		// 선택되어있던 Panel을 선택하도록 한다.
		if(selectedIndex != -1 && selectedIndex < tp_eventTab.getTabCount())
			tp_eventTab.setSelectedIndex(selectedIndex);
	}
	
	private void renewConditionComboBoxImTapPanel() {
		// 생성한 Event를 패널에 전달
//		for (int i = 0; i < getEventList().size(); i++) {
//			eventTabPanelList.get(i).renewConditionComboBox();
//			eventTabPanelList.get(i).revalidate();
//		}
		tp_eventTab.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_OK || e.getSource() == btn_apply) {
			// 데이터를 파일로 저장한다. 만약 EventEditorSystem이 Map에 저장된다면 파일에 저장하지 않고 단지 메인프레임에 넘겨준다.
			
			// btn_OK 라면 창을 닫는다.
			if(e.getSource() == btn_OK)	this.dispose();
			
		} else if (e.getSource() == btn_cancel) {
			this.dispose();
			
		} else if (e.getSource() == btn_addEvent) {
			addNewEvent();
		} else if (e.getSource() == btn_clearEvent) {
			
		} else if (e.getSource() == btn_deleteEvent) {
			// 이미 이벤트 리스트에 이벤트가 1개밖에 없다면 새로 뒤에 새로운 이벤트를 삽입하고 삭제한다.
//			if(eventTabPanelList.size() == 1) {
//				getEventList().add(new Event());
//				addTabPanel(getEventList().get(getEventList().size()-1));
//			}
			
			// 삭제할 패널의 인덱스
			int indexComp = tp_eventTab.getSelectedIndex();
			// Event를 삭제한다.
			deleteEvent(indexComp);
			// 탭을 갱신한다.
			renewTabPanels(indexComp);
			
		} else if (e.getSource() == btn_editFlagList) {
			new EditFlagListDlg(this.owner);
			// 수정된 flag name으로 각 Tap Panel 안의 ComboBox를 갱신한다.
			renewConditionComboBoxImTapPanel();
		}
	}
}
