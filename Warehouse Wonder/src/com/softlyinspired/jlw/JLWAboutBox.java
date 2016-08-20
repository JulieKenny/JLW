package com.softlyinspired.jlw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class JLWAboutBox extends JDialog {
	  
public JLWAboutBox() {
    this.setModal(true);
	this.setSize(400,200);    
	this.setLocationRelativeTo(null); /* Put in centre of screen */
	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	Box b = Box.createVerticalBox();

    b.add(Box.createGlue());
    b.add(new JLabel("  Warehouse Wonder"));
    b.add(new JLabel("  (C) Julie Kenny 2016"));
    b.add(new JLabel("  www.softlyInspired.com"));
    b.add(Box.createGlue());
    getContentPane().add(b, "Center");

    JPanel p2 = new JPanel();
    JButton ok = new JButton("Ok");
    p2.add(ok);
    getContentPane().add(p2, "South");

    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        setVisible(false);
      }
    });

    this.setVisible(true);
  }


}
           