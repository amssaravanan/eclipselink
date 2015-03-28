/*******************************************************************************
 * Copyright (c) 2013, 2015  Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Blaise Doughan - 2.4.2 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.unmarshaller.validation;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"street", "city"})
public class Address {

    public String street;
    public String city;

    @Override
    public boolean equals(Object obj) {
        if(null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        Address test = (Address) obj;
        if(!street.equals(test.street)) {
            return false;
        }
        if(!city.equals(test.city)) {
            return false;
        }
        return true;
    }

}
