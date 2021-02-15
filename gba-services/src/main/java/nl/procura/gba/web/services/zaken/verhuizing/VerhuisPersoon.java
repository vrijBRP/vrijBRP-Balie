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

package nl.procura.gba.web.services.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.zaken.algemeen.IdentificatieNummers;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VerhuisPersoon implements IdentificatieNummers {

  private long          code             = -1;
  private AnrFieldValue anr              = new AnrFieldValue();
  private BsnFieldValue bsn              = new BsnFieldValue();
  private AangifteSoort aangifte         = AangifteSoort.ONBEKEND;
  private FieldValue    gemeenteHerkomst = new FieldValue();
  private DocumentPL    persoon          = null;
  private boolean       geenVerwerking   = false;

  public VerhuisPersoon() {
  }

  public AangifteSoort getAangifte() {
    return aangifte;
  }

  public void setAangifte(AangifteSoort aangifte) {
    this.aangifte = aangifte;
  }

  @Override
  public AnrFieldValue getAnummer() {

    if (!pos(anr.getValue()) && getPersoon() != null) {
      return new AnrFieldValue(getPersoon().getPersoon().getAnummer());
    }

    return anr;
  }

  @Override
  public void setAnummer(AnrFieldValue anr) {
    this.anr = anr;
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {

    if (!pos(bsn.getValue()) && getPersoon() != null) {
      return new BsnFieldValue(getPersoon().getPersoon().getBurgerservicenummer());
    }

    return bsn;
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    this.bsn = bsn;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public FieldValue getGemeenteHerkomst() {
    return gemeenteHerkomst;
  }

  public void setGemeenteHerkomst(FieldValue gemeenteHerkomst) {
    this.gemeenteHerkomst = FieldValue.from(gemeenteHerkomst);
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  public boolean isGeenVerwerking() {
    return geenVerwerking;
  }

  public void setGeenVerwerking(boolean geenVerwerking) {
    this.geenVerwerking = geenVerwerking;
  }
}
