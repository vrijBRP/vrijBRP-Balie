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

package nl.procura.gba.web.modules.zaken.vog.page10;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.base.UnknownGBAElementException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page11.Page11Vog;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.vog.*;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Tonen aanvraag
 */

public class Page10Vog extends VogAanvraagPage {

  private Page10VogForm1 form1 = null;

  public Page10Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    setInfo("Als het postadres afwijkt van het woonadres, vul dan het postadres in. (dus alleen het laatste deel).");
    addComponent(new VogHeaderForm(aanvraag));
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      if (!GenericDao.isSaved(getAanvraag())) {

        BasePLExt pl = getPl();

        Naam naam = pl.getPersoon().getNaam();

        Geboorte geb = pl.getPersoon().getGeboorte();
        GbaDateFieldValue gebDat = new GbaDateFieldValue(geb.getGeboortedatum());
        FieldValue gebPlaats = new FieldValue(geb.getGeboorteplaats().getVal(),
            geb.getGeboorteplaats().getDescr());
        FieldValue gebLand = new FieldValue(geb.getGeboorteland().getVal(),
            geb.getGeboorteland().getDescr());
        Geslacht gesl = Geslacht.get(pl.getPersoon().getGeslacht().getVal());

        Adres vb = pl.getVerblijfplaats().getAdres();
        String straat = vb.getStraat().getValue().getVal();
        String plaats = vb.getGemeente().getValue().getDescr();
        FieldValue land = new FieldValue("6030", "Nederland");

        VogAanvrager a = getAanvraag().getAanvrager();
        a.setBurgerServiceNummer(new BsnFieldValue(pl.getPersoon().getBsn().getVal()));
        a.setAnummer(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));
        a.setGeslachtsnaam(naam.getGeslachtsnaam().getValue().getVal());
        a.setVoorvoegsel(naam.getVoorvoegsel().getValue().getVal());
        a.setVoornamen(naam.getVoornamen().getValue().getVal());

        a.setDatumGeboorte(gebDat);
        a.setPlaatsGeboren(gebPlaats);
        a.setLandGeboren(gebLand);

        a.setGeslacht(gesl);
        a.setAanschrijf(naam.getNaamNaamgebruikEersteVoornaam());

        a.setTelMobiel(getCAant(pl, ContactgegevensService.TEL_MOBIEL));
        a.setTelThuis(getCAant(pl, ContactgegevensService.TEL_THUIS));
        a.setTelWerk(getCAant(pl, ContactgegevensService.TEL_WERK));
        a.setEmail(getCAant(pl, ContactgegevensService.EMAIL));

        a.setStraat(vb.getStraat().getValue().getVal());
        a.setHnr(along(vb.getHuisnummer().getValue().getVal()));
        a.setHnrL(vb.getHuisletter().getValue().getVal());
        a.setHnrT(vb.getHuisnummertoev().getValue().getVal());
        a.setPc(new FieldValue(vb.getPostcode().getValue().getVal(),
            vb.getPostcode().getValue().getDescr()));
        a.setPlaats(vb.getPlaats().getValue().getDescr());
        a.setLand(new FieldValue("6030", "Nederland"));

        List<VogNationaliteit> nationaliteiten = new ArrayList<>();

        // Alle nationaliteiten

        for (BasePLSet set : pl.getCat(GBACat.NATIO).getSets()) {
          BasePLValue waarde = set.getLatestRec().getElemVal(GBAElem.NATIONALITEIT);
          if (pos(waarde.getVal())) {
            nationaliteiten.add(new VogNationaliteit(waarde.getVal(), waarde.getDescr()));
          }
        }

        a.setNationaliteiten(nationaliteiten);

        getAanvraag().getAanvrager().setAfwijkendAdres(emp(straat) || emp(plaats) || !pos(land.getValue()));

        for (BasePLSet set : pl.getCat(GBACat.PERSOON).getSets()) {
          for (BasePLRec h : set.getHistRecs()) {
            if (!h.isIncorrect()) {
              getAanvraag().getHistorie().add(getHistorie(h));
            }
          }
        }

      }

      form1 = new Page10VogForm1(getAanvraag());

      addComponent(form1);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page10VogBean1 b = form1.getBean();

    VogAanvrager a = getAanvraag().getAanvrager();
    a.setAanschrijf(b.getAanschrNaamAanvrager());
    a.setStraat(b.getStraat());
    a.setHnr(along(defaultNul(astr(b.getHnr()))));
    a.setHnrL(b.getHnrL());
    a.setHnrT(b.getHnrT());
    a.setPc(b.getPcAanvrager());
    a.setPlaats(b.getPlaatsAanvrager());
    a.setLand(b.getLandAanvrager());

    VogsService d = getApplication().getServices().getVogService();
    d.save(getAanvraag());

    getNavigation().goToPage(new Page11Vog(getAanvraag()));

    super.onNextPage();
  }

  private String getCAant(BasePLExt pl, String a) {
    return getServices().getContactgegevensService().getContactWaarde(pl, a);
  }

  private VogAanvragerHistorie getHistorie(BasePLRec p) throws UnknownGBAElementException {

    VogAanvragerHistorie v = new VogAanvragerHistorie();

    v.setBurgerServiceNummer(new BsnFieldValue(astr(p.getElemVal(GBAElem.BSN).getVal())));
    v.setGeslachtsnaam(astr(p.getElemVal(GBAElem.GESLACHTSNAAM).getVal()));
    v.setGeslacht(Geslacht.get(astr(p.getElemVal(GBAElem.GESLACHTSAAND).getVal()).toUpperCase()));
    v.setVoorvoegsel(astr(p.getElemVal(GBAElem.VOORV_GESLACHTSNAAM).getVal()));
    v.setVoornamen(astr(p.getElemVal(GBAElem.VOORNAMEN).getVal()));
    v.setDatumGeboorte(new GbaDateFieldValue(p.getElemVal(GBAElem.GEBOORTEDATUM)));

    BasePLValue land = p.getElem(GBAElem.GEBOORTELAND).getValue();
    v.setLandGeboren(new FieldValue(astr(land.getVal()), land.getDescr()));

    BasePLValue gem = p.getElem(GBAElem.GEBOORTEPLAATS).getValue();
    v.setGemeenteGeboren(new FieldValue(astr(gem.getVal()), gem.getDescr()));

    return v;
  }
}
