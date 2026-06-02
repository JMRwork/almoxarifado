import { useState, useEffect } from 'react';

const ProductForm = ({ initialData, onSave, onCancel }) => {
    const [formData, setFormData] = useState({
        nome: '',
        codigo: '',
        quantidade: 0,
        localizacao: '',
    });

    useEffect(() => {
        if (initialData) {
            setFormData({
                nome: initialData.nome,
                codigo: initialData.codigo,
                quantidade: initialData.quantidade,
                localizacao: initialData.localizacao || '',
            });
        }
    }, [initialData]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: name === 'quantidade' ? parseInt(value) || 0 : value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Validação simples
        if (!formData.nome || !formData.codigo) {
            alert('Nome e código são obrigatórios.');
            return;
        }
        onSave(formData);
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h3>{initialData ? 'Editar Produto' : 'Novo Produto'}</h3>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Nome *</label>
                        <input
                            type="text"
                            name="nome"
                            value={formData.nome}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Código *</label>
                        <input
                            type="text"
                            name="codigo"
                            value={formData.codigo}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Quantidade</label>
                        <input
                            type="number"
                            name="quantidade"
                            value={formData.quantidade}
                            onChange={handleChange}
                            min="0"
                        />
                    </div>
                    <div className="form-group">
                        <label>Localização</label>
                        <input
                            type="text"
                            name="localizacao"
                            value={formData.localizacao}
                            onChange={handleChange}
                            placeholder="Ex: Prateleira A2"
                        />
                    </div>
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

export default ProductForm;