/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA.Models.Controllers;

import JPA.Models.Controllers.exceptions.IllegalOrphanException;
import JPA.Models.Controllers.exceptions.NonexistentEntityException;
import JPA.Models.Controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import JPA.Models.Users;
import JPA.Models.Orderdetial;
import JPA.Models.Orders;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Asus
 */
public class OrdersJpaController implements Serializable {

    public OrdersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orders orders) throws RollbackFailureException, Exception {
        if (orders.getOrderdetialList() == null) {
            orders.setOrderdetialList(new ArrayList<Orderdetial>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users userid = orders.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getUserid());
                orders.setUserid(userid);
            }
            List<Orderdetial> attachedOrderdetialList = new ArrayList<Orderdetial>();
            for (Orderdetial orderdetialListOrderdetialToAttach : orders.getOrderdetialList()) {
                orderdetialListOrderdetialToAttach = em.getReference(orderdetialListOrderdetialToAttach.getClass(), orderdetialListOrderdetialToAttach.getOrderdeid());
                attachedOrderdetialList.add(orderdetialListOrderdetialToAttach);
            }
            orders.setOrderdetialList(attachedOrderdetialList);
            em.persist(orders);
            if (userid != null) {
                userid.getOrdersList().add(orders);
                userid = em.merge(userid);
            }
            for (Orderdetial orderdetialListOrderdetial : orders.getOrderdetialList()) {
                Orders oldOrderidOfOrderdetialListOrderdetial = orderdetialListOrderdetial.getOrderid();
                orderdetialListOrderdetial.setOrderid(orders);
                orderdetialListOrderdetial = em.merge(orderdetialListOrderdetial);
                if (oldOrderidOfOrderdetialListOrderdetial != null) {
                    oldOrderidOfOrderdetialListOrderdetial.getOrderdetialList().remove(orderdetialListOrderdetial);
                    oldOrderidOfOrderdetialListOrderdetial = em.merge(oldOrderidOfOrderdetialListOrderdetial);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orders orders) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orders persistentOrders = em.find(Orders.class, orders.getOrderid());
            Users useridOld = persistentOrders.getUserid();
            Users useridNew = orders.getUserid();
            List<Orderdetial> orderdetialListOld = persistentOrders.getOrderdetialList();
            List<Orderdetial> orderdetialListNew = orders.getOrderdetialList();
            List<String> illegalOrphanMessages = null;
            for (Orderdetial orderdetialListOldOrderdetial : orderdetialListOld) {
                if (!orderdetialListNew.contains(orderdetialListOldOrderdetial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderdetial " + orderdetialListOldOrderdetial + " since its orderid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getUserid());
                orders.setUserid(useridNew);
            }
            List<Orderdetial> attachedOrderdetialListNew = new ArrayList<Orderdetial>();
            for (Orderdetial orderdetialListNewOrderdetialToAttach : orderdetialListNew) {
                orderdetialListNewOrderdetialToAttach = em.getReference(orderdetialListNewOrderdetialToAttach.getClass(), orderdetialListNewOrderdetialToAttach.getOrderdeid());
                attachedOrderdetialListNew.add(orderdetialListNewOrderdetialToAttach);
            }
            orderdetialListNew = attachedOrderdetialListNew;
            orders.setOrderdetialList(orderdetialListNew);
            orders = em.merge(orders);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getOrdersList().remove(orders);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getOrdersList().add(orders);
                useridNew = em.merge(useridNew);
            }
            for (Orderdetial orderdetialListNewOrderdetial : orderdetialListNew) {
                if (!orderdetialListOld.contains(orderdetialListNewOrderdetial)) {
                    Orders oldOrderidOfOrderdetialListNewOrderdetial = orderdetialListNewOrderdetial.getOrderid();
                    orderdetialListNewOrderdetial.setOrderid(orders);
                    orderdetialListNewOrderdetial = em.merge(orderdetialListNewOrderdetial);
                    if (oldOrderidOfOrderdetialListNewOrderdetial != null && !oldOrderidOfOrderdetialListNewOrderdetial.equals(orders)) {
                        oldOrderidOfOrderdetialListNewOrderdetial.getOrderdetialList().remove(orderdetialListNewOrderdetial);
                        oldOrderidOfOrderdetialListNewOrderdetial = em.merge(oldOrderidOfOrderdetialListNewOrderdetial);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orders.getOrderid();
                if (findOrders(id) == null) {
                    throw new NonexistentEntityException("The orders with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orders orders;
            try {
                orders = em.getReference(Orders.class, id);
                orders.getOrderid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orders with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orderdetial> orderdetialListOrphanCheck = orders.getOrderdetialList();
            for (Orderdetial orderdetialListOrphanCheckOrderdetial : orderdetialListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orders (" + orders + ") cannot be destroyed since the Orderdetial " + orderdetialListOrphanCheckOrderdetial + " in its orderdetialList field has a non-nullable orderid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users userid = orders.getUserid();
            if (userid != null) {
                userid.getOrdersList().remove(orders);
                userid = em.merge(userid);
            }
            em.remove(orders);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orders> findOrdersEntities() {
        return findOrdersEntities(true, -1, -1);
    }

    public List<Orders> findOrdersEntities(int maxResults, int firstResult) {
        return findOrdersEntities(false, maxResults, firstResult);
    }

    private List<Orders> findOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orders.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Orders findOrders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orders.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orders> rt = cq.from(Orders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
