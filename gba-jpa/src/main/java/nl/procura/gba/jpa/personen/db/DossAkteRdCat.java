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

@Entity
@Table(name = "doss_akte_rd_cat")
public class DossAkteRdCat extends nl.procura.gba.jpa.personen.db.BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_akte_rd_cat",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_akte_rd_cat",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_akte_rd_cat")
  @Column(name = "c_doss_akte_rd_cat",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossAkteRdCat;

  @Column(name = "doss_akte_rd_cat")
  private String dossAkteRdCat;

  @OneToMany(mappedBy = "dossAkteRdCat")
  private List<DossAkteRd> dossAkteRds;

  public DossAkteRdCat() {
  }

  public Long getCDossAkteRdCat() {
    return this.cDossAkteRdCat;
  }

  public void setCDossAkteRdCat(Long cDossAkteRdCat) {
    this.cDossAkteRdCat = cDossAkteRdCat;
  }

  public String getDossAkteRdCat() {
    return this.dossAkteRdCat;
  }

  public void setDossAkteRdCat(String dossAkteRdCat) {
    this.dossAkteRdCat = dossAkteRdCat;
  }

  public List<DossAkteRd> getDossAkteRds() {
    return this.dossAkteRds;
  }

  public void setDossAkteRds(List<DossAkteRd> dossAkteRds) {
    this.dossAkteRds = dossAkteRds;
  }

  public DossAkteRd addDossAkteRd(DossAkteRd dossAkteRd) {
    getDossAkteRds().add(dossAkteRd);
    dossAkteRd.setDossAkteRdCat(this);

    return dossAkteRd;
  }

  public DossAkteRd removeDossAkteRd(DossAkteRd dossAkteRd) {
    getDossAkteRds().remove(dossAkteRd);
    dossAkteRd.setDossAkteRdCat(null);

    return dossAkteRd;
  }

}
