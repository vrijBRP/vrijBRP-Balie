package nl.procura.diensten.gba.ple.extensions.formats;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLValue;

public class NaamTest {

  @Test
  public void mustShowCorrectInitials() {
    BasePLElem voornamen = getBasePLElem("Cle-xaïs Ruth -Maria Sigmélie Migdailine Amelia");
    BasePLElem geslachtsnaam = getBasePLElem("Bakker");
    BasePLElem voorvoegsel = new BasePLElem();
    BasePLElem titel = new BasePLElem();
    BasePLElem naamgebruik = new BasePLElem();
    Naam partner = null;

    Naam naam = new Naam(voornamen, geslachtsnaam, voorvoegsel, titel, naamgebruik, partner);
    Assert.assertEquals("C-x.R.M.S.M.A.", naam.getInit());
    Assert.assertEquals("C.R.M.S.M.A.", naam.getInitNen());
  }

  private static BasePLElem getBasePLElem(String value) {
    BasePLElem elem = new BasePLElem();
    elem.setValue(new BasePLValue(value, value));
    return elem;
  }
}
