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

package nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.HuwAmbt;
import nl.procura.gba.web.services.interfaces.Geldigheid;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class HuwelijksAmbtenaar extends HuwAmbt implements Geldigheid {

  private static final long serialVersionUID = -1218181049188497309L;

  public HuwelijksAmbtenaar() {
    setNaam("");
    setEmail("");
    setTelefoon("");
    setToelichting("");
  }

  public List<String> getAliassen() {
    List<String> aliassen = new ArrayList<>();
    for (String alias : StringUtils.defaultIfBlank(getAlias(), "").split(",")) {
      if (StringUtils.isNotBlank(alias)) {
        aliassen.add(alias.trim());
      }
    }

    return aliassen;
  }

  public void setAliassen(List<String> aliassen) {
    setAlias(StringUtils.join(aliassen, ","));
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsn()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  public Long getCodeHuwelijksAmbtenaar() {
    return getCHuwAmbt();
  }

  public void setCodeHuwelijksAmbtenaar(long code) {
    setCHuwAmbt(code);
  }

  @Override
  public DateTime getDatumEinde() {
    return new DateTime(getdEnd());
  }

  @Override
  public void setDatumEinde(DateTime dateTime) {
    setdEnd(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getdIn());
  }

  @Override
  public void setDatumIngang(DateTime dateTime) {
    setdIn(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public GeldigheidStatus getGeldigheidStatus() {
    return GeldigheidStatus.get(this);
  }

  public String getHuwelijksAmbtenaar() {
    return getNaam();
  }

  public void setHuwelijksAmbtenaar(String huwelijksAmbtenaar) {
    setNaam(huwelijksAmbtenaar);
  }

  public String getTelefoon() {
    return getTel();
  }

  public void setTelefoon(String telefoon) {
    setTel(telefoon);
  }

  public String getToelichting() {
    return getOms();
  }

  public void setToelichting(String toelichting) {
    setOms(toelichting);
  }
}
