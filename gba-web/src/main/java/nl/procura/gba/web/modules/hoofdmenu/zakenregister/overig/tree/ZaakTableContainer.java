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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.tree;

import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.gba.web.services.zaken.algemeen.ZaakArgumentenBuilder.*;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.FOUT_BIJ_VERWERKING;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.VERHUIZING_AANGIFTE;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.SubModuleZaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page12.Page12Module;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page13.Page13Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page14.Page14Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page15.Page15Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page16.Page16Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page160.Page160Module;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4Module;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.Page5Module;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page6.Page6Module;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8Module;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page9.Page9Module;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakTypeStatussen;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakArgumenten;
import nl.procura.gba.web.services.zaken.zakenregister.ZaakItem;
import nl.procura.gba.web.services.zaken.zakenregister.ZakenregisterService;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class ZaakTableContainer extends HierarchicalContainer implements ProcuraContainer {

  public static final String LEAF_CLASS = "Waarde";

  public static final String  STATUS             = "Statussen";
  public static final String  FILTER             = "Filter";
  public static final String  ZOEKEN             = "Zoeken";
  public static final String  MIJN_ZAKEN         = "Mijn";
  public static final String  ZONDER_BEHANDELAAR = "Zonder behandelaar";
  public static final String  FAVORIETEN         = "Favorieten";
  public static final String  PROBLEMEN          = "Problemen";
  public static final String  BULKACTIES         = "Bulkacties";
  public static final String  ZAAK               = "Zaken";
  public static final String  HUWELIJK           = "Huwelijk / GPS";
  public static final String  OVERLIJDEN         = "Overlijden";
  public static final String  AANTAL             = "Aantal";
  private static final String INITOPEN           = "initOpen";

  private static final String BULK_RIJBEWIJS = "Rijbewijzen";
  private static final String BULK_TMV       = "Terugmeldingen";
  private static final String BULK_UITT      = "Uittreksels";
  private static final String BULK_CORR      = "Correspondentie";
  private static final String BULK_CONTROLE  = "Controles";

  private final List<ZaakAantalItem> zaakItems = new ArrayList<>();
  private Services                   serviceContainer;

  public ZaakTableContainer(Services serviceContainer) {

    setServiceContainer(serviceContainer);

    addContainerProperty(OMSCHRIJVING, String.class, "");
    addContainerProperty(AANTAL, String.class, "");
    addContainerProperty(LEAF_CLASS, Class.class, null);
    addContainerProperty(INITOPEN, Boolean.class, false);

    removeAllItems();

    // Items toevoegen
    addTreeItem(ZOEKEN, false, true, Page4Module.class);
    addTreeItem(MIJN_ZAKEN, false, true, Page15Zaken.class);

    if (getServiceContainer().getZaakAttribuutService().isZakenBehandelenEnable()) {
      addTreeItem(ZONDER_BEHANDELAAR, false, true, Page14Zaken.class);
    }
    addTreeItem(FAVORIETEN, false, true, Page16Zaken.class);
    addTreeItem(PROBLEMEN, false, true, Page13Zaken.class);
    addTreeItem(FILTER, false, true, Page160Module.class);
    addTreeItem(STATUS, true, true, HorizontalLayout.class);
    addTreeItem(BULKACTIES, true, false, HorizontalLayout.class);
    addTreeItem(ZAAK, true, true, HorizontalLayout.class);

    addTreeItem(BULK_RIJBEWIJS, BULKACTIES, false, true, Page5Module.class);
    addTreeItem(BULK_TMV, BULKACTIES, false, true, Page6Module.class);
    addTreeItem(new ZaakAantalItem().setCaption(BULK_UITT), BULKACTIES, false, true, Page8Module.class);
    addTreeItem(new ZaakAantalItem().setCaption(BULK_CORR), BULKACTIES, false, true, Page12Module.class);
    addTreeItem(BULK_CONTROLE, BULKACTIES, false, true, Page9Module.class);

    ZaakType[] huwelijkTypes = { HUWELIJK_GPS_GEMEENTE, OMZETTING_GPS, ONTBINDING_GEMEENTE };
    ZaakType[] afstammingTypes = { GEBOORTE, ERKENNING, NAAMSKEUZE };
    ZaakType[] overlijdenTypes = { OVERLIJDEN_IN_GEMEENTE, OVERLIJDEN_IN_BUITENLAND, LEVENLOOS, LIJKVINDING };

    addZaakItem(afstammingTypes, ZAAK, true, false, SubModuleZaken.class, "Naam & Afst.");
    addZaakItem(CORRESPONDENTIE, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(COVOG, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(REGISTRATION, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(GEGEVENSVERSTREKKING, ZAAK, true, false, SubModuleZaken.class, "Geg. verstrekking");
    addZaakItem(GPK, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(huwelijkTypes, ZAAK, true, false, SubModuleZaken.class, "Huwelijk / GPS");
    addZaakItem(INHOUD_VERMIS, ZAAK, true, false, SubModuleZaken.class, "Inhoud. / vermis.");
    addZaakItem(INDICATIE, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(INBOX, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(NAAMGEBRUIK, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(ONDERZOEK, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(PL_MUTATION, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(RISK_ANALYSIS, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(overlijdenTypes, ZAAK, true, false, SubModuleZaken.class, "Overlijden");
    addZaakItem(REISDOCUMENT, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(RIJBEWIJS, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(TERUGMELDING, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(UITTREKSEL, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(VERHUIZING, ZAAK, true, false, SubModuleZaken.class, "");
    addZaakItem(VERSTREKKINGSBEPERKING, ZAAK, true, false, SubModuleZaken.class, "");

    for (ZaakStatusType status : ZaakTypeStatussen.getAlle()) {
      if (status.isCombiStatus()) {
        continue;
      }
      addStatusItem(new ZaakAantalItem().setStatus(status));
    }

    recount();
  }

  @Override
  public Item addItem(Object itemId) {
    Item item = super.addItem(itemId);
    if (itemId instanceof ZaakAantalItem) {
      ((ZaakAantalItem) itemId).setId(getItemIds().size()).setItem(item);
    }

    return item;
  }

  public Services getServiceContainer() {
    return serviceContainer;
  }

  public void setServiceContainer(Services serviceContainer) {
    this.serviceContainer = serviceContainer;
  }

  public void recount() {

    ZakenregisterService zakenregister = getServiceContainer().getZakenregisterService();

    getItem(MIJN_ZAKEN).getItemProperty(AANTAL).setValue(getAantalMijnZaken());
    getItem(FAVORIETEN).getItemProperty(AANTAL).setValue(getAantalFavorieteZaken());
    getItem(PROBLEMEN).getItemProperty(AANTAL).setValue(getAantalMetAttribuut(FOUT_BIJ_VERWERKING));

    if (getServiceContainer().getZaakAttribuutService().isZakenBehandelenEnable()) {
      getItem(ZONDER_BEHANDELAAR).getItemProperty(AANTAL).setValue(getAantalNieuweZaken());
    }

    // Bulk acties
    for (ZaakAantalItem zi : getItems(ZaakAantalItem.class)) {
      if (zi.getCaption().equals(BULK_UITT)) {
        DocumentZaakArgumenten args = new DocumentZaakArgumenten();
        args.setStatussen(OPGENOMEN, INBEHANDELING);
        args.setDocumentTypes(PL_UITTREKSEL);
        zi.setAantal(getServiceContainer().getDocumentZakenService().getZakenCount(args));
        zi.getItem().getItemProperty(AANTAL).setValue(zi.isValid() ? zi.getAantal() : "-");

      } else if (zi.getCaption().equals(BULK_CORR)) {
        DocumentZaakArgumenten args = new DocumentZaakArgumenten();
        args.setStatussen(OPGENOMEN, INBEHANDELING);
        args.setDocumentTypes(VERHUIZING_AANGIFTE);
        zi.setAantal(getServiceContainer().getDocumentZakenService().getZakenCount(args));
        zi.getItem().getItemProperty(AANTAL).setValue(zi.isValid() ? zi.getAantal() : "-");
      }
    }

    // Zaaktypes / - statussen
    ZakenAantalViewHandler zaakAantallen = new ZakenAantalViewHandler(zakenregister.getZaakAantallen());
    for (ZaakAantalItem zi : getItems(ZaakAantalItem.class)) {
      if (zi.getTypes() != null && zi.getStatus() != null) {
        zi.setAantal(getAantalZaken(zi, zaakAantallen));
        zi.getItem().getItemProperty(AANTAL).setValue(zi.isValid() ? zi.getAantal() : "-");
      }
    }

    for (ZaakAantalItem zi : getItems(ZaakAantalItem.class)) {
      if (zi.getTypes() == null && zi.getStatus() != null) {
        int count = 0;
        for (ZaakAantalItem zaakItem : zaakItems) {
          if (zaakItem.getStatus() == zi.getStatus()) {
            count += zaakItem.getAantal();
          }
        }

        zi.setAantal(count);
        zi.getItem().getItemProperty(AANTAL).setValue(zi.isValid() ? count : "-");
      }
    }

    setAantalOmschrijving(PROBLEMEN);
    setAantalOmschrijving(BULKACTIES);
    setAantalOmschrijving(ZAAK);
  }

  private long getAantalFavorieteZaken() {
    ZaakArgumenten zaakArgumenten = favorieteZaken(getServiceContainer());
    return getServiceContainer().getZakenService().getMinimaleZaken(zaakArgumenten).size();
  }

  private long getAantalMijnZaken() {
    ZaakArgumenten zaakArgumenten = mijnZaken(getServiceContainer());
    return getServiceContainer().getZakenService().getMinimaleZaken(zaakArgumenten).size();
  }

  private long getAantalNieuweZaken() {
    ZaakArgumenten zaakArgumenten = nieuweZaken(getServiceContainer());
    return getServiceContainer().getZakenService().getMinimaleZaken(zaakArgumenten).size();
  }

  private long getAantalMetAttribuut(ZaakAttribuutType type) {
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten();
    zaakArgumenten.addAttributen(type.getCode());
    zaakArgumenten.getNegeerStatussen().addAll(ZaakStatusType.getMetEindStatus());
    return getServiceContainer().getZakenService().getMinimaleZaken(zaakArgumenten).size();
  }

  private long getAantalZaken(ZaakItem zaakItem, ZakenAantalViewHandler zaakAantallen) {
    ZakenregisterService zakenregister = getServiceContainer().getZakenregisterService();
    return zaakAantallen.getAantal(zakenregister.getZaakArgumenten(zaakItem, zakenregister.getNegeerStatussen()));
  }

  private void addStatusItem(ZaakItem z) {
    addTreeItem(z, STATUS, false, true, SubModuleZaken.class);
  }

  private Item addTreeItem(Object itemId, boolean childrenAllowed, boolean initOpen,
      Class<? extends Component> componentClass) {

    Item item = addItem(itemId);
    setChildrenAllowed(itemId, childrenAllowed);
    String oms = astr(itemId);

    item.getItemProperty(OMSCHRIJVING).setValue(oms);
    item.getItemProperty(INITOPEN).setValue(initOpen);
    item.getItemProperty(LEAF_CLASS).setValue(componentClass);

    return item;
  }

  private Item addTreeItem(Object itemId, Object parentId, boolean childrenAllowed, boolean initOpen,
      Class<? extends Component> componentClass) {

    Item item = addTreeItem(itemId, childrenAllowed, initOpen, componentClass);
    setParent(itemId, parentId);
    return item;
  }

  private void addZaakItem(ZaakItem z, Item item, Object oms) {
    for (ZaakStatusType type : ZaakTypeStatussen.getAlle(z.getTypes())) {
      if (type.isCombiStatus()) {
        continue;
      }

      ZaakAantalItem zaakItem = new ZaakAantalItem();
      zaakItem.setStatus(type).setTypes(z.getTypes());
      addZaakStatusItem(zaakItem, z, false, false, SubModuleZaken.class);
    }

    item.getItemProperty(OMSCHRIJVING).setValue(oms);
  }

  private void addZaakItem(ZaakType itemId, Object parentId, boolean childrenAllowed, boolean initOpen,
      Class<? extends Component> componentClass, String oms) {

    addZaakItem(new ZaakType[]{ itemId }, parentId, childrenAllowed, initOpen, componentClass, oms);
  }

  private void addZaakItem(ZaakType[] itemId, Object parentId, boolean childrenAllowed, boolean initOpen,
      Class<? extends Component> componentClass, String oms) {

    ZaakAantalItem z = new ZaakAantalItem();
    z.setStatus(null).setTypes(itemId);
    Item item = addTreeItem(z, parentId, childrenAllowed, initOpen, componentClass);
    Object itemOms = item.getItemProperty(OMSCHRIJVING).getValue();
    addZaakItem(z, item, fil(oms) ? oms : itemOms);
  }

  private Item addZaakStatusItem(ZaakAantalItem z, Object parentId, boolean childrenAllowed, boolean initOpen,
      Class<? extends Component> componentClass) {

    Item item = addTreeItem(z, parentId, childrenAllowed, initOpen, componentClass);
    zaakItems.add(z);
    return item;
  }

  private long getAantalOmschrijving(Object item) {

    int count = 0;
    if (item instanceof ZaakAantalItem) {
      count += ((ZaakAantalItem) item).getAantal();
    }

    if (hasChildren(item)) {
      for (Object child : getChildren(item)) {
        if (child instanceof ZaakAantalItem) {
          count += getAantalOmschrijving(child);
        }
      }
    }

    return count;
  }

  private <T> List<T> getItems(Class<T> cls) {
    List<T> zitems = new ArrayList<>();
    for (Object id : getItemIds()) {
      if (id.getClass() == cls) {
        zitems.add((T) id);
      }
    }

    return zitems;
  }

  /**
   * Zet aantal in de omschrijving door alle aantallen van de subNodes bij elkaar op te tellen
   */
  private void setAantalOmschrijving(Object itemId) {

    Item item = getItem(itemId);
    long aantal = getAantalOmschrijving(itemId);

    if (aantal > 0) {
      item.getItemProperty(AANTAL).setValue(astr(aantal));
    }

    if (hasChildren(itemId)) {
      for (Object child : getChildren(itemId)) {
        setAantalOmschrijving(child);
      }
    }
  }
}
