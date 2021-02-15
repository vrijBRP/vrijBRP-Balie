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

package nl.procura.gba.web.modules.bs.registration.relatives;

import static nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.vaadin.ui.Field;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.bs.registration.page40.relations.partner.PartnerDetailsBean;
import nl.procura.gba.web.modules.bs.registration.page40.relations.partner.PartnerRelationForm1;
import nl.procura.gba.web.services.bs.registration.CommitmentType;
import nl.procura.gba.web.services.gba.tabellen.TabellenServiceMock;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class PartnerRelationFormTest {

  public PartnerRelationFormTest() {
    TabellenServiceMock.init();
  }

  @Test
  public void notNetherlandsMustShowForeignMunicipality() {
    // given
    DossPersRelation relation = new DossPersRelation();
    relation.setCustomStartDate(BigDecimal.valueOf(2018_01_31));
    relation.setStartDateCountry(TEST_COUNTRY_CODE);
    String customMunicipality = "Custom Municipality";
    relation.setStartDateMunicipality(customMunicipality);
    relation.setCommitmentType(CommitmentType.MARRIAGE.getCode());

    // when
    PartnerRelationForm1 form = new PartnerRelationForm1(relation);

    // then
    GbaDateFieldValue startDate = (GbaDateFieldValue) form.getField(PartnerDetailsBean.F_START_DATE).getValue();
    assertEquals("31-01-2018", startDate.toString());
    FieldValue startCountry = (FieldValue) form.getField(PartnerDetailsBean.F_START_COUNTRY).getValue();
    assertEquals(TEST_COUNTRY_DESC, startCountry.toString());
    Field startMunicipality = form.getField(PartnerDetailsBean.F_START_MUNICIPALITY);
    assertFalse(startMunicipality.isVisible());
    Field startForeignMunicipality = form.getField(PartnerDetailsBean.F_START_FOREIGN_MUNICIPALITY);
    assertTrue(startForeignMunicipality.isVisible());
    assertEquals(customMunicipality, startForeignMunicipality.getValue().toString());
    assertEquals(CommitmentType.MARRIAGE, form.getField(PartnerDetailsBean.F_COMMITMENT_TYPE).getValue());
  }

  @Test
  public void netherlandsMustShowMunicipalityList() {
    // given
    DossPersRelation relation = new DossPersRelation();
    relation.setCustomStartDate(BigDecimal.valueOf(2018_01_31));
    relation.setStartDateCountry(NETHERLANDS_COUNTRY_CODE);
    relation.setStartDateMunicipality(TEST_MUNICIPALITY_CODE);
    relation.setCommitmentType(CommitmentType.GPS.getCode());

    // when
    PartnerRelationForm1 form = new PartnerRelationForm1(relation);

    GbaDateFieldValue startDate = (GbaDateFieldValue) form.getField(PartnerDetailsBean.F_START_DATE).getValue();
    assertEquals("31-01-2018", startDate.toString());
    FieldValue startCountry = (FieldValue) form.getField(PartnerDetailsBean.F_START_COUNTRY).getValue();
    assertEquals(NETHERLANDS_COUNTRY_DESC, startCountry.toString());
    Field startMunicipality = form.getField(PartnerDetailsBean.F_START_MUNICIPALITY);
    assertTrue(startMunicipality.isVisible());
    assertEquals(TEST_MUNICIPALITY_DESC, startMunicipality.getValue().toString());
    Field startForeignMunicipality = form.getField(PartnerDetailsBean.F_START_FOREIGN_MUNICIPALITY);
    assertFalse(startForeignMunicipality.isVisible());
    assertEquals(CommitmentType.GPS, form.getField(PartnerDetailsBean.F_COMMITMENT_TYPE).getValue());
  }
}
