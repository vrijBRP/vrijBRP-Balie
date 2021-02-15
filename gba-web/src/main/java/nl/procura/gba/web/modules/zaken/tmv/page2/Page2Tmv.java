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

package nl.procura.gba.web.modules.zaken.tmv.page2;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;
import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvOpslagLayout;
import nl.procura.gba.web.modules.zaken.tmv.objects.TmvRecord;
import nl.procura.gba.web.modules.zaken.tmv.page3.Page3Tmv;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingDetail;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class Page2Tmv extends PlPage {

  private TmvOpslagLayout tmvLayout = null;

  public Page2Tmv(List<TmvRecord> list) {

    super("Terugmelding");

    addButton(buttonPrev);
    addButton(buttonSave);
    tmvLayout = new TmvOpslagLayout(list);

    addExpandComponent(tmvLayout);
  }

  @Override
  public void onSave() {

    Cat1PersoonExt persoon = getServices().getPersonenWsService().getHuidige().getPersoon();

    // Terugmelding
    TerugmeldingAanvraag tmv = (TerugmeldingAanvraag) getServices().getTerugmeldingService().getNewZaak();
    tmv.setAnummer(new AnrFieldValue(persoon.getAnr().getVal()));
    tmv.setBurgerServiceNummer(new BsnFieldValue(persoon.getBsn().getVal()));
    tmv.setDatumTijdInvoer(new DateTime());
    tmv.setIngevoerdDoor(new UsrFieldValue(getApplication().getServices().getGebruiker()));
    tmv.setTerugmelding(astr(tmvLayout.getToelichting().getValue()));

    // Terugmelding details
    for (TmvRecord record : tmvLayout.getTmvRecords()) {

      TerugmeldingDetail detail = new TerugmeldingDetail();

      detail.setCCat(record.getCat());
      detail.setCElem(record.getElem());
      detail.setVolgnr(record.getSet());

      detail.setValNieuw(astr(record.getNieuweWaarde()));
      detail.setValOrigineel(record.getHuidigeWaarde().getVal());

      tmv.getDetails().add(detail);
    }

    getServices().getTerugmeldingService().save(tmv);

    getApplication().getProcess().endProcess();

    getNavigation().removeOtherPages();
    getNavigation().goToPage(Page3Tmv.class);

    successMessage("Terugmelding is opgeslagen.");

    super.onNextPage();
  }
}
