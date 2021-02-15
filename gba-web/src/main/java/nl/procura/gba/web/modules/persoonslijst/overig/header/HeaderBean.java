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

package nl.procura.gba.web.modules.persoonslijst.overig.header;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class HeaderBean implements Serializable {

  public static final String NAAM           = "naam";
  public static final String BSN            = "bsn";
  public static final String ADRES          = "adres";
  public static final String BURGSTAAT      = "burgstaat";
  public static final String ANR            = "anr";
  public static final String STATUS         = "status";
  public static final String GEBOORTEDATUM  = "geboortedatum";
  public static final String GESLACHT       = "geslacht";
  private BaseWKValue        adresIndicatie = null;

  @Field(type = FieldType.LABEL,
      caption = "Naam")
  private String naam = "";

  @Field(type = FieldType.LABEL,
      caption = "BSN")
  private String bsn = "";

  @Field(type = FieldType.LABEL,
      caption = "Adres")
  private String adres = "";

  @Field(type = FieldType.LABEL,
      caption = "Burg. Staat")
  private String burgstaat = "";

  @Field(type = FieldType.LABEL,
      caption = "A-nummer")
  private String anr = "";

  @Field(type = FieldType.LABEL,
      caption = "Status")
  private String status = "";

  @Field(type = FieldType.LABEL,
      caption = "Geboortedatum")
  private String geboortedatum = "";

  @Field(type = FieldType.LABEL,
      caption = "Geslacht")
  private String geslacht = "";

  public void setAdresIndicatie(Services services, BasePLExt pl) {

    // Zoek de adres-indicatie op

    if (pl.getDatasource() == PLEDatasource.PROCURA) {

      Adres adres = pl.getVerblijfplaats().getAdres();

      ZoekArgumenten z = ZoekArgumenten.of(adres);
      z.setGeen_bewoners(true);

      if (z.isGevuld()) {
        List<BaseWKExt> adressen = services.getPersonenWsService().getAdres(z, true).getBasisWkWrappers();

        if (adressen.size() == 1) {
          this.adresIndicatie = adressen.get(0).getBasisWk().getAdres_indicatie();
        }
      }
    }
  }
}
