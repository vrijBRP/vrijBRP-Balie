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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page1;

import static java.util.Collections.singletonList;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElementAntwoord;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.*;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.personmutationsindex.WindowMutationsApproval;
import nl.procura.gba.web.modules.zaken.personmutationsindex.page2.Page2MutationsIndex;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Anummer;

import lombok.AllArgsConstructor;
import lombok.Data;

public class Page1MutationsIndex extends NormalPageTemplate {

  private final BasePLExt          pl;
  private Page1MutationsIndexForm  form  = null;
  private Page1MutationsIndexTable table = null;

  public Page1MutationsIndex(BasePLExt pl) {
    this.pl = pl;
    setSpacing(true);
    addButton(buttonSearch);
    addButton(buttonNext, 1f);
    buttonNext.setCaption("Goedkeuren (F2)");
    addButton(buttonClose);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      Page1MutationsIndexBean indexbean = new Page1MutationsIndexBean();
      indexbean.setPersoon(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));
      form = new Page1MutationsIndexForm(pl, indexbean);
      form.setBeanChangeListener(bean -> table.setMutations(findMutations(bean)));
      table = new Page1MutationsIndexTable() {

        @Override
        public void onDoubleClick(Record record) {
          MutationRestElement mutation = record.getObject(MutationRestElement.class);
          getNavigation().goToPage(new Page2MutationsIndex(mutation));
        }
      };

      addComponent(form);
      addExpandComponent(table);
      table.setMutations(findMutations(indexbean));

    } else if (event.isEvent(AfterReturn.class)) {
      getWindow().setWidth("1200px");
      getWindow().center();
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    if (table.isSelectedRecords()) {
      List<ApprovalElement> elements = new ArrayList<>();
      boolean currentPersonApproved = isCurrentPersonApproved();
      table.getSelectedValues(MutationRestElement.class)
          .forEach(mutation -> {
            MutationApproveResponseRestElement responseElement = approve(mutation);
            elements.add(new ApprovalElement(mutation, responseElement));
          });
      getParentWindow().addWindow(new WindowMutationsApproval(elements));
      if (currentPersonApproved) {
        getApplication().reloadCurrentPersonList();
      }
      table.init();
    } else {
      throw new ProException(WARNING, "Geen records geselecteerd");
    }
  }

  private MutationApproveResponseRestElement approve(MutationRestElement mutation) {
    String query = "procura.personen.probev.mutaties.goedkeuren";
    MutationApproveRequestRestElement vraag = new MutationApproveRequestRestElement();
    vraag.setAnummer(mutation.getAnr().getWaarde());
    BsmRestElementAntwoord antwoord = new BsmRestElementAntwoord();
    getApplication().getServices().getBsmService().bsmQuery(query, vraag, antwoord);
    MutationApproveResponseRestElement responseElement = new MutationApproveResponseRestElement();
    responseElement.setAntwoordElement(antwoord.getAntwoordElement());
    return responseElement;
  }

  private List<MutationRestElement> findMutations(Page1MutationsIndexBean bean) {
    String query = "procura.personen.probev.mutaties.zoeken";
    int max = 15;
    MutationSearchRequestRestElement vraag = new MutationSearchRequestRestElement().setMax(max);
    if (bean != null) {
      if (bean.getPersoon() != null) {
        vraag.setAnummer(singletonList(bean.getPersoon().getStringValue()));
      }
      if (bean.getAnr() != null) {
        vraag.setAnummer(singletonList(bean.getAnr().getStringValue()));
      }
      if (bean.getRelaties() != null) {
        vraag.setRelatives(bean.getRelaties());
      }
      if (bean.getStatus() != null) {
        vraag.setStatus(bean.getStatus().getCode());
      }
      if (bean.getCat() != null) {
        vraag.setCat(bean.getCat().getCode());
      }
      if (bean.getDatumMutatie() != null) {
        vraag.setDateMutationFrom(astr(bean.getDatumMutatie().getdFrom()));
        vraag.setDateMutationUntil(astr(bean.getDatumMutatie().getdTo()));
      }
    }

    BsmRestElementAntwoord antwoord = new BsmRestElementAntwoord();
    getApplication().getServices().getBsmService().bsmQuery(query, vraag, antwoord);
    MutationSearchResponseRestElement antwoordElem = new MutationSearchResponseRestElement();
    antwoordElem.setAntwoordElement(antwoord.getAntwoordElement());
    return antwoordElem.getMutations();
  }

  private boolean isCurrentPersonApproved() {
    return table.getSelectedValues(MutationRestElement.class).stream()
        .anyMatch(mutation -> new Anummer(mutation.getAnr().getWaarde())
            .eq(pl.getPersoon().getAnr().getVal()));
  }

  public void reload() {
    table.init();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Data
  @AllArgsConstructor
  public static class ApprovalElement {

    private MutationRestElement                mutationRestElement;
    private MutationApproveResponseRestElement responseRestElement;
  }
}
