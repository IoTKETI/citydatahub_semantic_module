package com.semantic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DataPropSelectionPanel extends JPanel{

	InterfaceConfiguration IC = new InterfaceConfiguration();
	
	private JPanel innerArrangement_1, innerArrangement_2, 
	   			   innerArrangement_3, innerArrangement_4;
	
	JButton btn_GetDataProp, btn_AddDataRelation, btn_RemoveDataRelation, btn_SaveTemplate;
	JButton btn_Yes, btn_No;
	JComboBox combo_DataProp, combo_DataDomain, combo_DataRange;
	JList list_DataProp, list_DataDomain, list_DataRange;
	DefaultListModel dlm_DataProp, dlm_DataDomain, dlm_DataRange;
	JScrollPane scrolP_DataProp, scrolP_DataDomain, scrolP_DataRange;
	JOptionPane extraOptionPane;
	
	ObjPropSelectionPanel obj_Prop_Selection_Panel;
	InfoEntryMain ieMain;
	
	EntrySet<String, String, String> data_property_Set;
	
	LinkedList<String[]> selectedDataPropList;
	
	DataPropSelectionPanel(ObjPropSelectionPanel opsPanel){
		
		obj_Prop_Selection_Panel = opsPanel;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		initializeComponents();
		
		//add(innerArrangement_1, BorderLayout.SOUTH);
		add(btn_GetDataProp);
		add( Box.createVerticalStrut(50) );
		add(innerArrangement_1);
		add( Box.createVerticalStrut(10) );
		add(innerArrangement_2);
		add( Box.createVerticalStrut(10) );
		add(innerArrangement_3);
		add(btn_SaveTemplate);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		setVisible(true);
		
		// Setting Event Listeners......
		btn_GetDataProp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				ieMain = obj_Prop_Selection_Panel.getInfoEntryMain();
				
				data_property_Set = ieMain.explore_DataProperties( obj_Prop_Selection_Panel.getGraphList() );
				
				LinkedList<String> data_Properties = new LinkedList<String>();
				
				Iterator relation_Itor = data_property_Set.getIterator();
				
				while(relation_Itor.hasNext()) {
					
					Map.Entry relationPair = (Map.Entry) relation_Itor.next();
					
					data_Properties.add( relationPair.getKey().toString() );
				}
				
				combo_DataProp.setModel( new DefaultComboBoxModel( data_Properties.toArray() ) );
			}
			
		});
		
		combo_DataProp.addActionListener( new TheButtonHandler() );
		btn_AddDataRelation.addActionListener( new TheButtonHandler() );
		btn_RemoveDataRelation.addActionListener( new TheButtonHandler() );
		btn_SaveTemplate.addActionListener( new TheButtonHandler() );
		btn_Yes.addActionListener( new TheButtonHandler() );
		btn_No.addActionListener( new TheButtonHandler() );
		
		list_DataProp.addListSelectionListener( new TheDataListHandler() );
		list_DataDomain.addListSelectionListener( new TheDataListHandler() );
		list_DataRange.addListSelectionListener( new TheDataListHandler() );
		
		
	}

	
	
	private void initializeComponents() {
		// TODO Auto-generated method stub
		

		//Initialization of selection entry ComboBoxes
		combo_DataProp = new JComboBox();
		combo_DataProp.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_DataProp.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/28) );
		
		combo_DataDomain = new JComboBox();
		combo_DataDomain.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_DataDomain.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/28) );
				
		combo_DataRange = new JComboBox();
		combo_DataRange.setFont(new Font("Tahoma", Font.PLAIN, 28));
		combo_DataRange.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/28) );
				
				
		//Initialization of button to get Object properties based on selected classes.
		btn_GetDataProp = new JButton("Get Data Properties");
		btn_GetDataProp.setToolTipText("Get Required Data Properties from Triplestore based on given Classes");
		btn_GetDataProp.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_GetDataProp.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		btn_GetDataProp.setAlignmentX(Component.CENTER_ALIGNMENT);
				
		//Initialization of button to Add and Remove Object properties based on selected classes.
		btn_AddDataRelation = new JButton("Add Data Relation");
		btn_AddDataRelation.setToolTipText("Get Required Object Properties from Triplestore based on given Classes");
		btn_AddDataRelation.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_AddDataRelation.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		//btn_AddDataRelation.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn_RemoveDataRelation = new JButton("Remove Data Relation");
		btn_RemoveDataRelation.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_RemoveDataRelation.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		
		
		//Initialization of button to get Object properties based on selected classes.
		btn_SaveTemplate = new JButton("Save Template");
		btn_SaveTemplate.setToolTipText("Save Template into text file.");
		btn_SaveTemplate.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_SaveTemplate.setMaximumSize( new Dimension(IC.SCREEN_SIZE.width/4, IC.SCREEN_SIZE.height/28) );
		btn_SaveTemplate.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				
		//Initialization of lists
		list_DataProp = new JList();
		list_DataProp.setFont(new Font("Tahoma", Font.PLAIN, 28));
		list_DataProp.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		list_DataProp.setVisibleRowCount(8);
		
		list_DataDomain = new JList();
		list_DataDomain.setFont(new Font("Tahoma", Font.PLAIN, 28));
		list_DataDomain.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		list_DataDomain.setVisibleRowCount(8);
				
		list_DataRange = new JList();
		list_DataRange.setFont(new Font("Tahoma", Font.PLAIN, 28));
		list_DataRange.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		list_DataRange.setVisibleRowCount(8);
				
		
		
		//Initialization of Default List Models for selected Object Properties, domains and ranges...
		dlm_DataDomain = new DefaultListModel();
		dlm_DataProp = new DefaultListModel();
		dlm_DataRange = new DefaultListModel();
		
		
		
		//Initialization of Scroll Panes
		scrolP_DataProp = new JScrollPane();
		scrolP_DataProp.setViewportView(list_DataProp);
		scrolP_DataProp.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/4));
		
		scrolP_DataDomain = new JScrollPane();
		scrolP_DataDomain.setViewportView(list_DataDomain);
		scrolP_DataDomain.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/4));
		
		scrolP_DataRange = new JScrollPane();
		scrolP_DataRange.setViewportView(list_DataRange);
		scrolP_DataRange.setPreferredSize(new Dimension(IC.SCREEN_SIZE.width/3, IC.SCREEN_SIZE.height/4));
		

		
		//Initialization of inner arrangement panel
		innerArrangement_1 = new JPanel();
		innerArrangement_1.setLayout(new BoxLayout(innerArrangement_1, BoxLayout.X_AXIS));
		innerArrangement_1.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		innerArrangement_2 = new JPanel();
		innerArrangement_2.setLayout(new BoxLayout(innerArrangement_2, BoxLayout.X_AXIS));
		innerArrangement_2.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		
		innerArrangement_3 = new JPanel();
		innerArrangement_3.setLayout(new BoxLayout(innerArrangement_3, BoxLayout.X_AXIS));
		innerArrangement_3.setBorder(new EmptyBorder(new Insets(20, 10, 20, 10)));
		

		
		//Initialization of Confirm Dialog Box
		//JDialog.setDefaultLookAndFeelDecorated(true);
//		JLabel message = new JLabel("There are some unused classes.\\\\nDo you want to keep them?");
//		message.setFont(new Font("Tahoma", Font.PLAIN, 30));
//		message.setText("There are some unused classes.\\nDo you want to keep them?");
//		message.setRows(5);
//		message.setColumns(10);
//		message.setEditable(true);
		//JScrollPane mypane = new JScrollPane(mytext);

//		extraDialog.setSize(IC.getScreenSizeOfPercent(30));	
		btn_Yes = new JButton("Yes");
		btn_Yes.setFont(new Font("Tahoma", Font.PLAIN, 30));
		//btn_Yes.setMaximumSize(getScreenSizeOfPercent(10, extraDialog));
		btn_No = new JButton("No");
		btn_No.setFont(new Font("Tahoma", Font.PLAIN, 30));
		//btn_No.setMaximumSize(getScreenSizeOfPercent(10, extraDialog));
		
//		Object[] objarr = {
//			    message,
//			    btn_Yes,
//			    btn_No
//			};
			//extraOptionPane = new JOptionPane(objarr, JOptionPane.PLAIN_MESSAGE);
		extraOptionPane = new JOptionPane();
		
//		extraDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		//Populating Arrangements
		innerArrangement_1.add( combo_DataDomain );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( combo_DataProp );
		innerArrangement_1.add( Box.createHorizontalStrut(50) );
		innerArrangement_1.add( combo_DataRange );
		innerArrangement_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		innerArrangement_2.add(btn_AddDataRelation);
		innerArrangement_2.add( Box.createHorizontalStrut(50) );
		innerArrangement_2.add(btn_RemoveDataRelation);
		innerArrangement_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		innerArrangement_3.add( scrolP_DataDomain );
		innerArrangement_3.add( Box.createHorizontalStrut(50) );
		innerArrangement_3.add( scrolP_DataProp );
		innerArrangement_3.add( Box.createHorizontalStrut(50) );
		innerArrangement_3.add( scrolP_DataRange );
		innerArrangement_3.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	private class TheButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if( e.getSource() == combo_DataProp ) {
				
				if(combo_DataProp.getItemCount()>0) {
					
					String selected_Property = combo_DataProp.getSelectedItem().toString();
					
					Iterator d_r_Itor = data_property_Set.get( selected_Property ).entrySet().iterator();
					
					LinkedList<String> fetched_List = new LinkedList<String>();
					
					String dataType = "";
					
					while( d_r_Itor.hasNext() ) {
						
						Map.Entry edgePair = (Map.Entry) d_r_Itor.next();
						
						fetched_List = populateIndividualListBasedOnClasses(
																	ieMain.getSubCLassesOf( edgePair.getKey().toString(), fetched_List )
																	);
						
						dataType = edgePair.getValue().toString();
					}
					
					combo_DataDomain.setModel( new DefaultComboBoxModel( fetched_List.toArray() ) );
					
					if( dataType.equals("n/a") ) {
					
						PrefixSet dataTypeSet = new PrefixSet();
						
						combo_DataRange.setModel( new DefaultComboBoxModel( dataTypeSet.getAllDataValueTypes().toArray() ) );
					
					}else {
						
						combo_DataRange.setModel( new DefaultComboBoxModel( new String[] { dataType } ) );
					}
				}
			
			}else if( e.getSource() == btn_AddDataRelation ) {
				
				dlm_DataDomain.addElement( combo_DataDomain.getSelectedItem().toString() );
				dlm_DataProp.addElement( combo_DataProp.getSelectedItem().toString() );
				dlm_DataRange.addElement( combo_DataRange.getSelectedItem().toString() );
				
				list_DataDomain.setModel(dlm_DataDomain);
				list_DataProp.setModel(dlm_DataProp);
				list_DataRange.setModel(dlm_DataRange);
				
				updateSelectedDataPropList();
				
			}else if( e.getSource() == btn_RemoveDataRelation ) {
				
				dlm_DataDomain.remove( list_DataDomain.getSelectedIndex() );
				dlm_DataProp.remove( list_DataProp.getSelectedIndex() );
				dlm_DataRange.remove( list_DataRange.getSelectedIndex() );
				
				list_DataDomain.setModel(dlm_DataDomain);
				list_DataProp.setModel(dlm_DataProp);
				list_DataRange.setModel(dlm_DataRange);
				
				updateSelectedDataPropList();
				
			}else if( e.getSource() == btn_SaveTemplate ) {
				
				TemplateFileStore tfs = new TemplateFileStore(
						obj_Prop_Selection_Panel.getEntryIndex(), 										
						obj_Prop_Selection_Panel.class_Selection_Panel.selectedIndividualList,
						obj_Prop_Selection_Panel.class_Selection_Panel.selectedClassList,
						obj_Prop_Selection_Panel.getSelectedObjProp(),
						selectedDataPropList
						);
				
				if( tfs.unusedClasses() ) {
					
					//extraDialog.setVisible(true);
					
					//setDialogBoxFont(confirmDialog.getComponents());
					
					JLabel message = new JLabel("There are some unused classes.\nDo you want to keep them?");
					message.setFont(new Font("Tahoma", Font.PLAIN, 30));
					
					Object[] objarr = {
						    message,
						    btn_Yes,
						    btn_No
						};
					
					/*JDialog extraDialog =*/ extraOptionPane.showOptionDialog(
																null, 
																"There are some unused classes.\nDo you want to keep them?", 
																"Unused Classes", 
																JOptionPane.DEFAULT_OPTION, 
																JOptionPane.INFORMATION_MESSAGE,
																null,
																objarr,
																null
							);
					
					//extraDialog.setResizable(true);
				    //extraDialog.setVisible(true);
				}
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
				 
				int userSelection = fileChooser.showSaveDialog(null);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				    tfs.saveTemplateInJSONFile(fileToSave.getAbsolutePath());
				}
				
			}else if(e.getSource() == btn_Yes ) {
				
				System.out.println("Yes button clicked");
				
				extraOptionPane.getRootFrame().dispose();
				
			}else if(e.getSource() == btn_No ) {
				
				System.out.println("No button clicked");
				
				extraOptionPane.getRootFrame().dispose();
			}
			
		}
		
	}
	
	private class TheDataListHandler implements ListSelectionListener{

		public void valueChanged(ListSelectionEvent event) {
			// TODO Auto-generated method stub
			
			if( event.getSource() == list_DataDomain ) {
				
				list_DataProp.setSelectedIndex( list_DataDomain.getSelectedIndex() );
				list_DataRange.setSelectedIndex( list_DataDomain.getSelectedIndex() );
				
			}else if( event.getSource() == list_DataProp ) {
				
				list_DataDomain.setSelectedIndex( list_DataProp.getSelectedIndex() );
				list_DataRange.setSelectedIndex( list_DataProp.getSelectedIndex() );
				
			}else if( event.getSource() == list_DataRange ) {
				
				list_DataDomain.setSelectedIndex( list_DataRange.getSelectedIndex() );
				list_DataProp.setSelectedIndex( list_DataRange.getSelectedIndex() );
				
			}
			
		}
		
	}
	
	
	private void updateSelectedDataPropList() {
		// TODO Auto-generated method stub
		
		selectedDataPropList = new LinkedList<String[]>();
		
		for(int i=0; i<dlm_DataProp.getSize(); i++) {
			
			String[] triple = new String[3];
			
			triple[0] = dlm_DataDomain.get(i).toString();
			triple[1] = dlm_DataProp.get(i).toString();
			triple[2] = dlm_DataRange.get(i).toString();
			
			selectedDataPropList.add(triple);
		}
	}
	
	LinkedList<String> populateIndividualListBasedOnClasses(LinkedList<String> input_classes){
		
		LinkedList<String> individual_List = new LinkedList<String>();
		
		for(int i=0; i<input_classes.size(); i++) {
			
			individual_List.addAll( 
					obj_Prop_Selection_Panel.class_Selection_Panel.getIndividualsOfClass(input_classes.get(i)) 
					);
		}
		
		return individual_List;
	}
	
	public Dimension getScreenSizeOfPercent(int percent, Component comp) {
		
		int percentWidth = (int) ((((float)percent)/100) * comp.getSize().width);
		
		int percentHeight = (int) ((((float)percent)/100) * comp.getSize().height);
		
		Dimension d = new Dimension(percentWidth, percentHeight);
		
		System.out.println("Size: " + d.width + ", " + d.height);
		
		return d;
	}
	
//	private void setDialogBoxFont(Component[] comp)
//    {  
//      for(int x = 0; x < comp.length; x++)  
//      {  
//        if(comp[x] instanceof Container) {
//        	
//        	setDialogBoxFont(((Container)comp[x]).getComponents());  
//        }
//        
//        Font defaultFont = comp[x].getFont();
//        
//        int defaultHeight = comp[x].getMaximumSize().height;
//        int defaultWidth = comp[x].getMaximumSize().width;
//        
//        System.out.println("Size: " + defaultHeight + ", " + defaultWidth);
//        System.out.println("Screen Size: " + IC.SCREEN_SIZE.height + ", " + IC.SCREEN_SIZE.width);
//        
//        int maxHeight = IC.getScreenSizeOfPercent(20).height;
//        int maxWidth = IC.getScreenSizeOfPercent(20).width;
//        
//        System.out.println("Maximum Size: " + maxHeight + ", " + maxWidth);
//        
//        Font newFont = defaultFont.deriveFont(IC.fontSize);
//        
//        System.out.println("Changed Font Size: " + newFont.getSize());
//        
//        try{comp[x].setFont(newFont );}  
//        catch(Exception e){
//        	System.out.println("\n Exception Caught in 'setDialogBoxFont' function!!");
//        	
//        }//do nothing  
//      }  
//    }
}
