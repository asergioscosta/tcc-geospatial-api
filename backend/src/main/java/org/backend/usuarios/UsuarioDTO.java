package org.backend.usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String login;
    private String name;
    private UserRole userRole;

    public static UsuarioDTO create(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getName(),
                usuario.getUserRole()
        );
    }
}