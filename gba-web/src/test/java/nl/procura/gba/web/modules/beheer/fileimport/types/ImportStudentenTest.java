/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.types;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.InputStream;
import java.util.List;

import nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport;
import oracle.sql.Datum;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportResult;
import nl.procura.standard.Resource;

import lombok.SneakyThrows;

public class ImportStudentenTest {

  @SneakyThrows
  @Test
  public void mustValidateStudentenImport() {
    InputStream stream = Resource.getAsInputStream("csvparser/studenten-import-test.csv");
    new RegistrantImport().convert(IOUtils.toByteArray(stream), null);
  }

  @Test(timeout = 2000)
  public void mustValidationLargeFilesWithInTwoSeconds() {
    StringBuilder sb = new StringBuilder();
    sb.append("Achternaam;Voorvoegsel;Voornamen;Geslacht;Geboortedatum;Geboorteplaats;Geboorteland;"
        + "Nationaliteit;Straatnaam;Huisnummer;Huisletter;Toevoeging;Postcode;Woonplaats;E-mail\n");
    for (int i = 0; i < 10000; i++) {
      sb.append("Jöhnson;;Jöhn;Man;1-2-2000;North Carolina;Verenigde Staten van Amerika;"
          + "Onbekend;Dorpsstraat;1;A;B;1234AA;Nijmegen;test@procura.nl\n");
    }
    List<FileImportRecord> records = new RegistrantImport().convert(sb.toString().getBytes(UTF_8), null).getRecords();
    Assert.assertEquals("M", records.get(0).get(RegistrantImport.GESLACHT).getValue());
    Assert.assertEquals("20000201", records.get(0).get(RegistrantImport.GEBOORTEDATUM).getValue());
    Assert.assertEquals("", records.get(0).get(RegistrantImport.NATIONALITEIT).getValue());
    Assert.assertTrue(records.get(0).isValid());
  }

  @Test
  public void mustNotValidateWrongDate() {
    String sb = "Achternaam;Voorvoegsel;Voornamen;Geslacht;Geboortedatum;Geboorteplaats;Geboorteland;Nationaliteit;"
        + "Straatnaam;Huisnummer;Huisletter;Toevoeging;Postcode;Woonplaats;E-mail;Telefoon;Referentienummer;Land van vorig adres;"
        + "Datum ingang bewoning;Opmerkingen\n"
        + "Jöhnson;;Jöhn;Y;1-13-2000;North Carolina;Verenigde Staten van Amerika;Amerikaans Burger;"
        + "Dorpsstraat;1;A;B;1234AA;Nijmegen;test@procura.nl;06-1234566;443242342;Verenigde Staten van Amerika;"
        + "01-04-2023;Geen";
    FileImportResult result = new RegistrantImport().convert(sb.getBytes(UTF_8), null);
    Assert.assertEquals(1, result.getRemarks().size());
    Assert.assertEquals("1 van de 1 regels bevatten fouten", result.getRemarks().get(0));
    Assert.assertEquals("De geslachtaanduiding is niet geldig, de geboortedatum heeft geen geldig formaat",
        result.getRecords().get(0).getRemarksAsString());
  }
}
