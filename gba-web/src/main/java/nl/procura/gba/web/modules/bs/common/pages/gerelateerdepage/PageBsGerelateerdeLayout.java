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
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;

public class PageBsGerelateerdeLayout extends GbaVerticalLayout {

  private static final long VOLWASSEN_LEEFTIJD = 18;

  private final GbaApplication       application;
  private final DossierPersoon       dossierPersoon;
  private final Dossier              dossier;
  private final DossierPersoonType[] types;

  private final RelatiesTableLayout relatiesLayout1;
  private final RelatiesTableLayout relatiesLayout2;
  private final RelatiesTableLayout relatiesLayout3;

  public PageBsGerelateerdeLayout(GbaApplication application, Dossier dossier, DossierPersoon dossierPersoon,
      DossierPersoonType... types) {

    this.application = application;
    this.dossier = dossier;
    this.dossierPersoon = dossierPersoon;
    this.types = types;

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (!isPersonen(PARTNER, EXPARTNER, OUDER, DossierPersoonType.KIND)) {
      laadPersonen();
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

  public void laadPersonen() {
    application.getServices().getDossierService().deletePersonen(dossier, dossierPersoon.getPersonen());
    laadRelaties(types);
  }

  public void laadRelaties(DossierPersoonType[] types) {

    if (isType(asList(types), PARTNER, EXPARTNER)) {
      if (dossierPersoon.getPersonen(PARTNER, EXPARTNER).isEmpty()) {
        dossierPersoon.toevoegenPersonen(getTypePersonen(dossierPersoon, HUW_GPS));
      }
    }

    if (isType(asList(types), OUDER)) {
      for (DossierPersoon ouder : getTypePersonen(dossierPersoon, OUDER_1, OUDER_2)) {
        dossierPersoon.toevoegenPersoon(ouder);
      }
    }

    if (isType(asList(types), DossierPersoonType.KIND)) {
      if (dossierPersoon.getPersonen(DossierPersoonType.KIND).isEmpty()) {
        dossierPersoon.toevoegenPersonen(getTypePersonen(dossierPersoon, GBACat.KINDEREN));
      }
    }
  }

  // Override please
  @SuppressWarnings("unused")
  public void onDossierPersoon(DossierPersoon dossierPersoon) {
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

  /**
   * Vul persoonslijst op basis van BSN
   */
  private BasePLExt getPersoonslijst(BasePLRec cat) {

    BasePLExt pl;

    if (cat.hasElems()) {
      BasePLValue bsn = cat.getElemVal(GBAElem.BSN);

      if (fil(bsn.getVal())) {
        pl = getPl(new BsnFieldValue(bsn.getVal()));

        if (pl.getCats().size() > 0) {
          return pl;
        }
      }
      return new BasePLExt();
    }

    return null;
  }

  private BasePLExt getPl(BsnFieldValue bsn) {
    return application.getServices().getPersonenWsService().findPL(bsn);
  }

  /**
   * Zoek de relaties van de persoon
   */
  private List<DossierPersoon> getTypePersonen(DossierPersoon betreffende, GBACat... categorieen) {
    List<DossierPersoon> personen = new ArrayList<>();

    for (GBACat categorie : categorieen) {
      if (betreffende.isIngeschreven()) {

        BasePLExt pl = getPl(betreffende.getBurgerServiceNummer());
        List<BasePLSet> sets = pl.getCat(categorie).getSets();

        for (BasePLSet set : sets) {
          DossierPersoon persoon = getTypePersoon(pl, set, categorie);

          if (persoon.isVolledig() && !isVolwassenKind(persoon)) {
            personen.add(persoon);
          }
        }
      }
    }

    return personen;
  }

  private DossierPersoon getTypePersoon(BasePLExt persoonPl, BasePLSet set, GBACat gbaCat) {

    DossierPersoon persoon = new DossierPersoon();
    BasePLRec cat = set.getLatestRec();
    BasePLExt pl = getPersoonslijst(cat);

    if (pl == null) {
      return persoon;
    }

    switch (gbaCat) {
      case OUDER_1:
      case OUDER_2:
        persoon = new DossierPersoon(DossierPersoonType.OUDER);
        break;

      case HUW_GPS:
        persoon = new DossierPersoon(isHuidigePartner(cat) ? PARTNER : EXPARTNER);
        break;

      case KINDEREN:
        // Geen overleden kinderen tonen
        if (pl.getOverlijding().isOverleden()) {
          return persoon;
        }

        persoon = new DossierPersoon(DossierPersoonType.KIND);
        break;

      default:
        break;
    }

    /*
      Voeg de persoongegevens toe op basis van de categorie
     */
    if (pl.getPersoon().getBsn().getVal().isEmpty()) {

      BsPersoonUtils.kopieDossierPersoon(cat, persoon);
    } else {
      BsPersoonUtils.kopieDossierPersoon(pl, persoon);
    }

    // Verbintenis gegevens toevoegen

    if (GBACat.HUW_GPS == gbaCat) {

      BasePLRec sluiting = persoonPl.getHuwelijk().getSluiting(set, "");
      BasePLRec ontbinding = persoonPl.getHuwelijk().getOntbinding(set, "");

      BasePLValue sluitDatum = sluiting.getElemVal(DATUM_VERBINTENIS);
      BasePLValue sluitPlaats = sluiting.getElemVal(PLAATS_VERBINTENIS);
      BasePLValue sluitLand = sluiting.getElemVal(LAND_VERBINTENIS);

      BasePLValue ontbDatum = ontbinding.getElemVal(DATUM_ONTBINDING);
      BasePLValue ontbPlaats = ontbinding.getElemVal(PLAATS_ONTBINDING);
      BasePLValue ontbLand = ontbinding.getElemVal(LAND_ONTBINDING);
      BasePLValue ontbReden = ontbinding.getElemVal(REDEN_ONTBINDING);

      // Sluiting
      persoon.getVerbintenis().setSoort(
          SoortVerbintenis.get(sluiting.getElemVal(SOORT_VERBINTENIS).getVal()));
      persoon.getVerbintenis().getSluiting().setDatum(new DateTime(sluitDatum.getVal()));
      persoon.getVerbintenis().getSluiting().setPlaats(
          new FieldValue(sluitPlaats.getVal(), sluitPlaats.getDescr()));
      persoon.getVerbintenis().getSluiting().setLand(
          new FieldValue(sluitLand.getVal(), sluitLand.getDescr()));

      // Ontbinding
      persoon.getVerbintenis().getOntbinding().setDatum(new DateTime(ontbDatum.getVal()));
      persoon.getVerbintenis().getOntbinding().setPlaats(
          new FieldValue(ontbPlaats.getVal(), ontbPlaats.getDescr()));
      persoon.getVerbintenis().getOntbinding().setLand(
          new FieldValue(ontbLand.getVal(), ontbLand.getDescr()));
      persoon.getVerbintenis().getOntbinding().setReden(
          new FieldValue(ontbReden.getVal(), ontbReden.getDescr()));
    }

    return persoon;
  }

  private boolean isHuidigePartner(BasePLRec cat) {
    return emp(cat.getElemVal(GBAElem.DATUM_ONTBINDING).getVal());
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

  private boolean isVolwassenKind(DossierPersoon persoon) {
    boolean isKind = persoon.getDossierPersoonType().is(DossierPersoonType.KIND);
    boolean isVolwassen = along(persoon.getGeboorte().getLeeftijd()) >= VOLWASSEN_LEEFTIJD;
    return isKind && isVolwassen;
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
    private final Button               buttonAdd = new Button("Toevoegen");
    private final Button               buttonDel = new Button("Verwijderen");
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

      getRight().addButton(buttonAdd, this);
      getRight().addButton(buttonDel, this);
    }

    @Override
    public void buttonClick(ClickEvent event) {
      if (event.getButton() == buttonAdd) {
        onToevoegen();

      } else if (event.getButton() == buttonDel) {
        onVerwijderen();
      }
    }

    public RelatiesTable getTable() {
      return table;
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
