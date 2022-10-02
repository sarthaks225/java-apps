package com.hr.pl.model;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;

import java.awt.Font;
import java.io.*;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;

import java.util.*;
import javax.swing.JTable;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;



public class DesignationModel extends AbstractTableModel
{
    public static int FILE_NOT_EXISTS=0;
    public static int FILE_ALREADY_EXISTS=1;

    private LinkedList<DesignationInterface> designations;
    private DesignationManagerInterface designationManager;
    private Set<DesignationInterface> blDesignations;
    private String columnTitles[];
    
    
    public DesignationModel()
    {
        populateDataStructure();
    }

    private void  populateDataStructure()
    {
        this.designations=new LinkedList<DesignationInterface>();
        this.columnTitles=new String[2];
        this.columnTitles[0]="S.No";
        this.columnTitles[1]="Designation";
        try
        {
        this.designationManager=DesignationManager.getDesignationManager();
        this.blDesignations=this.designationManager.getDesignations();
        }
        catch(BLException ble)
        {
            //?????? do something
        }


        for(DesignationInterface designation:this.blDesignations)
        {
            this.designations.add(designation);
        }
        Collections.sort(this.designations,new Comparator<DesignationInterface>(){
            public int compare(DesignationInterface left,DesignationInterface right)
            {
               return  left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
            }
        });
    }

    public int getColumnCount()
    {
        return this.columnTitles.length;
    }

    public String getColumnName(int columnIndex)
    {
        return this.columnTitles[columnIndex];
    }

    public int getRowCount()
    {
        return this.designations.size();
    }

    public Object getValueAt(int rowIndex,int columnIndex)
    {
        if(columnIndex==0) return rowIndex+1;
        return this.designations.get(rowIndex).getTitle();
    }

    public Class getColumnClass(int columnIndex)
    {
        if(columnIndex==0) return Integer.class;
        return String.class;
    }

    public boolean isCellEditable(int rowIndex,int columnIndex)
    {
        return false;
    }

    //Apllication specific methods
    public void add(DesignationInterface designation) throws BLException
    {
        designationManager.add(designation);
        this.designations.add(designation);
        Collections.sort(this.designations,new Comparator<DesignationInterface>(){
            public int compare(DesignationInterface left,DesignationInterface right)
            {
                return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
            }

        });

        fireTableDataChanged(); //method to update table which is on screen

    }

    public int indexOfDesignation(DesignationInterface designation) throws BLException
    {
        Iterator<DesignationInterface> iterator=this.designations.iterator();
        DesignationInterface d;
        int index=0;
        while(iterator.hasNext())
        {
            d=iterator.next();
            if(d.getTitle().trim().equalsIgnoreCase(designation.getTitle().trim())) return index;
            index++;
        }
        BLException blException=new BLException();
        blException.setGenericException("Invalid designation");
        throw blException;

    }

    public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
    {
        Iterator<DesignationInterface> iterator=this.designations.iterator();
        DesignationInterface d;
        int index=0;
        while(iterator.hasNext())
        {
            d=iterator.next();
            if(partialLeftSearch==true)
            {
                if(d.getTitle().toUpperCase().startsWith(title.toUpperCase())) return index;
            }
            else
            {
                if(d.getTitle().equalsIgnoreCase(title)) return index;

            }
            index++;
        }

        BLException blException=new BLException();
        blException.setGenericException("title not found");
        throw blException;
    }

    public void update(DesignationInterface designation) throws BLException
    {
        
        designationManager.update(designation);

        Iterator<DesignationInterface> iterator=this.designations.iterator();
        int code=designation.getCode();
        int index=0;
        while(iterator.hasNext())
        {
            
            if(iterator.next().getCode()==code) break;
            index++;
        }

        if(index==this.designations.size())
        {
            BLException blException=new BLException();
            blException.setGenericException("invalid designation code");
        }
    
        this.designations.remove(index);
        this.designations.add(designation);
        
        Collections.sort(designations,new Comparator<DesignationInterface>(){
            public int compare(DesignationInterface left,DesignationInterface right)
            {
                return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
            }
        });
        fireTableDataChanged();
    }
    
    public void remove(int code) throws BLException
    {
        
        designationManager.remove(code);
        Iterator<DesignationInterface> iterator=this.designations.iterator();
        int index=0;
        while(iterator.hasNext())
        {
            
            if(iterator.next().getCode()==code) break;
            index++;
        }

        if(index==this.designations.size())
        {
            BLException blException=new BLException();
            blException.setGenericException("invalid designation code");
        }
    
        this.designations.remove(index);
        fireTableDataChanged();
    }
     
    public DesignationInterface getDesignationAt(int index) throws BLException
    {
        if(index<0 || index>this.designations.size())
        {
            BLException blException=new BLException();
            blException.setGenericException("invalid index "+index);
            throw blException;
        }

        return this.designations.get(index);

    }    

    public int exportToPDF(File file) throws BLException
    {        

        try
        {
            if(file.exists()==true) return FILE_ALREADY_EXISTS;
            Document document=new Document();

            OutputStream outputStream=new FileOutputStream(file.getAbsolutePath());
            PdfWriter.getInstance(document,outputStream);

                // font.............
            com.itextpdf.text.Font companyNameFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,25,com.itextpdf.text.Font.BOLD | com.itextpdf.text.Font.UNDERLINE);
            com.itextpdf.text.Font pageNumberFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,10,com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font reportTitleFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,15,com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font columnFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,15,com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font dataFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,15);
                // ..................
            PdfPTable headingTable;
            PdfPTable pageNumberTable;
            PdfPTable titleTable;
            PdfPTable dataTable;

            Paragraph companyName=new Paragraph("SARTHAK PVT. LTD.",companyNameFont);
            
            boolean newPage=true;
            int pageNumber=1;
            int dataTableSize=20;   //tableSizeOnPage....
            Integer index=0;
            int x=0;
            int numberOfPages=this.designations.size()/dataTableSize;
            if(this.designations.size()%dataTableSize!=0) numberOfPages++;

            DesignationInterface designation;


            Image logo=Image.getInstance(this.getClass().getResource("/icons/logo_img.jpg"));

            PdfPCell cell;
        
            document.open();

            while(true)     //while loop bengins .....
            {

            headingTable=new PdfPTable(2);         // headingTable begins...
            cell=new PdfPCell(logo);
            cell.setFixedHeight(40);
         
            cell.setIndent(50);
            cell.setBorder(Rectangle.NO_BORDER);
            headingTable.addCell(cell);
         
            cell=new PdfPCell(companyName);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setFixedHeight(40);
            cell.setIndent(-115);
       
            cell.setBorder(Rectangle.NO_BORDER);
            headingTable.addCell(cell);
            document.add(headingTable);       //headingTable ends........

            document.add(new Paragraph("\n "+"\n")); //for spacing in pdf.....
            
            pageNumberTable=new PdfPTable(1);       //pageNumberTable begins....
            cell=new PdfPCell(new Paragraph("Page no: "+pageNumber+"/"+numberOfPages,pageNumberFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            pageNumberTable.addCell(cell);
            document.add(pageNumberTable);              //pageNumberTable ends....
            
            document.add(new Paragraph("\n"+"\n")); //for spacing in pdf.....

            titleTable=new PdfPTable(1);                //titleTable begins......
            cell=new PdfPCell(new Paragraph("List Of Designations",reportTitleFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            titleTable.addCell(cell);
            document.add(titleTable);                   //titleTable ends......


            dataTable=new PdfPTable(2);                   //dataTable begins....

            cell=new PdfPCell(new Paragraph("S.No",columnFont));
            cell.setFixedHeight(30);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(cell);

            cell=new PdfPCell(new Paragraph("Designations",columnFont));
            cell.setFixedHeight(20);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dataTable.addCell(cell);

            for(x=0; x<dataTableSize && index<designations.size(); ++x)
            {
                designation=designations.get(index);
                index++;
                cell=new PdfPCell(new Paragraph((index.toString()),dataFont));
                cell.setFixedHeight(23);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dataTable.addCell(cell);
                cell=new PdfPCell(new Paragraph(designation.getTitle(),dataFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                dataTable.addCell(cell);
            }
            
            document.add(dataTable);
            document.add(new Paragraph("\n\n"+"  Software By: Sarthak Sharma"+"\n        "+"©All Right Reserved",dataFont));
            
            if(index<designations.size())
            {
                document.newPage();
                pageNumber++;
                continue;
            }
            else break;
            
            }   // while loop ends......
           
            document.close();
            outputStream.close();

        }catch(Exception e)
        {
            BLException ble=new BLException();
            ble.setGenericException(e.getMessage());
            throw ble;
        }

    
        return 0;
    }
}




