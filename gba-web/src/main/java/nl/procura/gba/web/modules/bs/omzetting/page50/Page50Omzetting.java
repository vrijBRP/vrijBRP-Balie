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

package nl.procura.gba.web.modules.bs.omzetting.page50;

import static java.util.Arrays.asList;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;
import static nl.procura.gba.web.modules.bs.omzetting.page50.Page50OmzettingBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BsVereistePage;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BsVereisteTable;
import nl.procura.gba.web.modules.bs.common.pages.vereiste.BurgerlijkeStandVereiste;
import nl.procura.gba.web.modules.bs.omzetting.page50.info.Page50InfoWindow;
import nl.procura.gba.web.modules.zaken.curatele.CurateleWindow;
import nl.procura.gba.web.services.bs.algemeen.functies.BsAkteUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.HLayout;

public class Page50Omzetting extends BsVereistePage<DossierOmzetting> {

  private final Button buttonResetAkte = new Button("Reset GPS gegevens");
  private Form1        form1           = null;
  private Form2        form2           = null;
  private Form3        form3           = null;

  public Page50Omzetting() {
    super("Omzetting GPS in huwelijk - vereisten");
  }

  @Override
  public boolean checkPage() {

    super.checkPage();

    form1.commit();
    form2.commit();
    form3.commit();

    getZaakDossier().setRechtPartner1(form1.getBean().getRechtP1());
    getZaakDossier().setRechtPartner2(form1.getBean().getRechtP2());

    getZaakDossier().setDatumPartnerschap(new DateTime(form2.getBean().getDatum()));
    getZaakDossier().setPlaatsPartnerschap(form2.getPlaats());
    getZaakDossier().setLandPartnerschap(form2.getBean().getLand());

    getZaakDossier().setAkteNummerPartnerschap(form3.getBean().getBsAkteNummer());
    getZaakDossier().setAkteBrpNummerPartnerschap(form3.getBean().getBrpAkteNummer());
    getZaakDossier().setAktePlaatsPartnerschap(form3.getBean().getPlaats());
    getZaakDossier().setAkteJaarPartnerschap(aval(form3.getBean().getJaar()));

    getProcessen().updateStatus();

    getApplication().getServices().getOmzettingService().save(getDossier());

    return true;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonResetAkte) {
      resetGpsGegevens();
      infoMessage("De GPS gegevens zijn opnieuw geladen");
    }

    super.handleEvent(button, keyCode);
  }

  public void resetGpsGegevens() {

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();

    Page50OmzettingBean2 bean2 = new Page50OmzettingBean2();
    Page50OmzettingBean3 bean3 = new Page50OmzettingBean3();

    updateBean(bean2, bean3, getZaakDossier(), p1, p2);

    form2.setBean(bean2);
    form3.setBean(bean3);
  }

  @Override
  protected void addOptieButtons() {

    getOptieLayout().getRight().addButton(buttonResetAkte, this);

    super.addOptieButtons();
  }

  @Override
  protected void addOptieComponenten() {

    BsVereisteTable table1 = new BsVereisteTable("d", "Brondocumenten", getVereisten());
    BsVereisteTable table2 = new BsVereisteTable("h", "Actueel huwelijk/GPS", getVereisten());

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();

    Page50OmzettingBean1 bean1 = new Page50OmzettingBean1(p1, p2);
    bean1.setRechtP1(getRecht(getZaakDossier().getRechtPartner1()));
    bean1.setRechtP2(getRecht(getZaakDossier().getRechtPartner2()));

    form1 = new Form1(bean1);
    form2 = new Form2();
    form3 = new Form3();

    resetGpsGegevens();

    HLayout h = new HLayout(form2, form3).sizeFull();
    addOptieComponenten(form1, "");
    addOptieComponenten(h, "");
    addOptieComponenten(table1);
    addOptieComponenten(table2);

    super.addOptieComponenten();
  }

  @Override
  protected void addVereisten() {

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();

    setBrondocument("d1", p1);
    setBrondocument("d2", p2);

    setGps("h1", p1);
    setGps("h2", p2);
  }

  @Override
  protected Window getCuraleWindow() {

    DossierPersoon p1 = getZaakDossier().getPartner1();
    DossierPersoon p2 = getZaakDossier().getPartner2();
    return new CurateleWindow(asList(p1, p2));
  }

  @Override
  protected Window getInfoWindow() {
    return new Page50InfoWindow();
  }

  @Override
  protected void updateVereisten() {

    for (DossierVereiste v : getZaakDossier().getDossier().getVereisten()) {

      if (fil(v.getVoldaan())) {
        getVereiste(v.getIdKey()).setVoldoet(v.getVoldaan());
      }

      getVereiste(v.getIdKey()).setOverruleReason(v.getToelichting());
    }
  }

  private FieldValue getRecht(FieldValue recht) {
    return pos(recht.getValue()) ? recht : Landelijk.getNederland();
  }

  private void setBrondocument(String id, DossierPersoon p) {

    getVereisten().add(new BurgerlijkeStandVereiste(id, "N.v.t.", "N", p));
  }

  private void setGps(String id, DossierPersoon p) {

    if (pos(p.getBurgerServiceNummer().getValue())) {

      BasePLExt pl = getPersoonslijst(p.getBurgerServiceNummer());

      if (pl.getPersoon().getBurgerlijkeStand().getStatus().getType().is(PARTNERSCHAP)) {

        getVereisten().add(new BurgerlijkeStandVereiste(id, "Actueel GPS", "J", p));
      } else {
        getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen actueel GPS", "N", p));
      }
    } else {

      getVereisten().add(new BurgerlijkeStandVereiste(id, "Geen informatie beschikbaar", "", p));
    }
  }

  private void updateBean(Page50OmzettingBean2 bean2, Page50OmzettingBean3 bean3, DossierOmzetting zaakDossier,
      DossierPersoon... personen) {

    if (zaakDossier.getDatumPartnerschap().getLongDate() > 0) {

      // GPS sluitingsgegevens
      bean2.setDatum(zaakDossier.getDatumPartnerschap().getDate());

      if (pos(zaakDossier.getPlaatsPartnerschap().getStringValue())) {
        bean2.setPlaatsNL(zaakDossier.getPlaatsPartnerschap());
      } else {
        bean2.setPlaatsBuitenland(zaakDossier.getPlaatsPartnerschap().toString());
      }

      bean2.setLand(zaakDossier.getLandPartnerschap());

      // GPS Aktegegevens
      bean3.setBsAkteNummer(zaakDossier.getAkteNummerPartnerschap());
      bean3.setBrpAkteNummer(zaakDossier.getAkteBrpNummerPartnerschap());
      bean3.setPlaats(zaakDossier.getAktePlaatsPartnerschap());

      if (pos(zaakDossier.getAkteJaarPartnerschap())) {
        bean3.setJaar(astr(zaakDossier.getAkteJaarPartnerschap()));
      }
    } else {

      // GPS gegevens niet gezet

      for (DossierPersoon p : personen) {

        if (p.isVolledig() && p.isIngeschreven()) {

          BasePLExt persoon = getPersoonslijst(p.getBurgerServiceNummer());
          BasePLRec partnerRecord = persoon.getLatestRec(GBACat.HUW_GPS);

          if (partnerRecord.getStatus() == GBARecStatus.CURRENT) {

            String dHuw = partnerRecord.getElemVal(DATUM_VERBINTENIS).getVal();
            String dOntb = partnerRecord.getElemVal(DATUM_ONTBINDING).getVal();
            String plaats = partnerRecord.getElemVal(PLAATS_VERBINTENIS).getVal();
            long land = along(partnerRecord.getElemVal(LAND_VERBINTENIS).getVal());
            String aktenummer = partnerRecord.getElemVal(AKTENR).getVal();
            String gemeente = partnerRecord.getElemVal(REGIST_GEM_AKTE).getVal();

            if (pos(dHuw) && aval(dOntb) < 0) {

              // Sluitingsgegevens
              bean2.setDatum(new DateTime(dHuw).getDate());
              bean2.setPlaatsNL(new FieldValue(plaats));
              bean2.setLand(new FieldValue(land));

              // Aktegegevens
              bean3.setJaar(new ProcuraDate(astr(dHuw)).getYear());
              bean3.setBsAkteNummer(BsAkteUtils.getBsAktenummer(aktenummer));
              bean3.setBrpAkteNummer(aktenummer);
              bean3.setPlaats(new FieldValue(gemeente));
            }
          }
        }
      }
    }
  }

  public class Form1 extends Page50OmzettingForm1 {

    private Form1(Page50OmzettingBean1 bean) {
      setBean(bean);
    }

    @Override
    public void init() {

      setCaption("Toegepast recht");
      setColumnWidths("190px", "");
      setOrder(NATIOP1, NATIOP2, RECHTP1, RECHTP2);
    }
  }

  public class Form2 extends Page50OmzettingForm2 {

    private Form2() {
      setBean(new Page50OmzettingBean2());
    }

    public FieldValue getPlaats() {

      GbaComboBox plaatsNl = getField(Page50OmzettingBean2.PLAATS_NL, GbaComboBox.class);
      GbaTextField plaatsBl = getField(Page50OmzettingBean2.PLAATS_BL, GbaTextField.class);

      if (plaatsNl.isVisible()) {
        return (FieldValue) plaatsNl.getValue();
      }

      return new FieldValue(astr(plaatsBl.getValue()));
    }
  }

  public class Form3 extends Page50OmzettingForm3 {

    private Form3() {
      setBean(new Page50OmzettingBean3());
    }
  }
}
