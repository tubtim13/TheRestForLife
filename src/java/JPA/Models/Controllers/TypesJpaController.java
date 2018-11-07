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
import JPA.Models.Products;
import JPA.Models.Types;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Asus
 */
public class TypesJpaController implements Serializable {

    public TypesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Types types) throws RollbackFailureException, Exception {
        if (types.getProductsList() == null) {
            types.setProductsList(new ArrayList<Products>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Products> attachedProductsList = new ArrayList<Products>();
            for (Products productsListProductsToAttach : types.getProductsList()) {
                productsListProductsToAttach = em.getReference(productsListProductsToAttach.getClass(), productsListProductsToAttach.getProductid());
                attachedProductsList.add(productsListProductsToAttach);
            }
            types.setProductsList(attachedProductsList);
            em.persist(types);
            for (Products productsListProducts : types.getProductsList()) {
                Types oldTypeidOfProductsListProducts = productsListProducts.getTypeid();
                productsListProducts.setTypeid(types);
                productsListProducts = em.merge(productsListProducts);
                if (oldTypeidOfProductsListProducts != null) {
                    oldTypeidOfProductsListProducts.getProductsList().remove(productsListProducts);
                    oldTypeidOfProductsListProducts = em.merge(oldTypeidOfProductsListProducts);
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

    public void edit(Types types) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Types persistentTypes = em.find(Types.class, types.getTypeid());
            List<Products> productsListOld = persistentTypes.getProductsList();
            List<Products> productsListNew = types.getProductsList();
            List<String> illegalOrphanMessages = null;
            for (Products productsListOldProducts : productsListOld) {
                if (!productsListNew.contains(productsListOldProducts)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Products " + productsListOldProducts + " since its typeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Products> attachedProductsListNew = new ArrayList<Products>();
            for (Products productsListNewProductsToAttach : productsListNew) {
                productsListNewProductsToAttach = em.getReference(productsListNewProductsToAttach.getClass(), productsListNewProductsToAttach.getProductid());
                attachedProductsListNew.add(productsListNewProductsToAttach);
            }
            productsListNew = attachedProductsListNew;
            types.setProductsList(productsListNew);
            types = em.merge(types);
            for (Products productsListNewProducts : productsListNew) {
                if (!productsListOld.contains(productsListNewProducts)) {
                    Types oldTypeidOfProductsListNewProducts = productsListNewProducts.getTypeid();
                    productsListNewProducts.setTypeid(types);
                    productsListNewProducts = em.merge(productsListNewProducts);
                    if (oldTypeidOfProductsListNewProducts != null && !oldTypeidOfProductsListNewProducts.equals(types)) {
                        oldTypeidOfProductsListNewProducts.getProductsList().remove(productsListNewProducts);
                        oldTypeidOfProductsListNewProducts = em.merge(oldTypeidOfProductsListNewProducts);
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
                Integer id = types.getTypeid();
                if (findTypes(id) == null) {
                    throw new NonexistentEntityException("The types with id " + id + " no longer exists.");
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
            Types types;
            try {
                types = em.getReference(Types.class, id);
                types.getTypeid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The types with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Products> productsListOrphanCheck = types.getProductsList();
            for (Products productsListOrphanCheckProducts : productsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Types (" + types + ") cannot be destroyed since the Products " + productsListOrphanCheckProducts + " in its productsList field has a non-nullable typeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(types);
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

    public List<Types> findTypesEntities() {
        return findTypesEntities(true, -1, -1);
    }

    public List<Types> findTypesEntities(int maxResults, int firstResult) {
        return findTypesEntities(false, maxResults, firstResult);
    }

    private List<Types> findTypesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Types.class));
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

    public Types findTypes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Types.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Types> rt = cq.from(Types.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
