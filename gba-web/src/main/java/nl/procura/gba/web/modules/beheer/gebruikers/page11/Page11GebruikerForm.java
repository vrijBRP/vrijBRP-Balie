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

package nl.procura.gba.web.modules.beheer.gebruikers.page11;

import static nl.procura.gba.web.modules.beheer.gebruikers.page11.Page11GebruikerBean.GEBRUIKER_NAAR;
import static nl.procura.gba.web.modules.beheer.gebruikers.page11.Page11GebruikerBean.GEBRUIKER_VAN;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.containers.GebruikerContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class Page11GebruikerForm extends GbaForm<Page11GebruikerBean> {

  public Page11GebruikerForm() {

    setOrder(GEBRUIKER_VAN, GEBRUIKER_NAAR);
    setColumnWidths("80px", "");

    setBean(new Page11GebruikerBean());
  }

  @Override
  public void attach() {

    List<Gebruiker> gebruikers = getApplication().getServices().getGebruikerService().getGebruikers(false);

    setContainer(getField(GEBRUIKER_VAN), gebruikers);
    setContainer(getField(GEBRUIKER_NAAR), gebruikers);

    getField(GEBRUIKER_VAN).setValue(new UsrFieldValue(202, ""));

    super.attach();
  }

  private void setContainer(Field field, List<Gebruiker> gebruikers) {

    GbaNativeSelect selectField = (GbaNativeSelect) field;
    selectField.setContainerDataSource(new GebruikerContainer(gebruikers, false));
  }
}
