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

import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.AKTENR;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.DATUM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.DUBBELE_NAAM;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.GEMEENTE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.NAAMSKEUZE;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.RECHT;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.TOESTEMMINGGEVER;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenErk.VERKLARING_GEZAG;
import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanBinnenNk.GESLACHTSNAAM;
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
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page35GeboorteFormErk extends GbaForm<Page35GeboorteBeanBinnenErk> {

  private final DossierGeboorte  geboorte;
  private       DossierErkenning erkenning;

  public Page35GeboorteFormErk(DossierGeboorte geboorte) {
    this.geboorte = geboorte;
    setColumnWidths("200px", "");
    setOrder(GEMEENTE, DATUM, AKTENR, TOESTEMMINGGEVER, VERKLARING_GEZAG, NAAMSKEUZE, TITEL, VOORVOEGSEL, GESLACHTSNAAM,
        DUBBELE_NAAM, RECHT);
    setGeboorte(geboorte);
  }

  @Override
  public Page35GeboorteBeanBinnenErk getNewBean() {
    return new Page35GeboorteBeanBinnenErk();
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanBinnenErk bean = new Page35GeboorteBeanBinnenErk();
    if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
      erkenning = geboorte.getErkenningVoorGeboorte();
      List<DossierAkte> aktes = erkenning.getDossier().getAktes();
      if (!aktes.isEmpty()) {
        bean.setAktenr(astr(aktes.get(0).getAkte()));
      }

      bean.setGemeente(erkenning.getGemeente().getDescription());
      bean.setDatum(astr(erkenning.getDossier().getDatumTijdInvoer().getFormatDate()));
      bean.setToestemminggever(erkenning.getToestemminggeverType().getOms() + " " + erkenning.getRechtbank());
      bean.setVerklaringGezag(erkenning.isVerklaringGezag() ? "Ja" : "Nee");
      bean.setNaamskeuze(erkenning.getNaamskeuzeType().getType());
      bean.setRecht(astr(erkenning.getLandAfstammingsRecht()));
      bean.setTitel(erkenning.getKeuzeTitel());
      bean.setVoorv(new FieldValue(erkenning.getKeuzeVoorvoegsel()));
      bean.setGeslachtsnaam(erkenning.getKeuzeGeslachtsnaam());
      bean.setDubbeleNaam(geboorte.getOrgKeuzeNaamDubbel());
    }

    setBean(bean);
    Field dubbeleNaam = getField(DUBBELE_NAAM);
    dubbeleNaam.setVisible(erkenning != null && geboorte.isOvergangsperiodeNaamskeuze());
    dubbeleNaam.setReadOnly(false);
    repaint();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(DUBBELE_NAAM)
        && geboorte.isOvergangsperiodeNaamskeuze()
        && erkenning != null) {
      column.addComponent(new Button("Naamselectie", event -> {
        getApplication().getParentWindow().addWindow(new BsNamenWindow(geboorte) {

          @Override
          public void setNaam(NaamsKeuze naamsKeuze) {
            getField(DUBBELE_NAAM).setValue(trim(String.format("%s %s",
                astr(naamsKeuze.getVoorvoegsel()), naamsKeuze.getGeslachtsnaam())));
          }
        });
      }));
    }

    super.afterSetColumn(column, field, property);
  }
}
