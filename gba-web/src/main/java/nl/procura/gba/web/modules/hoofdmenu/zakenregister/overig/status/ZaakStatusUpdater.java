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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.containers.ZaakStatusContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakTypeStatussen;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public abstract class ZaakStatusUpdater {

  private ZaakStatusType initieleStatus    = null;
  private String         initieleOpmerking = null;

  public ZaakStatusUpdater(GbaTable table) {
    init(table.getWindow(), table.getSelectedValues(Object.class));
  }

  public ZaakStatusUpdater(Window window, List<Zaak> zaken) {
    init(window, zaken);
  }

  public ZaakStatusUpdater(Window window, Zaak zaak, String opmerking, ZaakStatusType status) {

    this.initieleOpmerking = opmerking;
    this.initieleStatus = status;

    init(window, asList(zaak));
  }

  /**
   * Mag de gebruiker de eindstatus nog wijzigen?
   */
  public static void checkUpdatenStatusMogelijk(Application application, List<Zaak> zaken) {

    GbaApplication gbaApplication = (GbaApplication) application;

    boolean magEigenStatusWijzigen = isTru(gbaApplication.getParmValue(ParameterConstant.ZAKEN_STATUS_EIGEN_ZAAK));
    boolean magEindStatusWijzigen = isTru(gbaApplication.getParmValue(ParameterConstant.ZAKEN_EINDSTATUS));

    for (Zaak zaak : zaken) {
      boolean isGebruiker = along(
          zaak.getIngevoerdDoor().getValue()) == gbaApplication.getServices().getGebruiker().getCUsr();
      if (isGebruiker && !magEigenStatusWijzigen) {
        if (zaken.size() == 1) {
          throw new ProException(ENTRY, WARNING,
              "Deze zaak is door u ingevoerd en mag u zelf niet van status wijzigen");
        }
        throw new ProException(ENTRY, WARNING,
            "Minimaal één van de zaken is door u ingevoerd en mag u zelf niet van status wijzigen");
      }
      if (zaak.getStatus().isEindStatus() && !magEindStatusWijzigen) {
        throw new ProException(ENTRY, WARNING, "Zaken met een eindstatus kunnen niet meer worden gewijzigd");
      }
    }
  }

  public static void checkUpdatenStatusMogelijk(Application application, Zaak zaak) {
    checkUpdatenStatusMogelijk(application, asList(zaak));
  }

  public static void checkUpdatenStatusMogelijk(GbaApplication application, GbaTable table) {
    checkUpdatenStatusMogelijk(application, table.getSelectedValues(Zaak.class));
  }

  public ZaakStatusType getInitieleStatus() {
    return initieleStatus;
  }

  protected abstract void reload();

  private String getInitieleOpmerking() {
    return initieleOpmerking;
  }

  private void init(Window window, List objecten) {

    if (objecten.isEmpty()) {
      throw new ProException(WARNING, "Geen records geselecteerd");
    }

    List<Zaak> zaken = new ArrayList<>();

    GbaApplication application = (GbaApplication) window.getApplication();
    if (objecten.get(0) instanceof ZaakKey) {
      zaken.addAll(application.getServices().getZakenService().getMinimaleZaken(
          new ZaakArgumenten(objecten)));
    } else if (objecten.get(0) instanceof ZaakRecord) {
      for (ZaakRecord zr : (List<ZaakRecord>) objecten) {
        if (zr.getZaak() != null) {
          zaken.add(zr.getZaak());
        }
      }
    } else {
      zaken.addAll(objecten);
    }

    checkUpdatenStatusMogelijk(window.getApplication(), zaken);
    application.getParentWindow().addWindow(new StatusWindowNew(zaken));
  }

  public class StatusWindowNew extends GbaModalWindow {

    private final GbaNativeSelect select    = new GbaNativeSelect();
    private final TextArea        opmerking = new TextArea();

    public StatusWindowNew(final List<Zaak> zaken) {

      super("Nieuwe status", "340px");

      select.setWidth("100%");

      List<ZaakStatusType> statussen = new ArrayList<>();

      if (getInitieleOpmerking() == null) {
        statussen.addAll(ZaakTypeStatussen.getOvereenkomstige(zaken));
      } else {
        statussen.add(getInitieleStatus());
        select.setNullSelectionAllowed(false);
      }

      select.setContainerDataSource(new ZaakStatusContainer(statussen));
      select.setImmediate(true);

      // Zet initiele waarden
      select.setValue(getInitieleStatus());
      opmerking.setValue(astr(getInitieleOpmerking()));

      Button button = new Button("Uitvoeren");
      button.addListener((ClickListener) event -> {

        checkUpdatenStatusMogelijk(getGbaApplication(), zaken);

        for (Zaak zaak : zaken) {
          ZaakStatusType nieuweStatus = (ZaakStatusType) select.getValue();

          if (nieuweStatus == null) {
            throw new ProException(INFO, "Selecteer een status");
          }

          getGbaApplication().getServices().getZakenService().getService(zaak).updateStatus(zaak,
              zaak.getStatus(),
              nieuweStatus,
              astr(opmerking.getValue()));
        }

        reload();

        closeWindow();
      });

      VerticalLayout vLayout = new VerticalLayout();
      vLayout.setWidth("100%");
      vLayout.setSpacing(true);
      vLayout.setMargin(true);

      HorizontalLayout hLayout = new HorizontalLayout();
      hLayout.addComponent(select);
      hLayout.addComponent(button);

      hLayout.setWidth("100%");
      hLayout.setSpacing(true);
      hLayout.setExpandRatio(select, 1f);

      StringBuilder sb = new StringBuilder();

      for (Entry<ZaakType, List<Zaak>> entry : ZaakTypeStatussen.groupByType(zaken).entrySet()) {
        sb.append(entry.getValue().size() + " x " + entry.getKey().getOms() + ", ");
      }

      vLayout.addComponent(new InfoLayout("Te wijzigen zaken", trim(sb.toString())));
      vLayout.addComponent(new Ruler());

      opmerking.setInputPrompt("Opmerking (optioneel)");
      opmerking.setRows(2);
      opmerking.setSizeFull();

      vLayout.addComponent(opmerking);
      vLayout.addComponent(hLayout);

      if (ZaakTypeStatussen.groupByType(zaken).size() > 1) {
        Label footer = new Label("(Alleen overeenkomstige statussen worden getoond)", Label.CONTENT_XHTML);
        footer.setStyleName("status-footer");
        footer.setSizeUndefined();
        vLayout.addComponent(footer);
        vLayout.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
      }

      addComponent(vLayout);
    }

    public void checkUpdatenStatusMogelijk(GbaApplication application, GbaTable table) {
      checkUpdatenStatusMogelijk(application, table.getSelectedValues(Zaak.class));
    }

    /**
     * Mag de gebruiker de eindstatus nog wijzigen?
     */
    public void checkUpdatenStatusMogelijk(GbaApplication application, List<Zaak> zaken) {

      boolean magEigenStatusWijzigen = isTru(application.getParmValue(ParameterConstant.ZAKEN_STATUS_EIGEN_ZAAK));
      boolean magEindStatusWijzigen = isTru(application.getParmValue(ParameterConstant.ZAKEN_EINDSTATUS));

      for (Zaak zaak : zaken) {
        boolean isGebruiker = along(
            zaak.getIngevoerdDoor().getValue()) == application.getServices().getGebruiker().getCUsr();
        if (isGebruiker && !magEigenStatusWijzigen) {
          if (zaken.size() == 1) {
            throw new ProException(ENTRY, WARNING,
                "Deze zaak is door u ingevoerd en mag u zelf niet van status wijzigen");
          }
          throw new ProException(ENTRY, WARNING,
              "Minimaal één van de zaken is door u ingevoerd en mag u zelf niet van status wijzigen");
        }
        if (zaak.getStatus().isEindStatus() && !magEindStatusWijzigen) {
          throw new ProException(ENTRY, WARNING,
              "Zaken met een eindstatus kunnen niet meer worden gewijzigd");
        }
      }
    }
  }
}
