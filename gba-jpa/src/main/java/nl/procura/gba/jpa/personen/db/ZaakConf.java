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

import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.BooleanUtils;

@Entity
@Table(name = "zaak_conf")
public class ZaakConf extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_zaak_conf",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "zaak_conf",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_zaak_conf")
  @Column(name = "c_zaak_conf",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cZaakConf;

  @Column(name = "zaak_conf")
  private String zaakConf;

  @Column(name = "bron")
  private String bron;

  @Column(name = "leverancier")
  private String leverancier;

  @Column(name = "zaaktypes")
  private String zaakTypes;

  @Column(name = "ind_zaaksysteem_id")
  private Boolean indZaaksysteemId;

  @ManyToMany(mappedBy = "zaakConfs")
  private List<Profile> profiles;

  public ZaakConf() {
  }

  public Long getCZaakConf() {
    return this.cZaakConf;
  }

  public void setCZaakConf(Long cZaakConf) {
    this.cZaakConf = cZaakConf;
  }

  public List<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }

  public String getZaakConf() {
    return zaakConf;
  }

  public void setZaakConf(String zaakConf) {
    this.zaakConf = zaakConf;
  }

  public String getBron() {
    return bron;
  }

  public void setBron(String bron) {
    this.bron = bron;
  }

  public String getLeverancier() {
    return leverancier;
  }

  public void setLeverancier(String leverancier) {
    this.leverancier = leverancier;
  }

  public Boolean getIndZaaksysteemId() {
    return indZaaksysteemId;
  }

  public void setIndZaaksysteemId(Boolean indZaaksysteemId) {
    this.indZaaksysteemId = indZaaksysteemId;
  }

  public String getZaakTypes() {
    return zaakTypes;
  }

  public void setZaakTypes(String zaakTypes) {
    this.zaakTypes = zaakTypes;
  }

  public boolean isOptions() {
    return BooleanUtils.isTrue(getIndZaaksysteemId());
  }
}
