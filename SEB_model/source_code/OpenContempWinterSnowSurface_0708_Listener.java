package energymodels;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader.*;
import java.io.CharArrayReader.*;
import java.io.FileInputStream.*;
import java.awt.image.*;

/**
 * 
 *  Distributed Surface Energy Balance (SEB) model source code (developed for my PhD)
 *  Copyright (C) 2013  Chris Williams

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 * ****************************************************************************
 * 
 * This class creates an "Open a File" dialog box when the user clicks on the "Open Elevation Surface"
 * menu item on the frame created by ModelGUI.java - this imports a 2D array
 * of the file opened and also a 1D array which is manipulated to display the
 * opened file as an image
 *
 * @author Chris Williams 31/10/10
 **/
public class OpenContempWinterSnowSurface_0708_Listener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;

  OpenContempWinterSnowSurface_0708_Listener(GUIPanel pn, Storage s)
  {

    panel = pn;
    store = s;

  }

  /**
   * Creates an "Open a File" dialog box from which the user can open a file. Methods from GRIDJava.java
   * are then invoked to create 1D and 2D arrays of the opened file - the 1D array being used to display an
   * image version of the file via the development of a MemoryImageSource object
   **/
  public void actionPerformed(ActionEvent e)
  {

    // Creates the "Open a File" dialog box

    FileDialog fd = new FileDialog(new Frame (), "Open 2007 - 2008 winter snow surface file", FileDialog.LOAD);
    fd.setVisible(true);

    // Creates a new File object from the file that is opened

    File file = new File(fd.getDirectory() + fd.getFile());

    //File file = new File("F:\\Model_Test_Files\\59surfacez.txt");

    // Creates a GRIDJava object from the opened file
    
    if((fd.getDirectory()== null)||(fd.getFile() == null))
    {
    System.out.println("Winter snow (2007-2008) thickness file not uploaded");
    return;
    }
    
    else{
	

    GRIDJava2 gj = new GRIDJava2(file, true, store);

    // Adds the GRIDJava object to the panel

    panel.addGRIDJavaMethod(gj);

    // Creates a 2D array from the GRIDJava object

    double snow_surface_0708[][] = gj.getTwoDdoubleArray();

    store.set_snow_surface_0708(snow_surface_0708);
    
    // Creates a 1D array from the GRIDJava object

    int snow_surface_0708_1D[] = gj.getOneDintArray();

    // Creates an Image object

    Image temp = null;

    // Creates a MemoryImageSource object which uses methods from GRIDJava.java and takes in the 1D array created above

    MemoryImageSource mis = new MemoryImageSource(gj.getNumberOfColumns(), gj.getNumberOfRows(), snow_surface_0708_1D, 0, gj.getNumberOfColumns());

    // Using a toolkit, this creates the image and assigns it to the Image object created earlier (previously set to null)

    temp = panel.getToolkit().createImage(mis);

    // Displays the image on the panel

    panel.displayImage(temp);

    System.out.println("2007 - 2008 winter snow surface uploaded");
    
    }
  }
}
