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

package nl.procura.gba.web.modules.persoonslijst.overig.header;

import static nl.procura.gba.common.ZaakStatusType.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.vaadin.ui.Alignment;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

class Page1LopendeZaken extends ButtonPageTemplate {

  private final BasePLExt persoonslijst;
  private GbaTable        table = null;

  public Page1LopendeZaken(BasePLExt persoonslijst) {
    this.persoonslijst = persoonslijst;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void setColumns() {

          addColumn("Nr", 30);
          addColumn("Datum ingang", 130);
          addColumn("Zaaktype");
          addColumn("Status", 120).setUseHTML(true);
          addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

          super.setColumns();
        }

        @Override
        public void setRecords() {

          try {
            List<Zaak> aanvragen = getAanvragen();
            int i = aanvragen.size();

            for (Zaak z : aanvragen) {
              Record r = addRecord(z);

              r.addValue(i);
              r.addValue(z.getDatumIngang());
              r.addValue(z.getType());
              r.addValue(ZaakUtils.getStatus(z.getStatus()));

              i--;
            }
          } catch (Exception e) {
            getApplication().handleException(e);
          }
        }
      };

      // Button om naar de persoonslijst te gaan
      buttonNext.setCaption("Naar persoonslijst (F2)");
      buttonNext.addListener(this);
      buttonClose.addListener(this);

      // Add infolayout
      Naam n = persoonslijst.getPersoon().getNaam();
      String naam = n.getNaamNaamgebruikEersteVoornaam();
      InfoLayout info = new InfoLayout("De lopende zaken van " + naam, "");

      HLayout hLayout = new HLayout(info, buttonNext, buttonClose).widthFull();
      hLayout.setComponentAlignment(buttonNext, Alignment.MIDDLE_RIGHT);
      hLayout.setComponentAlignment(buttonClose, Alignment.MIDDLE_RIGHT);

      hLayout.setStyleName("infolayout");
      hLayout.setExpandRatio(info, 1f);
      addComponent(hLayout);

      // Add table
      addComponent(table);
    }

    super.event(event);
  }

  public List<Zaak> getAanvragen() {

    ZakenService db = getApplication().getServices().getZakenService();
    List<Zaak> zaken = new ArrayList<>();
    zaken.addAll(db.getMinimaleZaken(new ZaakArgumenten(persoonslijst, INCOMPLEET, OPGENOMEN, INBEHANDELING)));

    zaken.sort(new ZaakDatumIngangComparator());

    return zaken;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onNextPage() {
    getApplication().goToPl(getParentWindow(), "", persoonslijst.getDatasource(), persoonslijst);
    getWindow().closeWindow();
    super.onNextPage();
  }

  public class ZaakDatumIngangComparator implements Comparator<Zaak> {

    @Override
    public int compare(Zaak p1, Zaak p2) {

      long datum1 = p1.getDatumIngang().getLongDate();
      long datum2 = p2.getDatumIngang().getLongDate();

      return new CompareToBuilder().append(datum2, datum1).build();
    }
  }
}
