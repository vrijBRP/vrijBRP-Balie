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
@Table(name = "doss_huw_optie")
public class DossHuwOptie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private DossHuwOptiePK id;

  @Column()
  private String waarde;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss_huw",
      nullable = false,
      insertable = false,
      updatable = false)
  private DossHuw dossHuw;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_huw_locatie_optie",
      nullable = false,
      insertable = false,
      updatable = false)
  private HuwLocatieOptie huwLocatieOptie;

  public DossHuwOptie() {
  }

  @Override
  public DossHuwOptiePK getId() {
    return this.id;
  }

  public void setId(DossHuwOptiePK id) {
    this.id = id;
  }

  public String getWaarde() {
    return this.waarde;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }

  public DossHuw getDossHuw() {
    return this.dossHuw;
  }

  public void setDossHuw(DossHuw dossHuw) {
    this.dossHuw = dossHuw;
  }

  public HuwLocatieOptie getHuwLocatieOptie() {
    return this.huwLocatieOptie;
  }

  public void setHuwLocatieOptie(HuwLocatieOptie huwLocatieOptie) {
    this.huwLocatieOptie = huwLocatieOptie;
  }

}
