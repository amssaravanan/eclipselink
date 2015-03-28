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
 *     Denise Smith - May 2013
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.xmlvariablenode;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlVariableNode;

@XmlRootElement(name="root")
public class RootWithObjectList {

    public String name;

    @XmlVariableNode(value="thingName", type=Thing.class)
    public List<Object> things;

    public boolean equals(Object obj){
        if(obj instanceof RootWithObjectList){
            return things.equals(((RootWithObjectList)obj).things) &&
            name.equals(((RootWithObjectList)obj).name);
        }
        return false;
    }
}
