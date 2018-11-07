/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA.Models.Controllers;

import JPA.Models.Controllers.exceptions.NonexistentEntityException;
import JPA.Models.Controllers.exceptions.RollbackFailureException;
import JPA.Models.Orderdetial;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import JPA.Models.Orders;
import JPA.Models.Products;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Asus
 */
public class OrderdetialJpaController implements Serializable {

    public OrderdetialJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orderdetial orderdetial) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orders orderid = orderdetial.getOrderid();
            if (orderid != null) {
                orderid = em.getReference(orderid.getClass(), orderid.getOrderid());
                orderdetial.setOrderid(orderid);
            }
            Products productid = orderdetial.getProductid();
            if (productid != null) {
                productid = em.getReference(productid.getClass(), productid.getProductid());
                orderdetial.setProductid(productid);
            }
            em.persist(orderdetial);
            if (orderid != null) {
                orderid.getOrderdetialList().add(orderdetial);
                orderid = em.merge(orderid);
            }
            if (productid != null) {
                productid.getOrderdetialList().add(orderdetial);
                productid = em.merge(productid);
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

    public void edit(Orderdetial orderdetial) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orderdetial persistentOrderdetial = em.find(Orderdetial.class, orderdetial.getOrderdeid());
            Orders orderidOld = persistentOrderdetial.getOrderid();
            Orders orderidNew = orderdetial.getOrderid();
            Products productidOld = persistentOrderdetial.getProductid();
            Products productidNew = orderdetial.getProductid();
            if (orderidNew != null) {
                orderidNew = em.getReference(orderidNew.getClass(), orderidNew.getOrderid());
                orderdetial.setOrderid(orderidNew);
            }
            if (productidNew != null) {
                productidNew = em.getReference(productidNew.getClass(), productidNew.getProductid());
                orderdetial.setProductid(productidNew);
            }
            orderdetial = em.merge(orderdetial);
            if (orderidOld != null && !orderidOld.equals(orderidNew)) {
                orderidOld.getOrderdetialList().remove(orderdetial);
                orderidOld = em.merge(orderidOld);
            }
            if (orderidNew != null && !orderidNew.equals(orderidOld)) {
                orderidNew.getOrderdetialList().add(orderdetial);
                orderidNew = em.merge(orderidNew);
            }
            if (productidOld != null && !productidOld.equals(productidNew)) {
                productidOld.getOrderdetialList().remove(orderdetial);
                productidOld = em.merge(productidOld);
            }
            if (productidNew != null && !productidNew.equals(productidOld)) {
                productidNew.getOrderdetialList().add(orderdetial);
                productidNew = em.merge(productidNew);
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
                Integer id = orderdetial.getOrderdeid();
                if (findOrderdetial(id) == null) {
                    throw new NonexistentEntityException("The orderdetial with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orderdetial orderdetial;
            try {
                orderdetial = em.getReference(Orderdetial.class, id);
                orderdetial.getOrderdeid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderdetial with id " + id + " no longer exists.", enfe);
            }
            Orders orderid = orderdetial.getOrderid();
            if (orderid != null) {
                orderid.getOrderdetialList().remove(orderdetial);
                orderid = em.merge(orderid);
            }
            Products productid = orderdetial.getProductid();
            if (productid != null) {
                productid.getOrderdetialList().remove(orderdetial);
                productid = em.merge(productid);
            }
            em.remove(orderdetial);
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

    public List<Orderdetial> findOrderdetialEntities() {
        return findOrderdetialEntities(true, -1, -1);
    }

    public List<Orderdetial> findOrderdetialEntities(int maxResults, int firstResult) {
        return findOrderdetialEntities(false, maxResults, firstResult);
    }

    private List<Orderdetial> findOrderdetialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orderdetial.class));
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

    public Orderdetial findOrderdetial(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orderdetial.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderdetialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orderdetial> rt = cq.from(Orderdetial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
