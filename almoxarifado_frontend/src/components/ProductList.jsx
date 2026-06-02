const ProductList = ({ produtos, onEdit, onDelete }) => {
    if (produtos.length === 0) {
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
                {produtos.map((produto) => (
                    <tr key={produto.id}>
                        <td>{produto.codigo}</td>
                        <td>{produto.nome}</td>
                        <td>{produto.quantidade}</td>
                        <td>{produto.localizacao || '-'}</td>
                        <td>
                            <button className="btn-edit" onClick={() => onEdit(produto)}>
                                Editar
                            </button>
                            <button className="btn-delete" onClick={() => onDelete(produto.id)}>
                                Excluir
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default ProductList;