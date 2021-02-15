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

package nl.procura.gba.web.modules.bs.onderzoek.overzicht;

import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.vaadin.component.layout.table.TableLayout;

public abstract class OnderzoekOverzichtForm extends GbaForm<OnderzoekOverzichtBean> {

  public OnderzoekOverzichtForm(DossierOnderzoek zaakDossier) {
    setColumnWidths("200px", "");
    setCaptionAndOrder();
    setBean(zaakDossier);
  }

  protected abstract void setCaptionAndOrder();

  public void setBean(DossierOnderzoek onderzoek) {

    Adresformats aanlAdres = new Adresformats().setValues(onderzoek.getAanleidingAdres().getDescription(),
        onderzoek.getAanleidingHnr(),
        onderzoek.getAanleidingHnrL(),
        onderzoek.getAanleidingHnrT(),
        onderzoek.getAanleidingHnrA().getDescription(), "",
        onderzoek.getAanleidingPc().getDescription(), "",
        onderzoek.getAanleidingPlaats().getDescription(),
        onderzoek.getAanleidingGemeente().getDescription(), "",
        onderzoek.getAanleidingLand().getDescription(), "",
        onderzoek.getAanleidingBuitenl1(),
        onderzoek.getAanleidingBuitenl2(),
        onderzoek.getAanleidingBuitenl3());

    Adresformats resAdres = new Adresformats().setValues(onderzoek.getResultaatAdres().getDescription(),
        onderzoek.getResultaatHnr(), onderzoek.getResultaatHnrL(),
        onderzoek.getResultaatHnrT(),
        onderzoek.getResultaatHnrA().getDescription(), "",
        onderzoek.getResultaatPc().getDescription(), "",
        onderzoek.getResultaatPlaats().getDescription(),
        onderzoek.getResultaatGemeente().getDescription(), "",
        onderzoek.getResultaatLand().getDescription(), "",
        onderzoek.getResultaatBuitenl1(),
        onderzoek.getResultaatBuitenl2(),
        onderzoek.getResultaatBuitenl3());

    OnderzoekOverzichtBean bean = new OnderzoekOverzichtBean();
    bean.setBron(onderzoek.getOnderzoekBron().getOms());
    bean.setNaam(BsPersoonUtils.getNaam(onderzoek.getAangever()));
    bean.setDossiernrTmv(onderzoek.getAanlTmvNr());
    bean.setKenmerk(onderzoek.getAanlKenmerk());
    bean.setInstantie(onderzoek.getAanlInst());
    bean.setTav(trim(onderzoek.getAanlInstAanhef() + " " + onderzoek.getAanlInstVoorl() + " " +
        onderzoek.getAanlInstNaam()));
    bean.setAdres(onderzoek.getAanlInstAdres());
    bean.setPc(onderzoek.getAanlInstPc());
    bean.setPlaats(onderzoek.getAanlInstPlaats());
    bean.setAfdeling(onderzoek.getAanlAfdeling());
    bean.setDatumOntvangst(onderzoek.getDatumOntvangstMelding());
    bean.setAard(onderzoek.getOnderzoekAard());
    bean.setVermoedAdres(trim(onderzoek.getVermoedelijkAdres() + ": " + aanlAdres.getAdres()));
    bean.setBinnen5dagen(toYesNo(onderzoek.getBinnenTermijn()));
    bean.setDatumAanvangOnderzoek(onderzoek.getDatumAanvangOnderzoek());
    bean.setAandGegOnderzoek(onderzoek.getAanduidingGegevensOnderzoek());
    bean.setOnderzoekDoorAnderGedaan(toYesNo(onderzoek.getGedegenOnderzoek()));
    bean.setReden(onderzoek.getRedenTermijn());
    bean.setVoldoendeReden(toYesNo(onderzoek.getRedenOverslaan()));
    bean.setToelichting1(onderzoek.getToelichtingOverslaan());

    bean.setStartFase1Op(onderzoek.getFase1DatumIngang());
    bean.setStartFase1Tm(onderzoek.getFase1DatumEinde());
    bean.setReactieOntvangen(toYesNo(onderzoek.getFase1Reactie()));
    bean.setToelichting2(onderzoek.getFase1Toelichting());
    bean.setVervolgacties(toYesNo(onderzoek.getFase1Vervolg()));

    bean.setStartFase2Op(onderzoek.getFase2DatumIngang());
    bean.setStartFase2Tm(onderzoek.getFase2DatumEinde());
    bean.setOnderzoekTerPlaatse(toYesNo(onderzoek.getFase2OnderzoekGewenst()));
    bean.setToelichting3(onderzoek.getFase2Toelichting());

    bean.setBetrokkenen(onderzoek.getResultaatOnderzoekBetrokkene());
    bean.setDatumEindeOnderzoek(onderzoek.getDatumEindeOnderzoek());
    bean.setResultaatNogmaals(toYesNo(onderzoek.getNogmaalsAanschrijven()));
    bean.setResultaatAdres(resAdres.getAdres());
    bean.setResultaatPc(onderzoek.getResultaatPc());
    bean.setResultaatPcGemeente(onderzoek.getResultaatPc() + " / " + onderzoek.getResultaatGemeente());
    bean.setResultaatBuitenl1(onderzoek.getResultaatBuitenl1());
    bean.setResultaatBuitenl2(onderzoek.getResultaatBuitenl2());
    bean.setResultaatBuitenl3(onderzoek.getResultaatBuitenl3());
    bean.setResultaatLand(onderzoek.getResultaatLand());
    bean.setResultaatToel(onderzoek.getResultaatToelichting());

    setBean(bean);
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(OnderzoekOverzichtBean.TOELICHTING2)) {
      column.setColspan(3);
    }
    super.afterSetColumn(column, field, property);
  }

  @Override
  public OnderzoekOverzichtBean getNewBean() {
    return new OnderzoekOverzichtBean();
  }

  private String toYesNo(Boolean value) {
    return value != null ? (value ? "Ja" : "Nee") : "N.v.t.";
  }
}
