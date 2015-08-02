import javax.swing.*;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import java.net.UnknownHostException;
import java.util.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class scriptManagerFrame extends JFrame
 {
  public static void OpenFrame(String[] args)
  {
	  new scriptManagerFrame();
  }
  
	private static JButton buttonNewScript;
	private static JButton buttonReadScript;
	private static JButton buttonEditScript;	
	private static JButton buttonShowAll;
	private static Boolean visibleList = false ;
	int selectedScriptId = 0;	

	public scriptManagerFrame() 
	{
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Script Manager");
		 
		JPanel scriptManagerLayout = new JPanel();
		
		final DefaultListModel scriptListModel = new DefaultListModel();
		final JList scriptList = new JList(scriptListModel);
		final JScrollPane scriptListScrollPane = new JScrollPane(scriptList); 
		scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scriptList.addListSelectionListener(new ListSelectionListener(){
		
			public void valueChanged(ListSelectionEvent ie){
				int inx = scriptList.getSelectedIndex();

				String selectedScriptName = new String();
				selectedScriptName = scriptList.getSelectedValue().toString();
				selectedScriptId = Integer.parseInt(selectedScriptName.substring(0,selectedScriptName.indexOf(":")));
			}
		} );
		
		buttonShowAll = new JButton("Show all scripts");
		buttonShowAll.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				String allScripts[] = new String[100];
				validationScript scriptList = new validationScript();
				allScripts = scriptList.listall(); 
				scriptListModel.clear();

				//scriptList.addListSelectionListener();
				
				visibleList = !visibleList;
				scriptListScrollPane.setVisible(visibleList);
				
				if (visibleList) { 
					buttonShowAll.setText("Hide Scripts");
					for(int i =0; i < scriptList.scriptCount; i++)
					  {
						 if (allScripts[i] != null )
						 {
					       scriptListModel.addElement(allScripts[i]);
						 }
					  }
				}
				else {
					scriptListModel.clear();
					buttonShowAll.setText("Show all scripts");
				}
			}		
		});
		

		buttonNewScript = new JButton("Create new script");
		buttonNewScript.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			ScriptEditFrame scriptMakerFrame = new ScriptEditFrame(true,0);	
	
			}
		});
		
		buttonEditScript = new JButton("Edit script");
		buttonEditScript.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			int editScriptId = 0;
			if (selectedScriptId != 0)
				{
				editScriptId = selectedScriptId;
				}
			ScriptEditFrame scriptEditorFrame = new ScriptEditFrame(false,editScriptId);	
	
			}
		});
		
		buttonReadScript = new JButton("Read a script");
		buttonReadScript.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
			String inputMessage;
			int scriptError = 0;
			inputMessage = JOptionPane.showInputDialog("which script");	 
			if (inputMessage.length()>0 )
			{
				validationScript k = new validationScript();
				//String currentScript;
				scriptError = k.fetchScriptValue(Integer.parseInt(inputMessage));			
			}
			}
		});
		buttonReadScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		SpringLayout scriptPanelayout = new SpringLayout();
		
		// put items on panel 
		scriptManagerLayout.add(buttonShowAll,"1,5,left,center");
		scriptManagerLayout.add(buttonNewScript, "1, 7, left, center");
		scriptManagerLayout.add(buttonReadScript, "1, 9, left, center");
		scriptManagerLayout.add(buttonEditScript, "1, 9, left, center");		
		//scriptManagerLayout.add(scriptListScrollPane,"1,50, left,center");
		scriptManagerLayout.add(scriptListScrollPane);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonNewScript,5,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonNewScript,5,SpringLayout.WEST,scriptManagerLayout);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonReadScript,5,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonReadScript,250,SpringLayout.WEST,scriptManagerLayout);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonShowAll,50,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonShowAll,5,SpringLayout.WEST,scriptManagerLayout);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonEditScript,5,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonEditScript,150,SpringLayout.WEST,scriptManagerLayout);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptListScrollPane,90,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptListScrollPane,5,SpringLayout.WEST,scriptManagerLayout);		
		scriptPanelayout.putConstraint(SpringLayout.SOUTH,scriptListScrollPane,300,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.EAST,scriptListScrollPane,400,SpringLayout.WEST,scriptManagerLayout);		

		scriptManagerLayout.setLayout(scriptPanelayout);
		
		scriptListScrollPane.setVisible(false);
		
		// add panel to frame
		//getContentPane().add(scriptManagerLayout, BorderLayout.NORTH);
		getContentPane().add(scriptManagerLayout);
		this.setVisible(true);

		
	}
	

	
}
