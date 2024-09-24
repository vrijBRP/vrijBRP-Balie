package nl.procura.gba.web.services.beheer.zynyo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class SignedDocument {
    private String name;
    private String documentUUID;
    private String documentContent;

}
