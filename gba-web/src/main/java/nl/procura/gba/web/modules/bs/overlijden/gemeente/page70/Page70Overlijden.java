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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.page70;

import static nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging.MEER_DAN_6_WERKDAGEN;
import static nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging.MINDER_DAN_36_UUR;
import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.gemeente.OverlijdenGemeenteService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page70Overlijden extends BsPrintPage<DossierOverlijdenGemeente> {

  private static final String BEGRAVEN       = "begraven";
  private static final String ARTIKEL_301    = "301";
  private static final String KIND           = "kind";
  private static final String UITSTEL        = "uitstel";
  private static final String LAISSEZ        = "laissez";
  private static final String INTERNATIONAAL = "internationaal";

  public Page70Overlijden() {
    super("Overlijden - Afdrukken", "Afdrukken documenten overlijdenaangifte");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNext.setCaption("Proces voltooien (F2)");

      setModel(getZaakDossier());
    }

    super.event(event);
  }

  public boolean isMinderjarig(DossierPersoon persoon) {
    return (persoon.isVolledig() && (aval(persoon.getGeboorte().getLeeftijd()) < 18));
  }

  @Override
  public void onNextPage() {

    OverlijdenGemeenteService overlijden = getApplication().getServices().getOverlijdenGemeenteService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    overlijden.save(getDossier());

    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {

    // Als persoon buiten gemeente woont
    // dan altijd kennisgeving selecteren.

    String doc = document.getDocument().toLowerCase();

    // Als aangiftetermijn = later dan 6e werkdag
    TermijnLijkbezorging termijn = getZaakDossier().getTermijnLijkbezorging();

    if (isDoc(doc, UITSTEL, BEGRAVEN)) {
      return (termijn == MINDER_DAN_36_UUR || termijn == MEER_DAN_6_WERKDAGEN);
    }

    // Als er minderjarige kinderen zijn dan een 301 kennisgeving selecteren
    if (isDoc(doc, ARTIKEL_301, KIND)) {
      return isMinderjarigKinderen();
    }

    // Als er er sprake is van 'buiten benelux' dan internationaal uittreksel selecteren
    if (isDoc(doc, INTERNATIONAAL)) {
      return getZaakDossier().isBuitenBenelux();
    }

    // Als buiten benelux dan Laissez-passer selecteren
    if (isDoc(doc, LAISSEZ)) {
      return getZaakDossier().isBuitenBenelux();
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
    return new DocumentType[]{ DocumentType.OVERLIJDEN };
  }

  private boolean isMinderjarigKinderen() {
    for (DossierPersoon kind : getZaakDossier().getKinderen()) {
      if (isMinderjarig(kind)) {
        return true;
      }
    }
    return false;
  }
}
