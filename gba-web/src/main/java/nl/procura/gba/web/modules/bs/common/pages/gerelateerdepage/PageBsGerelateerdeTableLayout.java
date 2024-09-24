/*
 * Copyright 2023 - 2024 Procura B.V.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.Fieldset;

public abstract class PageBsGerelateerdeTableLayout extends GbaVerticalLayout {

  private final GbaApplication application;
  private final Dossier        dossier;

  private final List<RelatiesTableLayout> relatiesLayouts = new ArrayList<>();

  public PageBsGerelateerdeTableLayout(GbaApplication application, Dossier dossier) {
    this.application = application;
    this.dossier = dossier;
  }

  protected RelatiesTableLayout addLayout(RelatiesTableLayout layout) {
    relatiesLayouts.add(layout);
    return layout;
  }

  public void init() {
    relatiesLayouts.forEach(relatiesTableLayout -> relatiesTableLayout.getTable().init());
  }

  public void laadPersonen(Collection<DossierPersoon> personen, DossierPersoonType type) {
    application.getServices().getDossierService().deletePersonen(dossier, personen);
    laadRelaties(type);
  }

  public abstract List<DossierPersoon> getPersonen(DossierPersoonType... types);

  public abstract List<DossierPersoon> getPersonen();

  public abstract void laadRelaties(DossierPersoonType... types);

  public abstract void verwijderPersoon(DossierPersoon persoon);

  public abstract void toevoegenPersonen(List<DossierPersoon> personen);

  // Override please
  @SuppressWarnings("unused")
  public abstract void onDossierPersoon(DossierPersoon dossierPersoon);

  @SuppressWarnings("unused")
  public abstract void onHerladen(DossierPersoonType[] types);

  @SuppressWarnings("unused")
  protected void onToevoegen(DossierPersoonType type) {
  }

  @SuppressWarnings("unused")
  protected void onVerwijderen(DossierPersoon persoon) {
  }

  /**
   * Wordt aangeroepen nadat de personen zijn geladen
   */
  protected void afterLaadPersonen() {
  }

  public boolean isType(List<DossierPersoonType> typeList, DossierPersoonType... types) {

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

  public class RelatiesTableLayout extends OptieLayout implements ClickListener {

    private final DossierPersoonType[] types;
    private final Button               buttonReload = new Button("Herladen");
    private final Button               buttonAdd    = new Button("Toevoegen");
    private final Button               buttonDel    = new Button("Verwijderen");
    private final RelatiesTable        table;

    public RelatiesTableLayout(String caption, DossierPersoonType... types) {
      this.table = DossierPersoonType.KIND.is(types)
          ? new RelatiesTableKind()
          : new RelatiesTable();
      this.types = types;

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
      PageBsGerelateerdeTableLayout.this.onHerladen(types);
      init();
    }

    private void onToevoegen() {
      if (types.length > 1) {
        DossierTypeWindow dossierTypeWindow = new DossierTypeWindow(types) {

          @Override
          public void onClick(DossierPersoonType type) {
            PageBsGerelateerdeTableLayout.this.onToevoegen(type);
          }
        };
        getParentWindow().addWindow(dossierTypeWindow);

      } else {
        PageBsGerelateerdeTableLayout.this.onToevoegen(types[0]);
      }
    }

    private void onVerwijderen() {
      new DeleteProcedure<DossierPersoon>(table) {

        @Override
        protected void deleteValue(DossierPersoon persoon) {
          PageBsGerelateerdeTableLayout.this.onVerwijderen(persoon);
          verwijderPersoon(persoon);
          init();
        }
      };
    }

    private class RelatiesTable extends PageBsGerelateerdeTable {

      @Override
      public List<DossierPersoon> getPersonen() {
        return PageBsGerelateerdeTableLayout.this.getPersonen(types);
      }

      @Override
      public void onDoubleClick(Record record) {
        onDossierPersoon((DossierPersoon) record.getObject());
      }
    }

    private class RelatiesTableKind extends RelatiesTable {

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
  }
}
