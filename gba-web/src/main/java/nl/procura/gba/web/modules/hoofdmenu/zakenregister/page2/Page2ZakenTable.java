/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.component.table.indexed.LazyLoadable;
import nl.procura.vaadin.functies.VaadinUtils;

public abstract class Page2ZakenTable extends GbaTable implements LazyLoadable {

  private Page2ProfielenMap profielenMap   = null;
  private ZaakArgumenten    zaakArgumenten = new ZaakArgumenten();
  private ZaakSortering     sortering      = zaakArgumenten.getSortering();

  public Page2ZakenTable() {
    super("zakenregister");
  }

  /**
   * Geeft de profielen terug van de gebruiker die de zaak heeft ingevoerd
   */
  public String getIngevoerdDoor(Zaak zaak) {
    if (pos(zaak.getIngevoerdDoor().getValue())) {
      return String.format("%s (%s)", zaak.getIngevoerdDoor().getDescription(),
          profielenMap.getProfielen(zaak.getIngevoerdDoor()));
    }

    return "Onbekend";
  }

  public String getIngevoerdDoorProfielen(Zaak zaak) {
    if (pos(zaak.getIngevoerdDoor().getValue())) {
      return profielenMap.getProfielen(zaak.getIngevoerdDoor());
    }
    return "Onbekend";
  }

  @Override
  public void init() {

    ZakenregisterAccordionTab accordionTab = VaadinUtils.getChild(getWindow(), ZakenregisterAccordionTab.class);

    if (accordionTab == null) {
      profielenMap = new Page2ProfielenMap();
    } else {
      profielenMap = accordionTab.getProfielenMap();
    }

    super.init();
  }

  @Override
  public void onDoubleClick(Record record) {

    GbaPageTemplate page = VaadinUtils.getParent(this, GbaPageTemplate.class);
    ZaakKey zaakKey = record.getObject(ZaakKey.class);

    getApplication().getServices()
        .getZakenService()
        .getMinimaleZaken(new ZaakArgumenten(zaakKey))
        .stream()
        .findFirst()
        .ifPresent(zaak -> ZaakregisterNavigator.navigatoTo(zaak, page, false));

    super.onClick(record);
  }

  @Override
  public void onLazyLoad(List<Record> records) {
    lazyLoad(records, true);
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 50);
    addColumn("Zaaktype");
    addColumn("Gebruiker / profielen").setCollapsed(true);
    addColumn("Gebruiker", 170).setCollapsed(false);
    addColumn("Profielen").setCollapsed(true);
    addColumn("Bron", 130).setCollapsed(true);
    addColumn("Leverancier", 130).setCollapsed(true);
    addColumn("Datum ingang", 100);
    addColumn("Ingevoerd op", 130);

    super.setColumns();
  }

  @Override
  public void setRecords() {
  }

  protected void lazyLoad(List<Record> records, boolean minimaal) {

    // Zoekargumenten samenstellen
    ZaakArgumenten za = new ZaakArgumenten(zaakArgumenten);

    for (Record record : records) {
      za.addZaakKey(record.getObject(ZaakKey.class));
    }

    // Zoeken
    if (za.getZaakKeys().size() > 0) {
      List<Zaak> zaken = getApplication().getServices().getZakenService().getMinimaleZaken(za);

      if (!minimaal) { // Zoek de standaardzaken
        zaken = getApplication().getServices().getZakenService().getStandaardZaken(zaken);
      }

      for (Record record : records) {
        int nr = sortering.getNumber(getRecords().size(), record.getItemId());
        for (Zaak zaak : zaken) {
          if (record.getObject(ZaakKey.class).getZaakId().equals(zaak.getZaakId())) {
            loadZaak(nr, record, zaak);
          }
        }
      }
    }
  }

  protected abstract void loadZaak(int nr, Record record, Zaak zaak);

  public void setZaakArgumenten(ZaakArgumenten zaakArgumenten) {
    this.zaakArgumenten = zaakArgumenten;
  }

  public void setSortering(ZaakSortering sortering) {
    this.sortering = sortering;
  }
}
