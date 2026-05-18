package org.backend.usuarios;

import jakarta.transaction.Transactional;
import org.backend.enums.UserRole;
import org.backend.exception.RegraNegocioInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuario.getPassword() != null && !isSenhaCriptografada(usuario.getPassword())) {
            validarSenha(usuario.getPassword());
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        if (usuario.getUserRole() == null) {
            usuario.setUserRole(UserRole.USER);
        }

        validar(usuario);

        return usuarioRepository.save(usuario);
    }

    private boolean isSenhaCriptografada(String senha) {
        return senha != null &&
                (senha.startsWith("$2a$") ||
                        senha.startsWith("$2b$") ||
                        senha.startsWith("$2y$"));
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }

        return usuario;
    }

    @Transactional
    public void delete(Usuario usuario) {
        Objects.requireNonNull(usuario.getId());
        usuarioRepository.delete(usuario);
    }

    public void validar(Usuario usuario) {
        if (usuario.getName() == null || usuario.getName().trim().isEmpty()) {
            throw new RegraNegocioInvalidaException("Nome é obrigatório");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new RegraNegocioInvalidaException("Login inválido");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new RegraNegocioInvalidaException("Senha não pode ser vazia");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new RegraNegocioInvalidaException("Senha deve ter no mínimo 8 caracteres");
        }

        if (!senha.matches(".*[a-z].*")) {
            throw new RegraNegocioInvalidaException("Senha deve conter letra minúscula");
        }

        if (!senha.matches(".*[A-Z].*")) {
            throw new RegraNegocioInvalidaException("Senha deve conter letra maiúscula");
        }

        if (!senha.matches(".*\\d.*")) {
            throw new RegraNegocioInvalidaException("Senha deve conter número");
        }

        if (!senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new RegraNegocioInvalidaException("Senha deve conter caractere especial");
        }
    }
}