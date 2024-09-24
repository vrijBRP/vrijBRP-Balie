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
@Table(name = "location")
public class Location extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_location",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "location",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_location")
  @Column(name = "c_location",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cLocation;

  @Column(name = "code_raas",
      precision = 131089)
  private BigDecimal codeRaas;

  @Column(name = "code_ras",
      precision = 131089)
  private BigDecimal codeRas;

  @Column(name = "location_type",
      precision = 131089)
  private BigDecimal type;

  @Column(length = 2147483647)
  private String descr;

  @Column(name = "gkas_id")
  private String gkasId;

  @Column(length = 2147483647)
  private String ip;

  @Column()
  private String location;

  @Column(name="zynyo_device_id")
  private String zynyoDeviceId;

  @OneToMany(mappedBy = "location")
  private List<Nrd> nrds;

  @ManyToMany(mappedBy = "locations")
  private List<Printoptie> printopties;

  @OneToMany(mappedBy = "location")
  private List<Rdm01> rdm01s;

  @OneToMany(mappedBy = "location")
  private List<UittAanvr> uittAanvrs;

  @ManyToMany(mappedBy = "locations")
  @BatchFetch(BatchFetchType.IN)
  private List<Usr> usrs;

  public Location() {
  }

  public Location(long cLocation) {
    this();
    setCLocation(cLocation);
  }

  public Long getCLocation() {
    return this.cLocation;
  }

  public void setCLocation(Long cLocation) {
    this.cLocation = cLocation;
  }

  public BigDecimal getCodeRaas() {
    return this.codeRaas;
  }

  public void setCodeRaas(BigDecimal codeRaas) {
    this.codeRaas = codeRaas;
  }

  public BigDecimal getCodeRas() {
    return this.codeRas;
  }

  public void setCodeRas(BigDecimal codeRas) {
    this.codeRas = codeRas;
  }

  public String getDescr() {
    return this.descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getGkasId() {
    return this.gkasId;
  }

  public void setGkasId(String gkasId) {
    this.gkasId = gkasId;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getZynyoDeviceId() {
    return this.zynyoDeviceId;
  }

  public void setZynyoDeviceId(String zynyoDeviceId) {
    this.zynyoDeviceId = zynyoDeviceId;
  }

  public List<Nrd> getNrds() {
    return this.nrds;
  }

  public void setNrds(List<Nrd> nrds) {
    this.nrds = nrds;
  }

  public List<Printoptie> getPrintopties() {
    return this.printopties;
  }

  public void setPrintopties(List<Printoptie> printopties) {
    this.printopties = printopties;
  }

  public List<Rdm01> getRdm01s() {
    return this.rdm01s;
  }

  public void setRdm01s(List<Rdm01> rdm01s) {
    this.rdm01s = rdm01s;
  }

  public List<UittAanvr> getUittAanvrs() {
    return this.uittAanvrs;
  }

  public void setUittAanvrs(List<UittAanvr> uittAanvrs) {
    this.uittAanvrs = uittAanvrs;
  }

  public List<Usr> getUsrs() {
    return this.usrs;
  }

  public void setUsrs(List<Usr> usrs) {
    this.usrs = usrs;
  }

  public BigDecimal getType() {
    return type;
  }

  public void setType(BigDecimal type) {
    this.type = type;
  }
}
