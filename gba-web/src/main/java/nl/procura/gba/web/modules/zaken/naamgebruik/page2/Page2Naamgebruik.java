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

package nl.procura.gba.web.modules.zaken.naamgebruik.page2;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.Date;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.tables.GBATableValues;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.containers.NaamgebruikContainer;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.naamgebruik.page3.Page3Naamgebruik;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikAanvraag;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuwe aanvraag
 */

public class Page2Naamgebruik extends ZakenPage {

  private static final long ONGEHUWD = 1;

  private Page2NaamgebruikForm      form = null;
  private final NaamgebruikAanvraag aanvraag;

  public Page2Naamgebruik(NaamgebruikAanvraag aanvraag) {
    super("Naamgebruik: nieuw");
    this.aanvraag = aanvraag;
    addButton(buttonPrev);
    addButton(buttonNext);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      if (getPl().getPersoon().getBurgerlijkeStand().getStatus().getType().getCode() <= ONGEHUWD) {
        setInfo(setClass("red", "Bij deze persoon is geen huwelijk/GPS opgenomen."));
      }

      Page2NaamgebruikBean bean = new Page2NaamgebruikBean();
      bean.setDatumWijz(new Date());
      bean.setHuidigNaamgebruik(getHuidigeNaamgebruik());

      form = new Page2NaamgebruikForm(bean);
      form.getNieuwNaamgebruik().setDataSource(new Page2NaamgebruikContainer(getHuidigeNaamgebruik()));
      addComponent(form);
    }

    super.event(event);
  }

  public FieldValue getHuidigeNaamgebruik() {
    String ng = getPl().getPersoon().getNaam().getNaamgebruik().getValue().getVal();
    return new NaamgebruikContainer().get(ng);
  }

  @Override
  public void onNextPage() {

    form.commit();

    FieldValue hn = form.getBean().getHuidigNaamgebruik();
    FieldValue nn = form.getBean().getNieuwNaamgebruik();

    if (hn.equals(nn)) {
      throw new ProException(ENTRY, WARNING,
          "Selecteer een ander type naamgebruik. De persoon heeft al dat type naamgebruik.");
    }

    Page2NaamgebruikBean b = form.getBean();
    AnrFieldValue anr = new AnrFieldValue(getPl().getPersoon().getAnr().getVal());
    BsnFieldValue bsn = new BsnFieldValue(getPl().getPersoon().getBsn().getVal());

    aanvraag.setAnummer(anr);
    aanvraag.setBurgerServiceNummer(bsn);
    aanvraag.setNaamgebruikType(b.getNieuwNaamgebruik());
    aanvraag.setDatumTijdInvoer(new DateTime());
    aanvraag.setDatumIngang(new DateTime(b.getDatumWijz()));
    aanvraag.setIngevoerdDoor(new UsrFieldValue(getApplication().getServices().getGebruiker()));

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(aanvraag);

    getNavigation().goToPage(new Page3Naamgebruik(aanvraag));
  }

  public class Page2NaamgebruikContainer extends IndexedContainer {

    private Page2NaamgebruikContainer(FieldValue fieldValue) {

      for (GBATableValues.Value w : GBATable.AAND_NAAMGEBRUIK.getValues().getValues()) {

        if (fieldValue.getValue().equals(w.getKey())) {
          continue;
        }

        addItem(new FieldValue(w.getKey(), w.getKey() + ": " + w.getValue()));
      }
    }
  }
}
