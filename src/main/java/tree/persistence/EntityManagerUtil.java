/*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package tree.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * <h3>Утилита для доступа к Entity Manager</h3>
 * <p>
 *     http://stackoverflow.com/questions/15071238/entitymanager-threadlocal-pattern-with-jpa-in-jse
 * </p>
 * <p>Author: predtechenskaya (predtechenskaya@i-teco.ru)</p>
 * <p>Date: 21.01.14</p>
 */
public class EntityManagerUtil {
    public static final EntityManagerFactory emf;
        private static final ThreadLocal<EntityManager> threadLocal;

        static {
            try{
                emf = Persistence.createEntityManagerFactory("main");
                threadLocal = new ThreadLocal<EntityManager>();
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }

        public static EntityManager getEm() {
            EntityManager em = threadLocal.get();

            if (em == null) {
                em = emf.createEntityManager();
                threadLocal.set(em);
            }
            return em;
        }

        public static void closeEm(){
            EntityManager em = threadLocal.get();
            if (em != null) {
                em.close();
                threadLocal.set(null);
            }
        }

}
