/*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package tree.persistence.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

/**
 * <h3>Entity объекта - текстового узла</h3>
 * <p></p>
 * <p>Author: predtechenskaya (predtechenskaya@i-teco.ru)</p>
 * <p>Date: 21.01.14</p>
 */
@NamedQueries({
    //Запрос на корневой узел
    @NamedQuery(name="Item.root", query = "SELECT i FROM item i WHERE i.parent=0"),
    //Запрос на поиск узлов
    @NamedQuery(name="Item.search", query = "SELECT i FROM item i WHERE i.text LIKE :searchtext")
})

@Entity(name="item")
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private BigInteger id;

    @Column(name="text")
    protected String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @NotFound(action = NotFoundAction.IGNORE)
    protected Item parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    protected List<Item> children;

    @Column(name="has_children")
    protected boolean hasChildren;


    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Item> getChildren() {
        return children;
    }

    public void setChildren(List<Item> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "[" + getId() + "] " + getText();
    }
}
