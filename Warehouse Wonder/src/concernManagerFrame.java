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

public class concernManagerFrame extends JFrame
 {
  public static void OpenFrame(String[] args)
  {
	  new concernManagerFrame();
  }
  
	private static JButton buttonNewConcern;
	private static JButton buttonEditConcern;	
	private static JButton buttonMenuApplyConcern;
	int selectedConcernId = 0;	

	public concernManagerFrame() 
	{
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Concern Manager");
		 
		JPanel concernManagerLayout = new JPanel();
		
		final DefaultListModel concernListModel = new DefaultListModel();
		final JList concernList = new JList(concernListModel);
		final JScrollPane concernListScrollPane = new JScrollPane(concernList); 
		concernList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		concernList.addListSelectionListener(new ListSelectionListener(){
		
			public void valueChanged(ListSelectionEvent ie){
				try {
				int inx = concernList.getSelectedIndex();
				String selectedConcernName = new String();
				selectedConcernName = concernList.getSelectedValue().toString();
				selectedConcernId = Integer.parseInt(selectedConcernName.substring(0,selectedConcernName.indexOf(":")-1));
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null,"Invalid Concern Listed", "Concern Manager",JOptionPane.PLAIN_MESSAGE);
				}
				
			}
		} );
		
		String[][] allConcerns = new String[100][2];	
		concernSet concernSet = new concernSet();
			
		allConcerns = concernSet.listall(); 
				
		concernListScrollPane.setVisible(true);		
		for(int i =0; i <= concernSet.concernCount ; i++)
			{
				if (allConcerns[i][0] != null )
				 {	 
				   concernListModel.addElement(allConcerns[i][0] + " : " + allConcerns[i][1]);
				 }
			 }

		buttonNewConcern = new JButton("Create new concern");
		buttonNewConcern.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			concernEditFrame concernMakerFrame = new concernEditFrame(-1,true);	
			}
		});
		
		buttonEditConcern = new JButton("Edit concern");
		buttonEditConcern.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			int editConcernId = 0;
			if (selectedConcernId != 0)
				{
				editConcernId = selectedConcernId;
				}
			concernEditFrame concernEditorFrame = new concernEditFrame(editConcernId,false);	
	
			}
		});

		buttonMenuApplyConcern = new JButton("Apply concern to menu");
		buttonMenuApplyConcern.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			int addingConcernId = 0;
			if (selectedConcernId != 0)
				{
				addingConcernId = selectedConcernId;
				mongoMenu m = new mongoMenu();
				try {
					m.addConcernItem(addingConcernId);
				} catch (UnknownHostException e) {
					System.out.println("e");
				}
				}
			//concernEditFrame concernEditorFrame = new concernEditFrame(editConcernId,false);	
	
			}
		});
		
		SpringLayout scriptPanelayout = new SpringLayout();
		
		// put items on panel 
		concernManagerLayout.add(buttonNewConcern, "1, 7, left, center");
		concernManagerLayout.add(buttonEditConcern, "1, 9, left, center");		
		concernManagerLayout.add(buttonMenuApplyConcern, "12,left,center");

		concernManagerLayout.add(concernListScrollPane);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonNewConcern,5,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonNewConcern,5,SpringLayout.WEST,concernManagerLayout);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonEditConcern,5,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonEditConcern,250,SpringLayout.WEST,concernManagerLayout);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonMenuApplyConcern,40,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonMenuApplyConcern,5,SpringLayout.WEST,concernManagerLayout);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,concernListScrollPane,90,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,concernListScrollPane,5,SpringLayout.WEST,concernManagerLayout);		
		scriptPanelayout.putConstraint(SpringLayout.SOUTH,concernListScrollPane,300,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.EAST,concernListScrollPane,400,SpringLayout.WEST,concernManagerLayout);		

		concernManagerLayout.setLayout(scriptPanelayout);
		
		// add panel to frame
		getContentPane().add(concernManagerLayout);
		this.setVisible(true);

	}
	
}
