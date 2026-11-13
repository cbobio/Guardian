/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.model.repository.LojaRepository;
import br.com.cbobio.Guardian.model.repository.RoleRepository;
import br.com.cbobio.Guardian.model.repository.UsuarioRepository;
import br.com.cbobio.Guardian.model.repository.specification.UsuarioSpecification;
import br.com.cbobio.Guardian.web.dto.mapper.UsuarioMapper;
import br.com.cbobio.Guardian.web.dto.request.usuario.FilterUsuarioDto;
import br.com.cbobio.Guardian.web.dto.request.usuario.UsuarioRequestDto;
import br.com.cbobio.Guardian.web.dto.response.usuario.UsuarioResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final LojaRepository lojaRepository;
    private final RoleRepository roleRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {

        validarUnicidade(dto, null);
        validarRelacionamentos(dto);

        TbUsuario usuario = usuarioMapper.toEntity(dto);

        TbUsuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDto(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Long id) {

        TbUsuario usuario = buscarUsuario(id);

        return usuarioMapper.toResponseDto(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDto> listarTodos(
            FilterUsuarioDto filtro,
            Pageable pageable) {

        Specification<TbUsuario> specification =
                UsuarioSpecification.fromFiltro(filtro);

        return usuarioRepository.findAll(specification, pageable)
                .map(usuarioMapper::toResponseDto);
    }

    @Transactional
    public UsuarioResponseDto atualizar(
            Long id,
            UsuarioRequestDto dto
    ) {

        TbUsuario usuario = buscarUsuario(id);

        validarUnicidade(dto, id);
        validarRelacionamentos(dto);

        TbUsuario dadosAtualizados = usuarioMapper.toEntity(dto);

        usuario.setMatricula(dto.getMatricula());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dadosAtualizados.getSenha());
        usuario.setAtivo(dto.getAtivo());
        usuario.setBloqueado(dto.getBloqueado());
        usuario.setLoja(dadosAtualizados.getLoja());
        usuario.setRole(dadosAtualizados.getRole());

        TbUsuario usuarioAtualizado =
                usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDto(usuarioAtualizado);
    }

    @Transactional
    public void remover(Long id) {

        TbUsuario usuario = buscarUsuario(id);

        usuarioRepository.delete(usuario);
    }

    private TbUsuario buscarUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado."
                        )
                );
    }

    private void validarUnicidade(
            UsuarioRequestDto dto,
            Long id
    ) {

        boolean matriculaExiste = id == null
                ? usuarioRepository.existsByMatricula(dto.getMatricula())
                : usuarioRepository.existsByMatriculaAndIdNot(
                dto.getMatricula(), id
        );

        if (matriculaExiste) {
            throw new IllegalArgumentException(
                    "Já existe um usuário cadastrado com a matrícula informada."
            );
        }

        boolean emailExiste = id == null
                ? usuarioRepository.existsByEmail(dto.getEmail())
                : usuarioRepository.existsByEmailAndIdNot(
                dto.getEmail(), id
        );

        if (emailExiste) {
            throw new IllegalArgumentException(
                    "Já existe um usuário cadastrado com o e-mail informado."
            );
        }
    }

    private void validarRelacionamentos(UsuarioRequestDto dto) {

        if (!lojaRepository.existsById(dto.getLojaId())) {
            throw new EntityNotFoundException(
                    "Loja não encontrada."
            );
        }

        if (!roleRepository.existsById(dto.getRoleId())) {
            throw new EntityNotFoundException(
                    "Role não encontrada."
            );
        }
    }
}

