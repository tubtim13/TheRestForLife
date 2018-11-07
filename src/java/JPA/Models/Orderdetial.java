/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA.Models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Asus
 */
@Entity
@Table(name = "ORDERDETIAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orderdetial.findAll", query = "SELECT o FROM Orderdetial o")
    , @NamedQuery(name = "Orderdetial.findByOrderdeid", query = "SELECT o FROM Orderdetial o WHERE o.orderdeid = :orderdeid")
    , @NamedQuery(name = "Orderdetial.findByAmount", query = "SELECT o FROM Orderdetial o WHERE o.amount = :amount")})
public class Orderdetial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ORDERDEID")
    private Integer orderdeid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT")
    private int amount;
    @JoinColumn(name = "ORDERID", referencedColumnName = "ORDERID")
    @ManyToOne(optional = false)
    private Orders orderid;
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @ManyToOne(optional = false)
    private Products productid;

    public Orderdetial() {
    }

    public Orderdetial(Integer orderdeid) {
        this.orderdeid = orderdeid;
    }

    public Orderdetial(Integer orderdeid, int amount) {
        this.orderdeid = orderdeid;
        this.amount = amount;
    }

    public Integer getOrderdeid() {
        return orderdeid;
    }

    public void setOrderdeid(Integer orderdeid) {
        this.orderdeid = orderdeid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Orders getOrderid() {
        return orderid;
    }

    public void setOrderid(Orders orderid) {
        this.orderid = orderid;
    }

    public Products getProductid() {
        return productid;
    }

    public void setProductid(Products productid) {
        this.productid = productid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderdeid != null ? orderdeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orderdetial)) {
            return false;
        }
        Orderdetial other = (Orderdetial) object;
        if ((this.orderdeid == null && other.orderdeid != null) || (this.orderdeid != null && !this.orderdeid.equals(other.orderdeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA.Models.Orderdetial[ orderdeid=" + orderdeid + " ]";
    }
    
}
