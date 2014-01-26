/*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package tree.persistence;

import javax.servlet.*;
import java.io.IOException;

/**
 * <h3>Фильтр для того, чтобы hibernate работал с local транзакциями и lazy-загрузкой</h3>
 * <p>
 *     Фактически реализация паттерна Open Session In View:
 *     EntitityManagerUtil отвечает за то, что в каждом потоке есть свой EntityManager;
 *     При каждом новом запросе происходит создание EntityManager, старт транзакции; после завершения обработки запроса
 *     транзакция коммитится, и EntityManager закрывается.
 *
 * </p>
 * <p>Author: predtechenskaya (predtechenskaya@i-teco.ru)</p>
 * <p>Date: 21.01.14</p>
 */
public class PersistenceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            EntityManagerUtil.getEm().getTransaction().begin();
            filterChain.doFilter(request, servletResponse);
            EntityManagerUtil.getEm().getTransaction().commit();
        }
        catch (Throwable e) {
            if ( EntityManagerUtil.getEm() != null && EntityManagerUtil.getEm().isOpen())
                EntityManagerUtil.getEm().getTransaction().rollback();
            throw e;

        } finally {
            EntityManagerUtil.closeEm();
        }

    }

    @Override
    public void destroy() {
    }
}
