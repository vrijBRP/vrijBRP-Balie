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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import static nl.procura.gba.common.ZaakStatusType.*;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.bs.common.pages.residentpage.ResidentPage.ResidentTable;
import nl.procura.gba.web.modules.persoonslijst.overig.header.LopendeZakenWindow;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

class FeedbackPage extends ButtonPageTemplate {

  private static final String NAAM = "naam";

  private final Relatie       relation;
  private final ResidentTable parentTable;
  private final Table         table;

  FeedbackPage(ResidentTable parentTable, Relatie relation) {

    addButton(buttonDel, 1F);
    addButton(buttonNext);
    addButton(buttonClose);

    buttonNext.setCaption("Toon zaken (F2)");

    setSpacing(true);

    this.parentTable = parentTable;
    this.relation = relation;
    table = new Table();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new Form(getRelatie()));

      addComponent(table);
    }

    super.event(event);
  }

  public Relatie getRelatie() {
    return relation;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<TerugmeldingAanvraag>(table) {

      @Override
      public void afterDelete() {
        parentTable.showResidents();
      }

      @Override
      public void deleteValue(TerugmeldingAanvraag aanvraag) {
        getServices().getTerugmeldingService().delete(aanvraag);
      }
    };
  }

  @Override
  public void onNextPage() {
    getParentWindow().addWindow(new LopendeZakenWindow(relation.getPl()));
    getWindow().closeWindow();
    super.onNextPage();
  }

  public final class Form extends GbaForm<Bean> {

    private Form(Relatie relatie) {

      setCaption("Terugmeldingen");
      setOrder(NAAM);
      setColumnWidths("80px", "");

      final Bean b = new Bean();
      b.setNaam(relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
      setBean(b);
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = FieldType.LABEL, caption = "Naam")
    private String naam = "";
  }

  class Table extends FeedbackTable {

    @Override
    public void setRecords() {

      final ZaakArgumenten z = new ZaakArgumenten(getRelatie().getPl(), INCOMPLEET, OPGENOMEN, INBEHANDELING);

      for (final Zaak zaak : getApplication().getServices().getTerugmeldingService().getMinimalZaken(z)) {

        final TerugmeldingAanvraag tmv = (TerugmeldingAanvraag) zaak;
        final Record r = addRecord(zaak);

        r.addValue(zaak.getDatumTijdInvoer());
        r.addValue(ZaakUtils.getStatus(zaak.getStatus()));
        r.addValue(tmv.getTerugmelding());

      }
    }
  }
}
