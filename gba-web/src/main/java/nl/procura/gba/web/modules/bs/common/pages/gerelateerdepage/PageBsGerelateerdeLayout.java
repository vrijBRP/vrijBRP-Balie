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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import static java.util.Arrays.asList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getTypePersonen;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.Fieldset;

public class PageBsGerelateerdeLayout extends GbaVerticalLayout {

  private final GbaApplication application;
  private final DossierPersoon dossierPersoon;
  private final Dossier        dossier;

  private final RelatiesTableLayout relatiesLayout1;
  private final RelatiesTableLayout relatiesLayout2;
  private final RelatiesTableLayout relatiesLayout3;

  public PageBsGerelateerdeLayout(GbaApplication application, Dossier dossier,
      DossierPersoon dossierPersoon, DossierPersoonType... types) {

    this.application = application;
    this.dossier = dossier;
    this.dossierPersoon = dossierPersoon;

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (!isPersonen(PARTNER)) {
      laadPersonen(dossierPersoon.getPersonen(PARTNER), PARTNER);
      afterLaadPersonen();
    }

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (!isPersonen(EXPARTNER)) {
      laadPersonen(dossierPersoon.getPersonen(EXPARTNER), EXPARTNER);
      afterLaadPersonen();
    }

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (!isPersonen(OUDER)) {
      laadPersonen(dossierPersoon.getPersonen(OUDER), OUDER);
      afterLaadPersonen();
    }

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (!isPersonen(DossierPersoonType.KIND)) {
      laadPersonen(dossierPersoon.getPersonen(DossierPersoonType.KIND), KIND);
      afterLaadPersonen();
    }

    relatiesLayout1 = new RelatiesTableLayout("Partner(s)", PARTNER, EXPARTNER);
    relatiesLayout2 = new RelatiesTableLayout("Ouder(s)", OUDER);
    relatiesLayout3 = new RelatiesTableLayout("Minderjarige kind(eren)", DossierPersoonType.KIND);

    if (isType(asList(types), PARTNER, EXPARTNER)) {
      addExpandComponent(relatiesLayout1, 0.30f);
    }

    if (isType(asList(types), OUDER)) {
      addExpandComponent(relatiesLayout2, 0.30f);
    }

    if (isType(asList(types), DossierPersoonType.KIND)) {
      addExpandComponent(relatiesLayout3, 0.40f);
    }
  }

  public List<DossierPersoon> getPersonen() {
    List<DossierPersoon> personen = new ArrayList<>();
    personen.addAll(relatiesLayout1.getTable().getAllValues(DossierPersoon.class));
    personen.addAll(relatiesLayout2.getTable().getAllValues(DossierPersoon.class));
    personen.addAll(relatiesLayout3.getTable().getAllValues(DossierPersoon.class));
    return personen;
  }

  public void init() {
    relatiesLayout1.getTable().init();
    relatiesLayout2.getTable().init();
    relatiesLayout3.getTable().init();
  }

  public boolean isPersonen(DossierPersoonType... types) {
    return !dossierPersoon.getPersonen(types).isEmpty();
  }

  public void laadPersonen(Collection<DossierPersoon> personen, DossierPersoonType type) {
    application.getServices().getDossierService().deletePersonen(dossier, personen);
    laadRelaties(type);
  }

  public void laadRelaties(DossierPersoonType... types) {
    Services services = application.getServices();
    if (isType(asList(types), PARTNER, EXPARTNER)) {
      dossierPersoon.toevoegenPersonen(getTypePersonen(services, dossierPersoon, HUW_GPS));
    }

    if (isType(asList(types), OUDER)) {
      getTypePersonen(services, dossierPersoon, OUDER_1, OUDER_2).forEach(dossierPersoon::toevoegenPersoon);
    }

    if (isType(asList(types), DossierPersoonType.KIND)) {
      dossierPersoon.toevoegenPersonen(getTypePersonen(services, dossierPersoon, GBACat.KINDEREN));
    }
  }

  // Override please
  @SuppressWarnings("unused")
  public void onDossierPersoon(DossierPersoon dossierPersoon) {
  }

  @SuppressWarnings("unused")
  public void onHerladen(DossierPersoonType[] types) {
    for (DossierPersoonType type : types) {
      laadPersonen(dossierPersoon.getPersonen(type), type);
      afterLaadPersonen();
    }
  }

  @SuppressWarnings("unused")
  public void onToevoegen(DossierPersoonType type) {
  } // Override

  @SuppressWarnings("unused")
  public void onVerwijderen(DossierPersoon persoon) {
  } // Override

  /**
   * Wordt aangeroepen nadat de personen zijn geladen
   */
  protected void afterLaadPersonen() {
  }

  private boolean isType(List<DossierPersoonType> typeList, DossierPersoonType... types) {

    if (typeList.isEmpty()) {
      return true;
    }

    for (DossierPersoonType type : types) {
      if (typeList.contains(type)) {
        return true;
      }
    }

    return false;
  }

  private class ButtonType implements ClickListener {

    private final DossierPersoonType type;
    private final Button             button;

    public ButtonType(DossierPersoonType type) {
      this.type = type;
      button = new Button(type.getDescr(), this);
      button.setSizeFull();
    }

    @Override
    public void buttonClick(ClickEvent event) {
    }

    public Button getButton() {
      return button;
    }

    public DossierPersoonType getType() {
      return type;
    }
  }

  private class DossierTypeWindow extends GbaModalWindow {

    private final List<ButtonType> buttonTypes = new ArrayList<>();

    public DossierTypeWindow(DossierPersoonType... types) {

      super("Selecteer een type", "200px");

      VerticalLayout layout = new VerticalLayout();
      layout.setSpacing(true);
      layout.setMargin(true);

      for (DossierPersoonType type : types) {

        ButtonType buttonType = new ButtonType(type) {

          @Override
          public void buttonClick(ClickEvent event) {
            onClick(getType());
            closeWindow();
          }
        };

        buttonTypes.add(buttonType);
      }

      for (ButtonType buttonType : buttonTypes) {
        layout.addComponent(buttonType.getButton());
      }

      setContent(layout);
    }

    @SuppressWarnings("unused")
    public void onClick(DossierPersoonType type) {
    } // Override
  }

  private class RelatiesTable extends PageBsGerelateerdeTable {

    public RelatiesTable(DossierPersoonType... types) {
      super(dossierPersoon, types);
    }

    @Override
    public void onDoubleClick(Record record) {
      onDossierPersoon((DossierPersoon) record.getObject());
    }
  }

  private class RelatiesTableKind extends RelatiesTable {

    public RelatiesTableKind(DossierPersoonType... types) {
      super(types);
    }

    @Override
    public void setColumns() {

      setMultiSelect(true);
      setSelectable(true);

      addColumn("Nr", 30);
      addColumn("Naam", 338).setUseHTML(true);
      addColumn("Leeftijd", 60);
      addColumn("Type");
      addColumn("&nbsp;", 70).setClassType(Component.class);
    }
  }

  private class RelatiesTableLayout extends OptieLayout implements ClickListener {

    private final DossierPersoonType[] types;
    private final Button               buttonReload = new Button("Herladen");
    private final Button               buttonAdd    = new Button("Toevoegen");
    private final Button               buttonDel    = new Button("Verwijderen");
    private final RelatiesTable        table;

    public RelatiesTableLayout(String caption, DossierPersoonType... types) {

      this.types = types;
      this.table = DossierPersoonType.KIND.is(types)
          ? new RelatiesTableKind(types)
          : new RelatiesTable(types);

      getLeft().addComponent(new Fieldset(caption));
      getLeft().addExpandComponent(table);

      getRight().setWidth("140px");
      getRight().setCaption("Opties");

      getRight().addButton(buttonReload, this);
      getRight().addButton(buttonAdd, this);
      getRight().addButton(buttonDel, this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      if (event.getButton() == buttonReload) {
        onHerladen();

      } else if (event.getButton() == buttonAdd) {
        onToevoegen();

      } else if (event.getButton() == buttonDel) {
        onVerwijderen();
      }
    }

    public RelatiesTable getTable() {
      return table;
    }

    private void onHerladen() {
      PageBsGerelateerdeLayout.this.onHerladen(types);
      init();
    }

    private void onToevoegen() {

      if (types.length > 1) {
        DossierTypeWindow dossierTypeWindow = new DossierTypeWindow(types) {

          @Override
          public void onClick(DossierPersoonType type) {
            PageBsGerelateerdeLayout.this.onToevoegen(type);
          }
        };
        getParentWindow().addWindow(dossierTypeWindow);

      } else {
        PageBsGerelateerdeLayout.this.onToevoegen(types[0]);
      }
    }

    private void onVerwijderen() {

      new DeleteProcedure<DossierPersoon>(table) {

        @Override
        protected void deleteValue(DossierPersoon persoon) {
          PageBsGerelateerdeLayout.this.onVerwijderen(persoon);
          dossierPersoon.verwijderPersoon(persoon);
          init();
        }
      };
    }
  }
}
