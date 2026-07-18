const ItemList = ({ items, onEdit, onDelete, onHistory }) => {
    if (items.length === 0) {
        return <div className="loading">Nenhum produto cadastrado.</div>;
    }

    return (
        <table>
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Nome</th>
                    <th>Quantidade</th>
                    <th>Localização</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                {items.map((item) => (
                    <tr key={item.id}>
                        <td>{item.codigo}</td>
                        <td>{item.nome}</td>
                        <td>{item.quantidade}</td>
                        <td>{item.localizacao || '-'}</td>
                        <td>
                            <button className="btn-edit" onClick={() => onEdit(item)}>
                                Editar
                            </button>
                            <button className="btn-delete" onClick={() => onDelete(item.id)}>
                                Excluir
                            </button>
                            <button className="btn-secondary" onClick={() => onHistory(item.id)}>
                                Histórico
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default ItemList;