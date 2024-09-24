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

package nl.procura.gba.web.modules.bs.geboorte.page95;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.modules.bs.geboorte.page90.Page90Geboorte;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page95Geboorte<T extends DossierGeboorte> extends BsPrintPage<T> {

  public Page95Geboorte() {
    this("Geboorte - afdrukken", "Afdrukken documenten geboorteaangifte");
  }

  public Page95Geboorte(String title, Object model) {
    super(title, model);
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
  public void onNextPage() {

    GeboorteService geboortes = getApplication().getServices().getGeboorteService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    // Zet de status op de initiele status
    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    geboortes.save(getDossier());

    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {

    getNavigation().goBackToPage(Page90Geboorte.class);

    super.onPreviousPage();
  }

  @Override
  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {

    DossierGeboorte geboorte = (DossierGeboorte) getModel();
    String doc = document.getDocument().toLowerCase();

    // Als er sprake is van tardieve dan het document
    // met tardieve standaard selecteren.
    if (doc.toLowerCase().contains("tardieve") && geboorte.isTardieveAangifte()) {
      return true;
    }

    if (document.getDocumentKenmerken().get(DocumentKenmerkType.GEBOORTE_1) != null) {
      if (isGezagMoederErkenningOpJaGezet(getZaakDossier())) {
        return true;
      }
    }

    if (document.getDocumentKenmerken().get(DocumentKenmerkType.GEBOORTE_2) != null) {
      if (isErkenningIn2022Gedaan(getZaakDossier())) {
        return true;
      }
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

    List<DocumentType> types = new ArrayList<>();
    types.add(DocumentType.GEBOORTE);

    if (getZaakDossier().isSprakeLatereVermeldingErkenning()) {
      types.add(DocumentType.LATERE_VERMELDING_ERK);

    } else if (getZaakDossier().isSprakeLatereVermeldingNaamskeuze()) {
      types.add(DocumentType.LATERE_VERMELDING_NK);
    }

    return types.toArray(new DocumentType[0]);
  }

  private boolean isGezagMoederErkenningOpJaGezet(DossierGeboorte geboorte) {
    if (geboorte.getVragen().heeftErkenningBuitenProweb()) {
      if (geboorte.getErkenningBuitenProweb().isVerklaringGezag()) {
        return true;
      }
    }
    if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
      return geboorte.getErkenningVoorGeboorte().isVerklaringGezag();
    }
    return false;
  }

  private boolean isErkenningIn2022Gedaan(DossierGeboorte geboorte) {
    if (geboorte.getVragen().heeftErkenningBuitenProweb()) {
      return isIn2022(geboorte.getErkenningBuitenProweb().getDatumErkenning());
    }

    if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
      return isIn2022(geboorte.getErkenningVoorGeboorte().getDossier().getDatumTijdInvoer());
    }
    return false;
  }

  private boolean isIn2022(DateTime date) {
    return Instant.ofEpochMilli(date.getDate().getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDate().getYear() == 2022;
  }
}
