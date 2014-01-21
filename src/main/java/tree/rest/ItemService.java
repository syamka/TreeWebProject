/*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package tree.rest;

import tree.persistence.EntityManagerUtil;
import tree.persistence.entity.Item;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * <h3></h3>
 * <p></p>
 * <p>Author: predtechenskaya (predtechenskaya@i-teco.ru)</p>
 * <p>Date: 21.01.14</p>
 */

@Path(value = "item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemService {
    @GET
    @Path(value = "/{id:[0-9]+}/children")
    public List<JaxbItem> getChildren(@PathParam("id")BigInteger id){
        Item parent =  EntityManagerUtil.getEm().find(Item.class, id);
        List<JaxbItem> result = new LinkedList<JaxbItem>();
        for(Item child: parent.getChildren()){
            result.add(new JaxbItem(child));
        }
        return result;
    }

}
