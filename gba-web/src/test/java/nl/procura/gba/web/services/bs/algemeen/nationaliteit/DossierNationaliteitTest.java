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

package nl.procura.gba.web.services.bs.algemeen.nationaliteit;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import nl.procura.gba.jpa.personen.db.DossNatio;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierNationaliteitTest {

  public static final DossierNationaliteit NATIONALITY_1     = newNationalityDossier("98", "Test NATIONALITY 1");
  public static final DossierNationaliteit NATIONALITY_2     = newNationalityDossier("99", "Test NATIONALITY 2");
  public static final DossierNationaliteit NATIONALITY_DUTCH = newNationalityDossier("1", "Dutch NATIONALITY");

  private final DossierNationaliteit dossierNationaliteit = new DossierNationaliteit();

  @Test
  public void setNationaliteitMustSetCodeAndDescription() {
    // when
    dossierNationaliteit.setNationaliteit(new FieldValue("1", "Test"));
    // then
    assertEquals(BigDecimal.ONE, dossierNationaliteit.getCNatio());
    assertEquals("Test", dossierNationaliteit.getNatio());
  }

  @Test
  public void getNationaliteitOmschrijvingMustReturnFromMotherWhenTypeIsMother() {
    dossierNationaliteit.setNationaliteit(new FieldValue("1", "Test"));
    dossierNationaliteit.setAfgeleidVan(DossierPersoonType.MOEDER);
    // when
    String nationaliteitOmschrijving = dossierNationaliteit.getNationaliteitOmschrijving();
    // then
    assertEquals("Test (afgeleid van de moeder)", nationaliteitOmschrijving);
  }

  @Test
  public void getNationaliteitOmschrijvingMustReturnDescriptionWhenTypeIsUnknown() {
    dossierNationaliteit.setNationaliteit(new FieldValue("1", "Test"));
    dossierNationaliteit.setAfgeleidVan(DossierPersoonType.ONBEKEND);
    // when
    String nationaliteitOmschrijving = dossierNationaliteit.getNationaliteitOmschrijving();
    // then
    assertEquals("Test", nationaliteitOmschrijving);
  }

  public static DossierNationaliteit newNationalityDossier(String code, String description) {
    DossierNationaliteit dossierNationaliteit = new DossierNationaliteit();
    FieldValue testNationality = new FieldValue(code, description);
    dossierNationaliteit.setNationaliteit(testNationality);
    return dossierNationaliteit;
  }

  public static DossNatio nationalityWithCode(List<DossNatio> dossiers, BigDecimal code) {
    return dossiers.stream()
        .filter(natio -> natio.getCNatio().equals(code))
        .findFirst().orElseThrow(AssertionError::new);
  }
}
