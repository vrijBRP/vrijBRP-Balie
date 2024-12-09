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

package nl.procura.gba.web.modules.bs.naturalisatie.overzicht;

import static nl.procura.standard.Globalfunctions.astr;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.function.Function;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;

public abstract class NaturalisatieOverzichtForm extends GbaForm<NaturalisatieOverzichtBean> {

  public NaturalisatieOverzichtForm(DossierNaturalisatie zaakDossier) {
    setColumnWidths("200px", "");
    setCaptionAndOrder();
    setBean(zaakDossier);
  }

  protected abstract void setCaptionAndOrder();

  public void setBean(DossierNaturalisatie dossier) {

    int kinderenMeeNaturaliseren = dossier.getMedeVerzoekers().size();
    NaturalisatieOverzichtBean bean = new NaturalisatieOverzichtBean();
    bean.setAangever(BsPersoonUtils.getNaam(dossier.getAangever()));
    bean.setBevoegd(dossier.getBevoegdTotVerzoekType());
    bean.setOptieMogelijk(dossier.isOptie() ? "Ja" : "Nee");
    bean.setBasisVerzoek(dossier.getBasisVerzoekType());
    bean.setKinderenMeeNaturaliseren(kinderenMeeNaturaliseren > 0 ? "Ja, " + kinderenMeeNaturaliseren : "Nee");
    bean.setVertegenwoordiger(dossier.getVertegenwoordiger().getNaam().getNaam_naamgebruik_eerste_voornaam());

    bean.setVerklaringVerblijf(fromBoolean(dossier.getToetsverklOndertekend()));
    bean.setBereidAfleggenVerklaring(fromBoolean(dossier.getToetsBereidVerkl()));
    bean.setBetrokkeneBekendMetBetaling(fromBoolean(dossier.getToetsBetrokkBekend()));
    bean.setBereidAfstandNationaliteit(dossier.getBereidAfstandType());
    bean.setBewijsVanIdentiteit(fromBoolean(dossier.getToetsBewijsIdAanw()));
    bean.setBewijsVanNationaliteit(dossier.getBewijsBewijsNationaliteitType());
    bean.setBewijsnoodAangetoond(fromBoolean(dossier.getToetsBewijsnood()));
    bean.setGeldigeVerblijfsvergunning(dossier.getGeldigeVerblijfsvergunningType());

    bean.setNaamsvaststellingOfWijziging(dossier.getNaamVaststellingType());
    bean.setGeslachtsnaamVastgesteld(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getNaamstGesl));
    bean.setVoornamenVastgesteld(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getNaamstVoorn));
    bean.setGeslachtsnaamGewijzigd(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getNaamstGeslGew));

    bean.setBerichtOmtrentToelating(fromBoolean(dossier.getBehBotOpgevraagd()));
    bean.setMinderjarigeKinderen12(dossier.getKinderen12AkkoordType());
    bean.setMinderjarigeKinderen16(fromBoolean(dossier.getBehMinderjKind2()));
    bean.setAndereOuderAkkoord(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getAndereOuderAkkoordType));
    bean.setNaamAndereOuderWettelijk(
        countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getBsnToestemminggever));
    bean.setInformatieJustis(fromBoolean(dossier.getBehOpgevrJustis()));
    bean.setDatumAanvraag(new DateTime(dossier.getBehDAanvr()));

    bean.setEindeTermijn(new DateTime(dossier.getBehTermDEnd()));
    bean.setBeslissing(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getBeslissingType));
    bean.setAdvies(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getAdviesBurgemeesterType));
    bean.setDatumBevestiging(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getBehDBevest));
    bean.setDatumKoninklijkBesluit(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getBehDKoningBesluit));
    bean.setNummerKoninklijkBesluit(countVerzoekgegevens(dossier,
        DossierNaturalisatieVerzoeker::getBehNrKoningBesluit));

    bean.setCeremonie1(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getCeremonie1Bijgewoond));
    bean.setCeremonie2(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getCeremonie2Bijgewoond));
    bean.setCeremonie3(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getCeremonie3Bijgewoond));
    bean.setDatumUitreiking(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getCeremonieDUitreik));
    bean.setDatumVerval(countVerzoekgegevens(dossier, DossierNaturalisatieVerzoeker::getCeremonieDVerval));

    setBean(bean);
  }

  private String countVerzoekgegevens(DossierNaturalisatie dossier,
      Function<DossierNaturalisatieVerzoeker, Object> value) {
    long filled = dossier.getVerzoekerGegevens().stream()
        .filter(gegevens -> isNotBlank(astr(value.apply(gegevens))))
        .count();
    return String.format("Gevuld: %d / %d", filled, dossier.getVerzoekerGegevens().size());
  }

  @Override
  public NaturalisatieOverzichtBean getNewBean() {
    return new NaturalisatieOverzichtBean();
  }

  private String fromBoolean(Boolean value) {
    if (value != null) {
      return value ? "Ja" : "Nee";
    }
    return "";
  }
}
