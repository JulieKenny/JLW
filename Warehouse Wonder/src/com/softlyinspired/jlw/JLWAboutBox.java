/*
 * This code is part of JLW Warehouse Wonder
 * Copyright (c) 2016-  Julie Kenny @ Softly Inspired  All rights reserved. 
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
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
           