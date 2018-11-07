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
import JPA.Models.Types;
import JPA.Models.Orderdetial;
import JPA.Models.Products;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Asus
 */
public class ProductsJpaController implements Serializable {

    public ProductsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Products products) throws RollbackFailureException, Exception {
        if (products.getOrderdetialList() == null) {
            products.setOrderdetialList(new ArrayList<Orderdetial>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Types typeid = products.getTypeid();
            if (typeid != null) {
                typeid = em.getReference(typeid.getClass(), typeid.getTypeid());
                products.setTypeid(typeid);
            }
            List<Orderdetial> attachedOrderdetialList = new ArrayList<Orderdetial>();
            for (Orderdetial orderdetialListOrderdetialToAttach : products.getOrderdetialList()) {
                orderdetialListOrderdetialToAttach = em.getReference(orderdetialListOrderdetialToAttach.getClass(), orderdetialListOrderdetialToAttach.getOrderdeid());
                attachedOrderdetialList.add(orderdetialListOrderdetialToAttach);
            }
            products.setOrderdetialList(attachedOrderdetialList);
            em.persist(products);
            if (typeid != null) {
                typeid.getProductsList().add(products);
                typeid = em.merge(typeid);
            }
            for (Orderdetial orderdetialListOrderdetial : products.getOrderdetialList()) {
                Products oldProductidOfOrderdetialListOrderdetial = orderdetialListOrderdetial.getProductid();
                orderdetialListOrderdetial.setProductid(products);
                orderdetialListOrderdetial = em.merge(orderdetialListOrderdetial);
                if (oldProductidOfOrderdetialListOrderdetial != null) {
                    oldProductidOfOrderdetialListOrderdetial.getOrderdetialList().remove(orderdetialListOrderdetial);
                    oldProductidOfOrderdetialListOrderdetial = em.merge(oldProductidOfOrderdetialListOrderdetial);
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

    public void edit(Products products) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Products persistentProducts = em.find(Products.class, products.getProductid());
            Types typeidOld = persistentProducts.getTypeid();
            Types typeidNew = products.getTypeid();
            List<Orderdetial> orderdetialListOld = persistentProducts.getOrderdetialList();
            List<Orderdetial> orderdetialListNew = products.getOrderdetialList();
            List<String> illegalOrphanMessages = null;
            for (Orderdetial orderdetialListOldOrderdetial : orderdetialListOld) {
                if (!orderdetialListNew.contains(orderdetialListOldOrderdetial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderdetial " + orderdetialListOldOrderdetial + " since its productid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (typeidNew != null) {
                typeidNew = em.getReference(typeidNew.getClass(), typeidNew.getTypeid());
                products.setTypeid(typeidNew);
            }
            List<Orderdetial> attachedOrderdetialListNew = new ArrayList<Orderdetial>();
            for (Orderdetial orderdetialListNewOrderdetialToAttach : orderdetialListNew) {
                orderdetialListNewOrderdetialToAttach = em.getReference(orderdetialListNewOrderdetialToAttach.getClass(), orderdetialListNewOrderdetialToAttach.getOrderdeid());
                attachedOrderdetialListNew.add(orderdetialListNewOrderdetialToAttach);
            }
            orderdetialListNew = attachedOrderdetialListNew;
            products.setOrderdetialList(orderdetialListNew);
            products = em.merge(products);
            if (typeidOld != null && !typeidOld.equals(typeidNew)) {
                typeidOld.getProductsList().remove(products);
                typeidOld = em.merge(typeidOld);
            }
            if (typeidNew != null && !typeidNew.equals(typeidOld)) {
                typeidNew.getProductsList().add(products);
                typeidNew = em.merge(typeidNew);
            }
            for (Orderdetial orderdetialListNewOrderdetial : orderdetialListNew) {
                if (!orderdetialListOld.contains(orderdetialListNewOrderdetial)) {
                    Products oldProductidOfOrderdetialListNewOrderdetial = orderdetialListNewOrderdetial.getProductid();
                    orderdetialListNewOrderdetial.setProductid(products);
                    orderdetialListNewOrderdetial = em.merge(orderdetialListNewOrderdetial);
                    if (oldProductidOfOrderdetialListNewOrderdetial != null && !oldProductidOfOrderdetialListNewOrderdetial.equals(products)) {
                        oldProductidOfOrderdetialListNewOrderdetial.getOrderdetialList().remove(orderdetialListNewOrderdetial);
                        oldProductidOfOrderdetialListNewOrderdetial = em.merge(oldProductidOfOrderdetialListNewOrderdetial);
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
                Integer id = products.getProductid();
                if (findProducts(id) == null) {
                    throw new NonexistentEntityException("The products with id " + id + " no longer exists.");
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
            Products products;
            try {
                products = em.getReference(Products.class, id);
                products.getProductid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The products with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orderdetial> orderdetialListOrphanCheck = products.getOrderdetialList();
            for (Orderdetial orderdetialListOrphanCheckOrderdetial : orderdetialListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Products (" + products + ") cannot be destroyed since the Orderdetial " + orderdetialListOrphanCheckOrderdetial + " in its orderdetialList field has a non-nullable productid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Types typeid = products.getTypeid();
            if (typeid != null) {
                typeid.getProductsList().remove(products);
                typeid = em.merge(typeid);
            }
            em.remove(products);
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

    public List<Products> findProductsEntities() {
        return findProductsEntities(true, -1, -1);
    }

    public List<Products> findProductsEntities(int maxResults, int firstResult) {
        return findProductsEntities(false, maxResults, firstResult);
    }

    private List<Products> findProductsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Products.class));
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

    public Products findProducts(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Products.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Products> rt = cq.from(Products.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
