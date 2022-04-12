package com.semantic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EntrySelectionPanel extends JPanel {

	InterfaceConfiguration IC = new InterfaceConfiguration();
	
	JPanel innerArrangement_1, innerArrangement_2, innerArrangement_3;
	
	JScrollPane scrollPane_ChooseGraph, scrollPane_ChoosenGraph;
	JList listChooseGraph, listChoosenGraph;
	DefaultListModel dlm_Graph, dlm_SelectionGraph;
	
	JButton btnChooseGraph, btnRemoveGraph, btnChooseAllGraph, btnRemoveAllGraph; 
	
	JComboBox combo_entryList;
	JButton btnGetClasses, btnSaveClassEntry, btn_EnterEntryName;
	JLabel lbl_SelectedEntry, lbl_HintGraph;
	
	ClassSelectionPanel classSelectionPanel;
	
	JOptionPane inputEntryNamePane;
	JTextField tf_InputEntryName;
	
	JSONObject entriesObject = null;
	
	static final InfoEntryMain iem = new InfoEntryMain();
	String the_Entry_Index;
	LinkedList<String> selectedGraphList;
	EntrySet<String, Integer, String> the_ClassHierarchy;
	
	List<String> entry_options_list = new ArrayList<String>();
	
	public EntrySelectionPanel(ClassSelectionPanel cSP) {
		
		super(new BorderLayout());
		//super();
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		initializeEntryJSONListAndOptions();
		
		iem.initialize_Entry_Data( entriesObject );
		
		//System.out.println("\nConsole LOG--> In EntrySelectionPanel: iem.IE.count: \n" + iem.EI.count(iem.EI.getEntryLabel()));
		
		initializeComponents();
		
		classSelectionPanel = cSP;
		
		JLabel lbl_HintGraph;
		lbl_HintGraph = new JLabel("Select the Graph to be searched:");
		lbl_HintGraph.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		
		add(lbl_HintGraph, BorderLayout.PAGE_START);
		add(innerArrangement_1, BorderLayout.CENTER);
		add(innerArrangement_3, BorderLayout.PAGE_END);
		
		
		fetchGraphList();
		
		setVisible(true);
		
		TheHandler the_handler = new TheHandler();
		
		btnGetClasses.addActionListener( the_handler );
		btnSaveClassEntry.addActionListener( the_handler );
		btnChooseGraph.addActionListener( the_handler );
		btnRemoveGraph.addActionListener( the_handler );
		btnChooseAllGraph.addActionListener( the_handler );
		btnRemoveAllGraph.addActionListener( the_handler );
		btn_EnterEntryName.addActionListener( the_handler );
	}
	
	

	private void initializeComponents() {
		// TODO Auto-generated method stub
		
		//Initialization of inner arrangement panel
		
		//Initialization of inner arrangement panel for Graph List
		innerArrangement_1 = new JPanel();
		innerArrangement_1.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		innerArrangement_2 = new JPanel();
		innerArrangement_2.setLayout(new BoxLayout(innerArrangement_2, BoxLayout.Y_AXIS));
		innerArrangement_2.setBorder(new EmptyBorder(new Insets(20, 10, 15, 20)));
		
		
		innerArrangement_3 = new JPanel();
		innerArrangement_3.setLayout(new BoxLayout(innerArrangement_3, BoxLayout.X_AXIS));
		innerArrangement_3.setBorder(new EmptyBorder(new Insets(20, 10, 20, 20)));
		
		
		
		//Initialization of lists
		listChooseGraph = new JList();
		listChooseGraph.setFont(new Font("Tahoma", Font.PLAIN, 28));
		listChooseGraph.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		listChooseGraph.setVisibleRowCount(4);
					
		listChoosenGraph = new JList();
		listChoosenGraph.setFont(new Font("Tahoma", Font.PLAIN, 28));
		listChoosenGraph.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		listChoosenGraph.setVisibleRowCount(4);		
					
					
					
		//Initialization of Scroll Panes
		scrollPane_ChooseGraph = new JScrollPane();
		scrollPane_ChooseGraph.setViewportView(listChooseGraph);
		scrollPane_ChooseGraph.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3,IC.SCREEN_SIZE.width/10));
					
		scrollPane_ChoosenGraph = new JScrollPane();
		scrollPane_ChoosenGraph.setViewportView(listChoosenGraph);
		scrollPane_ChoosenGraph.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3,IC.SCREEN_SIZE.width/10));
		//scrollPane_ChoosenGraph.setAlignmentX(Component.LEFT_ALIGNMENT);
					
		//Initialization of Selection Lists
		dlm_Graph = new DefaultListModel();
		dlm_SelectionGraph = new DefaultListModel();
		
		
		//Initialization of list Buttons
		btnChooseGraph = new JButton("Select->");
		btnRemoveGraph = new JButton("<-Remove");
		btnChooseAllGraph = new JButton("Select All >");
		btnRemoveAllGraph = new JButton("< Remove All");
				
		btnChooseGraph.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnRemoveGraph.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnChooseAllGraph.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnRemoveAllGraph.setFont(new Font("Tahoma", Font.PLAIN, 28));
				
		btnChooseGraph.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		btnRemoveGraph.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		btnChooseAllGraph.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		btnRemoveAllGraph.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/14, IC.SCREEN_SIZE.height/50) );
		
		btn_EnterEntryName = new JButton("Save");
		btn_EnterEntryName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		
		tf_InputEntryName = new JTextField( "Entry Name" ,16);
		tf_InputEntryName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		//Initialization of selection entry list ComboBox
		combo_entryList = new JComboBox(getTemplateClassEntryList());
		combo_entryList.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_entryList.setToolTipText("Select Entry");
		//combo_entryList.setBounds(22, 37, 536, 37);
		combo_entryList.setMaximumSize(new Dimension(IC.SCREEN_SIZE.width/6,IC.SCREEN_SIZE.height/40));
		combo_entryList.setRenderer(new MyComboBoxRenderer("Select Entry for Template Creation"));
		combo_entryList.setSelectedIndex(-1);
		
		//Initialization of button for getting classes
		btnGetClasses = new JButton("Get Classes");
		btnGetClasses.setToolTipText("Get Required Classes from Triplestore..");
		btnGetClasses.setFont(new Font("Tahoma", Font.PLAIN, 28));
		
		//Initialization of button for getting classes
		btnSaveClassEntry = new JButton("Save Class Entry");
		btnSaveClassEntry.setToolTipText("Save Selected Classes for next time of template creation.");
		btnSaveClassEntry.setFont(new Font("Tahoma", Font.PLAIN, 28));
		
		//Initialization of status label
		lbl_SelectedEntry = new JLabel("Nothing Selected");
		lbl_SelectedEntry.setFont(new Font("Tahoma", Font.PLAIN, 28));
		
		inputEntryNamePane = new JOptionPane();
		
		innerArrangement_2.add( btnChooseGraph );
		innerArrangement_2.add( Box.createVerticalStrut(50) );
		innerArrangement_2.add( btnChooseAllGraph );
		innerArrangement_2.add( Box.createVerticalStrut(20) );
		innerArrangement_2.add( btnRemoveAllGraph );
		innerArrangement_2.add( Box.createVerticalStrut(50) );
		innerArrangement_2.add( btnRemoveGraph );
		
		innerArrangement_1.add( scrollPane_ChooseGraph );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( innerArrangement_2 );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( scrollPane_ChoosenGraph );
		
		
		innerArrangement_3.add(combo_entryList);
		innerArrangement_3.add(Box.createHorizontalStrut(20));
		innerArrangement_3.add(btnGetClasses);
		innerArrangement_3.add(Box.createHorizontalStrut(20));
		innerArrangement_3.add(btnSaveClassEntry);
		innerArrangement_3.add(Box.createHorizontalStrut(20));
		innerArrangement_3.add(lbl_SelectedEntry);
	}

	
	private class TheHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			String selected_graph =  "";
			
			if(e.getSource()==btnGetClasses) {
				
				the_Entry_Index = Integer.toString(combo_entryList.getSelectedIndex());
				String entry_from_comboList = combo_entryList.getSelectedItem().toString();
				
				lbl_SelectedEntry.setText("Selected Entry: " + entry_from_comboList);
				
				iem.set_Current_Entry_Values( entry_from_comboList, the_Entry_Index );
				
				LinkedList<String> class_list = iem.explore_TDB_CLasses( selectedGraphList );
				
				//Storing parent-subclass Hierarchy that will be used later in selecting Object properties.
				the_ClassHierarchy = iem.getClassHierarchy();
				
				classSelectionPanel.populateListChoose(class_list);
				
				classSelectionPanel.setGraphList( selectedGraphList );
				
				classSelectionPanel.setInfoEntryMain(iem);
				
				classSelectionPanel.setClassHierarchy(the_ClassHierarchy);
				
				classSelectionPanel.setCompleteClassList(class_list);
				
				if(!iem.getEntryIndex().equals("0")) {
					
					System.out.println("\nConsole LOG--> In EntrySelectionInterface: bhai is individual wali condition m gya h");
					classSelectionPanel.populateListChoosen(iem.getEntryIndividuals(entry_from_comboList));
					
					classSelectionPanel.updateSelectedClassList();
					classSelectionPanel.updateSelectedIndividualList();
				}
				
			}else if(e.getSource()==btnSaveClassEntry){
				
				JLabel message = new JLabel("Provide the Entry Name: \n");
				message.setFont(new Font("Tahoma", Font.PLAIN, 30));
				
				Object[] objarr = {
					    message,
					    tf_InputEntryName,
					    btn_EnterEntryName
					};
				
				/*JDialog extraDialog =*/ inputEntryNamePane.showOptionDialog(
															null, 
															null, 
															"Saving Class Entry", 
															JOptionPane.DEFAULT_OPTION, 
															JOptionPane.INFORMATION_MESSAGE,
															null,
															objarr,
															null
						);
				
			}else if( e.getSource()==btn_EnterEntryName ){
			
				saveClassEntryLocally( 
						tf_InputEntryName.getText(),
						classSelectionPanel.getSelectedClasses(),
						classSelectionPanel.selectedIndividualList
						);
				
				inputEntryNamePane.getRootFrame().dispose();
				
			}else {
				
				if(e.getSource()==btnChooseGraph) {
					
					selected_graph = listChooseGraph.getSelectedValue().toString();
					
					dlm_SelectionGraph.addElement( selected_graph );
					listChoosenGraph.setModel( dlm_SelectionGraph );
					dlm_Graph.removeElement( selected_graph );

				}else if(e.getSource()==btnRemoveGraph) {

					selected_graph = listChoosenGraph.getSelectedValue().toString();
					
					dlm_Graph.addElement( selected_graph );
					listChooseGraph.setModel(dlm_Graph);
					dlm_SelectionGraph.removeElement(selected_graph);

				}else if(e.getSource()==btnChooseAllGraph) {

					while(!dlm_Graph.isEmpty()) {
				
						selected_graph = dlm_Graph.firstElement().toString();
						
						dlm_Graph.removeElement(selected_graph);
						dlm_SelectionGraph.addElement(selected_graph);
					}
					
					listChooseGraph.setModel(dlm_Graph);
					listChoosenGraph.setModel(dlm_SelectionGraph);

				}else if(e.getSource()==btnRemoveAllGraph) {

					while(!dlm_SelectionGraph.isEmpty()) {
					
						selected_graph = dlm_SelectionGraph.firstElement().toString();
						dlm_SelectionGraph.removeElement(selected_graph);
						dlm_Graph.addElement(selected_graph);
					}
					
					listChooseGraph.setModel(dlm_Graph);
					listChoosenGraph.setModel(dlm_SelectionGraph);
				}
				
				updateSelectedGraphList();
			}
			
		}

		private void saveClassEntryLocally(String entryName, LinkedList<String> classList, LinkedList<String> individualList) {
			// TODO Auto-generated method stub
			
			String file_path = System.getProperty("user.dir") + "\\EntryStore\\Entries.json";
	        
			if(entriesObject == null) {
			
				entriesObject = new JSONObject();
			}
			
			JSONObject json_classes = new JSONObject();
			
			for(int offset=0; offset<classList.size(); offset++) {
				
				JSONArray json_individuals = new JSONArray();
				
				json_individuals.addAll( classSelectionPanel.getIndividualsOfClass( classList.get(offset) ) );
				
				json_classes.put(classList.get(offset), json_individuals);
			}
			
			entriesObject.put(entryName, json_classes);
				
				try {
					FileWriter writer = new FileWriter(file_path);
					
					writer.write( prettyJSONString(entriesObject) + "\n" );
					
					writer.flush();
					writer.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
		
	}
	
	
	class MyComboBoxRenderer extends JLabel implements ListCellRenderer{

		private String _title;

		public MyComboBoxRenderer(String title)
        {
            _title = title;
        }
		

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			// TODO Auto-generated method stub

			 if (index == -1 && value == null) setText(_title);
	            else setText(value.toString());
	            return this;
		}
		
	}
	
	
	
	private void initializeEntryJSONListAndOptions() {
		// TODO Auto-generated method stub
		
		//Initializing entry_options_list with only 1 option for now which is: "All Classes" 
		entry_options_list.add("All Classes");
		
		//JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try
        {
        	FileReader reader = new FileReader( System.getProperty("user.dir") + "\\EntryStore\\Entries.json" );
        	
            //Read JSON file
            entriesObject = (JSONObject) jsonParser.parse(reader);
 
        } catch (FileNotFoundException e) {
        	//System.out.println("File Not Found Exception ");
        	entriesObject = null;
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
        	//System.out.println("Parse Exception ");
			e.printStackTrace();
		}
        
        if(entriesObject != null) {
        	
        	Iterator<String> itor = entriesObject.keySet().iterator();
        	
        	while(itor.hasNext()) {
        	
        		//entry_options_list.
        		entry_options_list.add( itor.next() );
        	}
        }
		
	}
	
	
	private void fetchGraphList() {
		// TODO Auto-generated method stub
		
		LinkedList graphList = iem.explore_TripleStore_Graphs();
		
		for(int i=0; i<graphList.size(); i++) {	
			
			dlm_Graph.addElement( graphList.get(i) );
		}
		
		listChooseGraph.setModel(dlm_Graph);
	}
	
	void updateSelectedGraphList() {
		
		selectedGraphList = new LinkedList<String>();
		
		for(int i=0; i<dlm_SelectionGraph.getSize(); i++) {
			
			selectedGraphList.add(dlm_SelectionGraph.getElementAt(i).toString());
		}
	}
	
	
	String[] getTemplateClassEntryList() {
		
		return entry_options_list.toArray(new String[entry_options_list.size()]);
	}
	
	String getTheEntryIndex() {
		
		return the_Entry_Index;
	}
	
	EntrySet<String, Integer, String> getSavedClassHierarchy() {
		
		return the_ClassHierarchy;
	}
	
	String prettyJSONString(JSONObject uglyJSON) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		
		return gson.toJson( uglyJSON );
		
	}
}
