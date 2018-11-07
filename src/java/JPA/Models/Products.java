/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus
 */
@Entity
@Table(name = "PRODUCTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p")
    , @NamedQuery(name = "Products.findByProductid", query = "SELECT p FROM Products p WHERE p.productid = :productid")
    , @NamedQuery(name = "Products.findByProductname", query = "SELECT p FROM Products p WHERE p.productname = :productname")
    , @NamedQuery(name = "Products.findByDescription", query = "SELECT p FROM Products p WHERE p.description = :description")
    , @NamedQuery(name = "Products.findByPrice", query = "SELECT p FROM Products p WHERE p.price = :price")
    , @NamedQuery(name = "Products.findByBenefits", query = "SELECT p FROM Products p WHERE p.benefits = :benefits")
    , @NamedQuery(name = "Products.findBySpecies", query = "SELECT p FROM Products p WHERE p.species = :species")
    , @NamedQuery(name = "Products.findByAddeddate", query = "SELECT p FROM Products p WHERE p.addeddate = :addeddate")})
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRODUCTID")
    private Integer productid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PRODUCTNAME")
    private String productname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRICE")
    private int price;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "BENEFITS")
    private String benefits;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "SPECIES")
    private String species;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addeddate;
    @JoinColumn(name = "TYPEID", referencedColumnName = "TYPEID")
    @ManyToOne(optional = false)
    private Types typeid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productid")
    private List<Orderdetial> orderdetialList;

    public Products() {
    }

    public Products(Integer productid) {
        this.productid = productid;
    }

    public Products(Integer productid, String productname, String description, int price, String benefits, String species, Date addeddate) {
        this.productid = productid;
        this.productname = productname;
        this.description = description;
        this.price = price;
        this.benefits = benefits;
        this.species = species;
        this.addeddate = addeddate;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Date getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(Date addeddate) {
        this.addeddate = addeddate;
    }

    public Types getTypeid() {
        return typeid;
    }

    public void setTypeid(Types typeid) {
        this.typeid = typeid;
    }

    @XmlTransient
    public List<Orderdetial> getOrderdetialList() {
        return orderdetialList;
    }

    public void setOrderdetialList(List<Orderdetial> orderdetialList) {
        this.orderdetialList = orderdetialList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productid != null ? productid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Products)) {
            return false;
        }
        Products other = (Products) object;
        if ((this.productid == null && other.productid != null) || (this.productid != null && !this.productid.equals(other.productid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA.Models.Products[ productid=" + productid + " ]";
    }
    
}
