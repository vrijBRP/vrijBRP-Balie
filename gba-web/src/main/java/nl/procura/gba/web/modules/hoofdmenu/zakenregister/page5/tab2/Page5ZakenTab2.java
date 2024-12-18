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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.tab2;

import static nl.procura.gba.web.services.zaken.rijbewijs.NrdServices.getNrdAanvragenByStatus;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.Page5ZakenTab;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagStatus;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.rdw.processen.p1914.f02.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1914.f02.NATPAANVRGEG;
import nl.procura.rdw.processen.p1914.f02.RYBAANVROVERZ;
import nl.procura.rdw.processen.p1914.f02.STATRYBKGEG;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class Page5ZakenTab2 extends Page5ZakenTab {

  private Page5ZakenTab2Form form  = null;
  private Table              table = null;

  public Page5ZakenTab2() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSearch);
      setInfo("Ter informatie", "Zoeken rijbewijsaanvragen met de gekozen actuele status.");

      form = new Page5ZakenTab2Form();
      table = new Table();

      getButtonLayout().add(new GbaIndexedTableFilterLayout(table));
      addComponent(form);
      addComponent(new Fieldset("Resultaten"));
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {

    form.commit();
    table.clear();

    Page5ZakenTab2Bean1 bean = form.getBean();
    if (bean.getStatus().getCode() >= RijbewijsStatusType.RIJBEWIJS_NIET_UITGEREIKT.getCode()) {
      String question = "Zoeken op deze status kan lang duren en veel resultaten opleveren, doorgaan?";
      ConfirmDialog confirmDialog = new ConfirmDialog("Weet u het zeker?", question, "530px",
          ProcuraTheme.ICOON_24.QUESTION) {

        @Override
        public void buttonYes() {
          close();
          doSearch(bean);
        }
      };

      getWindow().addWindow(confirmDialog);
    } else {
      doSearch(bean);
    }
  }

  private void doSearch(Page5ZakenTab2Bean1 bean) {

    RijbewijsService rijbewijsService = getServices().getRijbewijsService();
    List<RYBAANVROVERZ> overzichten = getNrdAanvragenByStatus(rijbewijsService, bean.getStatus(),
        aval(bean.getMax()), null, this::warningMessage);

    int index = 0;
    for (RYBAANVROVERZ overzicht : overzichten) {

      List<NATPAANVRGEG> natpaanvrgeg = overzicht.getNatpaanvrtab().getNatpaanvrgeg();
      if (overzicht.getNatpaanvrtab() != null && !natpaanvrgeg.isEmpty()) {
        RijbewijsStatusType rijbewijsStatusType = RijbewijsStatusType.get(
            overzicht.getAanvrrybzgeg().getStatcoderybk().longValue());

        List<Long> anrs = natpaanvrgeg.stream()
            .map(x -> x.getNatpersoongeg().getGbanrnatp().longValue())
            .collect(Collectors.toList());

        PLResultComposite result = bean.getAangever() ? getNaamByAnr(anrs, aval(bean.getMax())) : null;

        for (NATPAANVRGEG aanvraag : natpaanvrgeg) {

          AANVRRYBKGEG rijbewijs = aanvraag.getAanvrrybkgeg();
          STATRYBKGEG status = aanvraag.getStatrybkgeg();

          RijbewijsAanvraag zaak = getAanvraag(rijbewijs.getAanvrnrrybk());
          long anr = aanvraag.getNatpersoongeg().getGbanrnatp().longValue();
          long bsn = aanvraag.getNatpersoongeg().getBurgservicenr().longValue();

          Record record = table.addRecord(zaak != null ? zaak : aanvraag);
          record.addValue(++index);
          record.addValue(zaak == null ? "" : new TableImage(Icons.getIcon(Icons.ICON_OK)));
          record.addValue(rijbewijs.getAanvrnrrybk());
          record.addValue(new DateTime(along(status.getStatdatrybk()), along(status.getStattydrybk()),
              DateTime.TimeType.TIME_4_DIGITS));

          if (zaak == null) {
            record.addValue("Geen zaak gevonden.");
            record.addValue(getIdNummer(anr, bsn));
            record.addValue(getNaam(result, anr, bsn));
            record.addValue(ZaakUtils.getStatus(ZaakStatusType.ONBEKEND));
            record.addValue("");
          } else {
            record.addValue(zaak.getZaakId());
            record.addValue(getIdNummer(anr, bsn));
            record.addValue(getNaam(result, anr, bsn));
            record.addValue(ZaakUtils.getStatus(zaak.getStatus()));

            boolean isAddNr = updateRbwNR(zaak, status);
            boolean isAddStatus = addStatus(zaak, status, rijbewijsStatusType);

            if (isAddNr || isAddStatus) {
              rijbewijsService.save(zaak);
            }

            StringBuilder msg = new StringBuilder();
            msg.append("Status ").append(rijbewijsStatusType.getCode());

            if (isAddStatus) {
              msg.append(" nu toegevoegd");
            } else {
              msg.append(" reeds bekend");
            }

            if (isAddNr) {
              msg.append(", rijbewijsnr. bijgewerkt");
            }

            record.addValue(msg);
          }

        }

        table.reloadRecords();
      } else {
        infoMessage("Geen aanvragen gevonden");
      }
    }
  }

  private boolean addStatus(RijbewijsAanvraag zaak, STATRYBKGEG status, RijbewijsStatusType rijbewijsStatusType) {
    RijbewijsAanvraagStatus rijbewijsStatus = zaak.getStatussen().getStatus(rijbewijsStatusType);
    if (rijbewijsStatus == null) {
      zaak.getStatussen().addStatus(getNieuweStatus(rijbewijsStatusType, status));
      return true;
    }

    return false;
  }

  private RijbewijsAanvraagStatus getNieuweStatus(RijbewijsStatusType status, STATRYBKGEG gegevens) {
    long dIn = gegevens.getStatdatrybk().longValue();
    long tIn = gegevens.getStattydrybk().longValue();
    return new RijbewijsAanvraagStatus(dIn, tIn, status, getApplication().getServices().getGebruiker());
  }

  private boolean updateRbwNR(RijbewijsAanvraag zaak, STATRYBKGEG status) {
    String rbwNr = astr(status.getRybstatrybk());
    boolean isNieuwRijbewijsNummer = pos(rbwNr) && !rbwNr.equals(zaak.getRijbewijsnummer());
    if (isNieuwRijbewijsNummer) { // Rijbewijsnummer updaten
      zaak.setRijbewijsnummer(rbwNr);
      return true;
    }

    return false;
  }
}
