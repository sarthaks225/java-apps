package com.hr.pl.model;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;

import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.interfaces.managers.*;
import com.hr.bl.pojo.*;
import com.hr.bl.managers.*;

import java.util.*;
import javax.swing.JTable;

public class DesignationModel extends AbstractTableModel
{
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
}




