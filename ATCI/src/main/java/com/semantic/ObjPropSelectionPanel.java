package com.semantic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ObjPropSelectionPanel extends JPanel {

	private JPanel innerArrangement_1, innerArrangement_2, innerArrangement_3;
	
	InterfaceConfiguration IC = new InterfaceConfiguration();
	
	JButton btnGetObjProp, btnAddProperty, btnRemoveProperty;
	JComboBox combo_ObjProp, combo_Domain, combo_Range;
	JScrollPane scrollPane_ObjProp, scrollPane_Domain, scrollPane_Range;
	JList list_ObjProp, list_Domain, list_Range;
	DefaultListModel dlm_ObjProp, dlm_Domain, dlm_Range;
	
	ClassSelectionPanel class_Selection_Panel;
	
	EntrySet<String, String, String> property_Set;
	
	LinkedList<String[]> selectedObjPropList;
	LinkedList<String> graph_list;
	
	InfoEntryMain iem;
	
	ObjPropSelectionPanel(ClassSelectionPanel cs_Panel){
		
		class_Selection_Panel = cs_Panel;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		initializeComponents();
		
		//add(innerArrangement_1, BorderLayout.SOUTH);
		add(btnGetObjProp);
		add( Box.createVerticalStrut(50) );
		add(innerArrangement_1);
		add( Box.createVerticalStrut(10) );
		add(innerArrangement_2);
		add( Box.createVerticalStrut(10) );
		add(innerArrangement_3);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setVisible(true);
		
		combo_ObjProp.addActionListener(new TheHandler());
		
		btnGetObjProp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				LinkedList<String> obj_Properties = new LinkedList<String>();
				
				iem = class_Selection_Panel.getInfoEntryMain();
				//iem.set_Current_Entry_Values( class_Selection_Panel.getEntryLabel(), class_Selection_Panel.getEntryIndex() );
				
				LinkedList<String> classes = class_Selection_Panel.getSelectedClasses();
				
				//VERY IMPORTANT: to set the CLass Hierarchy that will be used for selecting property domains and ranges
				iem.initializeClassHierarchy( class_Selection_Panel.getClassHierarchy() );
				iem.initializeClassesForThisConxext( class_Selection_Panel.getCompleteClassList() );
				
				graph_list = class_Selection_Panel.getGraphList();
				
				property_Set = iem.explore_ObjectProperties( classes, graph_list );
				
				Iterator relation_Itor = property_Set.getIterator();
				
				while(relation_Itor.hasNext()) {
					
					Map.Entry relationPair = (Map.Entry) relation_Itor.next();
					
					obj_Properties.add( relationPair.getKey().toString() );
				}
				
				combo_ObjProp.setModel( new DefaultComboBoxModel( obj_Properties.toArray() ) );
				
//				String index = Integer.toString(combo_entryList.getSelectedIndex());
//				String selected = String.format("Selected Entry: %s", combo_entryList.getSelectedItem());
//				
//				lblMsgLabel.setText(selected);
//				

//				
//				LinkedList<String> class_list = iem.main(index);
//				
//				objectPropertyPanel.populateListChoose(class_list);
			}
		});
		
		btnAddProperty.addActionListener(new TheHandler());
		btnRemoveProperty.addActionListener(new TheHandler());
		
		list_ObjProp.addListSelectionListener( new TheListHandler() );
		list_Domain.addListSelectionListener( new TheListHandler() );
		list_Range.addListSelectionListener( new TheListHandler() );
	}

	
	private void initializeComponents() {
		// TODO Auto-generated method stub
		
//		//Initialization of inner arrangement panel
//		innerArrangement_1 = new JPanel();
//		innerArrangement_1.setLayout(new BoxLayout(innerArrangement_1, BoxLayout.Y_AXIS));
//		innerArrangement_1.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		innerArrangement_1 = new JPanel();
		innerArrangement_1.setLayout(new BoxLayout(innerArrangement_1, BoxLayout.X_AXIS));
		innerArrangement_1.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		innerArrangement_2 = new JPanel();
		innerArrangement_2.setLayout(new BoxLayout(innerArrangement_2, BoxLayout.X_AXIS));
		innerArrangement_2.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		innerArrangement_3 = new JPanel();
		innerArrangement_3.setLayout(new BoxLayout(innerArrangement_3, BoxLayout.X_AXIS));
		innerArrangement_3.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		
		
		//Initialization of Default List Models for selected Object Properties, domains and ranges...
		dlm_Domain = new DefaultListModel();
		dlm_ObjProp = new DefaultListModel();
		dlm_Range = new DefaultListModel();
		
		
		
		//Initialization of selection entry ComboBoxes
		combo_ObjProp = new JComboBox();
		combo_ObjProp.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_ObjProp.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/28) );
		
		combo_Domain = new JComboBox();
		combo_Domain.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_Domain.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/28) );
		
		combo_Range = new JComboBox();
		combo_Range.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_Range.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/28) );
		
		
		//Initialization of button to get Object properties based on selected classes.
		btnGetObjProp = new JButton("Get Object Properties");
		btnGetObjProp.setToolTipText("Get Required Object Properties from Triplestore based on given Classes");
		btnGetObjProp.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnGetObjProp.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		btnGetObjProp.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Initialization of button to Add and Remove Object properties based on selected classes.
		btnAddProperty = new JButton("Add Relation (+)");
		btnAddProperty.setToolTipText("Get Required Object Properties from Triplestore based on given Classes");
		btnAddProperty.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnAddProperty.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		//btnAddProperty.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btnRemoveProperty = new JButton("Remove Relation (-)");
		btnRemoveProperty.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnRemoveProperty.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		
		
		
		//Initialization of lists
		list_ObjProp = new JList();
		list_ObjProp.setFont(new Font("Tahoma", Font.PLAIN, 28));
		list_ObjProp.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		list_ObjProp.setVisibleRowCount(8);
		
		list_Domain = new JList();
		list_Domain.setFont(new Font("Tahoma", Font.PLAIN, 28));
		list_Domain.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		list_Domain.setVisibleRowCount(8);
		
		list_Range = new JList();
		list_Range.setFont(new Font("Tahoma", Font.PLAIN, 28));
		list_Range.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		list_Range.setVisibleRowCount(8);
				
		
		//Initialization of Scroll Panes
		scrollPane_ObjProp = new JScrollPane();
		scrollPane_ObjProp.setViewportView(list_ObjProp);
		scrollPane_ObjProp.setPreferredSize(new Dimension(950,500));
		
		scrollPane_Domain = new JScrollPane();
		scrollPane_Domain.setViewportView(list_Domain);
		scrollPane_Domain.setPreferredSize(new Dimension(950,500));
		
		scrollPane_Range = new JScrollPane();
		scrollPane_Range.setViewportView(list_Range);
		scrollPane_Range.setPreferredSize(new Dimension(950,500));
		
		
		//Populating Arrangements
		innerArrangement_1.add( combo_Domain );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( combo_ObjProp );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( combo_Range );
		innerArrangement_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		innerArrangement_2.add(btnAddProperty);
		innerArrangement_2.add( Box.createHorizontalStrut(50) );
		innerArrangement_2.add(btnRemoveProperty);
		innerArrangement_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		innerArrangement_3.add( scrollPane_Domain );
		innerArrangement_3.add( Box.createHorizontalStrut(50) );
		innerArrangement_3.add( scrollPane_ObjProp );
		innerArrangement_3.add( Box.createHorizontalStrut(50) );
		innerArrangement_3.add( scrollPane_Range );
		innerArrangement_3.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//---> Here this code is added so the graph list is set at initial level, so that if
		//---> someone tries to get the data properties first, then graph list should be
		//---> available at that point....
		graph_list = class_Selection_Panel.getGraphList();
	}
	
	private class TheHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if( e.getSource() == combo_ObjProp ) {
			
				if(combo_ObjProp.getItemCount()>0) {
					
					String selected_Property = combo_ObjProp.getSelectedItem().toString();
					
					Iterator d_r_Itor = property_Set.get( selected_Property ).entrySet().iterator();
					
					LinkedList<String> fetched_d_List = new LinkedList<String>();
					LinkedList<String> fetched_r_List = new LinkedList<String>();
					
					while( d_r_Itor.hasNext() ) {
						
						Map.Entry edgePair = (Map.Entry) d_r_Itor.next();
						
						fetched_d_List = populateIndividualListBasedOnClasses(
																iem.getSubCLassesOf( edgePair.getKey().toString(), fetched_d_List )
																);
						
						fetched_r_List = populateIndividualListBasedOnClasses(
																iem.getSubCLassesOf( edgePair.getValue().toString(), fetched_r_List )
																);
					}
					
					combo_Domain.setModel( new DefaultComboBoxModel( fetched_d_List.toArray() ) );
					
					combo_Range.setModel( new DefaultComboBoxModel( fetched_r_List.toArray() ) );
				}
			
			}else if( e.getSource() == btnAddProperty ) {
				
				dlm_Domain.addElement( combo_Domain.getSelectedItem().toString() );
				dlm_ObjProp.addElement( combo_ObjProp.getSelectedItem().toString() );
				dlm_Range.addElement( combo_Range.getSelectedItem().toString() );
				
				list_Domain.setModel(dlm_Domain);
				list_ObjProp.setModel(dlm_ObjProp);
				list_Range.setModel(dlm_Range);
				
				updateSelectedObjPropList();
				
			}else if( e.getSource() == btnRemoveProperty ) {
				
				dlm_Domain.remove( list_Domain.getSelectedIndex() );
				dlm_ObjProp.remove( list_ObjProp.getSelectedIndex() );
				dlm_Range.remove( list_Range.getSelectedIndex() );
				
				list_Domain.setModel(dlm_Domain);
				list_ObjProp.setModel(dlm_ObjProp);
				list_Range.setModel(dlm_Range);
				
				updateSelectedObjPropList();
			}
		}
		
	}
	
	private class TheListHandler implements ListSelectionListener{

		public void valueChanged(ListSelectionEvent event) {
			// TODO Auto-generated method stub
			
			if( event.getSource() == list_Domain ) {
				
				list_ObjProp.setSelectedIndex( list_Domain.getSelectedIndex() );
				list_Range.setSelectedIndex( list_Domain.getSelectedIndex() );
				
			}else if( event.getSource() == list_ObjProp ) {
				
				list_Domain.setSelectedIndex( list_ObjProp.getSelectedIndex() );
				list_Range.setSelectedIndex( list_ObjProp.getSelectedIndex() );
				
			}else if( event.getSource() == list_Range ) {
				
				list_Domain.setSelectedIndex( list_Range.getSelectedIndex() );
				list_ObjProp.setSelectedIndex( list_Range.getSelectedIndex() );
				
			}
		}
	}
	
	InfoEntryMain getInfoEntryMain(){
		
		return iem;
	}
	
	LinkedList<String> getGraphList(){
		
		return graph_list;
	}
	
	LinkedList<String> getSelectedClasses(){
		
		return class_Selection_Panel.getSelectedClasses();
	}
	
	LinkedList<String[]> getSelectedObjProp(){
		
		return selectedObjPropList;
	}
	
	LinkedList<String> populateIndividualListBasedOnClasses(LinkedList<String> input_classes){
		
		LinkedList<String> individual_List = new LinkedList<String>();
		
		for(int i=0; i<input_classes.size(); i++) {
			
			individual_List.addAll( class_Selection_Panel.getIndividualsOfClass(input_classes.get(i)) );
		}
		
		return individual_List;
	}
	
	private void updateSelectedObjPropList() {
		// TODO Auto-generated method stub
		
		selectedObjPropList = new LinkedList<String[]>();
		
		for(int i=0; i<dlm_ObjProp.getSize(); i++) {
			
			String[] triple = new String[3];
			
			triple[0] = dlm_Domain.get(i).toString();
			triple[1] = dlm_ObjProp.get(i).toString();
			triple[2] = dlm_Range.get(i).toString();
			
			selectedObjPropList.add(triple);
		}
	}
	
	 String getEntryIndex() {
		
		return iem.getEntryIndex();
	}
	
}
