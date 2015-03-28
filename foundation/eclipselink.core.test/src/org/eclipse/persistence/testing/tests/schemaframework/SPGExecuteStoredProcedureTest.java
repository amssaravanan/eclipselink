/*******************************************************************************
 * Copyright (c) 1998, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/
package org.eclipse.persistence.testing.tests.schemaframework;

import org.eclipse.persistence.queries.*;
import org.eclipse.persistence.testing.framework.*;

/**
 * Author: Edwin Tang
 * The test executes and verifies the stored procedures in the database
 * generated by SPGGenerateAmendmentClassTest.
 */
public class SPGExecuteStoredProcedureTest extends TestCase {
    boolean shouldBindAllParameters;
    int insertSuccess = 1;
    int updateSuccess = 1;
    int deleteSuccess = 1;
    String parameterNamePrefix;
    boolean shouldUseNamedArguments;
    static final Integer menuID = new Integer(99);
    static final Integer restaurantID = new Integer(100);
    static final Integer dinerID = new Integer(101);
    static final Integer personID = new Integer(102);
    static final Integer locationID = new Integer(103);
    static final Integer waiterID = new Integer(104);
    static final Integer menuItemID = new Integer(105);
    static final String menuType = "Lunch";
    static final String menuTypeUpdate = "Dinner";
    static final String dinerFirstName = "Steve";
    static final String dinerFirstNameUpdate = "Stephen";
    static final String dinerLastName = "McDonald";
    static final String dinerClass = "B";
    static final String personFirstName = "Dan";
    static final String personFirstNameUpdate = "Danial";
    static final String personLastName = "Smith";
    static final String personClass = "A";
    static final String locationArea = "Downtown";
    static final String locationAreaUpdate = "Southeastern";
    static final String locationCity = "Ottawa";
    static final String waiterFirstName = "Joel";
    static final String waiterLastName = "Clark";
    static final String waiterSpeciality = "Speaking Spanish";
    static final String waiterSpecialityUpdate = "Speaking Spanish and Italian";
    static final String waiterClass = "A";
    static final String menuItemName = "Roasted beef and potato";
    static final Float menuItemPrice = new Float(20.99f);
    static final Float menuItemPriceUpdate = new Float(22.99f);
    static final String restaurantName = "May Flower";
    static final String restaurantNameUpdate = "Great Wall Restaurant";

    public SPGExecuteStoredProcedureTest() {
    }

    public void setup() {
        shouldUseNamedArguments = !getSession().getDatasourcePlatform().isMySQL();
        if(shouldUseNamedArguments) {
            if(getSession().getDatasourcePlatform().isOracle()) {
                parameterNamePrefix = "P_";
            } else {
                parameterNamePrefix = "";
            }
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_Menu");
        addArgumentValue(call, "ID",menuID);
        addArgumentValue(call, "REST_ID",restaurantID);
        addArgumentValue(call, "TYPE",menuType);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_Diner");
        addArgumentValue(call, "ID",dinerID);
        addArgumentValue(call, "F_NAME",dinerFirstName);
        addArgumentValue(call, "CLASS",dinerClass);
        addArgumentValue(call, "L_NAME",dinerLastName);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_Person");
        addArgumentValue(call, "ID",personID);
        addArgumentValue(call, "F_NAME",personFirstName);
        addArgumentValue(call, "CLASS",personClass);
        addArgumentValue(call, "L_NAME",personLastName);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_Location");
        addArgumentValue(call, "AREA",locationArea);
        addArgumentValue(call, "ID",locationID);
        addArgumentValue(call, "CITY",locationCity);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_Waiter");
        addArgumentValue(call, "ID",waiterID);
        addArgumentValue(call, "F_NAME",waiterFirstName);
        addArgumentValue(call, "SPECIALT",waiterSpeciality);
        addArgumentValue(call, "CLASS",waiterClass);
        addArgumentValue(call, "W_RST_ID",restaurantID);
        addArgumentValue(call, "L_NAME",waiterLastName);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_MenuItem");
        addArgumentValue(call, "ID",menuItemID);
        addArgumentValue(call, "MENU_ID",menuID);
        addArgumentValue(call, "PRICE",menuItemPrice);
        addArgumentValue(call, "NAME",menuItemName);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("INS_Restaurant");
        addArgumentValue(call, "ID",restaurantID);
        addArgumentValue(call, "NAME",restaurantName);
        insertSuccess = insertSuccess * getSession().executeNonSelectingCall(call);
        }
    }

    public void test() {
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_Menu");
        addArgumentValue(call, "ID",menuID);
        addArgumentValue(call, "REST_ID",restaurantID);
        addArgumentValue(call, "TYPE",menuTypeUpdate);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_Diner");
        addArgumentValue(call, "ID",dinerID);
        addArgumentValue(call, "F_NAME",dinerFirstNameUpdate);
        addArgumentValue(call, "CLASS",dinerClass);
        addArgumentValue(call, "L_NAME",dinerLastName);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_Person");
        addArgumentValue(call, "ID",personID);
        addArgumentValue(call, "F_NAME",personFirstNameUpdate);
        addArgumentValue(call, "CLASS",personClass);
        addArgumentValue(call, "L_NAME",personLastName);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_Location");
        addArgumentValue(call, "AREA",locationAreaUpdate);
        addArgumentValue(call, "ID",locationID);
        addArgumentValue(call, "CITY",locationCity);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_Waiter");
        addArgumentValue(call, "ID",waiterID);
        addArgumentValue(call, "F_NAME",waiterFirstName);
        addArgumentValue(call, "SPECIALT",waiterSpecialityUpdate);
        addArgumentValue(call, "CLASS",waiterClass);
        addArgumentValue(call, "W_RST_ID",restaurantID);
        addArgumentValue(call, "L_NAME",waiterLastName);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_MenuItem");
        addArgumentValue(call, "ID",menuItemID);
        addArgumentValue(call, "MENU_ID",menuID);
        addArgumentValue(call, "PRICE",menuItemPriceUpdate);
        addArgumentValue(call, "NAME",menuItemName);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("UPD_Restaurant");
        addArgumentValue(call, "ID",restaurantID);
        addArgumentValue(call, "NAME",restaurantNameUpdate);
        updateSuccess = updateSuccess * getSession().executeNonSelectingCall(call);
        }
    }

    public void reset() {
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_Menu");
        addArgumentValue(call, "ID",menuID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("D_1M_Menu_items");
        addArgumentValue(call, "ID",menuID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_Diner");
        addArgumentValue(call, "ID",dinerID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_Person");
        addArgumentValue(call, "ID",personID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_Location");
        addArgumentValue(call, "ID",locationID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_Waiter");
        addArgumentValue(call, "ID",waiterID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_MenuItem");
        addArgumentValue(call, "ID",menuItemID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("DEL_Restaurant");
        addArgumentValue(call, "ID",restaurantID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("D_1M_Restaurant_waiters");
        addArgumentValue(call, "ID",restaurantID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
        {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("D_1M_Restaurant_menus");
        addArgumentValue(call, "ID",restaurantID);
        deleteSuccess = deleteSuccess * getSession().executeNonSelectingCall(call);
        }
    }

    public void verify() {
        if(insertSuccess * updateSuccess * deleteSuccess == 0 ) {
          throw new TestErrorException("Failed to call stored procedures.") ;
        }
    }

    protected void addArgumentValue(StoredProcedureCall call, String name, Object value) {
        if(shouldUseNamedArguments) {
            call.addNamedArgumentValue(parameterNamePrefix + name, value);
        } else {
            call.addUnamedArgumentValue(value);
        }
    }
}
