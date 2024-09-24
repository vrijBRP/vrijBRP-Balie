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

package nl.procura.gba.web.modules.bs.registration.page10.adresselectie.selectie;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.theme.twee.Icons;

public class AddressSelectionPage extends NormalPageTemplate {

  private final Table             table;
  private final Consumer<Address> listener;
  private final List<Address>     addresses;
  private final boolean           gebruikPPDCodes;

  AddressSelectionPage(final List<Address> addresses, boolean gebruikPPDCodes, Consumer<Address> listener) {
    this.addresses = addresses;
    this.listener = listener;
    this.table = new Table();
    this.gebruikPPDCodes = gebruikPPDCodes;

    setInfo("Selecteer een adres.");
    addExpandComponent(this.table);
  }

  @Override
  public void onEnter() {
    listener.accept(this.table.getSelectedRecord().getObject(Address.class));
    super.onEnter();
  }

  private class Table extends GbaTable {

    @Override
    public void onClick(final Record record) {
      listener.accept(record.getObject(Address.class));
      super.onClick(record);
      ((ModalWindow) getWindow()).closeWindow();
    }

    @Override
    public void setColumns() {
      setSelectable(true);

      addColumn("G.V.B", 40).setClassType(Embedded.class);
      addColumn("In beheer", 70).setClassType(Component.class);
      addColumn("Adres");

      super.setColumns();
    }

    @Override
    public void setRecords() {
      int gebrPPD = aval(getApplication().getParmValue(ParameterConstant.GEBR_PPD));

      for (Address a : addresses) {
        Record r = addRecord(a);
        if (a.isSuitableForLiving()) {
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
        } else {
          r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
        }

        if (gebruikPPDCodes && (gebrPPD >= 0)) {
          if (gebrPPD == aval(a.getPPD())) {
            r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
          } else {
            r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
          }
        } else {
          r.addValue(new Label("N.v.t."));
        }

        String adres = a.getAddressLabel();
        if (pos(a.getEndDate())) {
          adres += " - beëindigd op " + date2str(a.getEndDate());
        }

        r.addValue(adres);
      }
    }
  }
}
