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

package nl.procura.gba.web.services.bs.algemeen.nationaliteit;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.jpa.personen.db.DossNatio;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierNationaliteit extends DossNatio {

  private static final long serialVersionUID = -8808839060132455801L;

  private FieldValue redenverkrijgingNederlanderschap = new FieldValue();

  public DossierNationaliteit() {
  }

  public DossierPersoonType getAfgeleidVan() {
    return DossierPersoonType.get(along(getAfgeleid()));
  }

  public void setAfgeleidVan(DossierPersoonType type) {
    setAfgeleid(toBigDecimal(type.getCode()));
  }

  public DateTime getDatumTijdOntlening() {
    return new DateTime(toBigDecimal(getDOntlening()), toBigDecimal(getTOntlening()));
  }

  public void setDatumTijdOntlening(DateTime dOntlening) {
    setDOntlening(toBigDecimal(dOntlening.getLongDate()));
    setTOntlening(toBigDecimal(dOntlening.getLongTime()));
  }

  public DateTime getDatumVerkrijging() {
    return new DateTime(getDVerkrijging());
  }

  public void setDatumVerkrijging(DateTime dt) {
    setDVerkrijging(toBigDecimal(dt.getLongDate()));
  }

  public DateTime getDatumVerlies() {
    return new DateTime(toBigDecimal(getDVerlies()));
  }

  public void setDatumVerlies(DateTime dVerlies) {
    setDVerlies(toBigDecimal(dVerlies.getLongDate()));
  }

  @Override
  public Object getId() {
    return getCDossNatio();
  }

  public FieldValue getNationaliteit() {
    return new FieldValue(getCNatio(), getNatio());
  }

  public void setNationaliteit(FieldValue value) {
    setCNatio(FieldValue.from(value).getBigDecimalValue());
    setNatio(FieldValue.from(value).getDescription());
  }

  public String getNationaliteitOmschrijving() {
    String afgeleid = "";
    DossierPersoonType type = getAfgeleidVan();
    if (type != DossierPersoonType.ONBEKEND) {
      afgeleid = " (afgeleid van " + type.getDescrExtra() + ")";
    }

    return getNationaliteit().getDescription() + afgeleid;
  }

  public FieldValue getRedenverkrijgingNederlanderschap() {
    // Update waarden als deze nog niet gezet is
    if (emp(redenverkrijgingNederlanderschap.getStringValue())) {
      redenverkrijgingNederlanderschap.setValue(getRedenverkrijging());
    }

    return redenverkrijgingNederlanderschap;
  }

  public void setRedenverkrijgingNederlanderschap(FieldValue reden) {
    this.redenverkrijgingNederlanderschap = FieldValue.from(reden);
    setRedenverkrijging(this.redenverkrijgingNederlanderschap.getStringValue());
  }

  public String getSinds() {
    return pos(
        getDatumVerkrijging().getLongDate()) ? getDatumVerkrijging().getFormatDate() : getVerkrijgingType().getDescr();
  }

  public DossierNationaliteitDatumVanafType getVerkrijgingType() {
    return DossierNationaliteitDatumVanafType.get(getTypeVerkrijging());
  }

  public void setVerkrijgingType(DossierNationaliteitDatumVanafType type) {
    setTypeVerkrijging(type.getCode());
  }

  public boolean isNationaliteit(DossierNationaliteit nationaliteit) {
    return nationaliteit.getNationaliteit().getValue() == getNationaliteit().getValue();
  }

  public void koppelenAan(DossierNationaliteiten dossier) {
    if (dossier instanceof Dossier) {
      setDoss(ReflectionUtil.deepCopyBean(Doss.class, dossier));
    } else if (dossier instanceof DossierPersoon) {
      setDossPer(ReflectionUtil.deepCopyBean(DossPer.class, dossier));
    }
  }
}
