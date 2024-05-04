package br.com.guifroes1984.ecom.services.jwt;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.guifroes1984.ecom.entity.Usuario;
import br.com.guifroes1984.ecom.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> optionalUsuario = usuarioRepository.findFirstByEmail(username);
		if (optionalUsuario.isEmpty()) throw new UsernameNotFoundException("Nome de usuário não encontrado", null);
		return new org.springframework.security.core.userdetails.User(optionalUsuario.get().getEmail(), optionalUsuario.get().getPassword()
				, new ArrayList<>());
	}

}
