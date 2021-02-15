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

package nl.procura.gba.web.modules.zaken.reisdocument.page18;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.data.validator.AbstractValidator;

import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;

public class DubbelNummerValidator extends AbstractValidator {

  private ReisdocumentService db;

  public DubbelNummerValidator(ReisdocumentService db) {
    super("");
    setDb(db);
  }

  public ReisdocumentService getDb() {
    return db;
  }

  public void setDb(ReisdocumentService db) {
    this.db = db;
  }

  @Override
  public boolean isValid(Object value) {

    Aanvraagnummer a = new Aanvraagnummer(astr(value));

    if (!a.isCorrect()) {
      setErrorMessage("Foutief aanvraagnummer");
      return false;
    }

    if (isReedsToegevoegd(a)) {
      setErrorMessage("Aanvraagnummer komt al voor in de database.");
      return false;
    }

    return true;
  }

  private boolean isReedsToegevoegd(Aanvraagnummer nr) {
    return getDb().nummerBestaat(nr);
  }
}
