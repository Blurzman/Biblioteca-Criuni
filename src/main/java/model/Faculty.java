package model;

import java.io.Serializable;

/**
 * Enum representing the faculties.
 *
 * @author Samuel
 */
public enum Faculty implements Serializable {
    MEDICINE("Facultad de Medicina"),
    ENGINEERING("Facultad de Ingeniería"),
    HUMANITIES("Facultad de Humanidades, Ciencias Sociales y Cultura Guaraní"),
    SCIENCE("Facultad de Ciencias y Tecnología"),
    ECONOMICS("Facultad de Ciencias Económicas y Administrativas"),
    LAWSCIENCE("Facultad De Ciencias Jurídicas"),
    AGRONOMY("Facultad de Ciencias Agropecuarias y Forestales");


    private final String displayName;

    Faculty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return this.displayName;
    }
}
