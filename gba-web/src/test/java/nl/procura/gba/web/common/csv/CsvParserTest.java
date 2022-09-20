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

package nl.procura.gba.web.common.csv;

import static org.junit.Assert.assertEquals;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.procura.gba.web.common.csv.CsvParser.Csv;
import nl.procura.standard.Resource;
import nl.procura.standard.exceptions.ProException;

/*
Lower-, uppercase, leading, trailing spaces.

Geslachtsnaam;   geboortedatum    ;voornaam
  Duck     ;09-06-1934;Donald
"Puk";1-1-2000;Pietje
 */
public class CsvParserTest {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void mustAllowCaseInsensitiveHeader() {
    Csv csv = getCorrectCsv();
    assertEquals("Geslachtsnaam", csv.getHeaders().getByName("geslachtsnaam").toString()); // Lowercase search
    assertEquals("Voornaam", csv.getHeaders().getByName("voornaam").toString()); // Lowercase in csv
    assertEquals("Geboortedatum", csv.getHeaders().getByName(" geboortedatum ").toString()); // With extra spaces
  }

  @Test
  public void mustNowAllowCaseWrongHeader() {
    exceptionRule.expect(ProException.class);
    exceptionRule.expectMessage("Kolom 'Achternaam' ontbreekt");
    Csv csv = getCorrectCsv();
    assertEquals("Geslachtsnaam", csv.getHeaders().getByName("achternaam").toString());
  }

  @Test
  public void mustAllowCaseInsensitiveValues() {
    Csv csv = getCorrectCsv();
    assertEquals("Duck", csv.getLines().get(0).getValue("geslachtsnaam").toString());
    assertEquals("Duck", csv.getLines().get(0).getValue("Geslachtsnaam").toString());
    assertEquals("Duck", csv.getLines().get(0).getValue(" Geslachtsnaam ").toString());
    assertEquals("Donald", csv.getLines().get(0).getValue("voornaam").toString());
    assertEquals("09-06-1934", csv.getLines().get(0).getValue("Geboortedatum").toString());
    assertEquals("Puk", csv.getLines().get(1).getValue("geslachtsnaam").toString());
  }

  @Test
  public void mustNowAllowCaseUnknownValues() {
    exceptionRule.expect(ProException.class);
    exceptionRule.expectMessage("Kolom 'Achternaam' ontbreekt");
    Csv csv = getCorrectCsv();
    assertEquals("Donald", csv.getLines().get(0).getValue("Achternaam").toString());
  }

  /**
   * Occurs in CSV, not in config
   */
  @Test
  public void mustNotAllowIncorrectHeaders() {
    Csv csv = CsvParser.parse(CsvConfig.builder()
        .header("Voornaam").and()
        .header("Geslachtsnaam").and()
        .header("Bla").and()
        .content(toContent())
        .build());

    Assert.assertEquals(2, csv.getRemarks().size());
    Assert.assertEquals("Kolom 'Geboortedatum' hoort in niet het bestand", csv.getRemarks().get(0));
    Assert.assertEquals("Kolom 'Bla' ontbreekt in het bestand", csv.getRemarks().get(1));
  }

  @Test
  public void mustSupportConversions() {
    Csv csv = CsvParser.parse(CsvConfig.builder()
        .converter(String::toUpperCase) // Converter for all values
        .header("Voornaam").and()
        .header("Geslachtsnaam").and()
        .header("Geboortedatum").converter(value -> "20000201").and() // Specific header converter
        .content(toContent())
        .build());
    Assert.assertEquals("DONALD", csv.getLines().get(0).getValue("Voornaam").getValue());
    Assert.assertEquals("20000201", csv.getLines().get(0).getValue("Geboortedatum").getValue());
  }

  private Csv getCorrectCsv() {
    return CsvParser.parse(CsvConfig.builder()
        .header("Voornaam").and()
        .header("Geslachtsnaam").and()
        .header("Geboortedatum").and()
        .content(toContent())
        .build());
  }

  @SneakyThrows
  private byte[] toContent() {
    return IOUtils.toByteArray(Resource.getAsInputStream("csvparser/test.csv"));
  }
}
