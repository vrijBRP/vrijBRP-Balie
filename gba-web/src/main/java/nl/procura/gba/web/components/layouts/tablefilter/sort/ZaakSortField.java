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

package nl.procura.gba.web.components.layouts.tablefilter.sort;

import com.vaadin.ui.NativeSelect;

import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.SorteringContainer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;

public class ZaakSortField extends NativeSelect {

  public ZaakSortField() {
    setWidth("190px");
    setNullSelectionAllowed(false);
    setContainerDataSource(new SorteringContainer());
    setImmediate(true);
    final Services services = Services.getInstance();
    final Gebruiker gebruiker = services.getGebruiker();

    // Get user parameter
    String sort = gebruiker.getParameters().get(ParameterConstant.ZAKEN_SORT).getValue();
    setValue(ZaakSortering.get(sort, ZaakSortering.DATUM_INGANG_NIEUW_OUD));

    // Update parameter on change
    addListener(new FieldChangeListener<ZaakSortering>() {

      @Override
      public void onChange(ZaakSortering value) {
        services.getParameterService()
            .saveParameter(ParameterConstant.ZAKEN_SORT, value.getCode(), gebruiker, null, true);
      }
    });
  }

  @Override
  public ZaakSortering getValue() {
    return (ZaakSortering) super.getValue();
  }
}
