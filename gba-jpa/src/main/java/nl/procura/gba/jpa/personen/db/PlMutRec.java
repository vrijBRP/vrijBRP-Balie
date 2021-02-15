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

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "pl_mut_rec")
@EqualsAndHashCode(of = "", callSuper = true)
public class PlMutRec extends BaseEntity<PlMutRecPK> {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private PlMutRecPK id;

  @Column(name = "val_org")
  private String valOrg;

  @Column(name = "val_org_descr")
  private String valOrgDescr;

  @Column(name = "val_new")
  private String valNew;

  @Column(name = "val_new_descr")
  private String valNewDescr;

  @Column(name = "changed",
      nullable = false)
  private BigDecimal changed;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_pl_mut",
      nullable = false,
      insertable = false,
      updatable = false)
  private PlMut plMut;

  public PlMutRec() {
  }
}
