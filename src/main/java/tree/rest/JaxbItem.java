/*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package tree.rest;

import tree.persistence.entity.Item;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * <h3></h3>
 * <p></p>
 * <p>Author: predtechenskaya (predtechenskaya@i-teco.ru)</p>
 * <p>Date: 21.01.14</p>
 */
@XmlRootElement
public class JaxbItem {

    public JaxbItem(){}

    public JaxbItem(Item item){
        this.id = item.getId();
        this.text = item.getText();
        this.hasChildren = item.isHasChildren();
    }

    public JaxbItem(boolean hasChildren, BigInteger id, String text) {
        this.hasChildren = hasChildren;
        this.id = id;
        this.text = text;
    }

    public BigInteger id;
    public String text;
    public boolean hasChildren;

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
