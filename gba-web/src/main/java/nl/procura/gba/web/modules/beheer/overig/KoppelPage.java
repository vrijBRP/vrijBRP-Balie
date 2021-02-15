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

package nl.procura.gba.web.modules.beheer.overig;

import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;
import static nl.procura.gba.web.services.beheer.KoppelActie.ONTKOPPEL;

import java.util.Arrays;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.page.buttons.ActieButton;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.gba.web.services.beheer.locatie.KoppelbaarAanLocatie;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.gba.web.services.zaken.documenten.*;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerk;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;

public abstract class KoppelPage extends NormalPageTemplate {

  private final Button buttonKoppelen         = new ActieButton(ProfielActieType.UPDATE, "Koppelen (F2)");
  private final Button buttonOntkoppelen      = new ActieButton(ProfielActieType.UPDATE, "Ontkoppelen (F8)");
  private final Button buttonAllesKoppelen    = new ActieButton(ProfielActieType.UPDATE, "Alles koppelen");
  private final Button buttonAllesOntkoppelen = new ActieButton(ProfielActieType.UPDATE, "Alles ontkoppelen");

  public KoppelPage(String caption) {

    super(caption);

    addButton(buttonPrev);
    addButton(buttonKoppelen);
    addButton(buttonOntkoppelen);
    addButton(buttonAllesKoppelen);
    addButton(buttonAllesOntkoppelen);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonKoppelen) || (keyCode == KeyCode.F2)) {
      koppelActie(KOPPEL);
    } else if ((button == buttonOntkoppelen) || (keyCode == KeyCode.F8)) {
      koppelActie(ONTKOPPEL);
    } else if ((button == buttonAllesKoppelen)) {
      allesKoppelActie(KOPPEL);
    } else if ((button == buttonAllesOntkoppelen)) {
      allesKoppelActie(ONTKOPPEL);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  protected abstract void allesKoppelActie(KoppelActie koppelActie);

  protected abstract void koppelActie(KoppelActie koppelActie);

  protected <K extends KoppelbaarAanDocument> void koppelActieDocumenten(KoppelActie koppelActie, List<K> koppelList,
      List<DocumentRecord> docList) {
    getServices().getDocumentService().koppelActie(koppelList, docList, koppelActie);
  }

  protected <K extends KoppelbaarAanDocumentKenmerk> void koppelActieDocumentKenmerk(KoppelActie koppelActie,
      List<K> koppelList,
      List<DocumentKenmerk> stempels) {
    getServices().getKenmerkService().koppelActie(koppelList, stempels, koppelActie);
  }

  protected <K extends KoppelbaarAanDocument> void koppelActieDocumentRecords(KoppelActie koppelActie,
      List<K> koppelList,
      List<DocumentRecord> DocumentRecorden) {
    getServices().getDocumentService().koppelActie(koppelList, DocumentRecorden, koppelActie);
  }

  protected <K extends KoppelbaarAanDocumentStempel> void koppelActieDocumentStempel(KoppelActie koppelActie,
      List<K> koppelList,
      List<DocumentStempel> stempels) {
    getServices().getStempelService().koppelActie(koppelList, stempels, koppelActie);
  }

  protected <K extends KoppelbaarAanGebruiker> void koppelActieGebruikers(KoppelActie koppelActie, List<K> koppelList,
      List<Gebruiker> usrList) {
    getServices().getGebruikerService().koppelActie(koppelList, usrList, koppelActie);
  }

  protected <K extends KoppelbaarAanLocatie> void koppelActieLocaties(KoppelActie koppelActie, List<K> koppelList,
      List<Locatie> locList) {
    getServices().getLocatieService().koppelActie(koppelList, locList, koppelActie);
  }

  protected <K extends KoppelbaarAanPrintOptie> void koppelActiePrintOpties(KoppelActie koppelActie,
      List<K> koppelList,
      List<PrintOptie> printOpties) {
    getServices().getPrintOptieService().koppelActie(koppelList, printOpties, koppelActie);
  }

  protected <K extends KoppelbaarAanProfiel> void koppelActieProfielen(KoppelActie koppelActie, List<K> koppelList,
      List<Profiel> profielen) {
    getServices().getProfielService().koppelActie(koppelList, profielen, koppelActie);
  }

  protected <K extends KoppelbaarAanDocument> void koppelOntkoppelDocumentRecorden(KoppelActie koppelActie,
      List<K> koppelList,
      DocumentRecord DocumentRecord) {
    koppelActieDocumentRecords(koppelActie, koppelList, Arrays.asList(DocumentRecord));
  }

  protected <K extends KoppelbaarAanProfiel> void koppelOntkoppelProfielen(KoppelActie koppelActie,
      List<K> koppelList, Profiel profiel) {
    koppelActieProfielen(koppelActie, koppelList, Arrays.asList(profiel));
  }
}
