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

package nl.procura.gba.web.modules.bs.lv.page40;

import static java.util.Optional.ofNullable;
import static nl.procura.burgerzaken.gba.StringUtils.isBlank;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.BETREFT_OUDER_PERSOON;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.FAMRECHT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_IS;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_OUDER_GW;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.GESLN_OUDER_VG;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.KEUZE_GESLACHTSNAAM;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TOEGEPAST_RECHT;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TOEGEPAST_RECHT_ANDERS;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TOESTEMMING_ANDERS;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.TOESTEMMING_DOOR;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.VOORNAMEN_GW;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvField.VOORNAMEN_GW_IN;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvOuderType.MOEDER;
import static nl.procura.gba.web.services.bs.lv.afstamming.LvOuderType.VADER;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.LvType;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.lv.afstamming.KeuzeVaststellingGeslachtsnaam;
import nl.procura.gba.web.services.bs.lv.afstamming.LvField;
import nl.procura.gba.web.services.bs.lv.afstamming.LvGezagType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvOuderType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvToegepastRechtType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvToestemmingType;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page40LvForm2 extends Page40LvForm {

  private DossierLv zaakDossier;

  public Page40LvForm2() {
    setCaption("Inhoud document(en)");
    setColumnWidths("220px", "");
  }

  public void setBean(DossierLv zaakDossier) {
    this.zaakDossier = zaakDossier;
    setOrder(LvField.getForm2(zaakDossier.getSoort()));
    Page40LvBean1 bean = new Page40LvBean1();

    // Ouder
    bean.setBetreftOuder(LvOuderType.get(zaakDossier.getBetreftOuder(), null));
    bean.setOuderschapVastgesteld(LvOuderType.get(zaakDossier.getBetreftOuder(), null));
    bean.setOuderschapOntkend(LvOuderType.get(zaakDossier.getBetreftOuder(), null));
    bean.setFamRecht(LvOuderType.get(zaakDossier.getBetreftOuder(), null));
    bean.setErkenningDoor(LvOuderType.get(zaakDossier.getBetreftOuder(), null));

    bean.setGeslOuderVastgesteld(zaakDossier.getGeslOuder());
    bean.setGeslOuderGewijzigd(zaakDossier.getGeslOuder());

    bean.setVoornamenOuderVastgesteldAls(zaakDossier.getVoornOuder());

    // Kind
    bean.setKeuzeGeslachtsnaam(KeuzeVaststellingGeslachtsnaam.get(zaakDossier.getKeuzeGesl(), null));

    bean.setGeslachtsnaamIs(zaakDossier.getGesl());
    bean.setGeslachtsnaamVastgesteldAls(zaakDossier.getGesl());
    bean.setGeslachtsnaamGewijzigdIn(zaakDossier.getGesl());
    bean.setGeslachtsnaamKindGewijzigdIn(zaakDossier.getGesl());

    bean.setVoornamenGewijzigd(fil(zaakDossier.getVoorn()));
    bean.setVoornamenGewijzigdIn(zaakDossier.getVoorn());
    bean.setVoornamenVastgesteldAls(zaakDossier.getVoorn());
    bean.setGeslachtsaand(Geslacht.get(zaakDossier.getGeslAand()));

    LvToestemmingType toestemmingType = LvToestemmingType.get(zaakDossier.getToestemming());
    if (LvToestemmingType.ONBEKEND != toestemmingType) {
      bean.setToestemmingDoor(toestemmingType);
    } else {
      if (isBlank(zaakDossier.getToestemming())) {
        bean.setToestemmingDoor(null);
      } else {
        bean.setToestemmingDoor(LvToestemmingType.ANDERS);
        bean.setToestemmingAnders(zaakDossier.getToestemming());
      }
    }

    LvToegepastRechtType toegepastRechtType = LvToegepastRechtType.get(zaakDossier.getToegepastRecht().longValue());
    if (LvToegepastRechtType.ONBEKEND != toegepastRechtType) {
      bean.setToegepastRecht(toegepastRechtType);
    } else {
      if (zaakDossier.getToegepastRecht().longValue() <= 0) {
        bean.setToegepastRecht(null);
      } else {
        bean.setToegepastRecht(LvToegepastRechtType.ANDERS);
        bean.setToegepastRechtAnders(GbaTables.LAND.get(zaakDossier.getToegepastRecht()));
      }
    }

    bean.setGezag(LvGezagType.get(zaakDossier.getGezag(), null));
    bean.setGekozenRecht(zaakDossier.getGekozenRecht());
    bean.setDagVanWijziging(zaakDossier.getDatumWijziging());
    bean.setOuders(getOuders(zaakDossier));
    bean.setAdoptiefouders(getAdoptiefOuders(zaakDossier));

    setBean(bean);

    ProNativeSelect betreftOuderPersoon = getLvField(BETREFT_OUDER_PERSOON, ProNativeSelect.class);
    if (betreftOuderPersoon != null) {
      betreftOuderPersoon.setContainerDataSource(getKeuzeOuderContainer(zaakDossier));
      betreftOuderPersoon.setValue(new KeuzeOuder(zaakDossier.getBetreftOuderPersoon()));
    }
  }

  private KeuzeOuderContainer getKeuzeOuderContainer(DossierLv zaakDossier) {
    return new KeuzeOuderContainer(zaakDossier.getOuders());
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
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(VOORNAMEN_GW_IN.getName()) && getLvField(VOORNAMEN_GW) != null) {
      column.setAppend(true);
    }
    if (property.is(
        TOESTEMMING_ANDERS.getName(),
        BETREFT_OUDER_PERSOON.getName(),
        TOEGEPAST_RECHT_ANDERS.getName())) {
      column.setAppend(true);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    Field betreftOuder = getLvField(LvField.BETREFT_OUDER);
    Field ouderschapOntkend = getLvField(LvField.OUDERSCHAP_ONTKEND);
    Field ouderschapVastgesteld = getLvField(LvField.OUDERSCHAP_VASTGESTELD);
    Field erkenningDoor = getLvField(LvField.ERKENNING_DOOR);
    Field famRecht = getLvField(FAMRECHT);

    setGeslachtsnaam(betreftOuder);
    setGeslachtsnaam(ouderschapOntkend);
    setGeslachtsnaam(ouderschapVastgesteld);
    setGeslachtsnaam(famRecht);
    setGeslachtsnaam(erkenningDoor);
    // Voornaam
    Field voornaamGewijzigd = getLvField(VOORNAMEN_GW);
    Field voornaamGewijzigdIn = getLvField(VOORNAMEN_GW_IN);
    if (voornaamGewijzigd != null && voornaamGewijzigdIn != null) {
      voornaamGewijzigd.addListener((ValueChangeListener) valueChangeEvent -> {
        Boolean gewijzigd = (Boolean) valueChangeEvent.getProperty().getValue();
        voornaamGewijzigdIn.setVisible(gewijzigd);
        repaint();
      });
      voornaamGewijzigdIn.setVisible(voornaamGewijzigd.getValue() != null && (Boolean) voornaamGewijzigd.getValue());
    }

    if (LvType.WIJZIGING_GESLACHTSNAAM_TGV_HUWELIJK_GPS.is(zaakDossier.getSoort())) {
      KeuzeVaststellingGeslContainer container = KeuzeVaststellingGeslContainer.gewijzigd();
      ProNativeSelect field = getLvField(KEUZE_GESLACHTSNAAM, ProNativeSelect.class);
      field.setContainerDataSource(container);
      field.setValue(getBean().getKeuzeGeslachtsnaam());
    }

    Field toestemming = getLvField(TOESTEMMING_DOOR);
    if (toestemming != null) {
      toestemming.addListener((ValueChangeListener) event -> {
        checkToestemmingAnders((LvToestemmingType) event.getProperty().getValue());
      });
      checkToestemmingAnders((LvToestemmingType) toestemming.getValue());
    }

    Field toegepastRecht = getLvField(TOEGEPAST_RECHT);
    if (toegepastRecht != null) {
      toegepastRecht.addListener((ValueChangeListener) event -> {
        checkToegepastRechtAnders((LvToegepastRechtType) event.getProperty().getValue());
      });
      checkToegepastRechtAnders((LvToegepastRechtType) toegepastRecht.getValue());
    }

    super.afterSetBean();
  }

  private void setGeslachtsnaam(Field betreftOuder) {
    Field betreftOuderPersoon = getLvField(BETREFT_OUDER_PERSOON);
    if (betreftOuder != null && betreftOuderPersoon != null) {
      betreftOuder.addListener((ValueChangeListener) valueChangeEvent -> {
        LvOuderType ouderType = (LvOuderType) valueChangeEvent.getProperty().getValue();
        setOuderPersoon(betreftOuderPersoon, ouderType);
      });
      setOuderPersoon(betreftOuderPersoon, (LvOuderType) betreftOuder.getValue());
    }
  }

  private void setOuderPersoon(Field betreftOuderPersoon, LvOuderType ouderType) {
    betreftOuderPersoon.setVisible(MOEDER.equals(ouderType) || VADER.equals(ouderType));

    ifLvField(GESLN_OUDER_VG, field -> field.setVisible(betreftOuderPersoon.isVisible()));
    ifLvField(GESLN_OUDER_GW, field -> field.setVisible(betreftOuderPersoon.isVisible()));

    if (LvType.WETTIGING.is(zaakDossier.getSoort())) {
      ofNullable(getLvField(GESLN_IS)).ifPresent(f -> f.setVisible(betreftOuderPersoon.isVisible()));
    }
    repaint();
  }

  private void checkToestemmingAnders(LvToestemmingType toestemmingType) {
    boolean isAnders = LvToestemmingType.ANDERS == toestemmingType;
    Field anders = getLvField(TOESTEMMING_ANDERS);
    if (isAnders) {
      anders.setVisible(true);
    } else {
      anders.setValue("");
      anders.setVisible(false);
    }
    repaint();
  }

  private void checkToegepastRechtAnders(LvToegepastRechtType type) {
    boolean isAnders = LvToegepastRechtType.ANDERS == type;
    Field anders = getLvField(TOEGEPAST_RECHT_ANDERS);
    if (isAnders) {
      anders.setVisible(true);
    } else {
      anders.setValue("");
      anders.setVisible(false);
    }
    repaint();
  }

  @Override
  public Page40LvBean1 getNewBean() {
    return new Page40LvBean1();
  }

  private static class KeuzeOuderContainer extends ArrayListContainer {

    public KeuzeOuderContainer(List<DossierPersoon> personen) {
      personen.stream().map(KeuzeOuder::new).forEach(this::addItem);
    }
  }
}
