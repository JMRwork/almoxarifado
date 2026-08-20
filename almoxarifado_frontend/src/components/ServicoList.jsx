const ServicoList = ({ items, onEdit, onDelete }) => {
    if (!items || items.length === 0) {
        return <div className="loading">Nenhum serviço cadastrado.</div>;
    }

    return (
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Identificador</th>
                    <th>Descrição</th>
                    <th>Itens (Nome - Código - Descrição - Quantidade)</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                {items.map((servico) => {
                    const servicoItems = Array.isArray(servico.items) ? servico.items : [];

                    return (
                        <tr key={servico.id}>
                            <td>{servico.id}</td>
                            <td>{servico.identificador}</td>
                            <td>{servico.descricao}</td>
                            <td>
                                {servicoItems.length > 0 ? (
                                    <ul>
                                        {servicoItems.map((item) => (
                                            <li key={item.id}>
                                                {item.nome} - {item.codigo} - Quantidade: {item.quantidade}
                                            </li>
                                        ))}
                                    </ul>
                                ) : 'Nenhum item'}
                            </td>
                            <td>
                                <button className="btn-edit" onClick={() => onEdit(servico)}>
                                    Editar
                                </button>
                                <button className="btn-delete" onClick={() => onDelete(servico.id)}>
                                    Excluir
                                </button>
                            </td>
                        </tr>
                    );
                })}
            </tbody>
        </table>
    );
};

export default ServicoList;
