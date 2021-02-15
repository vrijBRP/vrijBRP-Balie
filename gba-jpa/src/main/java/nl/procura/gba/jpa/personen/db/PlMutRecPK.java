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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Embeddable
@EqualsAndHashCode
@Accessors(chain = true)
public class PlMutRecPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "c_pl_mut",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cPlMutRec;

  @Column(name = "elem",
      unique = true,
      nullable = false,
      precision = 131089)
  private int elem;

  public PlMutRecPK() {
  }

  public PlMutRecPK(int elem) {
    this.elem = elem;
  }

  public long getcPlMutRec() {
    return cPlMutRec;
  }

  public void setcPlMutRec(long cPlMutRec) {
    this.cPlMutRec = cPlMutRec;
  }

  public int getElem() {
    return elem;
  }

  public void setElem(int elem) {
    this.elem = elem;
  }
}
