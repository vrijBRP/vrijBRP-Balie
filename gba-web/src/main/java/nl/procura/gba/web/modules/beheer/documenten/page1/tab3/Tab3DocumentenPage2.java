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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab3;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab3.Tab3DocumentenPage2Bean.*;

import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;

public class Tab3DocumentenPage2 extends DocumentenTabPage {

  private DocumentAfnemer               documentAfnemer = null;
  private final Tab3DocumentenPage2Form form1;
  private final Tab3DocumentenPage2Form form2;

  public Tab3DocumentenPage2(DocumentAfnemer documentAfnemer) {

    super("Toevoegen / muteren afnemer");

    setDocumentAfnemer(documentAfnemer);

    addButton(buttonPrev, buttonNew, buttonSave);

    setMargin(true);

    form1 = new Tab3DocumentenPage2Form(documentAfnemer) {

      @Override
      protected void init() {
        setOrder(AFNEMER, TAV_AANHEF, TAV_VOORL, TAV_NAAM, ADRES, PC, PLAATS, EMAIL, GRONDSLAG);
      }

    };

    form2 = new Tab3DocumentenPage2Form(documentAfnemer) {

      @Override
      protected void init() {
        setCaption("Verstrekkingsbeperking");
        setOrder(VERSTREKBEP, TOEKENNEN);
      }

    };

    addComponent(form1);
    addComponent(form2);
  }

  public DocumentAfnemer getDocumentAfnemer() {
    return documentAfnemer;
  }

  public void setDocumentAfnemer(DocumentAfnemer documentAfnemer) {
    this.documentAfnemer = documentAfnemer;
  }

  @Override
  public void onNew() {

    form1.reset();

    setDocumentAfnemer(new DocumentAfnemer());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(new Tab3DocumentenPage1());
    getNavigation().removeOtherPages();
  }

  @Override
  public void onSave() {

    form1.commit();
    form2.commit();

    Tab3DocumentenPage2Bean b1 = form1.getBean();
    Tab3DocumentenPage2Bean b2 = form2.getBean();

    getDocumentAfnemer().setDocumentAfn(b1.getAfnemer());
    getDocumentAfnemer().setTerAttentieVanAanhef(b1.getTavAanhef());
    getDocumentAfnemer().setTavVoorl(b1.getTavVoorl());
    getDocumentAfnemer().setTavNaam(b1.getTavNaam());
    getDocumentAfnemer().setEmail(b1.getEmail());
    getDocumentAfnemer().setAdres(b1.getAdres());
    getDocumentAfnemer().setPostcode(b1.getPc());
    getDocumentAfnemer().setPlaats(b1.getPlaats());
    getDocumentAfnemer().setGrondslagType(b1.getGrondslag());
    getDocumentAfnemer().setVerstrekBep(b2.getVerstrekbep());
    getDocumentAfnemer().setToekenning(b2.getToekennen());

    getServices().getDocumentService().save(getDocumentAfnemer());

    successMessage("Documentafnemer is opgeslagen.");

  }
}
