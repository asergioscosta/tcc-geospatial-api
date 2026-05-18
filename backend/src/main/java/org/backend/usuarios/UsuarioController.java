package org.backend.usuarios;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.UserRole;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> get() {
        List<UsuarioDTO> usuarios = usuarioService.getUsuarios()
                .stream()
                .map(UsuarioDTO::create)
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado");
        }

        return ResponseEntity.ok(UsuarioDTO.create(usuario.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody UsuarioRequestDTO dto) {
        try {
            if (usuarioRepository.existsByLogin(dto.getLogin())) {
                return ResponseEntity.badRequest().body("Login já está em uso");
            }

            Usuario usuario = new Usuario();
            usuario.setName(dto.getName());
            usuario.setLogin(dto.getLogin());
            usuario.setPassword(dto.getPassword());
            usuario.setUserRole(UserRole.USER);

            usuario = usuarioService.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDTO.create(usuario));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid CredenciaisDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        var usuario = (Usuario) auth.getPrincipal();
        var token = tokenService.generateToken(usuario);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", usuario.getId());
        userData.put("name", usuario.getName());
        userData.put("login", usuario.getLogin());
        userData.put("admin", usuario.isAdmin());
        userData.put("userRole", usuario.getUserRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userData);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        try {
            var usuarioExistente = usuarioService.getUsuarioById(id);

            if (usuarioExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }

            Usuario usuario = usuarioExistente.get();

            if (dto.getLogin() != null &&
                    !usuario.getLogin().equals(dto.getLogin()) &&
                    usuarioRepository.existsByLogin(dto.getLogin())) {
                return ResponseEntity.badRequest().body("Login já está em uso");
            }

            if (dto.getName() != null) {
                usuario.setName(dto.getName());
            }

            if (dto.getLogin() != null) {
                usuario.setLogin(dto.getLogin());
            }

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPassword(dto.getPassword());
            }

            if (dto.getUserRole() != null) {
                usuario.setUserRole(dto.getUserRole());
            }

            usuario = usuarioService.save(usuario);

            return ResponseEntity.ok(UsuarioDTO.create(usuario));
        } catch (RegraNegocioInvalidaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var usuario = usuarioService.getUsuarioById(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        usuarioService.delete(usuario.get());
        return ResponseEntity.noContent().build();
    }
}