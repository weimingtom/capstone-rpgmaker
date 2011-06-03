package viewControl.editorDlg;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import userData.DeepCopier;
import viewControl.MainFrame;
import eventEditor.Event;
import eventEditor.EventEditorSystem;
import eventEditor.EventTile;
import eventEditor.exceptions.NotExistType;


// �ʿ��� �ڵ�ȭ �Լ�
// 1. ���� �ε��� �޾ƿ���
// 2. �÷��� ��� �޾ƿ���
// 3. �̺�Ʈ ��� �޾ƿ���
// 4. �г� �̹��� �޾ƿ���
// 5. �� �ڵ� ����
public class EventDlg extends EditorDlg implements ActionListener, MouseListener {

	private static final long serialVersionUID = 6090426854673494361L;
	
	private MainFrame owner;
	private EventEditorSystem eventEditsSys;	// EventEditorSystem �� Map�� ����ȴٸ� �� ������ �̿��� ���������Ӱ� ����ȴ�. 
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
		
		this.owner = parent;
		this.eventEditsSys = eventEditsSys;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.mapName = eventEditsSys.getName();
		this.eventTabPanelList = new LinkedList<EventEditPanel>();
		
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
		cb_objectType.addActionListener(this);
		
		// ���콺 �̺�Ʈ ����
		cb_objectType.addMouseListener(this);
		
		// �̺�Ʈ ������ ����. ���ο� �����͸� ���� �����ϰ� �ƴϸ� ������ �����Ϳ��� �ҷ��´�.
		if(isNew(eventEditsSys, startPoint, endPoint)) {
			addNewEvent();
		} else {
			try {
				cb_objectType.setSelectedIndex(getFirstEventTile().getObjectType());
				initEventPanel((EventTile)DeepCopier.deepCopy(getFirstEventTile()));
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// tp_eventTap�� �����Ѵ�.
		renewTabPanels(0);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp_eventTab.setPreferredSize(new java.awt.Dimension(682, 460));
		
		// ���̾ƿ� ����
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
					.addComponent(tp_eventTab, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
					.addGroup(layout.createSequentialGroup()
						.addComponent(btn_editFlagList)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 307, Short.MAX_VALUE)
						.addComponent(btn_OK, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_cancel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_apply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(37, 37, 37))
					.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
						.addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(l_mapName, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cb_objectType, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(btn_addEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_deleteEvent, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(btn_clearEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel1)
					.addComponent(l_mapName)
					.addComponent(btn_clearEvent)
					.addComponent(jLabel2)
					.addComponent(cb_objectType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(btn_deleteEvent)
					.addComponent(btn_addEvent))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(tp_eventTab, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
			if(eventEditsSys.getEventList(startPoint.y, startPoint.x) != null)
				return false;
			else
				return true;
		} else {
			return true;
		}
	}
 	
	private void initEventPanel(EventTile events) {
		// �̺�Ʈ�� ��� �����Ѵ�.
		
		// EventEditPanel�� �ϳ��� �����Ͽ� eventTabPanelList�� �ִ´�.
		List<Event> tmpEvents = events.getEventList();
		for (int i = 0; i < tmpEvents.size(); i++) {
			EventEditPanel addPanel = new EventEditPanel(owner, tmpEvents.get(i), cb_objectType.getSelectedIndex());
			eventTabPanelList.add(addPanel);
		}
	}
	
	private void addNewEvent() {
		// �̺�Ʈ �ϳ��� �����Ͽ� EventEditorSystem�� ����
		Event addEvent = new Event();
		// �� �̺�Ʈ�� �гο� �־��ش�.
		eventTabPanelList.add(new EventEditPanel(owner, addEvent, cb_objectType.getSelectedIndex()));
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
		while(!eventTabPanelList.isEmpty()) {
			eventTabPanelList.remove(0);
		}
		
		addNewEvent();
	}
	
	private void addTabPanel(Event event) {
		// ���޹��� event�� �гο� �ְ� eventTabPanelList�� �����Ѵ�.
		EventEditPanel addPanel = new EventEditPanel(owner, event, cb_objectType.getSelectedIndex());
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
		for (int i = 0; i < eventTabPanelList.size(); i++) {
			eventTabPanelList.get(i).renewConditionComboBox();
			eventTabPanelList.get(i).revalidate();
		}
		tp_eventTab.revalidate();
		this.repaint();
	}
	
	private Event getEventInTapPanel(int index) {
		return eventTabPanelList.get(index).getEvent();
	}
	private EventEditPanel getTapPanel(int index) {
		return eventTabPanelList.get(index);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_OK || e.getSource() == btn_apply) {
			// �����͸� ���Ϸ� �����Ѵ�. ���� eventEditsSys�� �̿��Ͽ� map ���� ������ �����Ѵ�.
			for (int i = startPoint.y; i < endPoint.y+1; i++) {
				for (int j = startPoint.x; j < endPoint.x+1; j++) {
					EventTile addEventTile = new EventTile(i, j);
					addEventTile.setObjectType(cb_objectType.getSelectedIndex());
					for (int k = 0; k < eventTabPanelList.size(); k++) {
						try {
							getEventInTapPanel(k).setActionType(getTapPanel(k).getActionType());
							getEventInTapPanel(k).setActorIndex(getTapPanel(k).getActorIndex());
							getEventInTapPanel(k).setStartType(getTapPanel(k).getStartType());
							getEventInTapPanel(k).setPreconditionFlag(0, getTapPanel(k).getCondition1());
							getEventInTapPanel(k).setPreconditionFlag(1, getTapPanel(k).getCondition2());
							getEventInTapPanel(k).setPreconditionFlag(2, getTapPanel(k).getCondition3());
							getEventInTapPanel(k).setDieChangeConditionIndex(getTapPanel(k).getDieConditionIndex());
							getEventInTapPanel(k).setDieChangeComditionState(getTapPanel(k).getDieConditionState());
						} catch (NotExistType e1) {
							e1.printStackTrace();
						}
						addEventTile.addEvent(getEventInTapPanel(k));
					}
					eventEditsSys.addEventTile(addEventTile);
				}
			}
			// btn_OK ��� â�� �ݴ´�.
			if(e.getSource() == btn_OK)	this.dispose();
			
		} else if (e.getSource() == btn_cancel) {
			this.dispose();
			
		} else if (e.getSource() == btn_addEvent) {
			addNewEvent();
			renewTabPanels(eventTabPanelList.size()-1);
			
		} else if (e.getSource() == btn_clearEvent) {
			// 0��° �гκ��� �����ϰ� �� �̺�Ʈ�� �ϳ� �����Ѵ�.
			clearEvents();
			// ���� �����Ѵ�.
			renewTabPanels(0);
			
		} else if (e.getSource() == btn_deleteEvent) {
			// ������ �г��� �ε���
			int indexComp = tp_eventTab.getSelectedIndex();
			// Event�� �����Ѵ�.
			deleteEvent(indexComp);
			// �̺�Ʈ�� 1���� ���ٸ� �� �̺�Ʈ�� �����Ѵ�.
			if(eventTabPanelList.size() == 0) addNewEvent();
			// ���� �����Ѵ�.
			renewTabPanels(indexComp);
			
		} else if (e.getSource() == btn_editFlagList) {
			new EditFlagListDlg(this.owner);
			// ������ flag name���� �� Tap Panel ���� ComboBox�� �����Ѵ�.
			for (int i = 0; i < eventTabPanelList.size(); i++)
				eventTabPanelList.get(i).renewConditionComboBox();
			renewConditionComboBoxImTapPanel();
			
		} else if(e.getSource() == cb_objectType) {
			for (int i = 0; i < eventTabPanelList.size(); i++) {
				eventTabPanelList.get(i).renewActorMenu(cb_objectType.getSelectedIndex());
			}
		}
	}
	
	private EventTile getFirstEventTile() {
		return eventEditsSys.getEventTile(startPoint.y, startPoint.x);
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
		if(e.getSource() == cb_objectType) {
			for (int i = 0; i < eventTabPanelList.size(); i++) {
				eventTabPanelList.get(i).renewActorMenu(cb_objectType.getSelectedIndex());
			}
		}
	}
}
