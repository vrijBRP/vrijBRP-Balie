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

package nl.procura.gbaws.web.vaadin.module.requests.page2;

import static ch.lambdaj.Lambda.join;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Label;

import nl.procura.gbaws.db.wrappers.RequestWrapper;
import nl.procura.gbaws.web.vaadin.layouts.DefaultPageLayout;
import nl.procura.gbaws.web.vaadin.module.requests.page2.RequestContent.Chapter;
import nl.procura.gbaws.web.vaadin.module.requests.page2.RequestContent.Item;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page2Request extends DefaultPageLayout {

  private final RequestWrapper request;

  public Page2Request(RequestWrapper request) {

    super("Bericht", true);
    this.request = request;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      RequestContent rc = new RequestContent(request.getInhoud());

      for (Chapter chapter : rc.getChapters()) {

        Fieldset fs = new Fieldset(chapter.getLabel());

        addComponent(fs);

        TableLayout tl = new TableLayout();

        List<String> empty = new ArrayList<>();

        for (Item item : chapter.getItems()) {

          String value = trim(item.getValue().toString());

          if (fil(value)) {

            tl.addLabel(convertLabel(item.getLabel()));
            tl.addData(new Label(convertValue(value)));
            addComponent(tl);
          } else {

            empty.add(item.getLabel());
          }
        }

        if (empty.size() > 0) {

          tl.addLabel("Niet gevuld");
          tl.addData(new Label(join(empty)));
          addComponent(tl);
        }
      }
    }

    super.event(event);
  }

  private String convertLabel(String label) {
    return label;
  }

  private String convertValue(String value) {

    if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
      return "Ja";
    } else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
      return "Nee";
    }

    return value;
  }
}
