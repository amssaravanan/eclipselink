/*******************************************************************************
 * Copyright (c) 2011, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *  - Denise Smith February 12, 2013
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.xmlelementref.prefix;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.testing.jaxb.xmlelementref.prefix2.Child;

@XmlRootElement
public class Root {

     @XmlElement
     public String name;
     @XmlElementRef
     public Child child;

     public Root() {
     }

     public boolean equals(Object obj){
         if(obj instanceof Root){
             Root compare = (Root)obj;
             return name.equals(compare.name) && child.equals(compare.child);
         }
         return false;
     }
}
