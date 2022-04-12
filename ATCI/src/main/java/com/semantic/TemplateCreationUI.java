package com.semantic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class TemplateCreationUI extends JFrame {

	JPanel mainPanel, inner_Arrangement;
	JScrollPane scrollPane_Main;
	
	public TemplateCreationUI() {
		
		super("Demo Template Creation Interface");
		
		createView();
		
		InterfaceConfiguration IC = new InterfaceConfiguration();
		
//		System.out.println("Screen Width: " + IC.SCREEN_SIZE.width + ", Screen Height: " + IC.SCREEN_SIZE.height );
//		System.out.println("Screen Resolution: " + IC.SCREEN_RESOLUTION );
		
//		setBounds( 0, 0, ic.SCREEN_SIZE.width, ic.SCREEN_SIZE.height );
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		pack();
		
		setVisible(true);
	}

	private void createView() {
		// TODO Auto-generated method stub
		
		
		inner_Arrangement = new JPanel();
		inner_Arrangement.setLayout(new BoxLayout(inner_Arrangement, BoxLayout.Y_AXIS));
		inner_Arrangement.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		ClassSelectionPanel cs_Panel = new ClassSelectionPanel();
		EntrySelectionPanel esPanel = new EntrySelectionPanel(cs_Panel);
		ObjPropSelectionPanel opsPanel = new ObjPropSelectionPanel(cs_Panel);
		
		//mainPanel.add(esPanel, BorderLayout.NORTH);
		inner_Arrangement.add(esPanel);
		inner_Arrangement.add(cs_Panel);
		inner_Arrangement.add( Box.createVerticalStrut(50) );
		inner_Arrangement.add( opsPanel );
		inner_Arrangement.add( new DataPropSelectionPanel(opsPanel) );
		
		mainPanel.add(inner_Arrangement, BorderLayout.CENTER);
		//mainPanel.add(new ObjPropSelectionPanel(cs_Panel), BorderLayout.SOUTH);
		
		//Initialization of Scroll Panes
		scrollPane_Main = new JScrollPane();
		scrollPane_Main.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
		scrollPane_Main.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 20));
		scrollPane_Main.setViewportView(mainPanel);
		
		getContentPane().add(scrollPane_Main);
		
	}
	
}
