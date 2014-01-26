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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.*;

/**
 * <h3>REST-сервис для обработки AJAX-запросов</h3>
 * <p>
 *     Содержит все методы для работы с текстовыми узлами
 * </p>
 * <p>Author: predtechenskaya (predtechenskaya@i-teco.ru)</p>
 * <p>Date: 21.01.14</p>
 */

@Path(value = "item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemService {

    /**
     * Список узлов, дочерних к узлу с id=strRoot, или корневой узел, если strRoot=root
     * @param strRoot строковое представление id или root
     * @return список узлов
     */
    @GET
    @Path(value = "/children")
    public List<JaxbItem> getChildren(@QueryParam("root") String strRoot){
        List<JaxbItem> result = new LinkedList<JaxbItem>();
        Item parent;
        BigInteger id = null;

        if(!strRoot.equals("source"))
            id = BigInteger.valueOf(Long.parseLong(strRoot));
        if((parent = getById(id)) != null){
            for(Item child: parent.getChildren()){
                result.add(new JaxbItem(child));
            }
        }
        else result.add(new JaxbItem(getRoot()));

        return result;
    }

    /**
     * Добавление текстового узла
     * @param text текст узла
     * @param parentId родительский узел
     * @return результат операции и сообщение об ошибки в случае неудачи
     */
    @POST
    @Path(value = "/add")
    public OperationResponse add(@FormParam("text") String text,
                        @FormParam("parent_id") BigInteger parentId){
        try{
            Item parent;
            if((parent = getById(parentId)) == null)
                throw new Exception("Передан неверный ID родительского узла");
            if(text == null || text.isEmpty())
                throw  new Exception("Не передан текст узла");

            Item child = new Item();
            child.setText(text);
            child.setParent(parent);
            EntityManagerUtil.getEm().persist(child);
            //у родителя теперь точно есть потомки
            parent.setHasChildren(true);
            EntityManagerUtil.getEm().merge(parent);
            return new OperationResponse(true, "");
        }
        catch(Exception e){
            return new OperationResponse(false, e.getMessage());
        }
    }

    /**
     * Удаление текстового узла
     * @param id удаляемого узла
     * @return результат операции и сообщение об ошибки в случае неудачи
     */
    @POST
    @Path(value = "/{id:[0-9]+}/delete")
    public OperationResponse delete(@PathParam("id") BigInteger id){
        try{
            Item item;
            if((item = getById(id)) == null)
               throw new Exception("Передан некорректный ID узла");
            //Запрещаем удалять, если есть дочерние
            if(item.isHasChildren())
                throw new Exception("Невозможно удалить узел, у которого есть дочерние элементы");
            //Пересчитаем, остались ли дочерние элементы
            if(item.getParent().getChildren().size() == 1)
                //Если нет, то у родителя больше нет детей
                item.getParent().setHasChildren(false);

            EntityManagerUtil.getEm().remove(item);
            return new OperationResponse(true, "");
        }
        catch(Exception e){
            return new OperationResponse(false, e.getMessage());
        }

    }

    /**
     * Поиск узлов по вхождению текста
     * @param searchtext текст
     * @return объект, содержащий список id результатов и все узлы, которые должны быть развернуты
     */
    @GET
    @Path(value = "/search")
    public SearchResult search(@QueryParam("searchtext") String searchtext){

        List<Item> resultSet = EntityManagerUtil.getEm().createNamedQuery("Item.search")
                                                        .setParameter("searchtext", "%" + searchtext + "%")
                                                        .getResultList();

        SearchResult result = new SearchResult();
        for(Item item: resultSet){
            result.addResult(item.getId());

            //Эта конструкция не очень оптимальна с точки зрения sql-запросов (каждый следующий родитель запрашивается отдельно)
            //В случае реальных проблем с производительностью возможно хранить для каждого узла в базе путь "наверх"
            List<BigInteger> branches = new LinkedList<BigInteger>();
            Item parent = item;
            while(parent.getParent() != null){
                branches.add(parent.getParent().getId());
                parent = parent.getParent();
            }
            Collections.reverse(branches);
            result.addBranches(branches);

        }
        return result;
    }


    /**
     * Получить объект корневого узла
     * @return корневой узел
     */
    protected Item getRoot(){
        return (Item) EntityManagerUtil.getEm().createNamedQuery("Item.root").getSingleResult();
    }

    /**
     * Найти объект по ID (включает доп.проверку на id != NULL, id != 0, чтобы не обрабатывать исключения)
     * @param id искомого объекта
     * @return искомый Item или null
     */
    protected Item getById(BigInteger id){
        if(id == null || !(id.doubleValue() > 0))
            return null;
        return (Item) EntityManagerUtil.getEm().find(Item.class, id);
    }

}
