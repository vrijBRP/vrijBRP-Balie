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

package nl.procura.gba.web.services.beheer.link;

import static nl.procura.standard.Globalfunctions.*;

import java.util.Map.Entry;
import java.util.Properties;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Link;
import nl.procura.standard.ProcuraDate;

public class PersonenLink extends Link {

  private Properties properties = new Properties();

  public PersonenLink() {
  }

  public PersonenLink(String id, PersonenLinkType type) {
    setId(id);
    setLinkType(type);
  }

  public DateTime getDatumEinde() {
    return new DateTime(getDEnd(), toBigDecimal(0));
  }

  public void setDatumEinde(DateTime datum) {
    setDEnd(toBigDecimal(datum.getLongDate()));
  }

  public DateTime getDatumIngang() {
    return new DateTime(getDIn(), toBigDecimal(0));
  }

  public PersonenLinkType getLinkType() {
    return PersonenLinkType.get(getType());
  }

  public void setLinkType(PersonenLinkType type) {
    setType(type.getCode());
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public String getPropertiesAsTekst() {
    StringBuilder msg = new StringBuilder();
    for (Entry<Object, Object> entry : getProperties().entrySet()) {
      msg.append(entry.getKey() + "=" + entry.getValue() + ", ");
    }

    return trim(msg.toString());
  }

  public String getProperty(String property) {
    return getProperties().getProperty(property);
  }

  public boolean isVerlopen() {
    return new ProcuraDate().diffInDays(astr(getDatumEinde().getLongDate())) <= 0;
  }
}
