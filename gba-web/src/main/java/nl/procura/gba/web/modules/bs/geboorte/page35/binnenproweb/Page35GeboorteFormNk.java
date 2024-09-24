/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb;

import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.AKTENR;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.DATUM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.DUBBELE_NAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.GEMEENTE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.GESLACHTSNAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.NAAMSKEUZE_PERSOON;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.TITEL;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.VOORVOEGSEL;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenPage.NaamsKeuze;
import nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage.BsNamenWindow;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page35GeboorteFormNk extends GbaForm<Page35GeboorteBeanBinnenNk> {

  private final DossierGeboorte   geboorte;
  private       DossierNaamskeuze naamskeuze;

  public Page35GeboorteFormNk(DossierGeboorte geboorte) {
    this.geboorte = geboorte;
    setColumnWidths("200px", "");
    setOrder(GEMEENTE, DATUM, AKTENR, NAAMSKEUZE_PERSOON, TITEL, VOORVOEGSEL, GESLACHTSNAAM, DUBBELE_NAAM);
    setGeboorte(geboorte);
  }

  @Override
  public Page35GeboorteBeanBinnenNk getNewBean() {
    return new Page35GeboorteBeanBinnenNk();
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanBinnenNk bean = new Page35GeboorteBeanBinnenNk();
    if (geboorte.getVragen().heeftNaamskeuzeVoorGeboorte()) {
      naamskeuze = geboorte.getNaamskeuzeVoorGeboorte();
      List<DossierAkte> aktes = naamskeuze.getDossier().getAktes();

      if (!aktes.isEmpty()) {
        bean.setAktenr(astr(aktes.get(0).getAkte()));
      }

      bean.setGemeente(naamskeuze.getGemeente().getDescription());
      bean.setDatum(astr(naamskeuze.getDossier().getDatumTijdInvoer().getFormatDate()));
      bean.setNaamskeuzePersoon(naamskeuze.getNaamskeuzePersoon());
      bean.setTitel(naamskeuze.getKeuzeTitel());
      bean.setVoorv(new FieldValue(naamskeuze.getKeuzeVoorvoegsel()));
      bean.setGeslachtsnaam(naamskeuze.getKeuzeGeslachtsnaam());
      bean.setDubbeleNaam(geboorte.getOrgKeuzeNaamDubbel());
    }

    setBean(bean);

    Field dubbeleNaam = getField(DUBBELE_NAAM);
    dubbeleNaam.setVisible(naamskeuze != null && geboorte.isOvergangsperiodeNaamskeuze());
    dubbeleNaam.setReadOnly(false);
    repaint();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(Page35GeboorteBeanBinnenErk.DUBBELE_NAAM)
        && geboorte.isOvergangsperiodeNaamskeuze()
        && naamskeuze != null) {
      column.addComponent(new Button("Naamselectie", event -> {
        getApplication().getParentWindow().addWindow(new BsNamenWindow(geboorte) {

          @Override
          public void setNaam(NaamsKeuze naamsKeuze) {
            getField(Page35GeboorteBeanBinnenErk.DUBBELE_NAAM).setValue(trim(String.format("%s %s",
                astr(naamsKeuze.getVoorvoegsel()), naamsKeuze.getGeslachtsnaam())));
          }
        });
      }));
    }

    super.afterSetColumn(column, field, property);
  }
}
