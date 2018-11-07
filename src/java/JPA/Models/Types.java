/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA.Models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus
 */
@Entity
@Table(name = "TYPES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Types.findAll", query = "SELECT t FROM Types t")
    , @NamedQuery(name = "Types.findByTypeid", query = "SELECT t FROM Types t WHERE t.typeid = :typeid")
    , @NamedQuery(name = "Types.findByTypetname", query = "SELECT t FROM Types t WHERE t.typetname = :typetname")})
public class Types implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TYPEID")
    private Integer typeid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "TYPETNAME")
    private String typetname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeid")
    private List<Products> productsList;

    public Types() {
    }

    public Types(Integer typeid) {
        this.typeid = typeid;
    }

    public Types(Integer typeid, String typetname) {
        this.typeid = typeid;
        this.typetname = typetname;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getTypetname() {
        return typetname;
    }

    public void setTypetname(String typetname) {
        this.typetname = typetname;
    }

    @XmlTransient
    public List<Products> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Products> productsList) {
        this.productsList = productsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typeid != null ? typeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Types)) {
            return false;
        }
        Types other = (Types) object;
        if ((this.typeid == null && other.typeid != null) || (this.typeid != null && !this.typeid.equals(other.typeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JPA.Models.Types[ typeid=" + typeid + " ]";
    }
    
}
