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

package nl.procura.gba.web.modules.bs.erkenning.page25;

import static ch.lambdaj.Lambda.joinFrom;
import static nl.procura.standard.Globalfunctions.astr;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.BeanHandler;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page25ErkenningBean1 implements Serializable {

  public static final String NAT_MOEDER = "natMoeder";
  public static final String VBT_MOEDER = "vbtMoeder";
  public static final String VB_MOEDER  = "vbMoeder";
  public static final String NAT_KIND   = "natKind";
  public static final String VBT_KIND   = "vbtKind";
  public static final String VB_KIND    = "vbKind";

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit(en) moeder")
  private String natMoeder = "";

  @Field(type = FieldType.LABEL,
      caption = "Verblijfstitelcode")
  private String vbtMoeder = "";

  @Field(type = FieldType.LABEL,
      caption = "Verblijfplaats moeder in")
  private String vbMoeder = "";

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit(en) kind")
  private String natKind = "";

  @Field(type = FieldType.LABEL,
      caption = "Verblijfstitelcode")
  private String vbtKind = "";

  @Field(type = FieldType.LABEL,
      caption = "Verblijfplaats kind in")
  private String vbKind = "";

  public Page25ErkenningBean1(DossierErkenning dossier) {

    setNatMoeder(dossier.getMoeder().getNationaliteitenOmschrijving());
    setVbtMoeder(dossier.getMoeder().getVerblijfstitelOmschrijving());
    setVbMoeder(dossier.getMoeder().getLand().getDescription());

    if (dossier.isBestaandKind()) {
      List<DossierNationaliteit> nationaliteiten = new ArrayList<>();
      for (DossierPersoon kind : dossier.getKinderen()) {
        setVbtKind(astr(kind.getVerblijfstitel()));
        setVbKind(astr(kind.getLand()));
        nationaliteiten.addAll(kind.getNationaliteiten());
        break;
      }
      if (dossier.getDossierGeboorte() != null) {
        nationaliteiten.addAll(dossier.getDossierGeboorte().getDossier().getNationaliteiten());
      }
      if (nationaliteiten.size() > 0) {
        setNatKind(joinFrom(nationaliteiten).getNationaliteitOmschrijving());
      }
    } else {
      setNatKind("Geen");
      setVbtKind("Geen");
      setVbKind("Geen");
    }

    BeanHandler.trim(this);
  }

  public String getNatKind() {
    return natKind;
  }

  public void setNatKind(String natKind) {
    this.natKind = natKind;
  }

  public String getNatMoeder() {
    return natMoeder;
  }

  public void setNatMoeder(String nat) {
    this.natMoeder = nat;
  }

  public String getVbKind() {
    return vbKind;
  }

  public void setVbKind(String vbKind) {
    this.vbKind = vbKind;
  }

  public String getVbMoeder() {
    return vbMoeder;
  }

  public void setVbMoeder(String vb) {
    this.vbMoeder = vb;
  }

  public String getVbtKind() {
    return vbtKind;
  }

  public void setVbtKind(String vbtKind) {
    this.vbtKind = vbtKind;
  }

  public String getVbtMoeder() {
    return vbtMoeder;
  }

  public void setVbtMoeder(String vbt) {
    this.vbtMoeder = vbt;
  }
}
