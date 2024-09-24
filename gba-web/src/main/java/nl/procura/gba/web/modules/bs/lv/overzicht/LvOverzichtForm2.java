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

package nl.procura.gba.web.modules.bs.lv.overzicht;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.LvType;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.lv.afstamming.KeuzeVaststellingGeslachtsnaam;
import nl.procura.gba.web.services.bs.lv.afstamming.LvDocumentDoorType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvField;
import nl.procura.gba.web.services.bs.lv.afstamming.LvOuderType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvSoortVerbintenisType;
import nl.procura.gba.web.services.gba.functies.Geslacht;

public class LvOverzichtForm2 extends ReadOnlyForm<LvOverzichtBean1> {

  private DossierLv zaakDossier;

  public LvOverzichtForm2(String caption) {
    setCaption(caption);
    setReadonlyAsText(true);
    setColumnWidths("220px", "");
  }

  public void setBean(DossierLv zaakDossier) {
    this.zaakDossier = zaakDossier;
    LvOverzichtBean1 bean = new LvOverzichtBean1();

    // Form1
    bean.setSoort(zaakDossier.getSoort());
    bean.setDatumLv(new DateTime(zaakDossier.getDatumLv()));

    // Form2
    bean.setAkteNummer(zaakDossier.getAkte());
    bean.setHuidigBrpAkteNummer(zaakDossier.getHuidigBrpAkte());

    if (zaakDossier.getSoort() != LvType.ONBEKEND) {
      bean.setNieuwBrpAkteNummer(zaakDossier.getNieuweBrpAkte());
    }

    bean.setAktePlaats(PLAATS.get(zaakDossier.getAkteGem()));

    if (pos(zaakDossier.getAkteJaar())) {
      bean.setAkteJaar(astr(zaakDossier.getAkteJaar().intValue()));
    }

    // Form3
    bean.setUitspraak(zaakDossier.getUitspraak());
    bean.setDatumUitspraak(new DateTime(zaakDossier.getDatumUitspraak()));
    bean.setDatumGewijsde(new DateTime(zaakDossier.getDatumGewijsde()));

    bean.setSoortVerbintenis(LvSoortVerbintenisType.get(zaakDossier.getSoortVerbintenis()));
    bean.setDocument(zaakDossier.getDoc());
    bean.setNummer(zaakDossier.getDocNr());
    bean.setDatum(new DateTime(zaakDossier.getDocDatum()));
    bean.setPlaats(zaakDossier.getDocPlaats());
    bean.setDoor(LvDocumentDoorType.get(zaakDossier.getDocDoor().intValue()));
    bean.setTweedeDocument(ofNullable(zaakDossier.getTweedeDoc())
        .map(value -> value ? "Ja" : "Nee")
        .orElse(""));
    bean.setTweedeDocumentOms(zaakDossier.getTweedeDocOms());
    bean.setTweedeDocumentDatum(new DateTime(zaakDossier.getTweedeDocDatum()));
    bean.setTweedeDocumentPlaats(zaakDossier.getTweedeDocPlaats());

    // Form2

    // Ouder
    LvOuderType betreftOuderType = LvOuderType.get(zaakDossier.getBetreftOuder());
    String betreftOuderPersoon = zaakDossier.getBetreftOuderPersoon().getNaam().getPred_adel_voorv_gesl_voorn();
    String betreftOuder = betreftOuderType.getOms() + (fil(betreftOuderPersoon)
        ? " (" + betreftOuderPersoon + ")"
        : "");
    bean.setBetreftOuder(betreftOuder);
    bean.setOuderschapVastgesteld(betreftOuder);
    bean.setOuderschapOntkend(betreftOuder);
    bean.setFamRecht(betreftOuder);

    bean.setGeslOuderVastgesteld(zaakDossier.getGeslOuder());
    bean.setGeslOuderGewijzigd(zaakDossier.getGeslOuder());

    bean.setVoornamenOuderVastgesteldAls(zaakDossier.getVoornOuder());

    // Kind
    bean.setKeuzeGeslachtsnaam(KeuzeVaststellingGeslachtsnaam.get(zaakDossier.getKeuzeGesl()));

    bean.setGeslachtsnaamIs(zaakDossier.getGesl());
    bean.setGeslachtsnaamVastgesteldAls(zaakDossier.getGesl());
    bean.setGeslachtsnaamGewijzigdIn(zaakDossier.getGesl());
    bean.setGeslachtsnaamKindGewijzigdIn(zaakDossier.getGesl());

    bean.setVoornamenGewijzigdIn(zaakDossier.getVoorn());
    bean.setVoornamenVastgesteldAls(zaakDossier.getVoorn());
    bean.setGeslachtsaand(Geslacht.get(zaakDossier.getGeslAand()));

    bean.setGekozenRecht(zaakDossier.getGekozenRecht());
    bean.setDagVanWijziging(new DateTime(zaakDossier.getDatumWijziging()));
    bean.setVerbeterd(zaakDossier.getVerbeterd().replaceAll("\n", "<br/>"));
    bean.setOuders(getOuders(zaakDossier));
    bean.setAdoptiefouders(getAdoptiefOuders(zaakDossier));

    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    LvOuderType ouderType = LvOuderType.get(zaakDossier.getBetreftOuder());
    if (LvOuderType.NVT.is(ouderType)) {
      ofNullable(getField(LvField.GESLN_OUDER_VG.getName())).ifPresent(f -> f.setVisible(false));
      ofNullable(getField(LvField.VOORN_OUDER.getName())).ifPresent(f -> f.setVisible(false));
    }
    if (Boolean.FALSE.equals(zaakDossier.getTweedeDoc())) {
      ofNullable(getField(LvField.TWEEDE_DOC_OMS.getName())).ifPresent(f -> f.setVisible(false));
      ofNullable(getField(LvField.TWEEDE_DOC_DATUM.getName())).ifPresent(f -> f.setVisible(false));
      ofNullable(getField(LvField.TWEEDE_DOC_PLAATS.getName())).ifPresent(f -> f.setVisible(false));
    }
    super.afterSetBean();
  }

  private String getAdoptiefOuders(DossierLv zaakDossier) {
    return getOuderOpsomming(zaakDossier.getAdoptiefouders());
  }

  private String getOuders(DossierLv zaakDossier) {
    return getOuderOpsomming(zaakDossier.getOuders());
  }

  private String getOuderOpsomming(List<DossierPersoon> adoptiefOuders) {
    StringBuilder sb = new StringBuilder();
    if (adoptiefOuders.isEmpty()) {
      sb.append("Niet van toepassing");
    } else {
      int nr = 0;
      for (DossierPersoon ouder : adoptiefOuders) {
        if (sb.length() > 0) {
          sb.append("<br/>");
        }
        sb.append(++nr)
            .append(": ")
            .append(ouder.getNaam().getPred_adel_voorv_gesl_voorn());
      }
    }
    return sb.toString();
  }

  @Override
  public LvOverzichtBean1 getNewBean() {
    return new LvOverzichtBean1();
  }
}
