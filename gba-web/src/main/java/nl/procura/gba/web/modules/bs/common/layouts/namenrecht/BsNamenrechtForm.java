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

package nl.procura.gba.web.modules.bs.common.layouts.namenrecht;

import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.EERSTE_KIND_TYPE;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.GESLACHTSNAAM;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.KIND_OUDER_DAN_16;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.NAAMSKEUZE_PERSOON;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.NAAMSKEUZE_TYPE;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.RECHT;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.TITEL;
import static nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtBean.VOORV;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.ERKENNER;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.PARTNER;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.VADER_DUO_MOEDER;
import static nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType.NAAMSKEUZE_VOOR_GEBOORTE;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.Arrays;

import com.vaadin.ui.Label;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.common.utils.BsNamenRechtUtils;
import nl.procura.gba.web.modules.bs.common.utils.BsOuderUtils;
import nl.procura.gba.web.modules.zaken.common.ToelichtingButton;
import nl.procura.gba.web.modules.zaken.common.ToelichtingWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.EersteKindType;
import nl.procura.gba.web.services.bs.erkenning.KindLeeftijdsType;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class BsNamenrechtForm extends GbaForm<BsNamenrechtBean> {

  private final DossierNamenrecht dossier;
  private final Services          services;
  private boolean                 readOnlyAllFields = false;
  private NaamsPersoonType        naamsPersoonType  = NaamsPersoonType.ONBEKEND;
  private Runnable                updateListener;

  public BsNamenrechtForm(Services services, DossierNamenrecht dossier) {
    this.services = services;
    this.dossier = dossier;

    setColumnWidths("170px", "");
    setReadThrough(true);
    setOrder(RECHT, KIND_OUDER_DAN_16, GESLACHTSNAAM, VOORV, TITEL,
        EERSTE_KIND_TYPE, NAAMSKEUZE_PERSOON, NAAMSKEUZE_TYPE);
    setReadOnlyAllFields(false);
  }

  public void setUpdateListener(Runnable updateListener) {
    this.updateListener = updateListener;
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {
    if (property.is(GESLACHTSNAAM)) {
      Label label = new Label("Druk op \"Naamselectie\" om de naam te selecteren.");
      label.setWidth("550px");
      column.addComponent(label);
    }
    if (property.is(TITEL)) {
      if (dossier instanceof DossierNaamskeuze) {
        column.addComponent(new ToelichtingButton() {

          @Override
          public void buttonClick(ClickEvent event) {
            String msg = "Een adellijke titel of een adellijk predikaat gaat alleen op de kinderen " +
                "over als zij de geslachtsnaam van hun adellijke vader verkrijgen.";
            getWindow().addWindow(new ToelichtingWindow("Voorwaarden adellijke titel / adellijk predikaat", msg));
          }
        });
      }
    }

    if (property.is(RECHT)) {
      column.addComponent(new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.NAMENRECHT, 0) {

        @Override
        public long getCode() {
          return getFieldValueCode(RECHT);
        }
      });
    }

    super.afterSetColumn(column, field, property);
  }

  public void checkRecht() {
    getField(RECHT, GbaNativeSelect.class).setDataSource(new BsNamenrechtContainer(services, dossier));
  }

  public NaamsPersoonType getNaamsPersoonType() {
    return naamsPersoonType;
  }

  @Override
  public BsNamenrechtBean getNewBean() {
    return new BsNamenrechtBean();
  }

  public boolean isReadOnlyAllFields() {
    return readOnlyAllFields;
  }

  public void setReadOnlyAllFields(boolean readOnlyAllFields) {
    this.readOnlyAllFields = readOnlyAllFields;
  }

  @Override
  public com.vaadin.ui.Field newField(com.vaadin.ui.Field field, Property property) {
    if (readOnlyAllFields && !property.is(GESLACHTSNAAM, VOORV, TITEL)) {
      field.setReadOnly(true);
    }
    return super.newField(field, property);
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    addListener(RECHT, GESLACHTSNAAM, VOORV, TITEL, EERSTE_KIND_TYPE, NAAMSKEUZE_PERSOON);
    setKindGegevens();
    onFieldChanged();
    setNaamskeuzeVeld(dossier.getNaamskeuzeType());
  }

  public void setDefaultNaamskeuzePersoon(NaamsPersoonType naamsPersoonType) {
    this.naamsPersoonType = naamsPersoonType;
  }

  public void setNaam(String geslachtsnaam, FieldValue voorvoegsel, FieldValue titel) {
    setValue(GESLACHTSNAAM, geslachtsnaam);
    getBean().setGeslachtsnaam(geslachtsnaam);

    setValue(VOORV, voorvoegsel);
    getBean().setVoorv(voorvoegsel);

    setValue(TITEL, titel);
    getBean().setTitel(titel);
  }

  public void setNewNaamskeuzePersoon(NaamsPersoonType naamsPersoonType) {
    setDefaultNaamskeuzePersoon(naamsPersoonType);
    setNaamskeuzePersoonVeld(naamsPersoonType);
  }

  public void setToegepastRechtVeld(FieldValue land) {
    setValue(RECHT, land);
    getBean().setRecht(land);
  }

  public void toDossier() {

    commit();

    dossier.setLandNaamRecht(getBean().getRecht());
    dossier.setKeuzeGeslachtsnaam(getBean().getGeslachtsnaam());
    dossier.setKeuzeVoorvoegsel(astr(getBean().getVoorv()));
    dossier.setKeuzeTitel(getBean().getTitel());
    dossier.setEersteKindType(getBean().getEersteKindType());
    dossier.setNaamskeuzePersoon(getBean().getNaamskeuzePersoon());
    dossier.setNaamskeuzeType(getBean().getNaamskeuzeType());

    // Namen toekennen
    if (dossier instanceof DossierGeboorte) {
      for (DossierPersoon kind : dossier.getKinderen()) {
        kind.setGeslachtsnaam(getBean().getGeslachtsnaam());
        kind.setVoorvoegsel(astr(getBean().getVoorv()));
        kind.setTitel(getBean().getTitel());
      }
    }

    setNaamskeuzeKinderen();
  }

  private void addListener(String... fields) {
    Arrays.stream(fields).forEach(field -> {
      getField(field).addListener(new FieldChangeListener<Object>() {

        @Override
        public void onChange(Object value) {
          onFieldChanged();
        }
      });
    });
  }

  private String getNaamErkenner() {
    return trim(dossier.getVaderErkenner().getVoorv() + " " + dossier.getVaderErkenner().getGeslachtsnaam());
  }

  private String getNaamMoeder() {
    return trim(dossier.getMoeder().getVoorv() + " " + dossier.getMoeder().getGeslachtsnaam());
  }

  public void initBean() {
    String keuzeGeslachtsnaam = "";
    String keuzeVoorv = "";
    FieldValue keuzeTitel = new FieldValue();

    if (dossier instanceof DossierGeboorte) {
      DossierGeboorte geboorte = to(dossier, DossierGeboorte.class);
      if (geboorte.getVragen().heeftErkenningVoorGeboorte()) {
        DossierNamenrecht dn = geboorte.getErkenningVoorGeboorte();

        keuzeGeslachtsnaam = dn.getKeuzeGeslachtsnaam();
        keuzeVoorv = dn.getKeuzeVoorvoegsel();
        keuzeTitel = dn.getKeuzeTitel();

        dossier.setEersteKindType(dn.getEersteKindType());
        dossier.setNaamskeuzePersoon(dn.getNaamskeuzePersoon());
        dossier.setNaamskeuzeType(dn.getNaamskeuzeType());
        dossier.setLandNaamRecht(dn.getLandNaamRecht());
        setReadOnlyAllFields(true);

      } else if (geboorte.getVragen().heeftErkenningBuitenProweb()) {
        ErkenningBuitenProweb dn = geboorte.getErkenningBuitenProweb();
        dossier.setEersteKindType(EersteKindType.ONBEKEND);

        if (dn.getNaamskeuzePersoon() == ERKENNER) {
          dossier.setNaamskeuzePersoon(ERKENNER);
          keuzeGeslachtsnaam = geboorte.getVader().getGeslachtsnaam();
          keuzeVoorv = geboorte.getVader().getVoorvoegsel();
          keuzeTitel = geboorte.getVader().getTitel();

        } else if (dn.getNaamskeuzePersoon() == NaamsPersoonType.MOEDER) {
          dossier.setNaamskeuzePersoon(NaamsPersoonType.MOEDER);
          keuzeGeslachtsnaam = geboorte.getMoeder().getGeslachtsnaam();
          keuzeVoorv = geboorte.getMoeder().getVoorvoegsel();
          keuzeTitel = geboorte.getMoeder().getTitel();
        }

        dossier.setNaamskeuzeType(dn.getNaamskeuzeType());

      } else if (geboorte.getVragen().heeftNaamskeuzeVoorGeboorte()) {
        DossierNaamskeuze nk = geboorte.getNaamskeuzeVoorGeboorte();
        keuzeGeslachtsnaam = nk.getKeuzeGeslachtsnaam();
        keuzeVoorv = nk.getKeuzeVoorvoegsel();
        keuzeTitel = nk.getKeuzeTitel();

        dossier.setEersteKindType(EersteKindType.JA);
        dossier.setNaamskeuzeType(NaamskeuzeVanToepassingType.JA);
        setReadOnlyAllFields(true);

      } else if (geboorte.getVragen().heeftNaamskeuzeBuitenProweb()) {
        setReadOnlyAllFields(true);
      }
    }

    setDefaultNaamskeuzePersoon(dossier.getNaamskeuzePersoon());

    BsNamenrechtBean bean = new BsNamenrechtBean();
    bean.setGeslachtsnaam(defaultIfBlank(dossier.getKeuzeGeslachtsnaam(), keuzeGeslachtsnaam));
    bean.setVoorv(new FieldValue(defaultIfBlank(dossier.getKeuzeVoorvoegsel(), keuzeVoorv)));
    bean.setTitel(
        new FieldValue(defaultIfBlank(dossier.getKeuzeTitel().getStringValue(), keuzeTitel.getStringValue())));

    bean.setKindOuderDan16(KindLeeftijdsType.OUDER_DAN_16.equals(dossier.getKindLeeftijdsType()) ? "Ja" : "Nee");
    if (dossier instanceof DossierNaamskeuze) {
      DossierNaamskeuze nk = (DossierNaamskeuze) dossier;
      if (nk.getDossierNaamskeuzeType() == NAAMSKEUZE_VOOR_GEBOORTE) {
        bean.setKindOuderDan16("Niet van toepassing");
      }
    }

    bean.setRecht(BsNamenRechtUtils.getNamenRecht(services, dossier.getLandNaamRecht(), dossier));
    bean.setNaamskeuzePersoon(dossier.getNaamskeuzePersoon());
    bean.setEersteKindType(dossier.getEersteKindType());
    bean.setNaamskeuzeType(dossier.getNaamskeuzeType());

    setBean(bean);
  }

  public String getGeslachtsnaam() {
    return astr(getField(GESLACHTSNAAM).getValue());
  }

  public String getVoorvoegsel() {
    return astr(getField(VOORV).getValue());
  }

  public FieldValue getTitel() {
    return (FieldValue) getField(TITEL).getValue();
  }

  private void onFieldChanged() {
    String naam = trim(astr(getValue(VOORV)) + " " + astr(getValue(GESLACHTSNAAM)));
    String naamMoeder = getNaamMoeder();
    String naamErkenner = getNaamErkenner();

    NaamsPersoonType partnerType = getCaseSpecificPartnerType();
    setNaamskeuzePersoonVeld(NaamsPersoonType.getNaamsPersoonType(naam, naamMoeder, naamErkenner, partnerType));

    boolean isNederlandsRecht = Landelijk.getNederland().equals(getField(RECHT).getValue());
    boolean eersteKindJa = EersteKindType.JA.equals(getValue(EERSTE_KIND_TYPE));
    boolean binnenHuwelijk = dossier.isGeborenBinnenHuwelijk();
    NaamsPersoonType naamsPersoonType = getValue(NAAMSKEUZE_PERSOON, NaamsPersoonType.class);
    boolean moederNaam = naamsPersoonType.is(NaamsPersoonType.MOEDER);
    boolean partnerNaam = naamsPersoonType.is(ERKENNER, VADER_DUO_MOEDER, PARTNER);

    if (!dossier.getVaderErkenner().isVolledig()) {
      // Geen vader, partner of erkenner, dus eerste kind is niet van toepassing
      setEersteKindVeld(EersteKindType.NVT);
      getField(EERSTE_KIND_TYPE).setReadOnly(true);
    }

    if (isNederlandsRecht) {
      setNaamskeuzeVeld(NaamskeuzeVanToepassingType.NVT);

      if (dossier.getKindLeeftijdsType() == KindLeeftijdsType.OUDER_DAN_16) {
        setNaamskeuzeVeld(NaamskeuzeVanToepassingType.JA);

      } else if (eersteKindJa) {
        if (naamsPersoonType.getCombiTypes() != null) { // Dubbele naam dan altijd ja
          setNaamskeuzeVeld(NaamskeuzeVanToepassingType.JA);

        } else if (binnenHuwelijk && moederNaam) {
          setNaamskeuzeVeld(NaamskeuzeVanToepassingType.JA);

        } else if (!binnenHuwelijk && partnerNaam) {
          setNaamskeuzeVeld(NaamskeuzeVanToepassingType.JA);

        } else if (!binnenHuwelijk && moederNaam) {
          setNaamskeuzeVeld(NaamskeuzeVanToepassingType.NEE);

        } else if (binnenHuwelijk && partnerNaam) {
          setNaamskeuzeVeld(NaamskeuzeVanToepassingType.NEE);
        }
      }
    }

    if (dossier instanceof DossierNaamskeuze) {
      getField(NAAMSKEUZE_TYPE).setVisible(false);
    }

    if (updateListener != null) {
      updateListener.run();
    }

    repaint();
  }

  private void setEersteKindVeld(EersteKindType type) {
    setValue(EERSTE_KIND_TYPE, type);
    getBean().setEersteKindType(type);
  }

  private void setKindGegevens() {
    boolean eersteKindOnbekend = EersteKindType.ONBEKEND.equals(dossier.getEersteKindType());

    if (eersteKindOnbekend) {
      boolean isMatchMoederBsn = pos(BsOuderUtils.getEerdereKinderen(services, dossier, false, false));
      boolean isMatchVaderBsn = pos(BsOuderUtils.getEerdereKinderen(services, dossier, true, false));

      boolean isMatchMoederNaam = pos(BsOuderUtils.getEerdereKinderen(services, dossier, false, true));
      boolean isMatchVaderNaam = pos(BsOuderUtils.getEerdereKinderen(services, dossier, true, true));

      if (isMatchMoederBsn && isMatchVaderBsn) { // Niet pre-fillen
        setValue(EERSTE_KIND_TYPE, EersteKindType.NEE);

      } else {
        setValue(EERSTE_KIND_TYPE, EersteKindType.JA);
      }

      if (!(isMatchMoederNaam && isMatchVaderNaam)) {
        if (isMatchMoederNaam) {
          setKindVelden(dossier.getMoeder());

        } else if (isMatchVaderNaam) {
          setKindVelden(dossier.getVaderErkenner());
        }
      }
    }
  }

  private void setKindVelden(DossierPersoon persoon) {
    setValue(GESLACHTSNAAM, persoon.getGeslachtsnaam());
    setValue(VOORV, fil(persoon.getVoorvoegsel()) ? new FieldValue(persoon.getVoorvoegsel()) : null);
    setValue(TITEL, persoon.getTitel());
  }

  private void setNaamskeuzeKinderen() {

    int kindNr = 0;
    for (DossierPersoon kind : dossier.getKinderen()) {
      kindNr++;
      NaamskeuzeVanToepassingType type = dossier.getNaamskeuzeType();

      if (type == NaamskeuzeVanToepassingType.JA) {
        // Eerste kind heeft naamskeuze
        kind.setNaamskeuzeType(kindNr == 1
            ? NaamskeuzeVanToepassingType.JA
            : NaamskeuzeVanToepassingType.NEE);

      } else {
        kind.setNaamskeuzeType(type);
      }

      getApplication().getServices().getDossierService().savePersoon(kind);
    }
  }

  private void setNaamskeuzePersoonVeld(NaamsPersoonType type) {
    setValue(NAAMSKEUZE_PERSOON, type);
    getBean().setNaamskeuzePersoon(type);
  }

  private void setNaamskeuzeVeld(NaamskeuzeVanToepassingType type) {
    setValue(NAAMSKEUZE_TYPE, type);
    getBean().setNaamskeuzeType(type);
  }

  private NaamsPersoonType getCaseSpecificPartnerType() {
    if (dossier instanceof DossierErkenning) {
      return ERKENNER;

    } else if (dossier instanceof DossierNaamskeuze) {
      return PARTNER;

    } else {
      return VADER_DUO_MOEDER;
    }
  }
}
