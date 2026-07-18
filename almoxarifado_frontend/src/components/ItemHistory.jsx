import { useState, useEffect } from 'react';

const ItemHistory = ({ itemId, onBack }) => {
    const [historico, setHistorico] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchHistorico = async () => {
            try {
                const response = await fetch(`http://localhost:8080/historico/items/${itemId}`);
                if (!response.ok) {
                    throw new Error('Erro ao carregar histórico');
                }
                const data = await response.json();
                setHistorico(data);
                setError(null);
            } catch (err) {
                setError('Não foi possível carregar o histórico do item.');
                console.error('Erro ao carregar histórico:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchHistorico();
    }, [itemId]);

    if (loading) {
        return <div>Carregando histórico...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    return (
        <div>
            <h2>Histórico de Alterações - Item #{itemId}</h2>
            <button onClick={onBack}>Voltar</button>
            {console.log(historico)}
            {historico.length === 0 ? (
                <p>Nenhuma alteração registrada para este item.</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>Revisão</th>
                            <th>Data</th>
                            <th>Usuário</th>
                            <th>Nome</th>
                            <th>Código</th>
                            <th>Quantidade</th>
                            <th>Localização</th>
                        </tr>
                    </thead>
                    <tbody>
                        {historico.content.map((item) => (
                            <tr key={item.revisionNumber}>
                                <td>{item.revisionNumber}</td>
                                <td>{new Date(item.metadata.revisionDate).toLocaleString()}</td>
                                <td>{item.usuario || 'Sistema'}</td>
                                <td>{item.entity.nome}</td>
                                <td>{item.entity.codigo}</td>
                                <td>{item.entity.quantidade}</td>
                                <td>{item.entity.localizacao || '-'}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default ItemHistory;