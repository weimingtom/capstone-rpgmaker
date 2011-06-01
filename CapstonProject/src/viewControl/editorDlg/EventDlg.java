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


// �ʿ��� �ڵ�ȭ �Լ�
// 1. ���� �ε��� �޾ƿ���
// 2. �÷��� ��� �޾ƿ���
// 3. �̺�Ʈ ��� �޾ƿ���
// 4. �г� �̹��� �޾ƿ���
// 5. �� �ڵ� ����
public class EventDlg extends EditorDlg implements ActionListener {

	private static final long serialVersionUID = 6090426854673494361L;
	
	private EventEditorSystem eventEditsSys;	// EventEditorSystem �� Map�� ����ȴٸ� �� ������ �̿��� ���������Ӱ� ����ȴ�. 
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

	// EventEditorSystem �� Map ���Ͽ� ���Եȴٸ� �����ڵ� �����Ǿ�� �Ѵ�.
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
		
		// map �̸��� �����Ѵ�. ��, Ȯ���ڴ� �����Ѵ�.
		this.mapName = eventEditsSys.getName();
		
		// �г��� �����Ѵ�.
		createEventEditPanelList(tmpEventEditsSys.getEventTile(startPoint.y, startPoint.x));
		
		setSize(new Dimension(600, 700));
		setResizable(false);
		initComponents();
		setVisible(true);
		setModal(true);
	}
	
	private void initComponents() {
		// ���� �ʱ�ȭ
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
		
		// ��ư�� �׼��̺�Ʈ ����
		btn_OK.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_apply.addActionListener(this);
		btn_addEvent.addActionListener(this);
		btn_deleteEvent.addActionListener(this);
		btn_clearEvent.addActionListener(this);
		btn_editFlagList.addActionListener(this);
		
		// tp_eventTap�� �����Ѵ�.
		renewTabPanels(0);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp_eventTab.setPreferredSize(new java.awt.Dimension(682, 460));
		
		// ���̾ƿ� ����
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
//		// EventTile �ϳ��� �����ϰ� ���� ���� Event�� �߰��Ѵ�.
//		EventTile addEventTile = new EventTile(startPoint.y, startPoint.x);
//		
//		// eventEditsSys�� �����ϰ� EventTile �ϳ��� �����Ѵ�.
//		tmpEventEditsSys.addEventTile(addEventTile);
//	}
	
	private void createEventEditPanelList(EventTile events) {
		// ���ο� ����Ʈ�� �����Ѵ�. �����Ͱ� �־��ٸ� ��� ������.
		eventTabPanelList = new LinkedList<EventEditPanel>();
		
		// EventEditPanel�� �ϳ��� �����Ͽ� eventTabPanelList�� �ִ´�.
		List<Event> tmpEvents = events.getEventList();
		for (int i = 0; i < tmpEvents.size(); i++) {
			EventEditPanel addPanel = new EventEditPanel(tmpEvents.get(i));
			eventTabPanelList.add(addPanel);
		}
	}
	
	private void addNewEvent() {
		// �̺�Ʈ �ϳ��� �����Ͽ� EventEditorSystem�� ����
		Event addEvent = new Event();
		// �� �̺�Ʈ�� �гο� �־��ش�.
		addTabPanel(addEvent);
	}
	
	private void addEvent(Event addEvent) {
		addTabPanel(addEvent);
	}
	
	private void deleteEvent(int index) {
		// �ش� index�� �г��� �����Ѵ�.
		eventTabPanelList.remove(index);
		
		// ������ �гκ��� tp_eventTap�� �г��� �ٽ� �����Ѵ�.
		renewTabPanels(index);
	}
	
	private void clearEvents() {
		
	}

	private void addTabPanel(Event event) {
		// ���޹��� event�� �гο� �ְ� eventTabPanelList�� �����Ѵ�.
		EventEditPanel addPanel = new EventEditPanel(event);
		eventTabPanelList.add(addPanel);
		
		// ���� ������ �г��� tp_eventTap�� �ִ´�.
		tp_eventTab.addTab(new String("Event"+tp_eventTab.getTabCount()), addPanel);
	}
	
	private void renewTabPanels(int beginIndex) {
		int selectedIndex = 0;
		if(tp_eventTab!=null)
			selectedIndex = tp_eventTab.getSelectedIndex();
		
		// Panel���� �����Ѵ�.
		while(tp_eventTab.getTabCount() != beginIndex)
			tp_eventTab.remove(beginIndex);
		
		// �ٽ� Panel���� �����Ѵ�.
		for (int i = beginIndex; i < eventTabPanelList.size(); i++) {
			tp_eventTab.addTab(new String("Event"+i), eventTabPanelList.get(i));
		}
		
		// ���õǾ��ִ� Panel�� �����ϵ��� �Ѵ�.
		if(selectedIndex != -1 && selectedIndex < tp_eventTab.getTabCount())
			tp_eventTab.setSelectedIndex(selectedIndex);
	}
	
	private void renewConditionComboBoxImTapPanel() {
		// ������ Event�� �гο� ����
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
			// �����͸� ���Ϸ� �����Ѵ�. ���� EventEditorSystem�� Map�� ����ȴٸ� ���Ͽ� �������� �ʰ� ���� ���������ӿ� �Ѱ��ش�.
			
			// btn_OK ��� â�� �ݴ´�.
			if(e.getSource() == btn_OK)	this.dispose();
			
		} else if (e.getSource() == btn_cancel) {
			this.dispose();
			
		} else if (e.getSource() == btn_addEvent) {
			addNewEvent();
		} else if (e.getSource() == btn_clearEvent) {
			
		} else if (e.getSource() == btn_deleteEvent) {
			// �̹� �̺�Ʈ ����Ʈ�� �̺�Ʈ�� 1���ۿ� ���ٸ� ���� �ڿ� ���ο� �̺�Ʈ�� �����ϰ� �����Ѵ�.
//			if(eventTabPanelList.size() == 1) {
//				getEventList().add(new Event());
//				addTabPanel(getEventList().get(getEventList().size()-1));
//			}
			
			// ������ �г��� �ε���
			int indexComp = tp_eventTab.getSelectedIndex();
			// Event�� �����Ѵ�.
			deleteEvent(indexComp);
			// ���� �����Ѵ�.
			renewTabPanels(indexComp);
			
		} else if (e.getSource() == btn_editFlagList) {
			new EditFlagListDlg(this.owner);
			// ������ flag name���� �� Tap Panel ���� ComboBox�� �����Ѵ�.
			renewConditionComboBoxImTapPanel();
		}
	}
}
