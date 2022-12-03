package com.hr.bl.managers;

public class Managers 
{
    private enum ManagerType{DESIGNATION,EMPLOYEE};
    public static ManagerType DESIGNATION=ManagerType.DESIGNATION;
    public static ManagerType EMPLOYEE=ManagerType.EMPLOYEE;
    
    public static String getManagerType(ManagerType managerType)
    {
        if(managerType==DESIGNATION) return "DesignationManager";
        if(managerType==EMPLOYEE) return "EmployeeManager";
        return "";// this will never happen
    }

    public static  class Designation
    {
        private enum Action{ADD_DESIGNATION,UPDATE_DESIGNATION,REMOVE_DESIGNATION,GET_DESIGNATION_BY_CODE,
        GET_DESIGNATION_BY_TITLE,GET_DESIGNATION_COUNT,DESIGNATION_CODE_EXISTS,DESIGNATION_TITLE_EXISTS,
        GET_DESIGNATIONS
        };

        public static Action ADD_DESIGNATION=Action.ADD_DESIGNATION;
        public static Action UPDATE_DESIGNATION=Action.UPDATE_DESIGNATION;
        public static Action REMOVE_DESIGNATION=Action.REMOVE_DESIGNATION;
        public static Action GET_DESIGNATION_BY_CODE=Action.GET_DESIGNATION_BY_CODE;
        public static Action GET_DESIGNATION_BY_TITLE=Action.GET_DESIGNATION_BY_TITLE;
        public static Action GET_DESIGNATION_COUNT=Action.GET_DESIGNATION_COUNT;
        public static Action DESIGNATION_CODE_EXISTS=Action.DESIGNATION_CODE_EXISTS;
        public static Action DESIGNATION_TITLE_EXISTS=Action.DESIGNATION_TITLE_EXISTS;
        public static Action  GET_DESIGNATIONS=Action.GET_DESIGNATIONS;

        public static String getAction(Designation.Action action)
        {
            if(action==ADD_DESIGNATION) return "addDesigntion";
            if(action==UPDATE_DESIGNATION) return "updateDesignation";
            if(action==REMOVE_DESIGNATION) return "removeDesignation";
            if(action==GET_DESIGNATION_BY_CODE) return "getDesignationByCode";
            if(action==GET_DESIGNATION_BY_TITLE) return "getDesignationByTitle";
            if(action==GET_DESIGNATION_COUNT) return "getDesignationCount";
            if(action==DESIGNATION_CODE_EXISTS) return "designationCodeExists";
            if(action==DESIGNATION_TITLE_EXISTS) return "designationTitleExists";
            if(action==GET_DESIGNATIONS) return "getDesignations";
            return ""; // this will never happen
        }


    }

}
