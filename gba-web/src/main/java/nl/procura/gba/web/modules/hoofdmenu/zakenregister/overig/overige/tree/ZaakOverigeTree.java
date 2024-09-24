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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.overige.tree;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;
import static nl.procura.commons.core.exceptions.ProExceptionType.PROGRAMMING;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page1.Page1ZaakAttribuut;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page1.Page1ZaakBehandelaar;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page1.Page1ZaakIdentificatie;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page1.Page1ZaakRelatie;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.tree.ZaakTableContainer;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class ZaakOverigeTree extends Tree {

  private final Zaak                   zaak;
  private final Page1ZaakIdentificatie pageIdentificatie;
  private final Page1ZaakRelatie       pageRelatie;
  private final Page1ZaakAttribuut     pageAttribuut;
  private final Page1ZaakBehandelaar   pageBehandelaar;
  private final ZaakOverigeContainer   container;

  public ZaakOverigeTree(Zaak zaak) {

    this.zaak = zaak;

    setImmediate(true);
    setSelectable(true);
    setWidth("170px");

    setItemCaptionPropertyId(ZaakTableContainer.OMSCHRIJVING);
    setStyleName(GbaWebTheme.TABLE_BORDERLESS);
    addStyleName("zaken_overige_tree");

    pageIdentificatie = getPageIdentificatie();
    pageRelatie = getPageRelatie();
    pageAttribuut = getPageAttribuut();
    pageBehandelaar = getPageBehandelaar();
    container = new ZaakOverigeContainer();

    setContainerDataSource(container);
  }

  @Override
  public void attach() {

    onReload();

    super.attach();
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public Page1ZaakAttribuut getPageAttribuut() {

    return new Page1ZaakAttribuut(zaak) {

      @Override
      public void onReload() {
        ZaakOverigeTree.this.onReload();
      }
    };
  }

  public Page1ZaakBehandelaar getPageBehandelaar() {

    return new Page1ZaakBehandelaar(zaak) {

      @Override
      public void onReload() {
        ZaakOverigeTree.this.onReload();
      }
    };
  }

  public Page1ZaakIdentificatie getPageIdentificatie() {

    return new Page1ZaakIdentificatie(zaak) {

      @Override
      public void onReload() {
        ZaakOverigeTree.this.onReload();
      }
    };
  }

  public Page1ZaakRelatie getPageRelatie() {

    return new Page1ZaakRelatie(zaak) {

      @Override
      public void onReload() {
        ZaakOverigeTree.this.onReload();
      }
    };
  }

  public void onReload() {
    getApplication().getServices().getZakenService().getService(zaak).setZaakHistory(zaak);
    update(ZaakOverigeContainer.IDENTIFICATIES,
        "Identificaties (" + zaak.getZaakHistorie().getIdentificaties().size() + ")");
    update(ZaakOverigeContainer.ATTRIBUTEN,
        "Attributen (" + zaak.getZaakHistorie().getAttribuutHistorie().size() + ")");
    update(ZaakOverigeContainer.RELATIES, "Gekoppelde zaken (" + zaak.getZaakHistorie().getRelaties().size() + ")");
  }

  public abstract void selecteerModule(ZakenModuleTemplate module);

  @Override
  @SuppressWarnings("unchecked")
  public void setContainerDataSource(Container newDataSource) {

    super.setContainerDataSource(newDataSource);

    addListener((ValueChangeListener) event -> {

      Property property = getContainerProperty(getValue(), ZaakOverigeContainer.ID);

      if (property != null) {

        String omschrijving = (String) property.getValue();

        try {

          switch (omschrijving) {
            case ZaakOverigeContainer.IDENTIFICATIES:
              selecteerModule(new Module(pageIdentificatie));
              break;

            case ZaakOverigeContainer.ATTRIBUTEN:
              selecteerModule(new Module(pageAttribuut));
              break;

            case ZaakOverigeContainer.RELATIES:
              selecteerModule(new Module(pageRelatie));
              break;

            case ZaakOverigeContainer.BEHANDELAARS:
              selecteerModule(new Module(pageBehandelaar));
              break;

            default:
              throw new ProException(PROGRAMMING, ERROR, "Onbekende omschrijving: " + omschrijving);
          }

        } catch (Exception e) {
          throw new ProException(DATABASE, ERROR, "Kan component niet laden.", e);
        }
      }
    });

    select(ZaakOverigeContainer.IDENTIFICATIES);
  }

  private void update(String itemId, String value) {
    container.getItem(itemId).getItemProperty(ZaakOverigeContainer.OMSCHRIJVING).setValue(value);
  }

  public class Module extends ZakenModuleTemplate {

    private final NormalPageTemplate page;

    public Module(NormalPageTemplate page) {
      this.page = page;
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(page);
      }
    }
  }
}
