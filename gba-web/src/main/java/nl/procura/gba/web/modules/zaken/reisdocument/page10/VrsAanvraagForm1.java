/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static java.lang.String.join;
import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsAanvraagBean1.NAAM;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import java.time.LocalDate;
import java.util.function.Function;

import com.vaadin.ui.Field;

import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAanvraaginformatieResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseAutoriteitVerstrekking;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseGoedkeuringGelaat;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseInstantie;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseInstantielocatie;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseNationaliteit;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponsePersonalisatiegegevens;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponsePersonalisatiegegevens.GeslachtOmschrijvingEnum;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponsePersonalisatiegegevens.SpoedaanvraagEnum;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponsePersonalisatiegegevens.WijzeVermeldingPartnerOmschrijvingEnum;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseReisdocument;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseReisdocumentsoort;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponseVingerafdrukkenAfwezig;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class VrsAanvraagForm1 extends ReadOnlyForm<VrsAanvraagForm1> {

  public VrsAanvraagForm1(ControleAanvraagVolledigResponse response, String... fields) {
    setOrder(fields);
    setReadThrough(true);
    setReadonlyAsText(false);
    setColumnWidths("160px", "", "210px", "");

    ControleAanvraagVolledigResponseAanvraaginformatieResponse info = response.getAanvraaginformatie();

    VrsAanvraagBean1 bean = new VrsAanvraagBean1();

    ControleAanvraagVolledigResponseReisdocument reisd = response.getReisdocument();
    if (reisd != null) {
      bean.setDocumentnummer(reisd.getDocumentNummer());
    }

    bean.setAanvraagnummer(ofNullable(info.getAanvraagnummer())
        .map(nr -> new Aanvraagnummer(astr(nr)).getFormatNummer())
        .orElse(""));
    bean.setAanvraagdatum(toDate(info.getAanvraagdatum()));
    bean.setDocumentSoort(toString(info.getReisdocumentsoort(),
        ControleAanvraagVolledigResponseReisdocumentsoort::getOmschrijving));
    bean.setInstantieLocatie(toString(info.getInstantielocatie(),
        ControleAanvraagVolledigResponseInstantielocatie::getNaam));
    bean.setArtikel23b(Boolean.TRUE.equals(info.getArtikel23b()) ? "Ja" : "Nee");
    bean.setNietPersoonlijkAanwezig(info.getNietPersoonlijkAanwezig());
    bean.setAutoriteitVerstrekking(toString(info.getAutoriteitVerstrekking(),
        ControleAanvraagVolledigResponseAutoriteitVerstrekking::getOmschrijving));
    bean.setInleverinstantie(toString(info.getInleverinstantie(),
        ControleAanvraagVolledigResponseInstantie::getOmschrijving));
    bean.setInleverdatum(toDate(info.getInleverdatum()));
    bean.setVerzochtEindeGeldigheid(toDate(info.getVerzochtEindeGeldigheid()));
    bean.setRedenvingerafwezig(toString(info.getRedenvingerafwezig(),
        ControleAanvraagVolledigResponseVingerafdrukkenAfwezig::getOmschrijving));
    bean.setGoedkeuringGelaat(toString(info.getGoedkeuringGelaat(),
        ControleAanvraagVolledigResponseGoedkeuringGelaat::getOmschrijving));
    bean.setNietInStaatTotOndertekening(Boolean.TRUE.equals(info.getNietInStaatTotOndertekening()) ? "Ja" : "Nee");
    bean.setAutoriteitVanAfgifte(info.getAutoriteitVanAfgifte());

    ControleAanvraagVolledigResponsePersonalisatiegegevens persoon = response.getPersonalisatiegegevens();
    bean.setBsn(toString(persoon.getBsn(), bsn -> Bsn.format(astr(bsn))) + " / "
        + toString(persoon.getAnummer(), anr -> Anr.format(astr(anr))));
    bean.setNaam(trim(join(" ",
        astr(persoon.getVoornamen()),
        astr(persoon.getVoorvoegselGeslachtsnaam()),
        astr(persoon.getGeslachtsnaam()))));
    bean.setGeboortedatum(toString(persoon.getGeboortedatum(), date -> new ProcuraDate(date).getFormatDate()));
    bean.setGeboorteplaats(persoon.getGeboorteplaats());
    bean.setNationaliteit(toString(persoon.getNationaliteit(),
        ControleAanvraagVolledigResponseNationaliteit::getOmschrijving));
    bean.setGeslacht(toString(persoon.getGeslachtOmschrijving(), GeslachtOmschrijvingEnum::toString));
    bean.setSpoed(toString(persoon.getSpoedaanvraag(), SpoedaanvraagEnum::toString));
    bean.setDatumVerstrekking(toDate(persoon.getDatumverstrekking()));
    bean.setLengte(astr(persoon.getLengte()));
    bean.setPseudoniem(persoon.getPseudoniem());
    bean.setPartner(trim(join(" ",
        astr(persoon.getVoorvoegselGeslachtsnaamPartner()),
        astr(persoon.getGeslachtsnaamPartner()))));
    bean.setVermeldingPartner(toString(persoon.getWijzeVermeldingPartnerOmschrijving(),
        WijzeVermeldingPartnerOmschrijvingEnum::getValue));
    bean.setVerblijfsdocument(ofNullable(persoon.getVerblijfsdocumentnummer())
        .map(verblijfsdocument -> verblijfsdocument + " " + toDate(persoon.getVerblijfsdocumentEindegeldigheid()))
        .orElse(""));
    bean.setVermeldingTaal(join(", ",
        astr(persoon.getFunctievermeldingNederlands()),
        astr(persoon.getFunctievermeldingEngels()),
        astr(persoon.getFunctievermeldingFrans())));
    bean.setTvv(persoon.getTerVervangingVanDocumentnummer());
    bean.setUitgezonderdeLanden(persoon.getUitgezonderdLanden());
    bean.setGeldigLanden(persoon.getGeldigVoorLanden());
    bean.setStaatloos(Boolean.TRUE.equals(persoon.getStaatloze()) ? "Ja" : "Nee");

    setBean(bean);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(NAAM)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  private static <T> String toString(T object, Function<T, String> codeSupplier) {
    if (object != null) {
      return codeSupplier.apply(object);
    }
    return "";
  }

  private static String toDate(LocalDate info) {
    return ofNullable(info)
        .map(date -> DateTime.of(date).getFormatDate())
        .orElse("");
  }
}
