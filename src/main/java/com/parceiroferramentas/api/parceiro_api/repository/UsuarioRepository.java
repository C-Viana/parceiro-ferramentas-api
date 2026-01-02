package com.parceiroferramentas.api.parceiro_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.parceiroferramentas.api.parceiro_api.model.Permissao;
import com.parceiroferramentas.api.parceiro_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //@Query("SELECT u FROM Usuario u WHERE u.nomeUsuario =:username")
    //Usuario findUsuarioByNomeUsuario(@Param("username") String username);
    Usuario findUsuarioByUsername(String username);

    Page<Usuario> findByAuthoritiesContains(Permissao authority, Pageable pageable);
}
