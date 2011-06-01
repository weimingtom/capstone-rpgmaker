package viewControl.editorDlg;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import viewControl.MainFrame;
import characterEditor.NPCEditorSystem;
import characterEditor.exceptions.IllegalLevelNumber;
import equipment.Armors;
import equipment.Equipment;
import equipment.Weapons;

public class NewNPCDlg extends EditorDlg implements ActionListener  {

	private static final long serialVersionUID = 1L;
	
	private String loadFileName;
	private NPCEditorSystem npcEditSystem;
	private boolean isNew;
	
	private JButton btn_OK;
	private JButton btn_cancel;
	private JButton btn_moveUpAnimation;
	private JButton btn_moveDownAnimation;
	private JButton btn_moveRightAnimation;
	private JButton btn_moveLeftAnimation;
	private JButton btn_battleMoveUpAni;
	private JButton btn_battleMoveDownAni;
	private JButton btn_battleMoveRightAni;
	private JButton btn_battleMoveLeftAni;
	private JButton btn_attackUpAnimation;
	private JButton btn_attackDownAnimation;
	private JButton btn_attackRightAnimation;
	private JButton btn_attackLeftAnimation;
	private JButton btn_damageUpAnimation;
	private JButton btn_damageDownAnimation;
	private JButton btn_damageRightAnimation;
	private JButton btn_damageLeftAnimation;
	private JButton btn_skillAnimation;
	private JButton btn_dieAnimation;
	private JButton btn_eventAnimationList;
	
	private JTextField tf_charName;
	private JTextField tf_initLevel;
	private JTextField tf_maxLevel;
	
	private JTextField tf_initHP;
	private JTextField tf_initMP;
	private JTextField tf_initEXP;
	private JTextField tf_initStrength;
	private JTextField tf_initVitality;
	private JTextField tf_initIntelligence;
	private JTextField tf_initKnowledge;
	private JTextField tf_initAgility;
	
	private JTextField tf_growthCurveHP;
	private JTextField tf_growthCurveMP;
	private JTextField tf_growthCurveEXP;
	private JTextField tf_growthCurveStrength;
	private JTextField tf_growthCurveVitality;
	private JTextField tf_growthCurveIntelligence;
	private JTextField tf_growthCurveKnowledge;
	private JTextField tf_growthCurveAgility;
	
	private JTextField tf_speed;
	
	private JComboBox cb_indexChar;
	private JComboBox cb_indexJob;
	private JComboBox cb_weapon;
	private JComboBox cb_topArmor;
	private JComboBox cb_bottomsArmor;
	private JComboBox cb_helmetArmor;
	private JComboBox cb_shieldArmor;
	private JComboBox cb_accessoryArmor;
	
	public NewNPCDlg(MainFrame parent, boolean isNew, String fileName) {
		super(parent, "Edit Character");

		loadFileName = fileName;
		this.isNew = isNew;
		npcEditSystem = new NPCEditorSystem(projectPath);
		
		// ���Ӱ� �����ϴ� ���̸� charEditSystem�� �ʱ�ȭ�Ͽ� �����ϰ�
		// �׷��� �ʴٸ� ��θ� ���� Object�� �о�鿩 ������ �����Ѵ�.
		if (!isNew) {
			try {
				npcEditSystem.load(loadFileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		setSize(new Dimension(600, 700));
		setResizable(false);
		initComponents();
		setVisible(true);
		setModal(true);
	}

	private void initComponents() {
		
		// Button ����
		btn_OK = new JButton("O K");
		btn_cancel = new JButton("Cancel");
		btn_moveUpAnimation = new JButton("Move Up");
		btn_moveDownAnimation = new JButton("Move Down");
		btn_moveRightAnimation = new JButton("Move Right");
		btn_moveLeftAnimation = new JButton("Move Left");
		btn_battleMoveUpAni = new JButton("Battle Up");
		btn_battleMoveDownAni = new JButton("Battle Down");
		btn_battleMoveRightAni = new JButton("Battle Right");
		btn_battleMoveLeftAni = new JButton("Battle Left");
		btn_attackUpAnimation = new JButton("Attack Up");
		btn_attackDownAnimation = new JButton("Attack Down");
		btn_attackRightAnimation = new JButton("Attack Right");
		btn_attackLeftAnimation = new JButton("Attack Left");
		btn_damageUpAnimation = new JButton("Damage Up");
		btn_damageDownAnimation = new JButton("Damage Down");
		btn_damageRightAnimation = new JButton("Damage Right");
		btn_damageLeftAnimation = new JButton("Damage Left");
		btn_skillAnimation = new JButton("Skill");
		btn_dieAnimation = new JButton("Die");
		btn_eventAnimationList = new JButton("Event");
		
		btn_OK.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_moveUpAnimation.addActionListener(this);
		btn_moveDownAnimation.addActionListener(this);
		btn_moveRightAnimation.addActionListener(this);
		btn_moveLeftAnimation.addActionListener(this);
		btn_battleMoveUpAni.addActionListener(this);
		btn_battleMoveDownAni.addActionListener(this);
		btn_battleMoveRightAni.addActionListener(this);
		btn_battleMoveLeftAni.addActionListener(this);
		btn_attackUpAnimation.addActionListener(this);
		btn_attackDownAnimation.addActionListener(this);
		btn_attackRightAnimation.addActionListener(this);
		btn_attackLeftAnimation.addActionListener(this);
		btn_damageUpAnimation.addActionListener(this);
		btn_damageDownAnimation.addActionListener(this);
		btn_damageRightAnimation.addActionListener(this);
		btn_damageLeftAnimation.addActionListener(this);
		btn_skillAnimation.addActionListener(this);
		btn_dieAnimation.addActionListener(this);
		btn_eventAnimationList.addActionListener(this);
		
		// JTextField ����
		tf_charName = new JTextField(npcEditSystem.getName(), 15);
		tf_initLevel = new JTextField((new Integer(npcEditSystem.getInitLevel())).toString(), 3);
		tf_maxLevel = new JTextField((new Integer(npcEditSystem.getMaxLevel())).toString(), 3);
		
		tf_initHP = new JTextField((new Integer(npcEditSystem.getInitAbility().getHP())).toString(), 3);
		tf_initMP = new JTextField((new Integer(npcEditSystem.getInitAbility().getMP())).toString(), 3);
		tf_initEXP = new JTextField((new Integer(npcEditSystem.getInitAbility().getEXP())).toString(), 3);
		tf_initStrength = new JTextField((new Integer(npcEditSystem.getInitAbility().getStrength())).toString(), 3);
		tf_initVitality = new JTextField((new Integer(npcEditSystem.getInitAbility().getVitality())).toString(), 3);
		tf_initIntelligence = new JTextField((new Integer(npcEditSystem.getInitAbility().getIntelligence())).toString(), 3);
		tf_initKnowledge = new JTextField((new Integer(npcEditSystem.getInitAbility().getKnowledge())).toString(), 3);
		tf_initAgility = new JTextField((new Integer(npcEditSystem.getInitAbility().getAgility())).toString(), 3);
		
		tf_growthCurveHP = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getHP())).toString(), 3);
		tf_growthCurveMP = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getMP())).toString(), 3);
		tf_growthCurveEXP = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getEXP())).toString(), 3);
		tf_growthCurveStrength = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getStrength())).toString(), 3);
		tf_growthCurveVitality = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getVitality())).toString(), 3);
		tf_growthCurveIntelligence = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getIntelligence())).toString(), 3);
		tf_growthCurveKnowledge = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getKnowledge())).toString(), 3);
		tf_growthCurveAgility = new JTextField((new Integer(npcEditSystem.getGrowthCurve().getAgility())).toString(), 3);
		
		tf_speed = new JTextField((new Integer(npcEditSystem.getSpeed())).toString(), 3);
		
		// JComboBox
		cb_indexChar = setComboBoxList("character", 1000);
		
		cb_indexJob = setComboBoxList("job", 999);
		cb_indexJob.setSelectedIndex(npcEditSystem.getIndexJob());
		
		// ������ �ش��ϴ� ��� �о�´�.
		cb_weapon = setWeaponComboBoxList();
		cb_topArmor = setArmorComboBoxList(0);
		cb_bottomsArmor = setArmorComboBoxList(1);
		cb_helmetArmor = setArmorComboBoxList(2);
		cb_shieldArmor = setArmorComboBoxList(3);
		cb_accessoryArmor = setArmorComboBoxList(4);
		
		// ���ο� ���� �ƴϸ� ���޹��� ���Ͽ��� index�� �޾ƿ´�.
		if (!this.isNew) {
			cb_indexChar.setSelectedIndex(npcEditSystem.getIndex());
			cb_weapon.setSelectedIndex(getSelectedEquipment(0, 0, npcEditSystem.getIndexInitWeapon()));
			cb_topArmor.setSelectedIndex(getSelectedEquipment(1, 0, npcEditSystem.getIndexInitTopArmor()));
			cb_bottomsArmor.setSelectedIndex(getSelectedEquipment(1, 1, npcEditSystem.getIndexInitBottomsArmor()));
			cb_helmetArmor.setSelectedIndex(getSelectedEquipment(1, 2, npcEditSystem.getIndexInitHelmetArmor()));
			cb_shieldArmor.setSelectedIndex(getSelectedEquipment(1, 3, npcEditSystem.getIndexInitShieldArmor()));
			cb_accessoryArmor.setSelectedIndex(getSelectedEquipment(1, 4, npcEditSystem.getIndexInitAccessoryArmor()));
		}
		
		// �ɸ��� �̸�, �ʱⷹ��, �ִ뷹��
		JPanel p_charPanel = new JPanel();
		p_charPanel.setLayout(new GridBagLayout());
		
		JPanel p_charInfo = new JPanel();
		p_charInfo.setLayout(new GridBagLayout());
		addItem(p_charInfo, new JLabel("Index:"), 0,0,1,1, GridBagConstraints.EAST);
		addItem(p_charInfo, cb_indexChar, 1,0,2,1, GridBagConstraints.WEST);
		addItem(p_charInfo, new JLabel("Name:"), 2,0,1,1, GridBagConstraints.EAST);
		addItem(p_charInfo, tf_charName, 3,0,2,1, GridBagConstraints.WEST);
		addItem(p_charInfo, new JLabel("Motion Speed:"), 5,0,1,1, GridBagConstraints.EAST);
		addItem(p_charInfo, tf_speed, 6,0,2,1, GridBagConstraints.WEST);
		addItem(p_charInfo, new JLabel("Init Level:"), 0, 1, 1, 1, GridBagConstraints.EAST);
		addItem(p_charInfo, tf_initLevel, 1,1,1,1, GridBagConstraints.WEST);
		addItem(p_charInfo, new JLabel("Max Level:"), 2, 1, 1, 1, GridBagConstraints.EAST);
		addItem(p_charInfo, tf_maxLevel, 3,1,1,1, GridBagConstraints.WEST);
		addItem(p_charInfo, new JLabel("Job:"), 4, 1, 1, 1, GridBagConstraints.EAST);
		addItem(p_charInfo, cb_indexJob, 5,1,1,1, GridBagConstraints.WEST);
		
		// �ʱ� �ɷ�ġ
		JPanel p_initData = new JPanel();
		p_initData.setLayout(new GridBagLayout());
		addItem(p_initData, new JLabel("Init HP:"), 0,0,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initHP, 1,0,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init MP:"), 2,0,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initMP , 3,0,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init EXP:"), 0,1,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initEXP , 1,1,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init Strength:"), 2,1,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initStrength , 3,1,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init Vitality:"), 0,2,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initVitality , 1,2,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init Intelligence:"), 2,2,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initIntelligence , 3,2,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init Knowledge:"), 0,3,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initKnowledge , 1,3,1,1, GridBagConstraints.WEST);
		addItem(p_initData, new JLabel("Init Agility:"), 2,3,1,1, GridBagConstraints.EAST);
		addItem(p_initData, tf_initAgility , 3,3,1,1, GridBagConstraints.WEST);
		Box b_initData = Box.createVerticalBox();
		b_initData.setBorder(BorderFactory.createTitledBorder("Init Data"));
		b_initData.add(p_initData);
		
		// ���� �ɷ�ġ
		JPanel p_growthCurveData = new JPanel();
		p_growthCurveData.setLayout(new GridBagLayout());
		addItem(p_growthCurveData, new JLabel("Growth HP:"), 0,0,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveHP, 1,0,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth MP:"), 2,0,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveMP, 3,0,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth EXP:"), 0,1,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveEXP, 1,1,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth Strength:"), 2,1,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveStrength, 3,1,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth Vitality:"), 0,2,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveVitality, 1,2,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth Intelligence:"), 2,2,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveIntelligence, 3,2,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth Knowledge:"), 0,3,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveKnowledge, 1,3,1,1, GridBagConstraints.WEST);
		addItem(p_growthCurveData, new JLabel("Growth Agility:"), 2,3,1,1, GridBagConstraints.EAST);
		addItem(p_growthCurveData, tf_growthCurveAgility, 3,3,1,1, GridBagConstraints.WEST);
		Box b_growthCurveData = Box.createVerticalBox();
		b_growthCurveData.setBorder(BorderFactory.createTitledBorder("Growth Curve Data"));
		b_growthCurveData.add(p_growthCurveData);
		
		// �ִϸ��̼�
		JPanel p_animation = new JPanel();
		p_animation.setLayout(new GridBagLayout());
		addItem(p_animation, btn_moveUpAnimation, 0,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_moveDownAnimation, 1,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_moveRightAnimation, 2,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_moveLeftAnimation, 3,1,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_battleMoveUpAni, 0,2,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_battleMoveDownAni, 1,2,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_battleMoveRightAni, 2,2,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_battleMoveLeftAni, 3,2,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_attackUpAnimation, 0,3,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_attackDownAnimation, 1,3,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_attackRightAnimation, 2,3,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_attackLeftAnimation, 3,3,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_damageUpAnimation, 0,4,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_damageDownAnimation, 1,4,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_damageRightAnimation, 2,4,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_damageLeftAnimation, 3,4,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_skillAnimation, 0,5,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_dieAnimation, 1,5,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		addItem(p_animation, btn_eventAnimationList, 2,5,1,1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		Box b_animation = Box.createVerticalBox();
		b_animation.setBorder(BorderFactory.createTitledBorder("Animations"));
		b_animation.add(p_animation);
		
		// ���
		JPanel p_equipment = new JPanel();
		p_equipment.setLayout(new GridBagLayout());
		addItem(p_equipment, new JLabel("Weapon:"), 0,0,1,1, GridBagConstraints.EAST);
		addItem(p_equipment, cb_weapon, 1,0,1,1, GridBagConstraints.WEST);
		addItem(p_equipment, new JLabel("Top Armor:"), 0,1,1,1, GridBagConstraints.EAST);
		addItem(p_equipment, cb_topArmor, 1,1,1,1, GridBagConstraints.WEST);
		addItem(p_equipment, new JLabel("Bottoms Armor:"), 0,2,1,1, GridBagConstraints.EAST);
		addItem(p_equipment, cb_bottomsArmor, 1,2,1,1, GridBagConstraints.WEST);
		addItem(p_equipment, new JLabel("Helmet Armor:"), 0,3,1,1, GridBagConstraints.EAST);
		addItem(p_equipment, cb_helmetArmor, 1,3,1,1, GridBagConstraints.WEST);
		addItem(p_equipment, new JLabel("Shield Armor:"), 0,4,1,1, GridBagConstraints.EAST);
		addItem(p_equipment, cb_shieldArmor, 1,4,1,1, GridBagConstraints.WEST);
		addItem(p_equipment, new JLabel("Accessory Armor:"), 0,5,1,1, GridBagConstraints.EAST);
		addItem(p_equipment, cb_accessoryArmor, 1,5,1,1, GridBagConstraints.WEST);
		Box b_equipment = Box.createVerticalBox();
		b_equipment.setBorder(BorderFactory.createTitledBorder("Init Equipments"));
		b_equipment.add(p_equipment);
		
		// OK, Cancel ��ư
		JPanel p_complete = new JPanel();
		p_complete.setLayout(new GridBagLayout());
		addItem(p_complete, btn_OK, 0,0,1,1, GridBagConstraints.WEST);
		addItem(p_complete, btn_cancel, 1,0,1,1, GridBagConstraints.WEST);

		addItem(p_charPanel, p_charInfo, 0,0,3,1, GridBagConstraints.WEST);
		addItem(p_charPanel, b_initData, 0,1,1,1, GridBagConstraints.WEST);
		addItem(p_charPanel, b_growthCurveData, 1,1,2,1, GridBagConstraints.WEST);
		addItem(p_charPanel, b_animation, 0,2,2,1, GridBagConstraints.WEST);
		addItem(p_charPanel, b_equipment, 2,2,1,1, GridBagConstraints.WEST);
		addItem(p_charPanel, p_complete, 2,3,1,1, GridBagConstraints.EAST);
		
		this.add(p_charPanel);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_OK) {
			if(tf_charName.getText().compareTo("") != 0) {
				// �Է¹��� �����͸� ��ü �ȿ� �����Ѵ�.
				int initLevel = Integer.parseInt(tf_initLevel.getText());
				int maxLevel = Integer.parseInt(tf_maxLevel.getText());
				int initHP = Integer.parseInt(tf_initHP.getText());
				int initMP = Integer.parseInt(tf_initMP.getText());
				int initEXP = Integer.parseInt(tf_initEXP.getText());
				int initStrength = Integer.parseInt(tf_initStrength.getText());
				int initVitality = Integer.parseInt(tf_initVitality.getText());
				int initIntelligence = Integer.parseInt(tf_initIntelligence.getText());
				int initKnowledge = Integer.parseInt(tf_initKnowledge.getText());
				int initAgility = Integer.parseInt(tf_initAgility.getText());
				int increaseHP = Integer.parseInt(tf_growthCurveHP.getText());
				int increaseMP = Integer.parseInt(tf_growthCurveMP.getText());
				int increaseEXP = Integer.parseInt(tf_growthCurveEXP.getText());
				int increaseStrength = Integer.parseInt(tf_growthCurveStrength.getText());
				int increaseVitality = Integer.parseInt(tf_growthCurveVitality.getText());
				int increaseIntelligence = Integer.parseInt(tf_growthCurveIntelligence.getText());
				int increaseKnowledge = Integer.parseInt(tf_growthCurveKnowledge.getText());
				int increaseAgility = Integer.parseInt(tf_growthCurveAgility.getText());
				
				npcEditSystem.setIndex(cb_indexChar.getSelectedIndex());
				npcEditSystem.setName(tf_charName.getText());
				try {
					npcEditSystem.setInitLevel(initLevel);
					npcEditSystem.setMaxLevel(maxLevel);
				} catch (IllegalLevelNumber e1) {
					e1.printStackTrace();
				}
				npcEditSystem.setInitAbility(initHP, initMP, initEXP, initStrength, initVitality, initIntelligence, initKnowledge, initAgility);
				npcEditSystem.setGrowthCurve(increaseHP, increaseMP, increaseEXP, increaseStrength, increaseVitality, increaseIntelligence, increaseKnowledge, increaseAgility);
				
				npcEditSystem.setIndexJob(cb_indexJob.getSelectedIndex());
				npcEditSystem.setSpeed(new Integer(tf_speed.getText()));
				
				// ���� ���õ� �ε����� ����Ǵ� ���� �ƴ϶� ���õ� ����� �ε����� ����ȴ�.
				// �ٸ� �޺��ڽ��� �޸� ���õ� �ε����� ����� �ε����� �ٸ� �� �ֱ� �����̴�.
				if(cb_weapon.getSelectedItem() != null)
					npcEditSystem.setIndexInitWeapon(new Integer(((String)(cb_weapon.getSelectedItem())).substring(0, 3)));
				if(cb_topArmor.getSelectedItem() != null)
					npcEditSystem.setIndexInitTopArmor(new Integer(((String)(cb_topArmor.getSelectedItem())).substring(0, 3)));
				if(cb_bottomsArmor.getSelectedItem() != null)
					npcEditSystem.setIndexInitBottomsArmor(new Integer(((String)(cb_bottomsArmor.getSelectedItem())).substring(0, 3)));
				if(cb_helmetArmor.getSelectedItem() != null)
					npcEditSystem.setIndexInitHelmetArmor(new Integer(((String)(cb_helmetArmor.getSelectedItem())).substring(0, 3)));
				if(cb_shieldArmor.getSelectedItem() != null)
					npcEditSystem.setIndexInitShieldArmor(new Integer(((String)(cb_shieldArmor.getSelectedItem())).substring(0, 3)));
				if(cb_accessoryArmor.getSelectedItem() != null)
					npcEditSystem.setIndexInitAccessoryArmor(new Integer(((String)(cb_accessoryArmor.getSelectedItem())).substring(0, 3)));
	
				// ��ü�� ���Ϸ� �����Ѵ�. ���������� ���� �ε����� ������ ���� �������ش�.
				result = npcEditSystem.save();
				
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Not Exist Object Name!", "Waring", JOptionPane.WARNING_MESSAGE);
			}
			
		} else if (e.getSource() == btn_cancel) {
			dispose();
			
		}  else if (e.getSource() == btn_moveUpAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getMoveUpAnimation());
			
		} else if (e.getSource() == btn_moveDownAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getMoveDownAnimation());
			
		} else if (e.getSource() == btn_moveRightAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getMoveRightAnimation());
			
		} else if (e.getSource() == btn_moveLeftAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getMoveLeftAnimation());
			
		} else if (e.getSource() == btn_battleMoveUpAni) {
			new SetAnimationDlg(owner, npcEditSystem.getBattleMoveUpAni());
			
		} else if (e.getSource() == btn_battleMoveDownAni) {
			new SetAnimationDlg(owner, npcEditSystem.getBattleMoveDownAni());
			
		} else if (e.getSource() == btn_battleMoveRightAni) {
			new SetAnimationDlg(owner, npcEditSystem.getBattleMoveRightAni());
			
		} else if (e.getSource() == btn_battleMoveLeftAni) {
			new SetAnimationDlg(owner, npcEditSystem.getBattleMoveLeftAni());
			
		} else if (e.getSource() == btn_attackUpAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getAttackUpAnimation());
			
		} else if (e.getSource() == btn_attackDownAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getAttackDownAnimation());
			
		} else if (e.getSource() == btn_attackRightAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getAttackRightAnimation());
			
		} else if (e.getSource() == btn_attackLeftAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getAttackLeftAnimation());
			
		} else if (e.getSource() == btn_damageUpAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getDamageUpAnimation());
			
		} else if (e.getSource() == btn_damageDownAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getDamageDownAnimation());
			
		} else if (e.getSource() == btn_damageRightAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getDamageRightAnimation());
			
		} else if (e.getSource() == btn_damageLeftAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getDamageLeftAnimation());
			
		} else if (e.getSource() == btn_skillAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getSkillAnimation());
			
		} else if (e.getSource() == btn_dieAnimation) {
			new SetAnimationDlg(owner, npcEditSystem.getDieAnimation());
			
		}  else if (e.getSource() == btn_eventAnimationList) {
			EventAniDlg dlg = new EventAniDlg(owner, "Edit Event Animation List", npcEditSystem.getEventAnimationList());
			if (npcEditSystem.getEventAnimationList().size() == 0)
				npcEditSystem.setEventAnimationList(dlg.getEventAnimationList());
		}
	}

	public NPCEditorSystem getNpcEditSystem() {
		return npcEditSystem;
	}
	
	// ���� ���õ� ������ ������ �� �ִ� ���⸦ �о�´�.
	private JComboBox setWeaponComboBoxList() {
		String[] titles = new String[999];
		JComboBox cb_returnList = new JComboBox();
		// �ش��ϴ� ������ ������ �ľ��Ͽ� �����Ѵ�.
		int length = 0;
		
		// ���� ������ ����.
		File folder = new File(projectPath+"\\Weapon");
		if (folder.exists()) {
			// ���� ���� ���� ��� ������ �о�´�.
			File[] fileList = folder.listFiles();
			
			// ���⸦ �ϳ��� ���캸�� ���� �ӽ� ����
			Weapons weapon = new Weapons(projectPath);
			
			// ���⸦ �ϳ��� ����� ������ �ε����� ������ ���� �̸��� �����Ѵ�.
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].getName().charAt(0) == '.')	continue;
				if (fileList[i].getName().charAt(0) >= '0' && fileList[i].getName().charAt(0) <= '9') {
					// ���⸦ �����.
					try {
						weapon.load(fileList[i].getCanonicalPath());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// ���� �ε����� ������ Ȯ���Ѵ�.
					if(weapon.getIndexJob() == this.cb_indexJob.getSelectedIndex()) {
						// �ε����� ���ٸ� ���� �̸� ����.
						titles[length] = fileList[i].getName().substring(0, fileList[i].getName().lastIndexOf("."));
						length++;
					}
				}
			}
		}
		
		// object �̸� �迭�� cb_returnList�� �����Ѵ�.
		for (int i = 0; i < length; i++) {
			cb_returnList.addItem(titles[i]);
		}
		
		return cb_returnList;
	}
	
	// ���� ���õ� ������ ������ �� �ִ� ���� �о�´�.
	private JComboBox setArmorComboBoxList(int armorType) {
		String[] titles = new String[999];
		JComboBox cb_returnList = new JComboBox();
		// �ش��ϴ� ���� ������ �ľ��Ͽ� �����Ѵ�.
		int length = 0;
		
		// �� ������ ����.
		File folder = new File(projectPath+"\\Armor");
		if (folder.exists()) {
			// �� ���� ���� ��� ������ �о�´�.
			File[] fileList = folder.listFiles();
			
			// ���� �ϳ��� ���캸�� ���� �ӽ� ����
			Armors armor = new Armors(projectPath);
			
			// ���� �ϳ��� ����� ������ �ε����� Ÿ���� ������ ���� �̸��� �����Ѵ�.
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].getName().charAt(0) == '.')	continue;
				if (fileList[i].getName().charAt(0) >= '0' && fileList[i].getName().charAt(0) <= '9') {
					// ���� �����.
					try {
						armor.load(fileList[i].getCanonicalPath());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// ���� �ε����� ������ Ȯ���Ѵ�.
					if(armor.getIndexJob() == this.cb_indexJob.getSelectedIndex()
							&& armor.getEquipmentType() == armorType) {
						// �ε����� Ÿ���� ���ٸ� ���� �̸� ����.
						titles[length] = fileList[i].getName().substring(0, fileList[i].getName().lastIndexOf("."));
						length++;
					}
				}
			}
		}
		
		// object �̸� �迭�� cb_returnList�� �����Ѵ�.
		for (int i = 0; i < length; i++) {
			cb_returnList.addItem(titles[i]);
		}
		
		return cb_returnList;
	}
	
	// �ش� index�� ����� JComboBox ���� �ε����� ��ȯ�Ѵ�.
	// equipType=0:����, equipType=1:��
	// armorType=0:����, armorType=1:����, armorType=2:���, armorType=3:����, armorType=4:�Ǽ����� 
	private int getSelectedEquipment(int equipType, int armorType, int equipmentIndex) {
		Equipment equip;
		String folderPath;
		
		// ��� �ӽ÷� �����ϰ� �ش��ϴ� ������ ��θ� �����Ѵ�.
		if (equipType == 0) {
			equip = new Weapons(this.projectPath);
			folderPath = new String(this.projectPath + "\\weapon");
		} else if (equipType == 1) {
			equip = new Armors(this.projectPath);
			folderPath = new String(this.projectPath + "\\armor");
		} else {
			System.err.println("error: NewCharacterDlg.getSelectedEquipment() (equipType: " + equipType + ")");
			return 0;
		}
		
		// ��� ���� ���� ����Ʈ�� ��´�.
		File[] fileList = new File(folderPath).listFiles();
		
		// �ش� ����� ���� Ÿ�԰� ������ ��� �ľ��Ͽ� JComboBox�� SelectedImdex�� �����Ѵ�.
		int indexComboBox = -1;
		for (int i=0; i < fileList.length; i++) {
			if (fileList[i].getName().charAt(0) == '.') continue;
			
			
			if (equipType == 0) {
				// ������ ���
				try {
					((Weapons)equip).load(fileList[i].getAbsolutePath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				if(npcEditSystem.getIndexJob() == ((Weapons)equip).getIndexJob())
					indexComboBox++;
				
				// �о�� ������ index�� equipmentIndex�� ������ ����
				if (equipmentIndex == ((Weapons)equip).getIndex())
					break;
				
			} else {
				// ���� ���
				try {
					((Armors)equip).load(fileList[i].getAbsolutePath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				if (   armorType == ((Armors)equip).getEquipmentType()
					&& npcEditSystem.getIndexJob() == ((Armors)equip).getIndexJob())
						indexComboBox++;
				
				// �о�� ������ index�� equipmentIndex�� ������ ����
				if (equipmentIndex == ((Armors)equip).getIndex())
					break;
				
			}
		}
		
		return indexComboBox;
	}
}
