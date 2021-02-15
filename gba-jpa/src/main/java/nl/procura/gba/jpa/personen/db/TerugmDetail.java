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

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "terugm_detail")
public class TerugmDetail extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private TerugmDetailPK id;

  @Column(name = "val_nieuw",
      length = 2147483647)
  private String valNieuw;

  @Column(name = "val_origineel",
      length = 2147483647)
  private String valOrigineel;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_terugmelding",
      nullable = false,
      insertable = false,
      updatable = false)
  private Terugmelding terugmelding;

  public TerugmDetail() {
  }

  @Override
  public TerugmDetailPK getId() {
    return this.id;
  }

  public void setId(TerugmDetailPK id) {
    this.id = id;
  }

  public String getValNieuw() {
    return this.valNieuw;
  }

  public void setValNieuw(String valNieuw) {
    this.valNieuw = valNieuw;
  }

  public String getValOrigineel() {
    return this.valOrigineel;
  }

  public void setValOrigineel(String valOrigineel) {
    this.valOrigineel = valOrigineel;
  }

  public Terugmelding getTerugmelding() {
    return this.terugmelding;
  }

  public void setTerugmelding(Terugmelding terugmelding) {
    this.terugmelding = terugmelding;
  }

}
