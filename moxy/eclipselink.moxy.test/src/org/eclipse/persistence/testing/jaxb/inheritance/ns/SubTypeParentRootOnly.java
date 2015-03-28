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
 *     Oracle - December 2011
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.inheritance.ns;

import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "someNamespace")
public class SubTypeParentRootOnly extends BaseTypeWithRootElement{
    public boolean equals(Object obj){
        if(!(obj instanceof SubTypeParentRootOnly)){
            return false;
        }
        return true;
    }
}
