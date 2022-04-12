package com.semantic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ClassSelectionPanel extends JPanel{

	InterfaceConfiguration IC = new InterfaceConfiguration();
	
	private JPanel innerArrangement_1, innerArrangement_2;
	
	JScrollPane scrollPane_Choose, scrollPane_Choosen;
	JList listChoose, listChoosen;
	DefaultListModel dlm, dlm_Selection;
	JButton btnChooseClass, btnRemoveClass, btnChooseAll, btnRemoveAll; 
	
	InfoEntryMain iem = null;
	//String the_Entry_Index = "", the_Entry_Label;
	LinkedList<String> graphList, selectedClassList, completeClassList, selectedIndividualList;
	EntrySet<String, Integer, String> the_ClassHierarchy;
	
	ClassSelectionPanel(){
		
		initializeComponents();
		
		add(innerArrangement_1);
		
		setVisible(true);
	}

	
	private void initializeComponents() {
		// TODO Auto-generated method stub
		
		//Initialization of inner arrangement panel
		innerArrangement_1 = new JPanel();
		innerArrangement_1.setLayout(new BoxLayout(innerArrangement_1, BoxLayout.X_AXIS));
		innerArrangement_1.setBorder(new EmptyBorder(new Insets(20, 10, 20, 50)));
		
		innerArrangement_2 = new JPanel();
		innerArrangement_2.setLayout(new BoxLayout(innerArrangement_2, BoxLayout.Y_AXIS));
		innerArrangement_2.setBorder(new EmptyBorder(new Insets(20, 10, 150, 20)));

		
		
		//Initialization of lists
		listChoose = new JList();
		listChoose.setFont(new Font("Tahoma", Font.PLAIN, 28));
		listChoose.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		listChoose.setVisibleRowCount(8);
		
		listChoosen = new JList();
		listChoosen.setFont(new Font("Tahoma", Font.PLAIN, 28));
		listChoosen.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		listChoosen.setVisibleRowCount(8);		
		
		
		
		//Initialization of Scroll Panes
		scrollPane_Choose = new JScrollPane();
		scrollPane_Choose.setViewportView(listChoose);
		scrollPane_Choose.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3,IC.SCREEN_SIZE.width/4));
		
		scrollPane_Choosen = new JScrollPane();
		scrollPane_Choosen.setViewportView(listChoosen);
		scrollPane_Choosen.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3,IC.SCREEN_SIZE.width/4));
		
		//Initialization of Selection Lists
		dlm = new DefaultListModel();
		dlm_Selection = new DefaultListModel();
		
		//Initialization of Buttons
		btnChooseClass = new JButton("Select->");
		btnRemoveClass = new JButton("<-Remove");
		btnChooseAll = 	 new JButton("Select All >");
		btnRemoveAll =   new JButton("< Remove All");
		
		btnChooseClass.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnRemoveClass.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnChooseAll.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnRemoveAll.setFont(new Font("Tahoma", Font.PLAIN, 28));
		
		btnChooseClass.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		btnRemoveClass.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		btnChooseAll.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		btnRemoveAll.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		
		TheHandler the_handler = new TheHandler();
		
		btnChooseClass.addActionListener(the_handler);
		btnRemoveClass.addActionListener(the_handler);
		btnChooseAll.addActionListener(the_handler);
		btnRemoveAll.addActionListener(the_handler);
		
		
		
		innerArrangement_2.add( Box.createVerticalStrut(50) );
		innerArrangement_2.add( btnChooseClass );
		innerArrangement_2.add( Box.createVerticalStrut(100) );
		innerArrangement_2.add( btnChooseAll );
		innerArrangement_2.add( Box.createVerticalStrut(20) );
		innerArrangement_2.add( btnRemoveAll );
		innerArrangement_2.add( Box.createVerticalStrut(100) );
		innerArrangement_2.add( btnRemoveClass );
		
		innerArrangement_1.add( scrollPane_Choose );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( innerArrangement_2 );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( scrollPane_Choosen );
		
	}
	
	private class TheHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			String selected_class =  "";
			
			if(e.getSource()==btnChooseClass) {
				
										selected_class = listChoose.getSelectedValue().toString();
										
										//Check existing class URI and then create new Individal
										selected_class = get_New_Individual( selected_class );
										
										dlm_Selection.addElement( selected_class );
										listChoosen.setModel(dlm_Selection);
										//dlm.removeElement(selected_class);
			
			}else if(e.getSource()==btnRemoveClass) {
				
										selected_class = listChoosen.getSelectedValue().toString();
										//dlm.addElement( selected_class );
										//listChoose.setModel(dlm);
										dlm_Selection.removeElement(selected_class);
			
			}else if(e.getSource()==btnChooseAll) {
				
//										while(!dlm.isEmpty()) {
//										
//											selected_class = dlm.firstElement().toString();
//											//dlm.removeElement(selected_class);
//											dlm_Selection.addElement(selected_class);
//										}
										for(int i=0; i<dlm.getSize(); i++) {
											
											selected_class = dlm.get(i).toString();
											
											//Check existing class URI and then create new Individal
											selected_class = get_New_Individual( selected_class );
											
											dlm_Selection.addElement(selected_class);
										}
										
										//listChoose.setModel(dlm);
										listChoosen.setModel(dlm_Selection);
			
			}if(e.getSource()==btnRemoveAll) {
				
										while(!dlm_Selection.isEmpty()) {
											
											selected_class = dlm_Selection.firstElement().toString();
											dlm_Selection.removeElement(selected_class);
											//dlm.addElement(selected_class);
										}
										//listChoose.setModel(dlm);
										listChoosen.setModel(dlm_Selection);
			}
			
			updateSelectedClassList();
			updateSelectedIndividualList();
			//JOptionPane.showMessageDialog(null, s);
		}
			
		}
	
	void populateListChoose(LinkedList<String> class_list) {
		
		//dlm.clear();
		
		for(int i=0; i<class_list.size(); i++) {	
			
			if( !dlm.contains(class_list.get(i)) ) {
			
				dlm.addElement( class_list.get(i) );
			}
		}
		
		listChoose.setModel(dlm);
	}
	
	void populateListChoosen(ArrayList<String> individual_list) {
		
		dlm_Selection.clear();
		
		
		
		for(int i=0; i<individual_list.size(); i++) {	
			
			dlm_Selection.addElement( individual_list.get(i) );
		}
		
		listChoosen.setModel(dlm_Selection);
	}
	
	
	//Check existing class URI and return individual with new id
	String get_New_Individual(String class_uri) {
		
		//Creating Individual id: Adding "_1" at the end
		int class_length = class_uri.length();
		int id_offset = 1;
		class_uri = class_uri + "_" + Integer.toString(id_offset);
		
		//checking if it already existis: if it does then increamenting the id
		while( dlm_Selection.contains(class_uri) ){
			
			id_offset++;
			class_uri = class_uri.substring(0, class_length+1 ) + Integer.toString(id_offset);
		}
		
		return class_uri;
	}
	
	
	LinkedList<String> getSelectedClasses() {
		
		return selectedClassList;
	}
	
	void setInfoEntryMain(InfoEntryMain in_iem) {
		
		iem = in_iem;
	}
	
	InfoEntryMain getInfoEntryMain() {
	
	return iem;
	}
	
//	void setEntryIndex(String in_Index) {
//		
//		the_Entry_Index = in_Index;
//	}
//	
//	void setEntryLabel(String in_Label) {
//		
//		the_Entry_Label = in_Label;
//	}
//	
//	String getEntryIndex() {
//		
//		return the_Entry_Index;
//	}
//	
//	String getEntryLabel() {
//		
//		return the_Entry_Label;
//	}
	
	void setClassHierarchy(EntrySet<String, Integer, String> in_Hierarchy) {
		
		the_ClassHierarchy = in_Hierarchy;
	}
	
	EntrySet<String, Integer, String> getClassHierarchy() {
		
		return the_ClassHierarchy;
	}
	
	void updateSelectedClassList() {
		
		selectedClassList = new LinkedList<String>();
		
		for(int i=0; i<dlm_Selection.getSize(); i++) {
			
			String current_class = dlm_Selection.getElementAt(i).toString();
			current_class = current_class.substring(0, current_class.lastIndexOf("_"));
			
			if(!selectedClassList.contains(current_class)) {
				
				selectedClassList.add( current_class );
			}
		}
		System.out.println("SelectedClassList: " + selectedClassList);
	}
	
	void updateSelectedIndividualList() {
		
		selectedIndividualList = new LinkedList<String>();
		
		for(int i=0; i<dlm_Selection.getSize(); i++) {
			
			selectedIndividualList.add( dlm_Selection.getElementAt(i).toString() );
		}
	}
	
	LinkedList<String> getIndividualsOfClass(String input_class){
		
		LinkedList<String> individuals = new LinkedList<String>();
		
		for(int i=0; i<selectedIndividualList.size(); i++) {
			
			String current_individual = selectedIndividualList.get(i);
			
			if(current_individual.contains(input_class)) {
				
				individuals.add(current_individual);
			}
			
		}
		
		return individuals;
	}
	
	void setCompleteClassList(LinkedList<String> complete_list) {
		
		completeClassList = complete_list;
	}
	
	LinkedList<String> getCompleteClassList() {
		
		return completeClassList;
	}


	public void setGraphList(LinkedList<String> list) {
		// TODO Auto-generated method stub
		
		graphList = list;
	}
	
	public LinkedList<String> getGraphList(){
		
		return graphList;
	}
}
