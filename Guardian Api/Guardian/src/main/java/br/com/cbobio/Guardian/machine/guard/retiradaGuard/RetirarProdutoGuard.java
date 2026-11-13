
package br.com.cbobio.Guardian.machine.guard.retiradaGuard;


import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import br.com.cbobio.Guardian.machine.guard.MachineHeaders;
import br.com.cbobio.Guardian.model.entity.TbEstoque;
import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.model.repository.EstoqueRepository;
import br.com.cbobio.Guardian.model.repository.LojaRepository;
import br.com.cbobio.Guardian.model.repository.ProdutoRepository;
import br.com.cbobio.Guardian.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetirarProdutoGuard implements Guard<MachineEstados, MachineEventos> {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;

    @Override
    public boolean evaluate(StateContext<MachineEstados, MachineEventos> context) {

        Long usuarioId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.USUARIO_ID, Long.class);

        Long produtoId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.PRODUTO_ID, Long.class);

        Long lojaId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.LOJA_ID, Long.class);

        Integer quantidade = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.QUANTIDADE, Integer.class);

        if (usuarioId == null ||
                produtoId == null ||
                lojaId == null ||
                quantidade == null) {
            return false;
        }

        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null) {
            return false;
        }

        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            return false;
        }

        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            return false;
        }

        TbLoja loja = lojaRepository.findById(lojaId)
                .orElse(null);

        if (loja == null) {
            return false;
        }

        if (!Boolean.TRUE.equals(loja.getAtivo())) {
            return false;
        }

        TbProduto produto = produtoRepository.findById(produtoId)
                .orElse(null);

        if (produto == null) {
            return false;
        }

        if (!Boolean.TRUE.equals(produto.getAtivo())) {
            return false;
        }

        if (!Boolean.TRUE.equals(produto.getAltoRisco())) {
            return false;
        }

        if (quantidade <= 0) {
            return false;
        }

        TbEstoque estoque = estoqueRepository
                .findByLojaIdAndProdutoId(lojaId, produtoId)
                .orElse(null);

        if (estoque == null) {
            return false;
        }

        if (estoque.getQuantidade() < quantidade) {
            return false;
        }

        return true;
    }
}