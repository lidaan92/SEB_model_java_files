package energymodels;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

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
 * @author Chris 19/10/12
 */
public class SlopeListener implements ActionListener
{

  private GUIPanel panel = null;
  private Storage store = null;
  double A,B,C,D,E,F,G,H,I;
  double dzdy, dzdx;
  double surfaceZ[][], Slope_Surface[][];
  double slopeDegree;

  SlopeListener(GUIPanel pn, Storage s)
  {

    panel = pn;
    store = s;

  }
  
    @Override
  public void actionPerformed(ActionEvent e)
  {
      
      System.out.println("Slope button working");
      GRIDJava2 newerGJ = panel.getGRIDJavaMethod();

      double surfaceZ[][] = store.getElevation();
      double Slope_Surface[][] = new double[surfaceZ.length][surfaceZ[0].length];
      
      int NODATA_value = -9999;
      
      for(int i =0; i<surfaceZ.length; i++){
      for(int j = 0; j<surfaceZ[i].length; j++){

          if(surfaceZ[i][j] != NODATA_value){
               
              // Try/catch blocks exist in case of "out of bounds exceptions" 
              // which are likely when running the neighborhood searches at the 
              // array edges - this will be called upon less when using the 
              // larger arrays populated with No_Data (i.e. -9999.0) values
              //
              // These assign the values to letters A-I, according to positions 
              // within the array assuming the structure of:
              //   A   B   C
              //   D   E   F
              //   G   H   I
              
              try{    
              if((surfaceZ[i-1][j-1] != NODATA_value)){
                  A = surfaceZ[i-1][j-1];
              } else{
                  A = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe0){
                  A = surfaceZ[i][j];
              }   
                  
              try{
              if((surfaceZ[i-1][j] != NODATA_value)){
                  B = surfaceZ[i-1][j];           
              } else {
                  B = surfaceZ[i][j];  
              }
              } catch(ArrayIndexOutOfBoundsException aiobe1){
                  B = surfaceZ[i][j];
              }   
              
              try{
              if((surfaceZ[i-1][j+1] != NODATA_value)){
                  C = surfaceZ[i-1][j+1];
              } else {
                  //error1 = ("C fail");//
                  C = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe2){
                  //error1 = ("C fail catch"); //
                  C = surfaceZ[i][j];
              } 
              
              try{
              if((surfaceZ[i][j-1] != NODATA_value)) {
                  D = surfaceZ[i][j-1];
              } else {
                  D = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe3){
                  D = surfaceZ[i][j];
              } 
              
              try{
              if((surfaceZ[i][j] != NODATA_value)){
                  E = surfaceZ[i][j];
              } else {
                  E = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe4){
                  E = surfaceZ[i][j];
              }
              
              try{
              if((surfaceZ[i][j+1] != NODATA_value)) {
                  F = surfaceZ[i][j+1];
              } else {
                  F = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe5){
                  F = surfaceZ[i][j];
              }
             
              try{
              if((surfaceZ[i+1][j-1] != NODATA_value)){
                  G = surfaceZ[i+1][j-1];
              } else {
                  G = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe6){
                  G = surfaceZ[i][j];
              }
    
              try{
              if((surfaceZ[i+1][j] != NODATA_value)){
                  H = surfaceZ[i+1][j];
              } else {
                  H = surfaceZ[i][j];
              }
              } catch(ArrayIndexOutOfBoundsException aiobe7){
                  H = surfaceZ[i][j];
              }
                
              try{
              if((surfaceZ[i+1][j+1] != NODATA_value)){
                  I = surfaceZ[i+1][j+1];
              } else {
                  I = surfaceZ[i][j];
              }        
              } catch(ArrayIndexOutOfBoundsException aiobe8){
                  I = surfaceZ[i][j];
              }
              
          dzdx = (((C + (2*F) + I)-(A + (2*D)+G))/(8*5));
          dzdy = (((G + (2*H) + I)-(A + (2 * B) + C))/(8*5));
          slopeDegree = Math.atan(Math.sqrt((Math.pow(dzdx,2) 
                                + Math.pow(dzdy,2))))*(180/Math.PI);
          
          Slope_Surface[i][j] = slopeDegree;       
              
          } else if(surfaceZ[i][j] == NODATA_value){

              Slope_Surface[i][j] = NODATA_value;
             
       }
              
      }       
      
  }
      
      store.setSlope(Slope_Surface);
      System.out.println("Slope algortihm complete - slope surface stored.");

/**
* This bit will open a save box to save the slope layer as an 
* ASCII, using the coordinates of the opened up elevation surface
**/    
      
 //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   
    FileDialog fd = new FileDialog(new Frame(), 
                                        "Save slope surface", FileDialog.SAVE);
    fd.setVisible(true);

    File f = new File(fd.getDirectory() + fd.getFile());

    if((fd.getDirectory()== null)||(fd.getFile() == null)) // Prevents a nullnull file being saved - if this happens and you don't account for it,
    {                                                      // when you do nothing, the nullnull can be returned!
    System.out.println("Slope file not saved");
    return;
    }
    
    else{
        
    FileWriter fw;
    double[][] slopeSurface_toSAVE = store.getSlope();
    DecimalFormat df = new DecimalFormat("#.####");

    try
    {

      BufferedWriter bw = new BufferedWriter(new FileWriter(f));
      bw.write("ncols" + "         " + store.getOriginalNcols()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("nrows" + "         " + store.getOriginalNrows()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("xllcorner" + "     " + store.getOriginalXllcorner());
      bw.write(System.getProperty("line.separator"));
      bw.write("yllcorner" + "     " + store.getOriginalYllcorner());
      bw.write(System.getProperty("line.separator"));
      bw.write("cellsize" + "      " + store.getOriginalCellsize()); 
      bw.write(System.getProperty("line.separator"));
      bw.write("NODATA_value" + "  " + "-9999");
      bw.write(System.getProperty("line.separator"));

      /**
       *   Write out the array data
       **/
    
      String tempStr = "";

      for (int a = 0; a < Slope_Surface.length; a++)
      {
        for (int b = 0; b < Slope_Surface[a].length; b++)
        {


          if (Slope_Surface[a][b] == -9999.0)
          {

            bw.write("-9999 ");
          }
          else
          {

            bw.write(df.format(Slope_Surface[a][b]) + " ");

          }

        }
        bw.newLine();
      }

      bw.close();

    }
    catch (IOException ioe)
    {

      ioe.printStackTrace();

    }
    
    System.out.println("Slope surface stored" + f.getAbsolutePath());
    
    }
 //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
      
      }
   }
    

