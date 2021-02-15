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

package nl.procura.gba.web.modules.bs.ontbinding.page60;

import static nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis.HUWELIJK;
import static nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis.RECHTERLIJKE_UITSPRAAK;
import static nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis.WEDERZIJDS_GOEDVINDEN;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Button;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.ontbinding.OntbindingService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Ontbinding/einde GPS / huwelijk in gemeente
 */
public class Page60Ontbinding extends BsPrintPage<DossierOntbinding> {

  private static final String KENNISGEVING  = "kennisgeving";
  private static final String LV            = "lv";
  private static final String BETREFFENDE   = "betreffende";
  private static final String ALMELO        = "almelo";
  private static final String ECHTSCHEIDING = "echtscheiding";
  private static final String BEEINDIGING   = "indiging";
  private static final String ONTBINDING    = "ontbinding";
  private static final String GPS           = "gps";
  private static final String VERZOEK       = "verzoek";
  private static final String NAAMGEBRUIK   = "naamgebruik";
  private static final String P1            = "1";
  private static final String P2            = "2";
  private static final String RNI           = "rni";
  private static final String INSCHRIJVING  = "inschrijving";
  private static final String ADVOCAAT      = "advocaat";

  public Page60Ontbinding() {
    super("Ontbinding/einde huwelijk/GPS in gemeente - afdrukken", "Afdrukken documenten");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNext.setCaption("Proces voltooien (F2)");

      setModel(getZaakDossier());
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    getApplication().getServices().getOntbindingService().save(getDossier());

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    OntbindingService huwelijken = getApplication().getServices().getOntbindingService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    huwelijken.save(getDossier());

    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {

    String doc = document.getDocument().toLowerCase();
    if (HUWELIJK.equals(getZaakDossier().getSoortVerbintenis())) {
      if (isDoc(doc, LV, BETREFFENDE, ECHTSCHEIDING) || isDoc(doc, KENNISGEVING, LV, ALMELO, ECHTSCHEIDING)) {
        return true;
      }
    }

    if (RECHTERLIJKE_UITSPRAAK.equals(getZaakDossier().getWijzeBeeindigingVerbintenis())) {
      if (isDoc(doc, LV, BETREFFENDE, ONTBINDING, GPS) || isDoc(doc, KENNISGEVING, LV, ALMELO, ONTBINDING, GPS)) {
        return true;
      }
    }

    if (WEDERZIJDS_GOEDVINDEN.equals(getZaakDossier().getWijzeBeeindigingVerbintenis())) {
      if (isDoc(doc, LV, BETREFFENDE, BEEINDIGING, GPS) || isDoc(doc, KENNISGEVING, LV, ALMELO, BEEINDIGING,
          GPS)) {
        return true;
      }
    }

    if (fil(getZaakDossier().getNaamGebruikPartner1())) {
      if (isDoc(doc, VERZOEK, NAAMGEBRUIK, P1)) {
        return true;
      }
    }

    if (fil(getZaakDossier().getNaamGebruikPartner2())) {
      if (isDoc(doc, VERZOEK, NAAMGEBRUIK, P2)) {
        return true;
      }
    }

    if (getZaakDossier().getPartner1().isRNI() || getZaakDossier().getPartner2().isRNI()) {
      if (isDoc(doc, KENNISGEVING, RNI)) {
        return true;
      }
    }

    if (getZaakDossier().getAdvocatenkantoor().isVanToepassing() && isDoc(KENNISGEVING, INSCHRIJVING, ADVOCAAT)) {
      return true;
    }

    return super.onSelectDocument(document, isPreSelect);
  }

  @Override
  public void setButtons() {

    addButton(buttonPrev);
    addButton(getPrintButtons());
    addButton(buttonNext);

    super.setButtons();
  }

  @Override
  protected DocumentType[] getDocumentTypes() {
    return new DocumentType[]{ DocumentType.ONTBINDING_GEMEENTE };
  }
}
