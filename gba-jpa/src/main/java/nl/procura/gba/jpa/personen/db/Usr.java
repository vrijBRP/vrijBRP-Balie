/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.jpa.personen.db;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@NamedQueries({ @NamedQuery(name = "Usr.findAll",
    query = "select u from Usr u where u.cUsr > 0 order by u.usrfullname") })
@Table(name = "usr")
public class Usr extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_usr",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "usr",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_usr")
  @Column(name = "c_usr",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cUsr;

  @Column(precision = 131089)
  private BigDecimal admin;

  @Column(precision = 131089)
  private BigDecimal blok;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  @Column(length = 2147483647)
  private String descr;

  @Column(length = 2147483647)
  private String path;

  @Column()
  private String usr;

  @Column()
  private String usrfullname;

  @OneToMany(mappedBy = "usr")
  private List<BvhPark> bvhParks;

  @ManyToMany(mappedBy = "usrs")
  private List<Document> documents;

  @OneToMany(mappedBy = "usr")
  private List<Doss> dosses;

  @OneToMany(mappedBy = "usr")
  private List<Idvaststelling> idvaststellings;

  @OneToMany(mappedBy = "usr")
  private List<Nrd> nrds;

  @OneToMany(mappedBy = "usr")
  private List<Parm> parms;

  @OneToMany(mappedBy = "usr1")
  private List<Rdm01> rdm01s1;

  @OneToMany(mappedBy = "usr2")
  private List<Rdm01> rdm01s2;

  @OneToMany(mappedBy = "usr")
  private List<UittAanvr> uittAanvrs;

  @OneToMany(mappedBy = "usr")
  @OrderBy("id.dIn DESC, id.tIn DESC")
  @BatchFetch(BatchFetchType.IN)
  private List<UsrPwHist> usrPwHists;

  @ManyToMany
  @JoinTable(name = "usr_location",
      joinColumns = { @JoinColumn(name = "c_usr",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_location",
          nullable = false) })
  private List<Location> locations;

  @ManyToMany
  @BatchFetch(BatchFetchType.IN)
  @JoinTable(name = "usr_profile",
      joinColumns = { @JoinColumn(name = "c_usr",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_profile",
          nullable = false) })
  private List<Profile> profiles;

  @OneToMany(mappedBy = "usr")
  private List<UsrInfo> usrInfos;

  public Usr() {
  }

  public Usr(Long cUsr) {
    this(cUsr, "");
  }

  public Usr(Long cUsr, String userfullname) {
    setCUsr(cUsr);
    setUsrfullname(userfullname);
  }

  public Long getCUsr() {
    return this.cUsr;
  }

  public void setCUsr(Long cUsr) {
    this.cUsr = cUsr;
  }

  public BigDecimal getAdmin() {
    return this.admin;
  }

  public void setAdmin(BigDecimal admin) {
    this.admin = admin;
  }

  public BigDecimal getBlok() {
    return this.blok;
  }

  public void setBlok(BigDecimal blok) {
    this.blok = blok;
  }

  public BigDecimal getDIn() {
    return dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public BigDecimal getDEnd() {
    return this.dEnd;
  }

  public void setDEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }

  public String getDescr() {
    return this.descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getUsr() {
    return this.usr;
  }

  public void setUsr(String usr) {
    this.usr = usr;
  }

  public String getUsrfullname() {
    return this.usrfullname;
  }

  public void setUsrfullname(String usrfullname) {
    this.usrfullname = usrfullname;
  }

  public List<BvhPark> getBvhParks() {
    return this.bvhParks;
  }

  public void setBvhParks(List<BvhPark> bvhParks) {
    this.bvhParks = bvhParks;
  }

  public List<Document> getDocuments() {
    return this.documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public List<Doss> getDosses() {
    return this.dosses;
  }

  public void setDosses(List<Doss> dosses) {
    this.dosses = dosses;
  }

  public List<Idvaststelling> getIdvaststellings() {
    return this.idvaststellings;
  }

  public void setIdvaststellings(List<Idvaststelling> idvaststellings) {
    this.idvaststellings = idvaststellings;
  }

  public List<Nrd> getNrds() {
    return this.nrds;
  }

  public void setNrds(List<Nrd> nrds) {
    this.nrds = nrds;
  }

  public List<Parm> getParms() {
    return this.parms;
  }

  public void setParms(List<Parm> parms) {
    this.parms = parms;
  }

  public List<Rdm01> getRdm01s1() {
    return this.rdm01s1;
  }

  public void setRdm01s1(List<Rdm01> rdm01s1) {
    this.rdm01s1 = rdm01s1;
  }

  public List<Rdm01> getRdm01s2() {
    return this.rdm01s2;
  }

  public void setRdm01s2(List<Rdm01> rdm01s2) {
    this.rdm01s2 = rdm01s2;
  }

  public List<UittAanvr> getUittAanvrs() {
    return this.uittAanvrs;
  }

  public void setUittAanvrs(List<UittAanvr> uittAanvrs) {
    this.uittAanvrs = uittAanvrs;
  }

  public List<Location> getLocations() {
    return this.locations;
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  public List<Profile> getProfiles() {
    return this.profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }

  public List<UsrInfo> getUsrInfos() {
    return this.usrInfos;
  }

  public void setUsrInfos(List<UsrInfo> usrInfos) {
    this.usrInfos = usrInfos;
  }

  public List<UsrPwHist> getUsrPwHists() {
    return usrPwHists;
  }

  public void setUsrPwHists(List<UsrPwHist> usrPwHists) {
    this.usrPwHists = usrPwHists;
  }
}
