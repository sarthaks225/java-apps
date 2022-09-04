package com.hr.bl.interfaces.pojo;
import com.hr.bl.interfaces.pojo.*;
import java.util.*;
import java.math.*;
import com.hr.common.enums.*;

public interface EmployeeInterface extends Comparable<EmployeeInterface>,java.io.Serializable
{
public void setEmployeeId(String employeeId);
public String getEmployeeId();
public void setName(String name);
public String getName();
public void setDesignation(DesignationInterface desigantion);
public DesignationInterface getDesignation();
public void setDateOfBirth(Date dateOfBirth);
public Date getDateOfBirth();
public void setGender(GENDER gender);
public char getGender();
public void setIsIndian(boolean isIndian);
public boolean getIsIndian();
public void setBasicSalary(BigDecimal salary);
public BigDecimal getBasicSalary();
public void setPANNumber(String panNumber);
public String getPANNumber();
public void setAadharCardNumber(String aadarNumber);
public String getAadharCardNumber();
}