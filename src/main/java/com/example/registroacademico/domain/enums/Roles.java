package com.example.registroacademico.domain.enums;

public enum Roles {
    // 1. Definir las constantes pasando el número al constructor
    ROLE_ADMIN(1),
    ROLE_PROFESOR(2),
    ROLE_ESTUDIANTE(3); // Nota el punto y coma aquí

    // 2. Atributo privado para guardar el valor numérico
    private final int id;

    // 3. Constructor (siempre es privado en un enum, no lleva modificador de acceso)
    Roles(int id) {
        this.id = id;
    }

    // 4. Método para obtener el valor numérico
    public int getId() {
        return id;
    }

    public static Roles buscarPorId(int id) {
        for (Roles rol : Roles.values()) {
            if (rol.getId() == id) {
                return rol; // Retorna el rol si lo encuentra
            }
        }
        // Puedes lanzar una excepción o retornar null si el ID no existe
        throw new IllegalArgumentException("No existe un rol con el ID: " + id);
    }
}
