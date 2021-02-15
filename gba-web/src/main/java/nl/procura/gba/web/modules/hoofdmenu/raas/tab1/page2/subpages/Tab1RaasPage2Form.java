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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.dto.raas.aanvraag.*;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public abstract class Tab1RaasPage2Form extends GbaForm<Tab1RaasPage2Bean> {

  public Tab1RaasPage2Form(DocAanvraagDto documentAanvraag) {
    setColumnWidths("200px", "");
    createFields();
    initFields(documentAanvraag);
  }

  protected abstract void createFields();

  public void initFields(DocAanvraagDto da) {

    AanvraagDto aanvr = da.getAanvraag();
    LeveringDto lev = da.getLevering();
    AfsluitingDto afsl = da.getAfsluiting();
    AanvragerDto aanvrager = da.getAanvrager();
    PartnerDto partner = da.getPartner();
    AdresDto adres = da.getAdres();
    VermissingDto vermissing = da.getVermissing();
    ClausulesDto clausules = da.getClausules();

    Tab1RaasPage2Bean bean = new Tab1RaasPage2Bean();
    bean.setCode(set(da.getId().getValue()));
    bean.setStatusVerw(new RaasBeanValue(da.getStatusVerwerking().getValue(), true));
    bean.setAanvraagNummer(set(new Aanvraagnummer(da.getAanvraagNr().getValue().toString()).getFormatNummer()));
    bean.setNrNLDoc(set(da.getNrNederlandsDoc()));
    bean.setCRaas(set(da.getLocatieCode()));
    bean.setDatumTijdAanvr(getDatumTijd(aanvr.getDatum().getValue(), aanvr.getTijd().getValue()));
    bean.setStatusAanvr(new RaasBeanValue(aanvr.getStatus().getValue(), true));
    bean.setIndSpoed(set(da.getSpoed()));
    bean.setDatumTijdLev(getDatumTijd(lev.getDatum().getValue(), lev.getTijd().getValue()));
    bean.setStatusLev(new RaasBeanValue(lev.getStatus().getValue(), true));
    bean.setDatumTijdAfsl(getDatumTijd(afsl.getDatum().getValue(), afsl.getTijd().getValue()));
    bean.setStatusAfsl(new RaasBeanValue(afsl.getStatus().getValue(), true));
    bean.setNrNLDocIn(set(da.getNrNederlandsDocIn()));
    bean.setRdmDoc(new RaasBeanValue(da.getDocSoort().getValue(), true));
    bean.setDVerstrek(getDatum(da.getDatumVerstrekking().getValue()));
    bean.setDatumGeldigEnd(getDatum(da.getDatumEindeGeldigheid().getValue()));
    bean.setAnr(new AnrFieldValue(aanvrager.getAnr().getValue()));
    bean.setBsn(new BsnFieldValue(aanvrager.getBsn().getValue()));
    bean.setNaam(aanvrager.getNaam().getValue());
    bean.setTp(aanvrager.getTitelPredikaat());
    bean.setVoorv(aanvrager.getVoorvoegsel().getValue());
    bean.setVoorn(aanvrager.getVoornaam().getValue());
    bean.setDGeb(aanvrager.getGeboortedatum().getValue().toString());
    bean.setPGeb(aanvrager.getGeboorteplaats().getValue());
    bean.setLGeb(aanvrager.getGeboorteland().getValue());
    bean.setGeslacht(new RaasBeanValue(aanvrager.getGeslacht().getValue(), true));
    bean.setNaamgebruik(new RaasBeanValue(aanvrager.getNaamgebruik().getValue(), true));
    bean.setNatio(aanvrager.getNationaliteit().getValue());
    bean.setAandBijzNl(new RaasBeanValue(aanvrager.getAanduidingBijzonderNederlander().getValue(), true));
    bean.setNaamP(partner.getNaam().getValue());
    bean.setTpP(partner.getTitelPredikaat().getValue());
    bean.setVoorvP(partner.getVoorvoegsel());
    bean.setHuwSrt(new RaasBeanValue(da.getSoortHuwelijk().getValue(), true));
    bean.setRdnOntb(da.getRedenOntbinding().getValue());
    bean.setLengte(set(aanvrager.getLengte()));
    bean.setStraat(adres.getStraat().getValue());
    bean.setHnr(adres.getHuisnr().getValue().toString());
    bean.setHnrL(adres.getHuisletter().getValue());
    bean.setHnrT(adres.getHuisnrToevoeging().getValue());
    bean.setHnrA(adres.getHuisnrAanduiding().getValue());
    bean.setPostcode(adres.getPostcode().getValue());
    bean.setLocatie(adres.getLocatie().getValue());
    bean.setGemeentedeel(adres.getGemeentedeel().getValue());
    bean.setAutorit(set(da.getAutoriteit()));
    bean.setIndIdent(set(da.getIdentiteitVastgesteld().getValue() ? "Ja" : "Nee"));
    bean.setBewijsIdent(set(da.getBewijsstukkenIdentiteit()));
    bean.setToestem(set(da.getAantalToestemminggevers()));
    bean.setNrVbDoc(set(da.getVerblijfsdoc().getDocNr()));
    bean.setDatumVbDoc(getDatum(da.getVerblijfsdoc().getDatumEinde().getValue()));
    bean.setIndVerm(new RaasBeanValue(vermissing.getIndicatie().getValue(), true));
    bean.setPvVerm(vermissing.getProcesverbaal().getValue());
    bean.setNrVerm(vermissing.getDocNr().getValue());
    bean.setDatumVerm(new DateFieldValue(vermissing.getDatum().getValue()));
    bean.setAutoritVerm(vermissing.getAutoriteit().getValue());
    bean.setVerzoekVerm(vermissing.getVerzoek().getValue());
    bean.setClI(new RaasBeanValue(clausules.getClausuleI().getValue(), true));
    bean.setClIV(clausules.getClausuleIV().getValue());
    bean.setClV(new RaasBeanValue(clausules.getClausuleV().getValue(), true));
    bean.setClXA(clausules.getClausuleXA().getValue());
    bean.setClXB(clausules.getClausuleXB().getValue());
    bean.setClXIB(new RaasBeanValue(clausules.getClausuleXIB().getValue(), true));
    bean.setClXII(clausules.getClausuleXII().getValue());
    bean.setNietAanw(set(da.getRedenNietAanwezig()));
    bean.setUpdateProweb(da.getUpdateProweb().getValue() ? "Nog niet gedaan" : "N.v.t.");
    bean.setUpdateProbev(da.getUpdateProbev().getValue() ? "Nog niet gedaan" : "N.v.t.");

    setBean(bean);
    repaint();
    setReadOnly(true);
  }

  public String set(Object obj) {
    if (obj instanceof Integer) {
      Integer i = (Integer) obj;
      if (i < 0) {
        return "";
      }
    }
    return obj.toString();
  }

  private String getDatum(Integer datum) {
    return datum >= 0 ? new ProcuraDate(datum).getFormatDate() : "";
  }

  private String getDatumTijd(Integer datum, Integer tijd) {
    StringBuilder out = new StringBuilder();
    if (datum >= 0) {
      String dAanvraag = new ProcuraDate(datum).getFormatDate();
      out.append(dAanvraag);
    }
    if (tijd >= 0) {
      String tAanvraag = new ProcuraDate().setStringTime(astr(tijd)).getFormatTime();
      out.append(" om ").append(tAanvraag);
    }
    return out.toString();
  }

  //    private class RaasContainer extends ArrayListContainer {
  //        RaasContainer(List<FieldValue> values) {
  //            addItems(values);
  //        }
  //
  //        public FieldValue get(FieldValue value) {
  //            for (Object item : getAllItemIds()) {
  //                if (item.equals(value)) {
  //                    return (FieldValue) item;
  //                }
  //            }
  //
  //            return null;
  //        }
  //    }
  //
  //    @Override
  //    public void afterSetBean() {
  //
  //      setContainer(Tab1RaasPage2Bean.VOORV, toFieldValue(AdelTitel.Values.values()));
  //      setContainer(Tab1RaasPage2Bean.VOORV_P, toFieldValue(AdelTitel.Values.values()));
  //      setContainer(Tab1RaasPage2Bean.TP, toFieldValue(AdelTitel.Values.values()));
  //      setContainer(Tab1RaasPage2Bean.TP_P, toFieldValue(AdelTitel.Values.values()));
  //      setContainer(Tab1RaasPage2Bean.HNR_A, toFieldValue(Container.AANDUIDING, true));
  //      setContainer(Tab1RaasPage2Bean.GESLACHT, toFieldValue(Geslacht.Values.values()));
  //      setContainer(Tab1RaasPage2Bean.NAAMGEBRUIK, toFieldValue(new NaamgebruikContainer(), true));
  //      setContainer(Tab1RaasPage2Bean.HUW_SRT, toFieldValue(Container.SOORT_VERBINTENIS, true));
  //      setContainer(Tab1RaasPage2Bean.RDN_ONTB, toFieldValue(Container.REDEN_HUW_ONTB, true));
  //      setContainer(IND_VERM, toFieldValue(AanduidingVermissing.Values.values()));
  //
  //        Field indVerm = getField(IND_VERM);
  //        if (indVerm != null) {
  //            indVerm.addListener((ValueChangeListener) event -> {
  //                FieldValue indVermValue = (FieldValue) event.getProperty().getValue();
  //                updateVermissing(indVermValue);
  //                repaint();
  //            });
  //        }
  //
  //        super.afterSetBean();
  //    }
  //
  //    /**
  //     * Als indicatie voorkomt dan zijn datum, nummer en autoreit verplicht
  //     * Als indicatie V dan is pv ook verplicht
  //     */
  //    private void updateVermissing(FieldValue code) {
  //        getField(D_VERM).setRequired(false);
  //        getField(NR_VERM).setRequired(false);
  //        getField(AUTORIT_VERM).setRequired(false);
  //        getField(PV_VERM).setRequired(false);
  //
  //        if (code != null && fil(code.getStringValue())) {
  //            getField(D_VERM).setRequired(true);
  //            getField(NR_VERM).setRequired(true);
  //            getField(AUTORIT_VERM).setRequired(true);
  //            if (AanduidingVermissingType.VERMISSING.getCode().equals(code.getStringValue())) {
  //                getField(PV_VERM).setRequired(true);
  //            }
  //        }
  //    }
  //
  //    private List<FieldValue> toFieldValue(IndexedContainer container, boolean addId) {
  //        return container.getItemIds().stream().map(id -> {
  //            FieldValue fv = (FieldValue) id;
  //            return new RaasBeanValue(fv.getValue(), (addId ? (fv.getValue() + ": ") : "") + fv.getDescription());
  //        }).collect(Collectors.toList());
  //    }
  //
  //    private List<FieldValue> toFieldValue(ElementValue[] values) {
  //        return Arrays.stream(values)
  //                .map(ev -> new FieldValue(ev.getId(), ev.getDescription()))
  //                .collect(Collectors.toList());
  //    }
  //
  //    protected void setContainer(String fieldId, List<FieldValue> values) {
  //        ProNativeSelect field = getField(fieldId, ProNativeSelect.class);
  //        if (field != null) {
  //            FieldValue value = (FieldValue) field.getValue();
  //            System.out.println(value.getStringValue());
  //            RaasContainer container = new RaasContainer(values);
  //            field.setContainerDataSource(container);
  //            field.setValue(container.get(value));
  //        }
  //    }
}
