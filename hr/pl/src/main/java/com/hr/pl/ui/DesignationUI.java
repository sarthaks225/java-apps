package com.hr.pl.ui;
import com.hr.pl.model.*;
//import com.hr.pl.icons.*;
import com.hr.pl.*;
import com.hr.bl.exceptions.*;
import com.hr.bl.interfaces.pojo.*;
import com.hr.bl.pojo.*;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;



public class DesignationUI extends JFrame
{
    private JLabel titleLabel,searchLabel,searchErrorLabel;
    private JTextField searchTextField;
    private JButton clearSearchFieldButton;

    private JTable designationTable;
    private DesignationModel designationModel;

    private DesignationPanel designationPanel;
    private JScrollPane jsp;
    private Container container;

    private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF}
    private MODE mode;

    public DesignationUI()
    {
        super("Designation Manager");
        Image icon=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icons/logo_img.jpg"));
        setIconImage(icon);
        initComponents();
        setAppearance();
        addListeners();

        setViewMode();
        designationPanel.setViewMode();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents()
    {

        titleLabel=new JLabel("Designations");
        searchLabel=new JLabel("Search");
        searchErrorLabel=new JLabel("");
        searchTextField=new JTextField();
        clearSearchFieldButton=new JButton(new ImageIcon(this.getClass().getResource("/icons/close_img.png")));
        designationModel=new DesignationModel();

        designationTable=new JTable(designationModel);
        designationPanel=new DesignationPanel();
     
        jsp=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        container=getContentPane(); 


    }

    private void setAppearance()
    {
        Font titleFont=new Font("Lucida Console",Font.BOLD,18);
        Font captionFont=new Font("Lucida Console",Font.BOLD,15);
        Font dataFont=new Font("Lucida Console",Font.PLAIN,15);
        Font designationTableHeaderFont=new Font("Lucida Console",Font.BOLD,16);
        Font searchErrorFont=new Font("Lucida Console",Font.BOLD,13);
        titleLabel.setFont(titleFont);
        searchLabel.setFont(captionFont);
        searchTextField.setFont(dataFont);
        searchErrorLabel.setFont(searchErrorFont);
        searchErrorLabel.setForeground(Color.red);
        
        designationTable.setRowHeight(35);
        designationTable.setFont(dataFont);
        designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        designationTable.getColumnModel().getColumn(1).setPreferredWidth(345);
        
        JTableHeader designationTableHeader=designationTable.getTableHeader();
        designationTableHeader.setReorderingAllowed(false);
        designationTableHeader.setResizingAllowed(false);
        designationTableHeader.setFont(designationTableHeaderFont);

        designationTable.setTableHeader(designationTableHeader);
        designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        

        container.setLayout(null);
        int lm,tm;
        lm=0;
        tm=0;

        titleLabel.setBounds(lm+10,tm+5,200,50);
        container.add(titleLabel);
        searchErrorLabel.setBounds(lm+50+60,tm+5+50+10-25,400,35);
        container.add(searchErrorLabel);
        searchLabel.setBounds(lm+10,tm+5+50+10,150,40);
        container.add(searchLabel);
        searchTextField.setBounds(lm+5+110,tm+5+50+10,300,30);
        container.add(searchTextField);
        clearSearchFieldButton.setBounds(lm+5+110+300,tm+5+50+10,50,30);
        container.add(clearSearchFieldButton);

        jsp.setBounds(lm+10,tm+5+50+10+30+10,455,300);
        container.add(jsp);
        designationPanel.setBounds(lm+10,tm+5+100+300+10,455,200);
        container.add(designationPanel);

        int width=500;
        int height=680;
        setSize(width,height);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width/2-width/2,d.height/2-height/2);

    }

    private void addListeners()
    {
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent de)   
            {
                searchRow();
            }
            public void removeUpdate(DocumentEvent de)
            {
                searchRow();
            }

            public void insertUpdate(DocumentEvent de)
            {
                searchRow();

            }
            
            public void searchRow()
            {
                searchErrorLabel.setText("");
                String title=searchTextField.getText().trim();
                if(title.length()==0) return;
            
                int rowIndex;
                try
                {
                    rowIndex=designationModel.indexOfTitle(title, true);
                    designationTable.setRowSelectionInterval(rowIndex, rowIndex);

                    //Rectangle used to scroll in table for searched designation.....
                    Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
                    designationTable.scrollRectToVisible(rectangle);
                    //..........

                    designationPanel.setDesignation(designationModel.getDesignationAt(rowIndex));
                    designationPanel.designationLabel.setText(designationModel.getDesignationAt(rowIndex).getTitle());

                }catch(BLException ble)
                {
                    searchErrorLabel.setText(ble.getGenericException());

                }

            }
        });
        
        clearSearchFieldButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                searchTextField.setText("");
                searchTextField.requestFocus();
            }
        });

        designationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() { //for inner class designationPanel
            public void valueChanged(ListSelectionEvent listSelectionEvent)
            {
                int selectedRowIndex=designationTable.getSelectedRow();
                try
                {
                    DesignationInterface designation=designationModel.getDesignationAt(selectedRowIndex);
                    designationPanel.setDesignation(designation);
                }catch(BLException ble)
                {
                    designationPanel.clearDesignation();
                }
            }
        });

    }

    private void setViewMode()
    {
        this.mode=MODE.VIEW;
        if(designationModel.getRowCount()==0)
        {
            searchTextField.setEnabled(false);
            clearSearchFieldButton.setEnabled(false);
            designationTable.setEnabled(false);
        }
        else 
        {
            searchTextField.setEnabled(true);
            clearSearchFieldButton.setEnabled(true);
            designationTable.setEnabled(true);

        }
        searchTextField.requestFocus();
        
            
    }

    private void setAddMode()
    {
        this.mode=MODE.ADD;
        searchTextField.setEnabled(false);
        clearSearchFieldButton.setEnabled(false);
        designationTable.setEnabled(false);
    }

    private void setEditMode()
    {
        this.mode=MODE.EDIT;
        searchTextField.setEnabled(false);
        clearSearchFieldButton.setEnabled(false);
        designationTable.setEnabled(false);
    }

    private void setDeleteMode()
    {
        this.mode=MODE.DELETE;
        searchTextField.setEnabled(false);
        clearSearchFieldButton.setEnabled(false);
        designationTable.setEnabled(false);

    }

    private void setExportToPDFMode()
    {
        this.mode=MODE.EXPORT_TO_PDF;
        searchTextField.setEnabled(false);
        clearSearchFieldButton.setEnabled(false);
        designationTable.setEnabled(false);
    }


        // Inner class ...................
    class DesignationPanel extends JPanel
    {
        private JLabel designationCaptionLabel;
        private JLabel designationLabel;
        private JTextField textField;
        private JButton clearTextFieldButton;
        private JButton addButton,editButton,deleteButton,cancelButton,pdfButton;
        private JButton saveButton,updateButton;
        
        private DesignationInterface designation;
        public DesignationPanel()
        {
            setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
            initComponents();
            setAppearance();
            addListeners();
        }
        private void initComponents()
        {
            
            designationCaptionLabel=new JLabel("Designation");
            designationLabel=new JLabel("");
            textField=new JTextField();
            clearTextFieldButton=new JButton(new ImageIcon(DesignationUI.this.getClass().getResource("/icons/close_img.png")));
            addButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/add_img.png")));
            editButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/edit_img.png")));
            deleteButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/delete_img.png")));
            cancelButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/cancel_img.png")));
            pdfButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/pdf_img.png")));

            saveButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/save_update_img.png")));
            updateButton=new JButton(new javax.swing.ImageIcon(DesignationUI.this.getClass().getResource("/icons/save_update_img.png")));

            designation=null;
            
        }

        private void setAppearance()
        {
            Font captionFont=new Font("Lucida Console",Font.BOLD,15);
            Font dataFont=new Font("Lucida Console",Font.PLAIN,15);

            designationLabel.setFont(dataFont);
            designationCaptionLabel.setFont(captionFont);
            textField.setFont(dataFont);

            int lm,tm;
            lm=0;
            tm=0;

            setLayout(null);
            designationCaptionLabel.setBounds(lm+10,tm+10,130,40);
            add(designationCaptionLabel);

            designationLabel.setBounds(lm+10+130+5,tm+10+5,300,30);
            add(designationLabel);

            textField.setBounds(lm+10+120,tm+10+2+30,270,30);
            add(textField);
            
            clearTextFieldButton.setBounds(lm+10+120+270,tm+10+2+30,50,30);
            add(clearTextFieldButton);

            addButton.setBounds(lm+45,tm+10+40+20+20,70,70);
            add(addButton);

            editButton.setBounds(lm+45+70+5,tm+20+40+10+20,70,70);
            add(editButton);
            
            deleteButton.setBounds(lm+45+70+5+70+5,tm+20+40+10+20,70,70);
            add(deleteButton);

            cancelButton.setBounds(lm+45+70+5+70+5+70+5,tm+20+40+10+20,70,70);
            add(cancelButton);
            
            pdfButton.setBounds(lm+45+70+5+70+5+70+5+70+5,tm+20+40+10+20,70,70);
            add(pdfButton);

            saveButton.setBounds(lm+45,tm+10+40+20+20,70,70);
            add(saveButton);

            updateButton.setBounds(lm+45,tm+10+40+20+20,70,70);
            add(updateButton);

            setViewMode();
            DesignationUI.this.setViewMode();
        }

        private void addListeners()
        {

            addButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    
                    setAddMode();
                    DesignationUI.this.setAddMode();
                    textField.requestFocus();
                }
            });

            editButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                   
                    DesignationUI.this.setEditMode();
                    setEditMode();
                    textField.requestFocus();
                }

            });

            deleteButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                   
                    if(designation==null || designation.getTitle().trim().length()==0)
                    {
                        JOptionPane.showMessageDialog(DesignationUI.this,"Select a designation to remove","Message",JOptionPane.INFORMATION_MESSAGE);
                        return;

                    }
                    String title=designation.getTitle();
                    
                    try
                    {
                        int confirmation=JOptionPane.showConfirmDialog(DesignationUI.this, "remove designation ("+title+") ?", "Confirmation",JOptionPane.YES_NO_OPTION);
                        if(confirmation==JOptionPane.NO_OPTION)
                        {
                            
                            JOptionPane.showMessageDialog(DesignationUI.this,"operation canceled", "Message", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        designationModel.remove(designation.getCode());
                    }
                    catch(BLException ble)
                    {
                        searchErrorLabel.setText("("+designationLabel.getText()+") can not remove");
                        return;
                    }

                    JOptionPane.showMessageDialog(DesignationUI.this,"Designation ("+title+") removed", "Message", JOptionPane.INFORMATION_MESSAGE);                    
                    setViewMode();
                    DesignationUI.this.setViewMode();
                }

            });

            cancelButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                   
                    DesignationUI.this.designationTable.clearSelection();
                    setViewMode();
                    DesignationUI.this.setViewMode();
                }
            });

            saveButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    DesignationInterface d=new Designation();
                    String title=textField.getText().trim();
                    if(title.length()==0)
                    {

                        textField.requestFocus();
                        JOptionPane.showMessageDialog(DesignationUI.this, "Designation title require" , "Message", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                   

                    d.setTitle(title);
                    try
                    {
                        designationModel.add(d);

                        int rowIndex=designationModel.indexOfTitle(title, true);
                        designationTable.setRowSelectionInterval(rowIndex, rowIndex); 

                        designationPanel.setDesignation(designationModel.getDesignationAt(rowIndex));

                        //Rectangle used to scroll in table for searched designation.....
                        Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
                        designationTable.scrollRectToVisible(rectangle);
                        //..........

                    }catch(BLException ble)
                    {
                        JOptionPane.showMessageDialog(DesignationUI.this, "Designation ("+title+") already exists" , "Message", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    setViewMode();
                    DesignationUI.this.setViewMode();
                }

            });

            updateButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
        
                    int rowIndex=0;
                    String title=textField.getText().trim();
                    String oldTitle=designation.getTitle().trim();
                    if(title.length()==0)
                    {
                        JOptionPane.showMessageDialog(DesignationUI.this,"Designation requires to update ?","Message",JOptionPane.INFORMATION_MESSAGE);
                        textField.requestFocus();
                        return;
                    }
                    if(oldTitle.equalsIgnoreCase(title))
                    {
                        JOptionPane.showMessageDialog(DesignationUI.this,"No changes done by user", "Message", JOptionPane.INFORMATION_MESSAGE);
                        textField.requestFocus();
                        return;
                    }

                    DesignationInterface d=new Designation();
                    
                    try
                    {
                        d.setCode(designation.getCode());
                        d.setTitle(title);
                        
                        int confirmation=JOptionPane.showConfirmDialog(DesignationUI.this,"Designation ("+oldTitle+") will be changed to ("+title+")","Confirmation",JOptionPane.YES_NO_OPTION);
                        if(confirmation==JOptionPane.NO_OPTION)
                        {
                            JOptionPane.showMessageDialog(DesignationUI.this,"Operation aborted", "Message", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        designationModel.update(d);
                        
                        rowIndex=designationModel.indexOfTitle(title, true);
                        designationTable.setRowSelectionInterval(rowIndex, rowIndex);
                        designationPanel.setDesignation(designationModel.getDesignationAt(rowIndex));
                        //Rectangle used to scroll in table for searched designation.....
                        Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
                        designationTable.scrollRectToVisible(rectangle);
                        //..........

                    }catch(BLException ble)
                    {
                        JOptionPane.showMessageDialog(DesignationUI.this, "desigantion ("+title+") already exists" , "Message", JOptionPane.INFORMATION_MESSAGE);
                        textField.requestFocus();
                        return;
                    }
                    JOptionPane.showMessageDialog(DesignationUI.this, "("+oldTitle+") updated to ("+title+")" , "Message", JOptionPane.INFORMATION_MESSAGE);
                    setViewMode();
                    DesignationUI.this.setViewMode();



                    //.............                    
                }

            });

            clearTextFieldButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    textField.setText("");
                    textField.requestFocus();

                }
            });

            
            pdfButton.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent ae)
                {
                    JFileChooser fileChooser=new JFileChooser();
                    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){
                        public boolean accept(java.io.File file)
                        {
                            String fileName;
                            fileName=file.getName();
                            if(file.isDirectory()) return true;
                            else if(fileName.endsWith(".pdf")) return true;
                            return false;
                        }
                        public  java.lang.String getDescription()
                        {
                            return "pdf documents (*.pdf)";
                        }
                    });
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.setCurrentDirectory(new File("."));
                     // done done 
                    int selectedOption=fileChooser.showSaveDialog(DesignationUI.this);
                    
                    if(selectedOption==JFileChooser.APPROVE_OPTION)
                    {
                        try
                        {
                            File selectedFile=fileChooser.getSelectedFile();
                    
                            String pdfFile=selectedFile.getAbsolutePath();
                            if(pdfFile.endsWith(".")) pdfFile+="pdf";
                            else if(pdfFile.endsWith(".pdf")==false) pdfFile+=".pdf";

                            File file=new File(pdfFile);
                            File parent=new File(file.getParent());
                            if(parent.exists()==false || parent.isDirectory()==false)
                            {
                                JOptionPane.showMessageDialog(DesignationUI.this,"path does not exists: "+file.getParent(),"Message",JOptionPane.INFORMATION_MESSAGE);
                            }
                            else
                            {

                                createPDF(file);
                            }

                        }catch(BLException e)
                        {
                            JOptionPane.showMessageDialog(DesignationUI.this,e.getMessage(),"Message",JOptionPane.INFORMATION_MESSAGE);
                        }

                    }


                }

                public void createPDF(File file) throws BLException
                {
                    int checkResult,choice;
                    try
                    {
                    checkResult=designationModel.exportToPDF(file);
                    if(checkResult==DesignationModel.FILE_ALREADY_EXISTS)
                    {
                        choice=JOptionPane.showConfirmDialog(DesignationUI.this,"Do you want to replace with old pdf with new one?","File already exists",JOptionPane.YES_NO_OPTION);
                        if(choice==JOptionPane.YES_OPTION)
                        {
                            file.delete();
                            createPDF(file);
                        }
                        else return;
                    }

                    }catch(BLException e)
                    {
                        throw e;
                    }
                }



            });

        }

        public void setDesignation(DesignationInterface designation)
        {
            this.designation=designation;
            designationLabel.setText(designation.getTitle());

        }

        public void clearDesignation()
        {
            this.designation=null;
            designationLabel.setText("");

        }
        

        void setViewMode()
        {
            DesignationUI.this.setViewMode();
            addButton.setVisible(true);
            addButton.setEnabled(true);
            cancelButton.setEnabled(false);

            if(designationModel.getRowCount()==0)
            {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                pdfButton.setEnabled(false);
            }
            else
            {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                pdfButton.setEnabled(true);
            }

            saveButton.setVisible(false);
            updateButton.setVisible(false);
            clearTextFieldButton.setVisible(false);
            
            designationLabel.setVisible(true);
            designationLabel.setText("");
            textField.setText("");
            textField.setVisible(false);

        }
       
        void setAddMode()
        {
            DesignationUI.this.setAddMode();
            addButton.setEnabled(true);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cancelButton.setEnabled(true);
            pdfButton.setEnabled(false);

            addButton.setVisible(false);
            
            saveButton.setVisible(true);
            updateButton.setVisible(false);
            clearTextFieldButton.setVisible(true);
            designationLabel.setVisible(false);
            textField.setVisible(true);
            
        }

        void setEditMode()
        {

            if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
            {
                JOptionPane.showMessageDialog(this,"Select designation to edit");
                setViewMode();
                DesignationUI.this.setViewMode();
                return;
            }
            DesignationUI.this.setEditMode();

            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cancelButton.setEnabled(true);
            pdfButton.setEnabled(false);

            addButton.setVisible(false);

            saveButton.setVisible(false);
            updateButton.setVisible(true);
            clearTextFieldButton.setVisible(true);
            designationLabel.setVisible(true);
            designationLabel.setText(this.designation.getTitle());
            textField.setVisible(true);
            textField.setText(this.designation.getTitle());
        }

        void setDeleteMode()
        {
            if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
            {
                JOptionPane.showMessageDialog(this,"Select designation to delete");
                return;
            }
            DesignationUI.this.setDeleteMode();

            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cancelButton.setEnabled(false);
            pdfButton.setEnabled(false);

            addButton.setVisible(true);

            saveButton.setVisible(false);
            updateButton.setVisible(false);

            designationLabel.setVisible(false);
            textField.setVisible(false);
        }

        void setExportTOPDFMode()
        {
            DesignationUI.this.setExportToPDFMode();

            addButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            cancelButton.setEnabled(false);
            pdfButton.setEnabled(false);

            addButton.setVisible(true);

            saveButton.setVisible(false);
            updateButton.setVisible(false);

            designationLabel.setVisible(false);
            textField.setVisible(false);

            
        }

       
    }   // inner class ends

}
