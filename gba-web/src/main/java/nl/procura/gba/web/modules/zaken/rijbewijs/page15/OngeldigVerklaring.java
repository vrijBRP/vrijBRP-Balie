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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
public class OngeldigVerklaring {

  private BigInteger              rijbewijsNummer;
  private DateFieldValue          datumInlevering;
  private RedenOngeldigVerklaring redenOngeldig;
  private List<Categorie>         categorieen = new ArrayList<>();

  public void addCategorie(String code, DateFieldValue datum) {
    getCategorieen().add(new Categorie(code, datum));
  }

  @Data
  public class Categorie {

    private String         code = "";
    private DateFieldValue datum;

    public Categorie(String code, DateFieldValue datum) {
      super();
      this.code = code;
      this.datum = datum;
    }

    public boolean isDatumOngeldig() {
      return datum.getLongValue() > 0;
    }
  }
}
