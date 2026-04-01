package org.backend.usuarios;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.enums.UserRole;
import org.backend.exception.RecursoNaoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private final TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping()
    public ResponseEntity get() {
        List<Usuario> usuarios = usuarioService.getUsuarios();
        return ResponseEntity.ok(usuarios.stream().map(UsuarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario.map(UsuarioDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody UsuarioDTO dto) {
        try {
            if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Senha não pode ser vazia");
            }
            if (!validarSenha(dto.getPassword())) {
                return ResponseEntity.badRequest().body(
                        "A senha deve conter:\n" +
                                "- Mínimo 8 caracteres\n" +
                                "- Pelo menos 1 letra minúscula\n" +
                                "- Pelo menos 1 letra maiúscula\n" +
                                "- Pelo menos 1 número\n" +
                                "- Pelo menos 1 caractere especial (!@#$% etc.)"
                );
            }
            if (usuarioRepository.existsByLogin(dto.getLogin())) {
                return ResponseEntity.badRequest().body("Login já está em uso");
            }

            Usuario usuario = converter(dto);

            usuario.setUserRole(UserRole.USER);

            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuario = usuarioService.save(usuario);

            return new ResponseEntity(UsuarioDTO.create(usuario), HttpStatus.CREATED);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean validarSenha(String senha) {
        return senha != null &&
                senha.length() >= 8 &&
                senha.matches(".*[a-z].*") &&
                senha.matches(".*[A-Z].*") &&
                senha.matches(".*\\d.*") &&
                senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"); // 1 especial
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid CredenciaisDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

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

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody UsuarioDTO dto) {
        Optional<Usuario> usuarioExistente = usuarioService.getUsuarioById(id);
        if (!usuarioExistente.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            if (!usuarioExistente.get().getLogin().equals(dto.getLogin()) &&
                    usuarioRepository.existsByLogin(dto.getLogin())) {
                return ResponseEntity.badRequest().body("Login já está em uso");
            }

            Usuario usuario = converter(dto);
            usuario.setId(id);

            if (dto.getUserRole() == null) {
                usuario.setUserRole(usuarioExistente.get().getUserRole());
            }
            if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
                usuario.setPassword(usuarioExistente.get().getPassword());
            } else {
                if (!validarSenha(dto.getPassword())) {
                    return ResponseEntity.badRequest().body(
                            "A senha deve conter:\n" +
                                    "- Mínimo 8 caracteres\n" +
                                    "- Pelo menos 1 letra minúscula\n" +
                                    "- Pelo menos 1 letra maiúscula\n" +
                                    "- Pelo menos 1 número\n" +
                                    "- Pelo menos 1 caractere especial (!@#$% etc.)"
                    );
                }
                usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            usuario = usuarioService.save(usuario);
            return ResponseEntity.ok(UsuarioDTO.create(usuario));
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            usuarioService.delete(usuario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Usuario converter(UsuarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        return usuario;
    }
}
