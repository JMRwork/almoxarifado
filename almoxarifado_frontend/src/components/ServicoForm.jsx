import { useState, useEffect } from 'react';

const ServicoForm = ({ initialData, onSave, onCancel, itens = [] }) => {
    const [formData, setFormData] = useState({
        identificador: '',
        descricao: '',
        items: [],
    });
    useEffect(() => {
        if (initialData) {
            const items = Array.isArray(initialData.items)
                ? initialData.items.map((item) => ({ ...item, quantidade: item.quantidade || 1 }))
                : [];
            setFormData({
                identificador: initialData.identificador || '',
                descricao: initialData.descricao || '',
                items,
            });
        } else {
            setFormData({
                identificador: '',
                descricao: '',
                items: [],
            });
        }
    }, [initialData]);

    const handleChange = (e) => {
        const selectedIds = Array.from(e.target.selectedOptions, (option) => Number(option.value));
        const selectedItems = selectedIds
            .map((id) => itens.find((item) => Number(item.id) === id))
            .filter(Boolean)
            .map((item) => ({
                ...item,
                quantidade: formData.items.find((selectedItem) => selectedItem.id === item.id)?.quantidade || 1,
            }));

        setFormData((prev) => ({ ...prev, items: selectedItems }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!formData.identificador || !formData.descricao || formData.items.length === 0) {
            alert('Identificador, descrição e pelo menos um item são obrigatórios.');
            return;
        }

        onSave({
            identificador: formData.identificador,
            descricao: formData.descricao,
            items: formData.items.map((item) => ({
                id: item.id,
                nome: item.nome,
                codigo: item.codigo,
                quantidade: item.quantidade || 1,
            })),
        });
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h3>{initialData ? 'Editar Serviço' : 'Novo Serviço'}</h3>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Identificador *</label>
                        <input
                            type="text"
                            name="identificador"
                            value={formData.identificador}
                            onChange={(e) => setFormData((prev) => ({ ...prev, identificador: e.target.value }))}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Descrição *</label>
                        <input
                            type="text"
                            name="descricao"
                            value={formData.descricao}
                            onChange={(e) => setFormData((prev) => ({ ...prev, descricao: e.target.value }))}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Itens do serviço *</label>
                        <select className='form-select'
                            multiple
                            value={formData.items.map((item) => String(item.id))}
                            onChange={handleChange}
                            size={itens.length}
                            required
                        >
                            {!itens.length && <option value="">Nenhum item disponível</option>}
                            {itens.map((item) => (
                                <option key={item.id} value={String(item.id)}>
                                    {item.nome} ({item.codigo})
                                </option>
                            ))}
                        </select>
                    </div>
                    {formData.items.length > 0 ? (<div className="form-group">
                        <label>Determine Quantidade de Itens entre os selecionados *</label>

                        <ul>
                            {formData.items.map((item) => {
                                return item ? (
                                    <li key={item.id}>
                                        {item.nome} ({item.codigo}) - Quantidade:{' '}
                                        <input
                                            type="range"
                                            className="form-range"
                                            min="1"
                                            max={itens.find((catalogItem) => catalogItem.id === item.id)?.quantidade || 1}
                                            step="1"
                                            value={item.quantidade}
                                            onChange={(e) => setFormData((prev) => ({
                                                ...prev,
                                                items: prev.items.map((selectedItem) => selectedItem.id === item.id
                                                    ? { ...selectedItem, quantidade: Number(e.target.value) }
                                                    : selectedItem),
                                            }))}
                                        />
                                        <span>{item.quantidade}</span>
                                    </li>
                                ) : null;
                            })}
                        </ul>
                    </div>) : null}
                    <div className="form-actions">
                        <button type="button" className="btn-secondary" onClick={onCancel}>
                            Cancelar
                        </button>
                        <button type="submit" className="btn-primary">
                            Salvar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ServicoForm;
