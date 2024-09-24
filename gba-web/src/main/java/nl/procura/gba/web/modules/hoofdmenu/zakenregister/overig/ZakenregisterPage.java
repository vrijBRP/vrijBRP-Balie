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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.common.ZaakStatusType.WACHTKAMER;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.klapper.windows.KlapperInzageWindow;
import nl.procura.gba.web.modules.hoofdmenu.klapper.windows.KlapperOverzichtWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenTab;
import nl.procura.gba.web.modules.zaken.common.ZaakAanpassenButton;
import nl.procura.gba.web.modules.zaken.common.ZaakRequestInboxButton;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ZakenregisterPage<T extends Zaak> extends NormalPageTemplate {

  protected final ZaakAanpassenButton    buttonAanpassen = new ZaakAanpassenButton();
  protected final ZaakRequestInboxButton buttonVerzoek   = new ZaakRequestInboxButton();
  protected final Button                 buttonDoc       = new Button("Document afdrukken");
  protected final ZaakPersonenButton     buttonPersonen  = new ZaakPersonenButton();
  protected final Button                 buttonFiat      = new Button("Fiatteren");
  protected final Button                 buttonVerwerken = new Button("Nu verwerken");
  protected final Button                 buttonKlappers  = new Button("Klappers");
  private Zaak                           zaak;

  public ZakenregisterPage(T zaak, String title) {
    super(title);
    this.zaak = zaak;
    setMargin(true);
    buttonVerzoek.setZaak(zaak);
  }

  @SuppressWarnings("unchecked")
  public T getZaak() {
    return (T) zaak;
  }

  public void setZaak(T zaak) {
    this.zaak = zaak;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAanpassen) {
      buttonAanpassen.onClick(zaak, this::goToZaak);

    } else if (button == buttonVerzoek) {
      buttonVerzoek.onClick();

    } else if (button == buttonPersonen) {
      getWindow().addWindow(new ZaakPersonenMultiWindow(getZoekpersoonTypes()) {

        @Override
        public void onSelectPersoon(ZaakPersoonType type) {
          goToPersoon(type);
        }
      });
    } else if (button == buttonDoc) {
      goToDocument();

    } else if (button == buttonFiat) {
      ZaakStatusUpdater.checkUpdatenStatusMogelijk(getApplication(), zaak);

      if (zaak.getStatus() != WACHTKAMER) {
        throw new ProException(ProExceptionSeverity.INFO,
            "Alleen zaken met status 'Wachtkamer' kunnen worden gefiatteerd");
      }

      doFiat();
    } else if (button == buttonVerwerken) {
      if (zaak.getStatus() != OPGENOMEN) {
        throw new ProException(ProExceptionSeverity.INFO,
            "Alleen zaken met status 'Opgenomen' kunnen worden verwerkt");
      }

      doVerwerken();
    } else if (button == buttonKlappers) {
      goToKlappers();
    }

    super.handleEvent(button, keyCode);
  }

  /**
   * Ga terug naar de vorige pagina. Tenzij er geen pagina's zijn.
   */
  @Override
  public void onPreviousPage() {
    if (buttonPrev.getParent() != null) {
      if (getNavigation().getPreviousPage() != null) {
        getNavigation().goBackToPreviousPage();
      } else {
        getNavigation().goToPage(Page4ZakenTab.class);
        getNavigation().removeOtherPages();
      }
    }
  }

  protected void doFiat() {
  }

  protected void doVerwerken() {
  }

  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return null;
  }

  protected void goToDocument() {
  }

  protected void goToPersoon(String fragment, FieldValue... fieldValues) {
    for (FieldValue fieldValue : fieldValues) {
      if (fieldValue != null && pos(fieldValue.getValue())) {
        getApplication().goToPl(getWindow(), fragment, PLEDatasource.STANDAARD, fieldValue.getStringValue());
        return;
      }
    }

    throw new ProException(INFO, "Geen a-nummer of burgerservicenummer gevonden");
  }

  @SuppressWarnings("unused")
  protected void goToPersoon(ZaakPersoonType type) {
  }

  protected void goToZaak() {
  }

  private void goToKlappers() {

    if (getZaak() instanceof Dossier) {

      Dossier dossier = to(getZaak(), Dossier.class);
      List<DossierAkte> klappers = getServices().getAkteService().getAktes(dossier);

      if (klappers.size() == 1) { // Direct naar inzage scherm
        getParentWindow().addWindow(new KlapperInzageWindow(klappers.get(0)));
      } else {
        getParentWindow().addWindow(new KlapperOverzichtWindow(klappers));
      }
    }
  }
}
